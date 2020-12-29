package com.asconsoft.gintaa.store;

import com.asconsoft.gintaa.exception.AuctionSummaryNotFound;
import com.asconsoft.gintaa.model.AuctionSummary;
import com.asconsoft.gintaa.repository.AuctionSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;
import java.util.Optional;

@Component
@Slf4j
public class AuctionSummaryStore extends CacheStoreAdapter<String, AuctionSummary> implements Serializable {

    private static AuctionSummaryRepository auctionSummaryRepository;

    @Autowired
    public void setAuctionSummaryRepository(AuctionSummaryRepository auctionSummaryRepository) {
        this.auctionSummaryRepository = auctionSummaryRepository;
    }


    public AuctionSummary load(String s) throws CacheLoaderException {
        log.info(">>>>> storing AuctionSummary of id {} into cache", s);
        return auctionSummaryRepository.findById(s).orElseThrow(() -> new AuctionSummaryNotFound("XXX No AuctionSummary of id: " + s));
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends AuctionSummary> entry) throws CacheWriterException {
        log.info("<<<< storing {} from cache to repo", entry.getKey());
        AuctionSummary auctionSummary = entry.getValue();
        Optional<AuctionSummary> summary = auctionSummaryRepository.findByOfferId(auctionSummary.getOfferId());
        summary.ifPresent(value -> auctionSummary.setId(value.getId()));
        auctionSummaryRepository.save(auctionSummary);
    }

    @Override
    public void delete(Object o) throws CacheWriterException {
        log.info("XXXX deleting {}", o);
        auctionSummaryRepository.delete((AuctionSummary) o);
    }

    @Override
    public void loadCache(IgniteBiInClosure<String, AuctionSummary> clo, Object... args) {
        auctionSummaryRepository.findAll().forEach(auctionSummary -> clo.apply(auctionSummary.getOfferId(), auctionSummary));
    }
}
