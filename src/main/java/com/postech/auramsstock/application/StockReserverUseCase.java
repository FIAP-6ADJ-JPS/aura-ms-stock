package com.postech.auramsstock.application;

import com.postech.auramsstock.adapters.dto.RequestStockReserveDTO;
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
public class StockReserverUseCase {

    private final StockRepository stockRepository;

    @Transactional
    public boolean reserveProcess(RequestStockReserveDTO requestStockReserveDTO) {
        log.info("Tentando reservar o estoque para SKU: {} e quantidade: {}", requestStockReserveDTO.getSku(), requestStockReserveDTO.getQuantity());

        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(requestStockReserveDTO.getSku());

        if (stockOptional.isEmpty()) {
            log.warn("Estoque não disponível SKU: {}", requestStockReserveDTO.getSku());
            return false;
        }

        StockEntity stock = stockOptional.get();

        if (stock.getQuantity() < requestStockReserveDTO.getQuantity()) {
            log.warn("Estoque insuficiente SKU: {}. Disponível: {}, Solitidado: {}", requestStockReserveDTO.getSku(), stock.getQuantity(), requestStockReserveDTO.getQuantity());
            return false;
        }

        stock.setQuantity(stock.getQuantity() - requestStockReserveDTO.getQuantity());
        stockRepository.save(stock);

        log.info("Estoque reservado com sucesso, quandidade atual disponível: {}", stock.getQuantity());
        return true;
    }

    @Transactional
    public void returnStock(RequestStockReserveDTO requestStockReserveDTO) {
        log.info("Retornando ao estoque o SKU: {} e a quantidade: {}", requestStockReserveDTO.getSku(), requestStockReserveDTO.getQuantity());

        Optional<StockEntity> stockOptional = stockRepository.findBySkuProduct(requestStockReserveDTO.getSku());

        if (stockOptional.isEmpty()) {
            log.warn("Estoque não encontrado SKU: {}, criando uma nova gravação", requestStockReserveDTO.getSku());
            StockEntity newStock = new StockEntity();
            newStock.setSkuProduct(requestStockReserveDTO.getSku());
            newStock.setQuantity(requestStockReserveDTO.getQuantity());
            stockRepository.save(newStock);
            return;
        }

        StockEntity stock = stockOptional.get();
        stock.setQuantity(stock.getQuantity() + requestStockReserveDTO.getQuantity());
        stockRepository.save(stock);

        log.info("Estoque retornado com sucesso, retornando a quantidade para: {}", stock.getQuantity());
    }
}