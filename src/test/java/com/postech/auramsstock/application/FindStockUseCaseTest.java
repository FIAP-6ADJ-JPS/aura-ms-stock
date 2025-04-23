package com.postech.auramsstock.application;

import com.postech.auramsstock.database.StockRepository;
import com.postech.auramsstock.database.jpa.entity.StockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindStockUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private FindStockUseCase findStockUseCase;

    private StockEntity stockEntity;

    @BeforeEach
    void setUp() {
        stockEntity = new StockEntity();
        stockEntity.setId(1);
        stockEntity.setSkuProduct("SMGX20-BLK");
        stockEntity.setNameProduct("Smartphone Galaxy X20");
        stockEntity.setQuantity(50L);
        stockEntity.setValueUnit(new BigDecimal("999.99"));
        stockEntity.setValueSale(new BigDecimal("1099.99"));
        stockEntity.setTotalValue(new BigDecimal("49999.50"));
        stockEntity.setDtRegister(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return true when product exists and has stock")
    void checkStockReserveWhenProductExistsAndHasStock() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));

        boolean result = findStockUseCase.checkStockReserve("SMGX20-BLK");

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when product exists but has no stock")
    void checkStockReserveWhenProductExistsButNoStock() {
        stockEntity.setQuantity(0L);
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));

        boolean result = findStockUseCase.checkStockReserve("SMGX20-BLK");

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when product does not exist")
    void checkStockReserveWhenProductDoesNotExist() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.empty());

        boolean result = findStockUseCase.checkStockReserve("NONEXISTENT-SKU");

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return stock entity by ID when it exists")
    void findByIdWhenStockExists() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stockEntity));

        Optional<StockEntity> result = findStockUseCase.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("SMGX20-BLK", result.get().getSkuProduct());
        assertEquals(50L, result.get().getQuantity());
    }

    @Test
    @DisplayName("Should return empty optional when stock with ID does not exist")
    void findByIdWhenStockDoesNotExist() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<StockEntity> result = findStockUseCase.findById(999L);

        assertTrue(result.isEmpty());
    }
}