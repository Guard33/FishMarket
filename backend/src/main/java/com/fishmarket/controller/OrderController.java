package com.fishmarket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishmarket.model.Order;
import com.fishmarket.model.UserAccount;
import com.fishmarket.repository.OrderRepository;
import com.fishmarket.repository.UserAccountRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserAccountRepository userAccountRepository;
    private final ObjectMapper objectMapper;

    public OrderController(OrderRepository orderRepository, UserAccountRepository userAccountRepository,
            ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.userAccountRepository = userAccountRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<OrderResponse> getOrders(Authentication authentication) {
        UserAccount account = resolveAccount(authentication);
        List<Order> orders = orderRepository.findByUserAccountOrderByOrderDateDesc(account);
        return orders.stream().map(OrderResponse::new).toList();
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest, Authentication authentication)
            throws JsonProcessingException {
        UserAccount account = resolveAccount(authentication);
        String receiptJson = objectMapper.writeValueAsString(orderRequest.receipt());
        Order order = new Order(account, orderRequest.description(), receiptJson);
        order = orderRepository.save(order);
        return new OrderResponse(order);
    }

    private UserAccount resolveAccount(Authentication authentication) {
        String googleId = Optional.ofNullable(authentication).map(Authentication::getName).orElse(null);
        if (googleId == null) {
            throw new UnauthorizedException();
        }
        return userAccountRepository.findByGoogleId(googleId).orElseThrow(UnauthorizedException::new);
    }

    public record OrderResponse(Long id, String orderDate, String description, String receiptJson) {
        public OrderResponse(Order order) {
            this(order.getId(), order.getOrderDate().toString(), order.getDescription(), order.getReceiptJson());
        }
    }

    public record OrderRequest(String description, Object receipt) {
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class UnauthorizedException extends RuntimeException {
    }
}
