package com.food.order.system.restaurant.service;

import com.food.order.system.restaurant.service.entity.Restaurant;
import com.food.order.system.restaurant.service.event.OrderApprovalEvent;
import com.food.order.system.restaurant.service.event.OrderApprovedEvent;
import com.food.order.system.restaurant.service.event.OrderRejectedEvent;
import com.food.order.system.restaurant.service.valueobject.OrderApprovalStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.order.system.restaurant.service.common.DomainConstants.UTC;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {


    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages) {

        restaurant.validateOrder(failureMessages);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());

        if (failureMessages.isEmpty()) {
            log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }

    }
}
