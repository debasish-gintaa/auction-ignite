package com.asconsoft.gintaa.service;

import com.asconsoft.gintaa.mapper.AuctionMapper;
import com.asconsoft.gintaa.model.AuctionSummary;
import com.asconsoft.gintaa.payload.AuctionPayload;
import com.asconsoft.gintaa.payload.AuctionQueryPayload;
import com.asconsoft.gintaa.response.AuctionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {

    private final IgniteCache<String, AuctionSummary> auctionSummaryCache;
    private final AuctionMapper auctionMapper;

    public void saveAuctionSummary(AuctionPayload auctionPayload) {
        log.info("Saving new auction. Offer " + auctionPayload.getOfferName());
        AuctionSummary auctionSummary = AuctionSummary.builder()
                .offerId(auctionPayload.getOfferId())
                .offerName(auctionPayload.getOfferName())
                .offerDescription(auctionPayload.getOfferDescription())
                .buyoutPrice(auctionPayload.getBuyoutPrice())
                .basePrice(auctionPayload.getBasePrice())
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

    public Map<String, Object> getAuctionSummary(AuctionQueryPayload auctionQueryPayload, long offset, long limit) {
        log.info("request get all Auction");
//        Streams.stream(auctionSummaryCache.query(new ScanQuery<String, AuctionSummary>((k, v) -> auctionQueryPayload.applyFilter(v))))
        Map<String, Object> map;
        try (QueryCursor<Cache.Entry<String, AuctionSummary>> query = auctionSummaryCache.query(new ScanQuery<>((k, v) -> auctionQueryPayload.applyFilter(v)))) {
            List<AuctionResponse> list = StreamSupport.stream(query.spliterator(), true)
                    .map(objectObjectEntry -> auctionMapper.convertAuctionSummaryToResponse(objectObjectEntry.getValue()))
//                    .sorted(Comparator.comparing(AuctionResponse::getBidCount))
                    .skip(offset).limit(limit)
                    .collect(Collectors.toList());
            map = new HashMap<>();
            map.put("auction", list);
        }
        return map;
    }

    public void loadCache() {
        log.info("request load all Auction in cache");
        auctionSummaryCache.loadCache(null);
    }

    public void updateBidDetails(String offerId, Double currentBidPrice, Double buyoutPrice, long bidCount) {
        log.info("Updating bid details of offerId: " + offerId);
        AuctionSummary auctionSummary = auctionSummaryCache.get(offerId);
        auctionSummary.setCurrentBidPrice(currentBidPrice);
        auctionSummary.setBidCount(bidCount);
        auctionSummary.setActive(currentBidPrice < buyoutPrice);
        auctionSummaryCache.put(auctionSummary.getOfferId(), auctionSummary);
    }
}
