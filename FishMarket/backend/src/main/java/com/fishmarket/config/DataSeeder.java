package com.fishmarket.config;

import com.fishmarket.model.Fish;
import com.fishmarket.repository.FishRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final FishRepository fishRepository;

  public DataSeeder(FishRepository fishRepository) {
    this.fishRepository = fishRepository;
  }

  @Override
  public void run(String... args) {
    if (fishRepository.count() > 0) {
      return;
    }

    List<Fish> fish = List.of(
        new Fish("King Salmon", new BigDecimal("28.00"), 1.8, 24, "Copper River, AK",
            "Rich, buttery, ideal for searing"),
        new Fish("Pacific Halibut", new BigDecimal("24.00"), 1.3, 20, "Sitka, AK",
            "Lean, sweet, perfect for roasting"),
        new Fish("Black Cod", new BigDecimal("26.00"), 1.1, 18, "Monterey Bay, CA",
            "Silky, high omega-3, miso-ready"),
        new Fish("Swordfish Steak", new BigDecimal("22.00"), 1.6, 22, "San Diego, CA",
            "Firm, grill-friendly, meaty bite"),
        new Fish("Mahi Mahi", new BigDecimal("18.00"), 1.2, 19, "Honolulu, HI",
            "Bright flavor, quick pan-sear"),
        new Fish("Rainbow Trout", new BigDecimal("16.00"), 0.9, 17, "Idaho Springs, ID",
            "Delicate, flaky, pan-roast friendly"));

    fishRepository.saveAll(fish);
  }
}
