package com.postech.auramsstock.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.auramsstock.adapters.dto.RequestStockReserveDTO;
import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.application.DeleteStockUseCase;
import com.postech.auramsstock.application.FindStockUseCase;
import com.postech.auramsstock.application.StockReserverUseCase;
import com.postech.auramsstock.application.UpdateStockUseCase;
import com.postech.auramsstock.domain.Stock;
import com.postech.auramsstock.domain.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StockReserverUseCase stockReserverUseCase;

    @Mock
    private FindStockUseCase findStockUseCase;

    @Mock
    private UpdateStockUseCase updateStockUseCase;

    @Mock
    private DeleteStockUseCase deleteStockUseCase;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockController stockController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
        objectMapper.findAndRegisterModules(); // For LocalDateTime serialization
    }

    @Test
    public void testStockReservation() throws Exception {
        RequestStockReserveDTO dto = new RequestStockReserveDTO("SMGX20-BLK", 5L);

        when(stockReserverUseCase.reserveProcess(any(RequestStockReserveDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/v1/stocks/new-reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(stockReserverUseCase, times(1)).reserveProcess(any(RequestStockReserveDTO.class));
    }

    @Test
    public void testStockReturn() throws Exception {
        RequestStockReserveDTO dto = new RequestStockReserveDTO("SMGX20-BLK", 5L);

        doNothing().when(stockReserverUseCase).returnStock(any(RequestStockReserveDTO.class));

        mockMvc.perform(post("/api/v1/stocks/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(stockReserverUseCase, times(1)).returnStock(any(RequestStockReserveDTO.class));
    }

    @Test
    public void testCheckStockReserve() throws Exception {
        when(findStockUseCase.checkStockReserve(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/stocks/check-reserve")
                        .param("skuProduct", "SMGX20-BLK"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(findStockUseCase, times(1)).checkStockReserve("SMGX20-BLK");
    }

    @Test
    public void testDeleteStock() throws Exception {
        Long stockId = 1L;

        doNothing().when(deleteStockUseCase).delete(anyLong());

        mockMvc.perform(delete("/api/v1/stocks/{id}", stockId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(deleteStockUseCase, times(1)).delete(stockId);
    }

    @Test
    public void testUpdateStock() throws Exception {
        Long stockId = 1L;

        StockDTO stockDTO = new StockDTO();
        stockDTO.setSkuProduct("SMGX20-BLK");
        stockDTO.setNameProduct("Smartphone Galaxy X20");
        stockDTO.setQuantity(45L);
        stockDTO.setValueUnit(new BigDecimal("999.99"));
        stockDTO.setValueSale(new BigDecimal("1099.99"));
        stockDTO.setTotalValue(new BigDecimal("49499.55"));
        stockDTO.setStatus(StatusEnum.AVALIABLE);

        Stock stock = new Stock();
        stock.setId(1);
        stock.setSkuProduct("SMGX20-BLK");
        stock.setNameProduct("Smartphone Galaxy X20");
        stock.setQuantity(45L);
        stock.setValueUnit(new BigDecimal("999.99"));
        stock.setValueSale(new BigDecimal("1099.99"));
        stock.setTotalValue(new BigDecimal("49499.55"));
        stock.setStatus(StatusEnum.AVALIABLE);
        stock.setDtRegister(LocalDateTime.now());

        when(updateStockUseCase.updateStock(anyLong(), any(StockDTO.class))).thenReturn(stock);
        when(modelMapper.map(any(Stock.class), any())).thenReturn(stockDTO);

        mockMvc.perform(put("/api/v1/stocks/{id}", stockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuProduct").value("SMGX20-BLK"))
                .andExpect(jsonPath("$.nameProduct").value("Smartphone Galaxy X20"));

        verify(updateStockUseCase, times(1)).updateStock(anyLong(), any(StockDTO.class));
    }
}