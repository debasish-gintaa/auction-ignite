package com.asconsoft.gintaa.mapper;

import com.asconsoft.gintaa.model.AuctionSummary;
import com.asconsoft.gintaa.response.AuctionResponse;
import org.mapstruct.Mapper;

import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface AuctionMapper {

    default AuctionResponse convertAuctionSummaryToResponse(AuctionSummary auctionSummary) {
        return AuctionResponse.builder()
                .auctionId(auctionSummary.getId())
                .isActive(auctionSummary.isActive())
                .offerId(auctionSummary.getOfferId())
                .offerName(auctionSummary.getOfferName())
                .offerDescription(auctionSummary.getOfferDescription())
                .buyoutPrice(auctionSummary.getBuyoutPrice())
                .basePrice(auctionSummary.getBasePrice())
                .endDate(auctionSummary.getEndDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .startDate(auctionSummary.getStartDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .country(auctionSummary.getCountry())
                .state(auctionSummary.getState())
                .lat(auctionSummary.getLat())
                .lng(auctionSummary.getLng())
                .bidCount(auctionSummary.getBidCount())
                .currentBidPrice(auctionSummary.getCurrentBidPrice())
                .stepPrice(auctionSummary.getStepPrice())
                .build();
    }
}
