package com.br.integration.domain.service.state;

import com.br.integration.domain.entites.Order;

public class InPreparation implements OrderState {

    @Override
    public void inPreparation(Order order) {
        throw new IllegalStateException("Order is already in preparation.");
    }

    @Override
    public void forDelivery(Order order) {
        order.setStatus("FORDELIVERY");
        order.setState(new ForDelivery());
        System.out.println("Order #" + order.getId() + " is now out for delivery.");
    }

    @Override
    public void received(Order order) {
        throw new IllegalStateException("Order cannot be received while in preparation.");
    }
}
