package com.br.integration.domain.service.state;

import com.br.integration.domain.entites.Order;

public class Received implements OrderState {

    @Override
    public void inPreparation(Order order) {
        throw new IllegalStateException("Order is already completed.");
    }

    @Override
    public void forDelivery(Order order) {
        throw new IllegalStateException("Order cannot be sent again, it is already received.");
    }

    @Override
    public void received(Order order) {
        throw new IllegalStateException("Order is already marked as received.");
    }
}
