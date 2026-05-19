package com.fishmarket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishmarket.model.Order;
import com.fishmarket.model.UserAccount;
import com.fishmarket.repository.OrderRepository;
import com.fishmarket.repository.UserAccountRepository;
import com.fishmarket.service.EmailService;
import com.fishmarket.service.StripeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;
    private final UserAccountRepository userAccountRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final StripeService stripeService;

    public OrderController(
            OrderRepository orderRepository,
            UserAccountRepository userAccountRepository,
            ObjectMapper objectMapper,
            EmailService emailService,
            StripeService stripeService) {
        this.orderRepository = orderRepository;
        this.userAccountRepository = userAccountRepository;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
        this.stripeService = stripeService;
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

        // Process payment with Stripe (dummy/stub)
        String paymentIntentId = stripeService.createPaymentIntent(orderRequest.receipt());
        log.info("Stripe payment processing (stub). paymentIntentId={}", paymentIntentId);

        // Save address and payment info if requested
        if (orderRequest.saveInfo()) {
            // In a real implementation, you'd save this to a separate table.
            log.info("Saving payment info for userId={}", account.getId());
        }

        String receiptJson = objectMapper.writeValueAsString(orderRequest.receipt());

        // Temporary: include paymentIntentId in description so it's not lost.
        // Better: add a field on Order entity later.
        String finalDescription = orderRequest.description();
        if (paymentIntentId != null && !paymentIntentId.isBlank()) {
            finalDescription = finalDescription + " (paymentIntentId=" + paymentIntentId + ")";
        }

        Order order = new Order(account, finalDescription, receiptJson);
        order = orderRepository.save(order);

        // Send receipt email (DO NOT FAIL THE ORDER if email fails)
        String to = orderRequest.receiptEmail();
        if (to != null && !to.isBlank()) {
            try {
                String emailContent = emailService.generateReceiptEmailContent(orderRequest.receipt());
                emailService.sendReceiptEmail(to, "Your Fish Market Order Receipt", emailContent);
                log.info("Receipt email sent to {}", to);
            } catch (Exception e) {
                // This prevents the whole request from 500'ing just because SMTP is
                // misconfigured
                log.warn("Receipt email failed (order still placed). to={} error={}", to, e.getMessage());
            }
        }

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

    public record OrderRequest(String description, Object receipt, boolean saveInfo, String receiptEmail) {
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class UnauthorizedException extends RuntimeException {
    }
}
