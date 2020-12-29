package com.asconsoft.gintaa.api;

import com.asconsoft.gintaa.payload.ApiResponse;
import com.asconsoft.gintaa.payload.BidPayload;
import com.asconsoft.gintaa.service.BiddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/auction/bid")
@RequiredArgsConstructor
public class BiddingController {

    private final BiddingService biddingService;

    @PostMapping
    public ResponseEntity<ApiResponse> bidAuction(@Valid @RequestBody BidPayload bidPayload) {
        log.info("Request to bid auction");
        biddingService.bidAuction(bidPayload);
        ApiResponse response = ApiResponse.ofSuccess(
                "Bid save successfully",
                "Bid save successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/offer/{id}")
    public ResponseEntity<ApiResponse> bidAuction(@PathVariable("id") String offerId,
                                                  @RequestParam(value = "limit", defaultValue = "20") Long limit,
                                                  @RequestParam(value = "offset", defaultValue = "0") Long offset) {
        log.info("Request to bid auction");
        ApiResponse response = ApiResponse.ofSuccess(
                biddingService.getAllBidByOffer(offerId, limit, offset),
                "Bid details"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
