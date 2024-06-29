# Saga Pattern Implementation: Choreography and Orchestrator

## Overview

This repository contains a comprehensive implementation of the Saga Pattern, demonstrating both the Choreography and Orchestrator approaches. The Saga Pattern is a microservices design pattern used to manage distributed transactions and ensure data consistency across multiple services.

## Contents

- **Choreography Pattern**: Implementation of Saga using event-driven choreography, where each service publishes and listens to events to manage transactions.
- **Orchestrator Pattern**: Implementation of Saga using a central orchestrator service to coordinate and manage the transaction workflow across multiple services.

## Features

- **Microservices Architecture**: Modular services designed to showcase the principles of Saga Pattern.
- **Event-Driven Choreography**: Services communicate and coordinate through domain events.
- **Centralized Orchestrator**: A dedicated orchestrator service managing the transaction lifecycle.
- **Error Handling**: Robust mechanisms to handle failures and ensure eventual consistency.



<img width="1440" alt="Screenshot 2024-06-29 at 12 19 36 PM" src="https://github.com/suyxx/kafka-sagapattern-reactivejava/assets/30270626/e935c6b6-83a3-4c19-a00c-ad76fbf47ec4">
<img width="1440" alt="Screenshot 2024-06-29 at 12 20 02 PM" src="https://github.com/suyxx/kafka-sagapattern-reactivejava/assets/30270626/1b35fb03-4f3c-44c0-9e3a-0f7e7a51aaf5">
<img width="1440" alt="Screenshot 2024-06-29 at 12 19 26 PM" src="https://github.com/suyxx/kafka-sagapattern-reactivejava/assets/30270626/303b3d03-0637-4130-9b0e-836f118c6c37">
<img width="1440" alt="Screenshot 2024-06-24 at 12 35 17 PM" src="https://github.com/suyxx/kafka-sagapattern-reactivejava/assets/30270626/d0322dfc-9205-4495-923b-6755ab588238">
<img width="1440" alt="Screenshot 2024-06-24 at 12 35 17 PM" src="https://github.com/suyxx/kafka-sagapattern-reactivejava/assets/30270626/cf9b866a-2567-4dbf-bd59-11f85b5114e8">

