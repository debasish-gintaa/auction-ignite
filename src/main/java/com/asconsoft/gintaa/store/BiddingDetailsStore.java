package com.asconsoft.gintaa.store;

import com.asconsoft.gintaa.exception.BiddingDetailsNotFound;
import com.asconsoft.gintaa.model.BiddingDetails;
import com.asconsoft.gintaa.repository.BiddingDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;

@Component
@Slf4j
public class BiddingDetailsStore extends CacheStoreAdapter<String, BiddingDetails> implements Serializable {

    private static BiddingDetailsRepository biddingDetailsRepository;

    @Autowired
    public void setBiddingDetailsRepository(BiddingDetailsRepository biddingDetailsRepository) {
        this.biddingDetailsRepository = biddingDetailsRepository;
    }

    @Override
    public BiddingDetails load(String s) throws CacheLoaderException {
        log.info(">>>>> storing BiddingDetails of id {} into cache", s);
        return biddingDetailsRepository.findById(s).orElseThrow(()->new BiddingDetailsNotFound("XXX No author BiddingDetails of id: " + s));
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends BiddingDetails> entry) throws CacheWriterException {
        log.info("<<<< storing {} from cache to repo", entry.getKey());
        biddingDetailsRepository.save(entry.getValue());
    }

    @Override
    public void delete(Object o) throws CacheWriterException {
        log.info("XXXX deleting {}", o);
        biddingDetailsRepository.delete((BiddingDetails) o);
    }


}
