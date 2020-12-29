package com.asconsoft.gintaa.config;

import com.asconsoft.gintaa.model.AuctionSummary;
import com.asconsoft.gintaa.model.BiddingDetails;
import com.asconsoft.gintaa.store.AuctionSummaryStore;
import com.asconsoft.gintaa.store.BiddingDetailsStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;

@Configuration
@Slf4j
public class IgniteCacheConfiguration {

    private final Ignite ignite;

    public IgniteCacheConfiguration(Ignite ignite) {
        ignite.active(true);
        this.ignite = ignite;
    }


    @Bean(name = "auctionSummaryCache")
    public IgniteCache<String, AuctionSummary> getAuctionSummaryCache() {
        log.info("creating auctionSummaryCache Cache");
        return createCache("auctionSummaryCache", AuctionSummaryStore.class);
    }

    @Bean(name = "biddingDetailsCache")
    public IgniteCache<String, BiddingDetails> getBiddingDetailsCache() {
        log.info("creating biddingDetailsCache Cache");
        return createCache("biddingDetailsCache", BiddingDetailsStore.class);
    }


    private <K, V> IgniteCache<K, V> createCache(String cacheName, Class clazz) {
        CacheConfiguration<K, V> cacheConfig = new CacheConfiguration<>();
        cacheConfig.setName(cacheName);
        cacheConfig.setReadThrough(true);
        cacheConfig.setWriteThrough(true);
        cacheConfig.setWriteBehindEnabled(true);
        cacheConfig.setWriteBehindFlushFrequency(180000);
        cacheConfig.setBackups(1);
        cacheConfig.setCacheMode(CacheMode.REPLICATED);
        cacheConfig.setCacheStoreFactory(FactoryBuilder.factoryOf(clazz));
        IgniteCache<K, V> cache = ignite.getOrCreateCache(cacheConfig);
        log.info(cacheName + " created.");
        return cache;
    }
}
