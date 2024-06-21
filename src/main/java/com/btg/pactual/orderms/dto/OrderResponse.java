package com.btg.pactual.orderms.dto;

import com.btg.pactual.orderms.entities.Order;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        Long customerId,
        BigDecimal total
) {
    public static OrderResponse fromEntity(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getCostumerId(),
                order.getTotal()
        );
    }
}
