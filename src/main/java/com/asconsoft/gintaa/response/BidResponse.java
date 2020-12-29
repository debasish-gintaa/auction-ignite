package com.asconsoft.gintaa.response;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {

    private String bidDetailsId;
    private Double bidPrice;
    private String userIdentityId;
    private String offerId;
    private String userName;
    private String photoUrl;
}
