package com.postech.auramsstock.adapters;

import com.postech.auramsstock.adapters.dto.RequestStockReserveDTO;
import com.postech.auramsstock.application.StockReserver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stocks")
public class StockController {

    private final StockReserver stockReserver;

    public StockController(StockReserver stockReserver) {
        this.stockReserver = stockReserver;
    }

    @PostMapping("/new-reserve")
    public ResponseEntity<Boolean> stockReservetion(@RequestBody RequestStockReserveDTO requestStockReserveDTO) {
        stockReserver.reserveProcess(requestStockReserveDTO);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/return")
    public ResponseEntity<Boolean> stockReturn(@RequestBody RequestStockReserveDTO requestStockReserveDTO) {
        stockReserver.returnStock(requestStockReserveDTO);
        return ResponseEntity.ok(true);
    }


}