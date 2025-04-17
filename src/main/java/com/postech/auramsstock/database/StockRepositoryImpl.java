package com.postech.auramsstock.database;

import com.postech.auramsstock.database.jpa.entity.StockEntity;
import com.postech.auramsstock.database.jpa.repository.StockJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockRepositoryImpl implements StockRepository{

    private final StockJpaRepository stockJpaRepository;
    private final ModelMapper modelMapper;

    public StockRepositoryImpl(StockJpaRepository stockJpaRepository, ModelMapper modelMapper) {
        this.stockJpaRepository = stockJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StockEntity save(StockEntity stockEntity) {
        return stockJpaRepository.save(stockEntity);
    }

    @Override
    public Optional<StockEntity> findBySkuProduct(String stockEntity) {
        return Optional.empty();
    }
}
