package com.asconsoft.gintaa.exception;

import com.asconsoft.gintaa.common.exception.GintaaException;

public class AuctionAlreadyClose extends GintaaException {
    public AuctionAlreadyClose(String message) {
        super(message);
    }
}
