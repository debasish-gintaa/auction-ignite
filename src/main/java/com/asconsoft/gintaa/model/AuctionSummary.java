package com.asconsoft.gintaa.model;

import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auction_summary", schema = "gintaa_auction")
public class AuctionSummary extends BaseEntity {
    @Column(name = "offer_id", unique = true)
    @QuerySqlField(index = true)
    private String offerId;
    @Column(name = "offer_name")
    private String offerName;
    @Column(name = "offer_description")
    private String offerDescription;
    @Column(name = "buyout_price")
    private double buyoutPrice;
    @Column(name = "bid_count")
    private long bidCount;
    @Column(name = "current_bid_price")
    private double currentBidPrice;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "country")
    private String country;
    @Column(name = "state")
    private String state;
    @Column(name = "latitude")
    private String lat;
    @Column(name = "longitude")
    private String lng;
    @Column(name = "step_price")
    private double stepPrice;
}
