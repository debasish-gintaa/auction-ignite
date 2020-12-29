package com.asconsoft.gintaa.api;

import com.asconsoft.gintaa.payload.ApiResponse;
import com.asconsoft.gintaa.payload.AuctionPayload;
import com.asconsoft.gintaa.payload.AuctionQueryPayload;
import com.asconsoft.gintaa.response.AuctionResponse;
import com.asconsoft.gintaa.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/v1/auction")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> addNewAuction(@Valid @RequestBody AuctionPayload auctionPayload) {
        log.info("Request to save auction summary");
        auctionService.saveAuctionSummary(auctionPayload);
        ApiResponse<String> response = ApiResponse.ofSuccess(
                "Auction save successfully",
                "Auction save successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<ApiResponse<AuctionResponse>> getAuction(@PathVariable("offerId") String offerId) {
        log.info("Request to get auction by offerId: " + offerId);
        AuctionResponse auctionSummary = auctionService.getAuctionSummaryByOfferId(offerId);
        ApiResponse<AuctionResponse> response = ApiResponse.ofSuccess(
                auctionSummary,
                "Auction save successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<ApiResponse<String>> updateAuctionEndDate(
            @PathVariable("offerId") String offerId,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Request to update end date auction by offer: " + offerId);
        auctionService.updateEndDateOfAuction(offerId, endDate);
        ApiResponse<String> response = ApiResponse.ofSuccess(
                "Auction updated successfully",
                "Auction updated successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAuctionByFilter(@Valid @RequestBody AuctionQueryPayload auctionQueryPayload,
                                                          @RequestParam(value = "limit", defaultValue = "20") Long limit,
                                                          @RequestParam(value = "offset", defaultValue = "0") Long offset) {
        log.info("Request to get auction by filter");
        ApiResponse response = ApiResponse.ofSuccess(
                auctionService.getAuctionSummary(auctionQueryPayload, offset, limit),
                "Auction save successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
