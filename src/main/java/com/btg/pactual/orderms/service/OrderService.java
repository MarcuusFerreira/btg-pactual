package com.btg.pactual.orderms.service;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import com.btg.pactual.orderms.dto.OrderResponse;
import com.btg.pactual.orderms.entities.Order;
import com.btg.pactual.orderms.entities.OrderItem;
import com.btg.pactual.orderms.dto.OrderCreatedEvent;
import com.btg.pactual.orderms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(OrderCreatedEvent event) {
        var order = new Order(
                event.codigoPedido(),
                event.codigoCliente(),
                this.getOrderItems(event),
                this.getTotal(event)
        );
        orderRepository.save(order);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders =  orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }


    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens().stream().map(
                item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade()))
        ).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream().map(
                item -> new OrderItem(
                        item.produto(),
                        item.quantidade(),
                        item.preco()
                        )
                )
                .toList();
    }
}
