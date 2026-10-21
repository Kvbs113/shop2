package edu.arch.shop.customer.entity.service;

import edu.arch.shop.Customer.dto.CustomerRequest;
import edu.arch.shop.Customer.dto.CustomerResponse;
import edu.arch.shop.Customer.entity.Customer;
import edu.arch.shop.Customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository CustomerRepository;

    public CustomerService(CustomerRepository CustomerRepository) {
        this.CustomerRepository = CustomerRepository;
    }

    public List<CustomerResponse> findAll() {
        return CustomerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id) {
        return toResponse(findCustomer(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        validate(request);
        Customer Customer = new Customer(
                request.customerName(),
                request.productName(),
                request.quantity(),
                request.unitPrice()
        );
        return toResponse(CustomerRepository.save(Customer));
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        validate(request);
        Customer Customer = findCustomer(id);
        Customer.setCustomerName(request.customerName());
        Customer.setProductName(request.productName());
        Customer.setQuantity(request.quantity());
        Customer.setUnitPrice(request.unitPrice());
        return toResponse(CustomerRepository.save(Customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer Customer = findCustomer(id);
        CustomerRepository.delete(Customer);
    }

    private Customer findCustomer(Long id) {
        return CustomerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void validate(CustomerRequest request) {
        if (request.customerName() == null || request.customerName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is required");
        }
        if (request.customerName().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name is too long");
        }
        if (request.productName() == null || request.productName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (request.productName().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is too long");
        }
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (request.unitPrice() == null || request.unitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price must be positive");
        }
        if (request.unitPrice().scale() > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price can have at most 2 decimal places");
        }
        if (request.unitPrice().compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit price is too high");
        }
    }

    private CustomerResponse toResponse(Customer Customer) {
        BigDecimal totalPrice = Customer.getUnitPrice()
                .multiply(BigDecimal.valueOf(Customer.getQuantity()));

        return new CustomerResponse(
                Customer.getId(),
                Customer.getCustomerName(),
                Customer.getProductName(),
                Customer.getQuantity(),
                Customer.getUnitPrice(),
                totalPrice
        );
    }
}
