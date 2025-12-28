package com.fishmarket.config;

import com.fishmarket.model.Fish;
import com.fishmarket.model.GearItem;
import com.fishmarket.repository.FishRepository;
import com.fishmarket.repository.GearItemRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final FishRepository fishRepository;
  private final GearItemRepository gearItemRepository;

  public DataSeeder(FishRepository fishRepository, GearItemRepository gearItemRepository) {
    this.fishRepository = fishRepository;
    this.gearItemRepository = gearItemRepository;
  }

  @Override
  public void run(String... args) {
    if (fishRepository.count() == 0) {
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

    if (gearItemRepository.count() == 0) {
      List<GearItem> gear = List.of(
          new GearItem("Nightline Carbon Rod", new BigDecimal("189.00"),
              "Ultra-light carbon fiber, 7 ft, fast action"),
          new GearItem("Tidecaster 3000 Reel", new BigDecimal("149.00"),
              "Sealed drag, saltwater-ready, 10+1 bearings"),
          new GearItem("Glowjet Lure Set", new BigDecimal("42.00"),
              "Bioluminescent soft plastics for night bites"),
          new GearItem("Harborline Braid", new BigDecimal("24.00"),
              "30 lb test, stealth green, 300 yd spool"),
          new GearItem("Shore Lantern Headlamp", new BigDecimal("38.00"),
              "Red light mode, rechargeable, 350 lumens"),
          new GearItem("Dockside Tackle Box", new BigDecimal("65.00"),
              "Modular trays, waterproof seal, carry strap"));
      gearItemRepository.saveAll(gear);
    }
  }
}
