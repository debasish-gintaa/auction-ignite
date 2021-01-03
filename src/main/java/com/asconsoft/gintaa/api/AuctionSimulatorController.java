package com.asconsoft.gintaa.api;

import com.asconsoft.gintaa.payload.ApiResponse;
import com.asconsoft.gintaa.payload.AuctionPayload;
import com.asconsoft.gintaa.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("/v1/auction/simulate")
@RequiredArgsConstructor
public class AuctionSimulatorController {

    private final AuctionService auctionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> addNewAuction(@RequestParam Integer count) {
        log.info("Request to save auction summary");
        for (int i = 0; i < count; i++) {
            auctionService.saveAuctionSummary( AuctionPayload.builder()
                    .offerId(UUID.randomUUID().toString())
                    .offerName("Offer "+i)
                    .offerDescription("Offer "+i)
                    .basePrice(ThreadLocalRandom.current().nextDouble(100.0, 500.0))
                    .buyoutPrice(ThreadLocalRandom.current().nextDouble(1000.0, 1000000.0))
                    .country("India")
                    .state(Arrays.asList("WB","AP","MP","UP","RJ","GJ").get(ThreadLocalRandom.current().nextInt(0,6)))
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(0,60)))
                    .stepPrice(ThreadLocalRandom.current().nextDouble(1.0, 10.0))
                    .build());
        }
        ApiResponse<String> response = ApiResponse.ofSuccess(
                "Auction save successfully",
                "Auction save successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<String>> addNewAuction() {
        log.info("Request to load all auction in cache");
        auctionService.loadCache();
        ApiResponse<String> response = ApiResponse.ofSuccess(
                "Auction load successfully",
                "Auction load successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
