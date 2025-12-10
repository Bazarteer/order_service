package org.bazarteer.orderservice.repository;

import org.bazarteer.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Order, Integer> {
    
}
