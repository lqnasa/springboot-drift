package com.coder.lee.drift.client.config;

import com.facebook.drift.client.DriftClientFactory;
import com.facebook.drift.client.address.AddressSelector;
import com.facebook.drift.client.address.SimpleAddressSelector;
import com.facebook.drift.codec.ThriftCodecManager;
import com.facebook.drift.transport.netty.client.DriftNettyClientConfig;
import com.facebook.drift.transport.netty.client.DriftNettyMethodInvokerFactory;
import com.google.common.net.HostAndPort;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.weakref.jmx.internal.guava.collect.ImmutableList;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/5 0:33
 *
 * @author coderLee23
 */
@Log4j2
@Configuration
public class DriftClientConfig {

    @Bean
    public DriftClientFactory driftClientFactory() {
        List<HostAndPort> addresses = ImmutableList.of(HostAndPort.fromParts("localhost", 8888));
        ThriftCodecManager codecManager = new ThriftCodecManager();
        AddressSelector addressSelector = new SimpleAddressSelector(addresses, true);
        DriftNettyClientConfig config = new DriftNettyClientConfig()
                .setTrustCertificate(getCertificateChainFile())
                .setSslEnabled(true);

        DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory
                .createStaticDriftNettyMethodInvokerFactory(config);
        DriftClientFactory clientFactory = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector);
        log.info("clientFactory ==> {}", clientFactory);
        return clientFactory;
    }

    private File getCertificateChainFile() {
        return getResourceFile("rsa.crt");
    }

    private File getResourceFile(String name) {
        URL resource = DriftClientConfig.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found " + name);
        }
        return new File(resource.getFile());
    }
}
