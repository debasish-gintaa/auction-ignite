package com.asconsoft.gintaa.payload;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPayload {

    @NotNull(message = "bidPrice required")
    private double bidPrice;
    @NotNull(message = "userIdentityId required")
    private String userIdentityId;
    @NotNull(message = "offerId required")
    private String offerId;
    private String userName;
    private String photoUrl;
}
