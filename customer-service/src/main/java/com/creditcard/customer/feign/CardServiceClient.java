package com.creditcard.customer.feign;



import com.creditcard.customer.feign.dto.CardResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "card-service", url = "${card.service.url:http://localhost:8082}")
public interface CardServiceClient {

    @GetMapping("/api/cards/customer/{customerId}")
    List<CardResponseDTO> getCardsByCustomerId(@PathVariable Long customerId);
}