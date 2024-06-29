package org.suyash.payment.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.suyash.common.events.payment.PaymentStatus;
import org.suyash.payment.application.entity.Customer;
import org.suyash.payment.application.entity.CustomerPayment;
import org.suyash.payment.application.mapper.EntityDtoMapper;
import org.suyash.payment.application.repository.CustomerRepository;
import org.suyash.payment.application.repository.PaymentRepository;
import org.suyash.payment.common.dto.PaymentDto;
import org.suyash.payment.common.dto.PaymentProcessRequest;
import org.suyash.payment.common.exception.CustomerNotFoundException;
import org.suyash.payment.common.exception.InsufficientBalanceException;
import org.suyash.payment.common.service.PaymentService;
import org.suyash.util.DuplicateEventValidator;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private static final Mono<Customer> CUSTOMER_NOT_FOUND = Mono.error(new CustomerNotFoundException());
    private static final Mono<Customer> INSUFFICIENT_BALANCE = Mono.error(new InsufficientBalanceException());

    @Override
    @Transactional
    public Mono<PaymentDto> process(PaymentProcessRequest request) {
        return DuplicateEventValidator.validate(
                this.paymentRepository.existsByOrderId(request.orderId()),
                this.customerRepository.findById(request.customerId())
        ).switchIfEmpty(CUSTOMER_NOT_FOUND)
                .filter(c -> c.getBalance() >= request.amount())
                .switchIfEmpty(INSUFFICIENT_BALANCE)
                .flatMap(c -> this.deductPayment(c, request))
                .doOnNext(dto -> log.info("payment processed for {}", dto.orderId()));
    }

    private Mono<PaymentDto> deductPayment(Customer customer, PaymentProcessRequest request){
        var customerPayment = EntityDtoMapper.toCustomerPayment(request);
        customer.setBalance(customer.getBalance() - request.amount());
        customerPayment.setStatus(PaymentStatus.DEDUCTED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(customerPayment))
                .map(EntityDtoMapper::toDto);
    }

    @Override
    public Mono<PaymentDto> refund(UUID orderId) {
        return this.paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.DEDUCTED)
                .zipWhen( cp -> this.customerRepository.findById(cp.getCustomerId()))
                .flatMap(t -> this.refundPayment(t.getT1(), t.getT2()))
                .doOnNext(dto -> log.info("refund amount {} for {}", dto.amount(), dto.orderId()));
    }

    private Mono<PaymentDto> refundPayment(CustomerPayment customerPayment, Customer customer){
        customer.setBalance(customer.getBalance() + customerPayment.getAmount());
        customerPayment.setStatus(PaymentStatus.REFUNDED);
        return this.customerRepository.save(customer)
                .then(this.paymentRepository.save(customerPayment))
                .map(EntityDtoMapper::toDto);
    }
}
