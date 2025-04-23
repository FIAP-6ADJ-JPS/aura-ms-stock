package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.RequestStockReserveDTO;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockReserverUseCaseTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockReserverUseCase stockReserverUseCase;

    private StockEntity stockEntity;
    private RequestStockReserveDTO reserveDTO;

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

        reserveDTO = new RequestStockReserveDTO("SMGX20-BLK", 5L);
    }

    @Test
    @DisplayName("Should reserve stock successfully when product exists and has enough quantity")
    void reserveProcessSuccessful() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class))).thenReturn(stockEntity);

        boolean result = stockReserverUseCase.reserveProcess(reserveDTO);

        assertTrue(result);
        verify(stockRepository).findBySkuProduct("SMGX20-BLK");
        verify(stockRepository).save(any(StockEntity.class));
        // Verify the stock quantity was reduced
        assertEquals(45L, stockEntity.getQuantity());
    }

    @Test
    @DisplayName("Should fail to reserve when product does not exist")
    void reserveProcessProductNotFound() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.empty());

        boolean result = stockReserverUseCase.reserveProcess(reserveDTO);

        assertFalse(result);
        verify(stockRepository).findBySkuProduct("SMGX20-BLK");
        verify(stockRepository, never()).save(any(StockEntity.class));
    }

    @Test
    @DisplayName("Should fail to reserve when product has insufficient quantity")
    void reserveProcessInsufficientQuantity() {
        stockEntity.setQuantity(3L); // Less than requested quantity
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));

        boolean result = stockReserverUseCase.reserveProcess(reserveDTO);

        assertFalse(result);
        verify(stockRepository).findBySkuProduct("SMGX20-BLK");
        verify(stockRepository, never()).save(any(StockEntity.class));
    }

    @Test
    @DisplayName("Should return stock to inventory when product exists")
    void returnStockWhenProductExists() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.of(stockEntity));
        when(stockRepository.save(any(StockEntity.class))).thenReturn(stockEntity);

        stockReserverUseCase.returnStock(reserveDTO);

        verify(stockRepository).findBySkuProduct("SMGX20-BLK");
        verify(stockRepository).save(any(StockEntity.class));
        // Verify the stock quantity was increased
        assertEquals(55L, stockEntity.getQuantity());
    }

    @Test
    @DisplayName("Should create new stock item when product does not exist during return")
    void returnStockWhenProductDoesNotExist() {
        when(stockRepository.findBySkuProduct(anyString())).thenReturn(Optional.empty());
        when(stockRepository.save(any(StockEntity.class))).thenReturn(new StockEntity());

        stockReserverUseCase.returnStock(reserveDTO);

        verify(stockRepository).findBySkuProduct("SMGX20-BLK");
        verify(stockRepository).save(any(StockEntity.class));
    }

    private void assertEquals(long expected, Long actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + " but was: " + actual);
        }
    }
}