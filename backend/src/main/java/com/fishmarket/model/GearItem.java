package com.fishmarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class GearItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private BigDecimal price;
  private String notes;

  protected GearItem() {}

  public GearItem(String name, BigDecimal price, String notes) {
    this.name = name;
    this.price = price;
    this.notes = notes;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getNotes() {
    return notes;
  }
}
