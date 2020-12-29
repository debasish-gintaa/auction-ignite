package com.asconsoft.gintaa.mapper;

import com.asconsoft.gintaa.model.BiddingDetails;
import com.asconsoft.gintaa.payload.BidPayload;
import com.asconsoft.gintaa.response.BidResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BidMapper {

    default BiddingDetails convertBidPayloadToBiddingDetails(BidPayload bidPayload) {
        return BiddingDetails.builder()
                .offerId(bidPayload.getOfferId())
                .userIdentityId(bidPayload.getUserIdentityId())
                .biddingUserName(bidPayload.getUserName())
                .photoUrl(bidPayload.getPhotoUrl())
                .bidPrice(bidPayload.getBidPrice())
                .build();
    }

    default BidResponse convertBiddingDetailsToBidResponse(BiddingDetails biddingDetails) {
        return BidResponse.builder()
                .bidDetailsId(biddingDetails.getId())
                .offerId(biddingDetails.getOfferId())
                .userIdentityId(biddingDetails.getUserIdentityId())
                .userName(biddingDetails.getUserName())
                .photoUrl(biddingDetails.getPhotoUrl())
                .bidPrice(biddingDetails.getBidPrice())
                .build();
    }
}
