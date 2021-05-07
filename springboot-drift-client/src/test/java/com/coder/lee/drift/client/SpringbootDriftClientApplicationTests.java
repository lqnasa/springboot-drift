package com.coder.lee.drift.client;


import com.facebook.drift.client.DriftClientFactory;
import com.facebook.drift.client.address.AddressSelector;
import com.facebook.drift.client.address.SimpleAddressSelector;
import com.facebook.drift.codec.ThriftCodecManager;
import com.facebook.drift.transport.netty.client.DriftNettyClientConfig;
import com.facebook.drift.transport.netty.client.DriftNettyMethodInvokerFactory;
import com.google.common.net.HostAndPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.weakref.jmx.internal.guava.collect.ImmutableList;

import java.util.List;

@SpringBootTest
class SpringbootDriftClientApplicationTests {

    public DriftClientFactory driftClientFactory() {
        // server address
        List<HostAndPort> addresses = ImmutableList.of(HostAndPort.fromParts("localhost", 8888));

        // expensive services that should only be created once
        ThriftCodecManager codecManager = new ThriftCodecManager();
        AddressSelector addressSelector = new SimpleAddressSelector(addresses, true);
        DriftNettyClientConfig config = new DriftNettyClientConfig();

        // methodInvokerFactory must be closed
        DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory
                .createStaticDriftNettyMethodInvokerFactory(config);

        // client factory
        DriftClientFactory clientFactory = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector);
        return clientFactory;
    }

    @Test
    void contextLoads() {
    }

}