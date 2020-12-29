package com.asconsoft.gintaa.payload;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionPayload {

    @NotBlank(message = "Offer Id is required")
    private String offerId;
    @NotBlank(message = "Offer name is required")
    private String offerName;
    @NotBlank(message = "Offer description is required")
    private String offerDescription;
    @NotNull(message = "buyout price is required")
    private double buyoutPrice;
    @FutureOrPresent(message = "Estd date must be less than today")
    private LocalDateTime endDate;
    @Future(message = "start date is required")
    private LocalDateTime startDate;
    private String country;
    private String state;
    private String lat;
    private String lng;
    @NotNull(message = "step price is required")
    private double stepPrice;


}


