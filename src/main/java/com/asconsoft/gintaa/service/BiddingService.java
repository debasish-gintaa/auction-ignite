package com.asconsoft.gintaa.service;

import com.asconsoft.gintaa.exception.AuctionAlreadyClose;
import com.asconsoft.gintaa.exception.AuctionExpire;
import com.asconsoft.gintaa.exception.UnsupportedBidding;
import com.asconsoft.gintaa.mapper.BidMapper;
import com.asconsoft.gintaa.model.BiddingDetails;
import com.asconsoft.gintaa.payload.BidPayload;
import com.asconsoft.gintaa.response.AuctionResponse;
import com.asconsoft.gintaa.response.BidResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BiddingService {

    private final IgniteCache<String, BiddingDetails> biddingDetailsCache;
    private final AuctionService auctionService;
    private final BidMapper bidMapper;

    @Transactional
    public void bidAuction(BidPayload bidPayload) {
        log.info("Saving bid details.");
        BiddingDetails biddingDetails = bidMapper.convertBidPayloadToBiddingDetails(bidPayload);
        AuctionResponse auctionResponse = auctionService.getAuctionSummaryByOfferId(bidPayload.getOfferId());
        boolean active = auctionResponse.isActive();
        if (!active)
            throw new AuctionAlreadyClose("Not valid auction, Offer id: " + bidPayload.getOfferId());
        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(auctionResponse.getEndDate()), TimeZone.getDefault().toZoneId());
        if (endDate.isAfter(LocalDateTime.now()))
            throw new AuctionExpire("Not valid auction, Offer id: " + bidPayload.getOfferId());
        double basePrice = auctionResponse.getBasePrice();
        if (basePrice > bidPayload.getBidPrice())
            throw new UnsupportedBidding("Bidding price " + bidPayload.getBidPrice() + " not valid. Lower than base price");

        Long bidCount = auctionResponse.getBidCount();
        Double currentBidPrice = auctionResponse.getCurrentBidPrice();
        Double stepPrice = auctionResponse.getStepPrice();
        Double buyoutPrice = auctionResponse.getBuyoutPrice();
        if (bidPayload.getBidPrice() < (currentBidPrice + stepPrice) && bidPayload.getBidPrice() != buyoutPrice)
            throw new UnsupportedBidding("Bidding price " + bidPayload.getBidPrice() + " not valid");
        biddingDetailsCache.put(biddingDetails.getOfferId() + biddingDetails.getBidPrice(), biddingDetails);
        auctionService.updateBidDetails(biddingDetails.getOfferId(), biddingDetails.getBidPrice(), buyoutPrice, bidCount + 1);
    }

    public List<BidResponse> getAllBidByOffer(String offerId, long limit, long offset) {
        log.info("Fetching all bids of offer: " + offerId);
        ScanQuery<String, BiddingDetails> qry = new ScanQuery<>((s, biddingDetails) -> biddingDetails.getOfferId().equals(offerId));
        List<BidResponse> list = biddingDetailsCache.query(qry).getAll().stream().skip(offset).limit(limit).map(v -> bidMapper.convertBiddingDetailsToBidResponse(v.getValue())).collect(Collectors.toList());
        return list;
    }
}
