package com.fishmarket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FishMarketApplication {

  private static final Logger logger = LoggerFactory.getLogger(FishMarketApplication.class);

  public static void main(String[] args) {
    logger.info("🚀 Starting FishMarket application...");

    SpringApplication.run(FishMarketApplication.class, args);

    logger.info("✅ FishMarket application started successfully!");
    logger.info("🌐 Backend is running and ready to accept requests.");
  }
}
