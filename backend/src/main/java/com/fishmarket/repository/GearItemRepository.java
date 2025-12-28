package com.fishmarket.repository;

import com.fishmarket.model.GearItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearItemRepository extends JpaRepository<GearItem, Long> {}
