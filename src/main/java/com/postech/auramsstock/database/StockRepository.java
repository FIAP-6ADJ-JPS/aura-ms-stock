package com.postech.auramsstock.database;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository {
    StockEntity save(StockEntity stockEntity);
    Optional<StockEntity> findBySkuProduct(String stockEntity);
}
