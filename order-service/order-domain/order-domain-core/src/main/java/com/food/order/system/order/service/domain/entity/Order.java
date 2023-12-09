package com.food.order.system.order.service.domain.entity;

import com.food.order.system.domain.entity.AggregateRoot;
import com.food.order.system.domain.valueobject.*;
import com.food.order.system.order.service.domain.valueobject.StreetAddress;
import com.food.order.system.order.service.domain.valueobject.TrackingId;

import java.util.List;

/**
 * @Author mselvi
 * @Created 09.12.2023
 */

/*
* Order Aggregate'i için Aggregate Root olarak Order'ı seçtik.
* */
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder){
        super.setId(builder.orderId);
        this.customerId=builder.customerId;
        this.restaurantId= builder.restaurantId;
        this.streetAddress=builder.streetAddress;
        this.price=builder.price;
        this.items=builder.items;
        this.trackingId=builder.trackingId;
        this.orderStatus=builder.orderStatus;
        this.failureMessages=builder.failureMessages;
    }

    public Builder builder(){
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder{
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val){
            orderId=val;
            return this;
        }

        public Builder customerId(CustomerId val){
            customerId=val;
            return this;
        }

        public Builder restaurantId(RestaurantId val){
            restaurantId=val;
            return this;
        }

        public Builder streetAddress(StreetAddress val){
            streetAddress=val;
            return this;
        }

        public Builder price(Money val){
            price=val;
            return this;
        }

        public Builder items(List<OrderItem> val){
            items=val;
            return this;
        }

        public Builder trackingId(TrackingId val){
            trackingId=val;
            return this;
        }

        public Builder orderStatus(OrderStatus val){
            orderStatus=val;
            return this;
        }

        public Builder failureMessages(List<String> val){
            failureMessages=val;
            return this;
        }

        public Order build(){
            return new Order(this);
        }
    }
}

