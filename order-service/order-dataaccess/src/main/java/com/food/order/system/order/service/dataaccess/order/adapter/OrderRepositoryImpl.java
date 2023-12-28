package com.food.order.system.order.service.dataaccess.order.adapter;

import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.order.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.order.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.order.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.entity.OrderItem;
import com.food.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.domain.valueobject.StreetAddress;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.food.order.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

/**
 * @Author mselvi
 * @Created 18.12.2023
 */

/*
 * Order aggregate root'unun secondary adapter'ı
 * */
@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemsEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ? String.join(FAILURE_MESSAGES_DELIMITER, order.getFailureMessages()) : "")
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderJpaRepository.save(orderEntity).toModel();
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(orderId.getValue());
        return orderEntityOptional.map(OrderEntity::toModel);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findByTrackingId(trackingId.getValue());
        return orderEntityOptional.map(OrderEntity::toModel);
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntities(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .productId(orderItem.getProduct().getId().getValue())
                        .build()).collect(Collectors.toList());
    }
}
