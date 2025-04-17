package com.postech.auramsstock.adapters.messaging.producer;

import com.postech.auramsstock.adapters.dto.messaging.StockOperationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventProducer {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${aws.sqs.stock-operations-queue}")
    private String stockOperationsQueue;

    public void sendStockOperation(StockOperationRequest request) {
        log.info("Sending stock operation to queue: {}", request);
        queueMessagingTemplate.send(stockOperationsQueue,
                MessageBuilder.withPayload(request).build());
    }
}