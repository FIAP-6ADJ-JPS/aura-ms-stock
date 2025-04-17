package com.postech.auramsstock.database.jpa.repository;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<StockEntity, Long> {
}
