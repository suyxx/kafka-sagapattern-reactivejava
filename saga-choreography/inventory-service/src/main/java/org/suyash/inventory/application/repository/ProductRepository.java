package org.suyash.inventory.application.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.suyash.inventory.application.entity.Product;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
