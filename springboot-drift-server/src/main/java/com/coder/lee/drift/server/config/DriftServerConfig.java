package com.coder.lee.drift.server.config;


import com.facebook.drift.annotations.ThriftService;
import com.facebook.drift.codec.ThriftCodecManager;
import com.facebook.drift.server.DriftServer;
import com.facebook.drift.server.DriftService;
import com.facebook.drift.server.stats.NullMethodInvocationStatsFactory;
import com.facebook.drift.transport.netty.server.DriftNettyServerConfig;
import com.facebook.drift.transport.netty.server.DriftNettyServerTransportFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.weakref.jmx.internal.guava.collect.ImmutableSet;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/4 12:00
 *
 * @author coderLee23
 */
@Log4j2
@Configuration
public class DriftServerConfig {


    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public DriftServer driftServer() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ThriftService.class);
        log.info("beansWithAnnotation == > {}", beansWithAnnotation);
        Set<DriftService> driftServiceSet = beansWithAnnotation.values().stream().map(DriftService::new).collect(Collectors.toSet());
        DriftNettyServerConfig driftNettyServerConfig = driftNettyServerConfig();
        DriftServer driftServer = new DriftServer(
                new DriftNettyServerTransportFactory(driftNettyServerConfig),
                new ThriftCodecManager(),
                new NullMethodInvocationStatsFactory(),
                driftServiceSet,
                ImmutableSet.of());
        log.info("driftServer port ==>{}", driftNettyServerConfig.getPort());
        return driftServer;
    }


    private DriftNettyServerConfig driftNettyServerConfig() {
        DriftNettyServerConfig driftNettyServerConfig = new DriftNettyServerConfig()
                .setSslEnabled(true)
                .setTrustCertificate(getCertificateChainFile())
                .setKey(getPrivateKeyFile())
                .setPort(8888);

        log.info("driftNettyServerConfig ==> {}", driftNettyServerConfig);
        return driftNettyServerConfig;
    }

    private File getPrivateKeyFile() {
        return getResourceFile("rsa.key");
    }

    private File getCertificateChainFile() {
        return getResourceFile("rsa.crt");
    }

    private File getResourceFile(String name) {
        URL resource = DriftServerConfig.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found " + name);
        }
        return new File(resource.getFile());
    }
}
