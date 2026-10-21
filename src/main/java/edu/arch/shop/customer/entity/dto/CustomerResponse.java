package edu.arch.shop.customer.entity.dto;

import java.math.BigDecimal;

public record CustomerResponse(
        Long id,
        String customerName,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
