package com.asconsoft.gintaa.repository;

import com.asconsoft.gintaa.model.AuctionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionSummaryRepository extends JpaRepository<AuctionSummary, String> {

    Optional<AuctionSummary> findByOfferId(String offerId);
}
