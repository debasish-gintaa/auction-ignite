package com.asconsoft.gintaa.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class IgniteConfigurationProvider {

    @Bean
    public IgniteConfigurer nodeConfigurer() {
        return cfg -> {
//            Ignition.setClientMode(true);
            cfg.setIgniteInstanceName("my-ignite");

            DataStorageConfiguration storageCfg = new DataStorageConfiguration();
            DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
            dataRegionConfiguration.setName("Default_Region");
            dataRegionConfiguration.setMaxSize(4L * 1024 * 1024 * 1024);
            dataRegionConfiguration.setPersistenceEnabled(true);
            storageCfg.setDataRegionConfigurations(dataRegionConfiguration);
            cfg.setDataStorageConfiguration(storageCfg);

            TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
            TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
            ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47502"));
            tcpDiscoverySpi.setIpFinder(ipFinder);
            cfg.setDiscoverySpi(tcpDiscoverySpi);

            cfg.setRebalanceThreadPoolSize(4);
        };
    }
}
