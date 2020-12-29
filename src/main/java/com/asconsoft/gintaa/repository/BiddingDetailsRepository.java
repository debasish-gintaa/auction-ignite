package com.asconsoft.gintaa.repository;

import com.asconsoft.gintaa.model.BiddingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddingDetailsRepository extends JpaRepository<BiddingDetails, String> {
}
