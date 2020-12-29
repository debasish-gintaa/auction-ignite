package com.asconsoft.gintaa.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponse {

    private String auctionId;
    private String offerId;
    private String offerName;
    private String offerDescription;
    private double buyoutPrice;
    private LocalDateTime endDate;
    private LocalDateTime startDate;
    private String country;
    private String state;
    private String lat;
    private String lng;
    private double stepPrice;
    private Long bidCount;
    private double currentBidPrice;
}


