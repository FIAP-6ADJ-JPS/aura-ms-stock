package com.postech.auramsstock.application;

import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockReserver {

    private final StockRepository stockRepository;

    @Transactional
    public boolean decreaseStock(String skuProduct, Integer quantity) {
        log.info("Attempting to decrease stock for SKU: {} by quantity: {}", skuProduct, quantity);

        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(skuProduct);

        if (stockOptional.isEmpty()) {
            log.warn("Stock not found for SKU: {}", skuProduct);
            return false;
        }

        StockEntity stock = stockOptional.get();

        if (stock.getQuantity() < quantity) {
            log.warn("Insufficient stock for SKU: {}. Available: {}, Requested: {}", skuProduct, stock.getQuantity(), quantity);
            return false;
        }

        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);

        log.info("Stock decreased successfully. New quantity: {}", stock.getQuantity());
        return true;
    }

    @Transactional
    public void returnStock(String skuProduct, Long quantity) {
        log.info("Returning stock for SKU: {} by quantity: {}", skuProduct, quantity);

        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(skuProduct);

        if (stockOptional.isEmpty()) {
            log.warn("Stock not found for SKU: {}, creating new stock record", skuProduct);
            StockEntity newStock = new StockEntity();
            newStock.setSkuProduct(skuProduct);
            newStock.setQuantity(quantity);
            stockRepository.save(newStock);
            return;
        }

        StockEntity stock = stockOptional.get();
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);

        log.info("Stock returned successfully. New quantity: {}", stock.getQuantity());
    }
}