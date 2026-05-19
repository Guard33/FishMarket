package com.fishmarket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key:}")
    private String stripeApiKey;

    public String createPaymentIntent(Object receipt) {
        // Placeholder implementation until Stripe SDK + keys are wired.
        System.out.println("Stripe payment processing (stub).");
        return "pi_dummy_" + System.currentTimeMillis();
    }

    public boolean confirmPayment(String paymentIntentId) {
        // Placeholder implementation.
        System.out.println("Confirming payment (stub): " + paymentIntentId);
        return true;
    }
}
