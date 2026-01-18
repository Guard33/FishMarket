package com.fishmarket.repository;

import com.fishmarket.model.Order;
import com.fishmarket.model.UserAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserAccountOrderByOrderDateDesc(UserAccount userAccount);
}
