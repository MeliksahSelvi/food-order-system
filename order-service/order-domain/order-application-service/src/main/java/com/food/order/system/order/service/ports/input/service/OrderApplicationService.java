package com.food.order.system.order.service.ports.input.service;

import com.food.order.system.order.service.dto.create.CreateOrderCommand;
import com.food.order.system.order.service.dto.create.CreateOrderResponse;
import com.food.order.system.order.service.dto.track.TrackOrderQuery;
import com.food.order.system.order.service.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

/*
* Client tarafından kullanılacak input port. Implementation'u (primary adapter) yine application-service'de olacak.
* */
public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
