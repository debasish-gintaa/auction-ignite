package com.asconsoft.gintaa.service;

import com.asconsoft.gintaa.mapper.AuctionMapper;
import com.asconsoft.gintaa.model.AuctionSummary;
import com.asconsoft.gintaa.payload.AuctionPayload;
import com.asconsoft.gintaa.payload.AuctionQueryPayload;
import com.asconsoft.gintaa.response.AuctionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

    private final IgniteCache<String, AuctionSummary> auctionSummaryCache;
    private final AuctionMapper auctionMapper;

    public void saveAuctionSummary(AuctionPayload auctionPayload) {
        log.info("Saving new auction.");
        AuctionSummary auctionSummary = AuctionSummary.builder()
                .offerId(auctionPayload.getOfferId())
                .offerName(auctionPayload.getOfferName())
                .offerDescription(auctionPayload.getOfferDescription())
                .buyoutPrice(auctionPayload.getBuyoutPrice())
                .endDate(auctionPayload.getEndDate())
                .startDate(auctionPayload.getStartDate())
                .country(auctionPayload.getCountry())
                .state(auctionPayload.getState())
                .lat(auctionPayload.getLat())
                .lng(auctionPayload.getLng())
                .stepPrice(auctionPayload.getStepPrice())
                .build();
        auctionSummaryCache.put(auctionPayload.getOfferId(), auctionSummary);
    }

    public void updateEndDateOfAuction(String offerId, LocalDateTime endDate) {
        log.info("Updating auction end date: " + endDate);
        AuctionSummary auctionSummary = auctionSummaryCache.get(offerId);
        auctionSummary.setEndDate(endDate);
        auctionSummaryCache.put(auctionSummary.getOfferId(), auctionSummary);
    }

    public AuctionResponse getAuctionSummaryByOfferId(String offerId) {
        log.info("Fetching auction by offer {}.", offerId);
        AuctionSummary auctionSummary = auctionSummaryCache.get(offerId);
        return auctionMapper.convertAuctionSummaryToResponse(auctionSummary);
    }

    public List<AuctionResponse> getAuctionSummary(AuctionQueryPayload auctionQueryPayload, long offset, long limit) {
        log.info("request get all Auction");
        List<AuctionResponse> list = auctionSummaryCache.query(new ScanQuery<String, AuctionSummary>((k, v) -> auctionQueryPayload.applyFilter(v))).getAll().stream().skip(offset).limit(limit).map(objectObjectEntry -> auctionMapper.convertAuctionSummaryToResponse(objectObjectEntry.getValue())).collect(Collectors.toList());
        return list;
    }

    public void updateBidDetails(String offerId, Double currentBidPrice, long bidCount) {
        log.info("Updating bid details of offerId: " + offerId);
        AuctionSummary auctionSummary = auctionSummaryCache.get(offerId);
        auctionSummary.setCurrentBidPrice(currentBidPrice);
        auctionSummary.setBidCount(bidCount);
        auctionSummaryCache.put(auctionSummary.getOfferId(), auctionSummary);
    }
}
