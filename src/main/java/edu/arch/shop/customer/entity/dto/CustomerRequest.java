package edu.arch.shop.customer.entity.dto;

import java.math.BigDecimal;

public record CustomerRequest(
        String customerName,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {
}
