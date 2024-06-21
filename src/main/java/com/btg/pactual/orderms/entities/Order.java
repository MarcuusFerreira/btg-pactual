package com.btg.pactual.orderms.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "tb_orders")
public class Order {

    @MongoId
    private Long orderId;
    @Indexed(name = "customer_id_index")
    private Long customerId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;
    private List<OrderItem> items;

    public Order(Long orderId, Long customerId, List<OrderItem> items, BigDecimal total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.total = total;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCostumerId() {
        return customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

}
