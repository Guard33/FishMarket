package com.fishmarket.controller;

import com.fishmarket.model.GearItem;
import com.fishmarket.repository.GearItemRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gear")
public class GearController {
  private final GearItemRepository gearItemRepository;

  public GearController(GearItemRepository gearItemRepository) {
    this.gearItemRepository = gearItemRepository;
  }

  @GetMapping
  public List<GearItem> listGear() {
    return gearItemRepository.findAll(Sort.by("name"));
  }
}
