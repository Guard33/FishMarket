package com.fishmarket.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createPaymentIntent(Object receipt) {
        try {
            // Extract amount from receipt (dummy implementation)
            // In a real implementation, you'd parse the receipt JSON to get the total
            // amount
            long amount = 5000; // $50.00 in cents - this should be calculated from the receipt

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .putMetadata("receipt", receipt.toString())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // In a real implementation, you'd return the client_secret to the frontend
            // For now, we'll just return the payment intent ID
            return paymentIntent.getId();

        } catch (Exception e) {
            // For dummy implementation, we'll just log and return a mock ID
            System.out.println("Stripe payment processing (dummy): " + e.getMessage());
            return "pi_dummy_" + System.currentTimeMillis();
        }
    }

    public boolean confirmPayment(String paymentIntentId) {
        // Dummy implementation - in real implementation, you'd confirm the payment
        System.out.println("Confirming payment: " + paymentIntentId);
        return true;
    }
}
