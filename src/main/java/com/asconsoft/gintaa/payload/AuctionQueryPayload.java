package com.asconsoft.gintaa.payload;

import com.asconsoft.gintaa.model.AuctionSummary;
import com.google.common.base.Objects;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionQueryPayload {
    private long bidCount;
    private String state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public boolean applyFilter(AuctionSummary auctionSummary) {
        return (bidCount != 0 && auctionSummary.getBidCount() == bidCount) ||
                Objects.equal(state, auctionSummary.getState()) ||
                (startTime != null && startTime.isEqual(auctionSummary.getStartDate())) ||
                (endTime != null && endTime.isEqual(auctionSummary.getEndDate()));
    }
}
