package com.asconsoft.gintaa.model;

import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bidding_details", schema = "gintaa_auction")
public class BiddingDetails extends BaseEntity {

    @Column(name = "offer_id", unique = true)
    @QuerySqlField(index = true)
    private String offerId;
    @Column(name = "bid_price")
    private Double bidPrice;
    @Column(name = "bidding_user_identity_id")
    private String biddingUserIdentityId;
    @Column(name = "bidding_user_name")
    private String biddingUserName;
    @Column(name = "user_identity_id",unique = true)
    private String userIdentityId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "auction_summary")
    private String auctionSummary;
}
