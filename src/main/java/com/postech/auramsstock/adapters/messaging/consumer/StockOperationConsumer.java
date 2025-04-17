package com.postech.auramsstock.adapters.messaging.consumer;

import com.postech.auramsstock.adapters.dto.messaging.StockOperationRequest;
import com.postech.auramsstock.application.StockReserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class StockOperationConsumer {

    private final StockReserver stockReserver;

    @SqsListener("${aws.sqs.stock-operations-queue}")
    public void receiveStockOperation(StockOperationRequest request) {
        log.info("Received stock operation request: {}", request);

        try {
            if (request.getOperationType() == StockOperationRequest.OperationType.DECREASE) {
                boolean success = stockReserver.decreaseStock(request.getSkuProduct(), request.getQuantity());
                log.info("Stock decrease operation result: {}", success ? "Success" : "Failed (insufficient stock)");
            } else {
                stockReserver.returnStock(request.getSkuProduct(), request.getQuantity());
                log.info("Stock return operation completed");
            }
        } catch (Exception e) {
            log.error("Error processing stock operation: {}", e.getMessage(), e);
            // Aqui você pode implementar uma lógica de retry ou DLQ (Dead Letter Queue)
        }
    }
}