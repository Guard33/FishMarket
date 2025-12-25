package com.fishmarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Fish {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private BigDecimal price;
  private Double weight;
  private Integer length;
  private String origin;
  private String notes;

  protected Fish() {}

  public Fish(String name, BigDecimal price, Double weight, Integer length, String origin, String notes) {
    this.name = name;
    this.price = price;
    this.weight = weight;
    this.length = length;
    this.origin = origin;
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

  public Double getWeight() {
    return weight;
  }

  public Integer getLength() {
    return length;
  }

  public String getOrigin() {
    return origin;
  }

  public String getNotes() {
    return notes;
  }
}
