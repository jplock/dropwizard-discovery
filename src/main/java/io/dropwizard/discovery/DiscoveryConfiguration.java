package io.dropwizard.discovery;

import io.dropwizard.Configuration;

public interface DiscoveryConfiguration<T extends Configuration> {
    DiscoveryFactory getDiscoveryFactory(T configuration);
}
