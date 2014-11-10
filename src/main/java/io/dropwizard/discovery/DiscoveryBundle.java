package io.dropwizard.discovery;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.discovery.client.DiscoveryClient;
import io.dropwizard.discovery.core.CuratorAdvertisementListener;
import io.dropwizard.discovery.core.CuratorAdvertiser;
import io.dropwizard.discovery.core.CuratorFactory;
import io.dropwizard.discovery.core.JacksonInstanceSerializer;
import io.dropwizard.discovery.manage.CuratorAdvertiserManager;
import io.dropwizard.discovery.manage.ServiceDiscoveryManager;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.annotation.Nonnull;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.DownInstancePolicy;
import org.apache.curator.x.discovery.ProviderStrategy;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class DiscoveryBundle<T extends Configuration, V> implements ConfiguredBundle<T>, DiscoveryConfiguration<T, V> {

    private ServiceDiscovery<V> discovery;
    private ObjectMapper mapper;
    
    public abstract Class<V> getPayloadClass();

    @Override
    public void initialize(@Nonnull final Bootstrap<?> bootstrap) {
        mapper = bootstrap.getObjectMapper();
    }

    @Override
    public void run(@Nonnull final T configuration, @Nonnull final Environment environment) throws Exception {

        final DiscoveryFactory discoveryConfig = getDiscoveryFactory(configuration);
        // Allow disabling all discovery functionality
        if (discoveryConfig.isDisabled()) {
            return;
        }
        
        final CuratorFactory factory = new CuratorFactory(environment);
        final CuratorFramework framework = factory.build(discoveryConfig);

        final JacksonInstanceSerializer<V> serializer = new JacksonInstanceSerializer<V>(mapper, new TypeReference<ServiceInstance<V>>() {
        });
        discovery = ServiceDiscoveryBuilder.builder(getPayloadClass()).basePath(discoveryConfig.getBasePath()).client(framework).serializer(serializer).build();

        final CuratorAdvertiser<V> advertiser = getCuratorAdvertiser(discoveryConfig, discovery);

        // this listener is used to get the actual HTTP port this server is
        // listening on and uses that to register the service with ZK.
        environment.lifecycle().addServerLifecycleListener(new CuratorAdvertisementListener<V>(advertiser));

        // this managed service is used to register the shutdown handler to
        // de-advertise the service from ZK on shutdown.
        environment.lifecycle().manage(new CuratorAdvertiserManager<V>(advertiser));

        // this managed service is used to start and stop the service discovery
        environment.lifecycle().manage(new ServiceDiscoveryManager<V>(discovery));
    }

    /**
     * Return a new {@link DiscoveryClient} instance that uses a {@link RoundRobinStrategy} when selecting a instance to
     * return and the default {@link DownInstancePolicy}.
     * 
     * @param serviceName
     *            name of the service to monitor
     * @return {@link DiscoveryClient}
     */
    public DiscoveryClient<V> newDiscoveryClient(@Nonnull final String serviceName) {
        return newDiscoveryClient(serviceName, new RoundRobinStrategy<V>());
    }

    /**
     * Return a new {@link DiscoveryClient} instance uses a default {@link DownInstancePolicy} and the provided
     * {@link ProviderStrategy} for selecting an instance.
     * 
     * @param serviceName
     *            name of the service to monitor
     * @param providerStrategy
     *            {@link ProviderStrategy} to use when selecting an instance to return.
     * @return {@link DiscoveryClient}
     */
    public DiscoveryClient<V> newDiscoveryClient(@Nonnull final String serviceName, @Nonnull final ProviderStrategy<V> providerStrategy) {
        return new DiscoveryClient<V>(serviceName, discovery, new DownInstancePolicy(), providerStrategy);
    }
}
