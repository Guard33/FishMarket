package com.fishmarket.controller;

import com.fishmarket.model.Fish;
import com.fishmarket.repository.FishRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fish")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class FishController {
  private final FishRepository fishRepository;

  public FishController(FishRepository fishRepository) {
    this.fishRepository = fishRepository;
  }

  @GetMapping
  public List<Fish> listFish() {
    return fishRepository.findAll(Sort.by("name"));
  }
}
