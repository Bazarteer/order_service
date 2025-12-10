package org.bazarteer.orderservice.service;

import org.bazarteer.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.bazarteer.orderservice.model.Order;
import org.bazarteer.orderservice.proto.OrderServiceGrpc;
import org.bazarteer.orderservice.proto.PlaceOrderRequest;
import org.bazarteer.orderservice.proto.PlaceOrderResponse;

import java.time.LocalDateTime;

@GrpcService
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {

    @Autowired
    private RabbitMQPublisher publisher;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void placeOrder(PlaceOrderRequest req, StreamObserver<PlaceOrderResponse> responseObserver) { 
        try {

            Order order = new Order();
            order.setCustumerId(req.getCustumerId());
            order.setSellerId(req.getSellerId());
            order.setCustumerLocation(req.getCustumerLocation());
            order.setProductLocation(req.getProductLocation());
            order.setFinalPrice(req.getFinalPrice());
            order.setProductId(req.getProductId());
            order.setCreatedAt(LocalDateTime.now());

            orderRepository.save(order);

            publisher.publishOrderPlaced(order);

            PlaceOrderResponse res = PlaceOrderResponse.newBuilder().setOrderId(order.getId()).build();
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.out.println(e);
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Intenal server error").withCause(e).asRuntimeException());
        } 
    }

}
