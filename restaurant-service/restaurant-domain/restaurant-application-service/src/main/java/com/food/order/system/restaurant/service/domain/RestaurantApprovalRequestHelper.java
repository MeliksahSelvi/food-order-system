package com.food.order.system.restaurant.service.domain;

import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.Restaurant;
import com.food.order.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.order.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.order.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.order.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.food.order.system.restaurant.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.order.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.food.order.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.food.order.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;

    @Transactional
    public void persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        if (publishIfOutboxMessageProcessedForPayment(restaurantApprovalRequest)) {
            log.info("An outbox message with saga id: {} is already saved to database!",
                    restaurantApprovalRequest.getSagaId());
            return;
        }
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant, failureMessages);
        orderApprovalRepository.save(restaurant.getOrderApproval());

        orderOutboxHelper.persistOrderOutboxMessage(
                restaurantDataMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalRequest.getSagaId()));
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantInformation.isEmpty()) {
            log.error("Restaurant with id {} not found", restaurant.getId().getValue());
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() + " not found");
        }
        Restaurant restaurantEntity = restaurantInformation.get();

        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }

    private boolean publishIfOutboxMessageProcessedForPayment(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOrderApprovalStatusAndOutboxStatus(
                        UUID.fromString(restaurantApprovalRequest.getSagaId()),
                        OutboxStatus.COMPLETED);
        if (orderOutboxMessage.isPresent()) {
            restaurantApprovalResponseMessagePublisher.publish(
                    orderOutboxMessage.get(), orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }

}
