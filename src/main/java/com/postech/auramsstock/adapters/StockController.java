package com.postech.auramsstock.adapters;

import com.postech.auramsstock.adapters.dto.StockDTO;
import com.postech.auramsstock.adapters.dto.messaging.StockOperationRequest;
import com.postech.auramsstock.adapters.messaging.producer.StockEventProducer;
import com.postech.auramsstock.application.StockReserver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockReserver stockReserver;
    private final StockEventProducer stockEventProducer;


    @PostMapping("/decrease")
    public ResponseEntity<Boolean> decreaseStock(@RequestBody StockOperationRequest request) {
        request.setOperationType(StockOperationRequest.OperationType.DECREASE);
        boolean result = stockReserver.decreaseStock(request.getSkuProduct(), request.getQuantity().intValue());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnStock(@RequestBody StockOperationRequest request) {
        request.setOperationType(StockOperationRequest.OperationType.RETURN);
        stockReserver.returnStock(request.getSkuProduct(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queue/operation")
    public ResponseEntity<Void> queueStockOperation(@RequestBody StockOperationRequest request) {
        stockEventProducer.sendStockOperation(request);
        return ResponseEntity.accepted().build();
    }
}