package edu.arch.shop.customer.entity.repository;

import edu.arch.shop.Customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
