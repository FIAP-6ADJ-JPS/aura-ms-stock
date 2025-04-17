package com.postech.auramsstock.adapters.dto.client;

import com.postech.auramsstock.adapters.dto.client.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${app.product-service.url}")
public interface ProductClient {

    @GetMapping("/produtos/sku/{sku}")
    ProductResponse getProductBySku(@PathVariable("sku") String sku);
}
