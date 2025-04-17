package com.postech.auramsstock.adapters.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockOperationRequest {
    private String skuProduct;
    private Long quantity;
    private OperationType operationType;

    public enum OperationType {
        DECREASE, // Para baixa de estoque
        RETURN    // Para retorno de estoque
    }
}