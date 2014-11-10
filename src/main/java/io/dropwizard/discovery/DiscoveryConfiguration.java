package io.dropwizard.discovery;

import io.dropwizard.Configuration;
import io.dropwizard.discovery.core.CuratorAdvertiser;

import org.apache.curator.x.discovery.ServiceDiscovery;

public interface DiscoveryConfiguration<T extends Configuration, V> {
    DiscoveryFactory getDiscoveryFactory(T configuration);

    CuratorAdvertiser<V> getCuratorAdvertiser(final DiscoveryFactory discoveryConfig, final ServiceDiscovery<V> discovery);
}
