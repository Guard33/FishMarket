package com.fishmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReceiptEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public String generateReceiptEmailContent(Object receipt) {
        // This is a simple implementation - you can enhance this with HTML templates
        StringBuilder content = new StringBuilder();
        content.append("Thank you for your order!\n\n");
        content.append("Order Details:\n");
        content.append("================\n\n");

        // Add receipt details here - this would be enhanced based on your receipt
        // structure
        content.append("Your order has been processed successfully.\n\n");
        content.append("Best regards,\n");
        content.append("Fish Market Team");

        return content.toString();
    }
}
