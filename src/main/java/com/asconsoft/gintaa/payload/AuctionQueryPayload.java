package com.asconsoft.gintaa.payload;

import com.asconsoft.gintaa.model.AuctionSummary;
import com.google.common.base.Objects;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionQueryPayload {
    private String state;
    private long startDays;
    private long endDays;

    public boolean applyFilter(AuctionSummary auctionSummary) {
        return  (state == null || Objects.equal(state, auctionSummary.getState())) &&
                (startDays == 0 || auctionSummary.getStartDate().isAfter(LocalDateTime.now().minus(startDays, ChronoUnit.DAYS))) &&
                (endDays == 0 || auctionSummary.getEndDate().isAfter(LocalDateTime.now().minus(endDays, ChronoUnit.DAYS)));
    }
}
