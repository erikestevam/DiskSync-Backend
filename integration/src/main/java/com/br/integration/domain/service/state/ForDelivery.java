package com.br.integration.domain.service.state;

import com.br.integration.domain.entites.Order;

public class ForDelivery implements OrderState {

    @Override
    public void inPreparation(Order order) {
        throw new IllegalStateException("Order cannot go back to preparation.");
    }

    @Override
    public void forDelivery(Order order) {
        throw new IllegalStateException("Order is already out for delivery.");
    }

    @Override
    public void received(Order order) {
        order.setStatus("RECEIVED");
        order.setState(new Received());
        System.out.println("Order #" + order.getId() + " has been delivered.");
    }
}
