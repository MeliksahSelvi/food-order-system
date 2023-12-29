package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.order.service.domain.entity.Customer;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.entity.Product;
import com.food.order.system.order.service.domain.entity.Restaurant;
import com.food.order.system.order.service.domain.event.OrderCreatedEvent;
import com.food.order.system.order.service.domain.exception.OrderDomainException;
import com.food.order.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.order.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.order.system.order.service.domain.valueobject.ProductId;
import com.food.order.system.order.service.domain.valueobject.RestaurantId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = createOrderCommand.toModel();
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerOptional = customerRepository.findCustomer(customerId);
        if (customerOptional.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = createRestaurant(createOrderCommand);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: " + createOrderCommand.getRestaurantId());
        }
        return restaurantOptional.get();
    }

    private Restaurant createRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                                new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList())
                )
                .build();
    }

    private void saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId().getValue());
    }
}
