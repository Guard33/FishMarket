package com.fishmarket.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Lob;
import java.time.Instant;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(nullable = false)
    private Instant orderDate;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String receiptJson;

    protected Order() {
    }

    public Order(UserAccount userAccount, String description, String receiptJson) {
        this.userAccount = userAccount;
        this.description = description;
        this.receiptJson = receiptJson;
    }

    @PrePersist
    public void onCreate() {
        orderDate = Instant.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptJson() {
        return receiptJson;
    }

    public void setReceiptJson(String receiptJson) {
        this.receiptJson = receiptJson;
    }
}
