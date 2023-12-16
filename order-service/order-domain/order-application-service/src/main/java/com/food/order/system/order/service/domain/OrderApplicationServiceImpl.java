package com.food.order.system.order.service.domain;

import com.food.order.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.order.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @Author mselvi
 * @Created 16.12.2023
 */

@Slf4j
@Validated//interface'deki valid annotation'larının çalışması için
@Service
@RequiredArgsConstructor
/*
* public yapmadık çünkü dışarının bu implementation'a erişmesini istemiyoruz. onlar input port ile iletişim kursalar onlara yeterli.
* */
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler createCommandHandler;
    private final OrderTrackCommandHandler trackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return createCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return trackCommandHandler.trackOrder(trackOrderQuery);
    }
}
