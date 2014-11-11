package io.dropwizard.discovery;

import io.dropwizard.Configuration;
import io.dropwizard.discovery.core.CuratorAdvertiser;
import io.dropwizard.discovery.core.DefaultServiceInstanceFactory;
import io.dropwizard.discovery.core.InstanceMetadata;
import io.dropwizard.discovery.core.ServiceInstanceFactory;

import org.apache.curator.x.discovery.ServiceDiscovery;

/**
 * <code>BasicDiscoveryBundle</code> provides a more completed implementation of <code>DiscoveryBundle</code>, using
 * <code>InstanceMetadata</code> as payload for <code>ServiceInstance</code>
 * 
 * <p>
 * Users should extend DiscoveryBundle if more fine-grain control is necessary of the <code>ServiceInstance</code> payload.
 * </p>
 * @see io.dropwizard.discovery.core.InstanceMetadata
 * @see io.dropwizard.discovery.DiscoveryBundle
 */
public abstract class BasicDiscoveryBundle<T extends Configuration> extends DiscoveryBundle<T, InstanceMetadata> {
     
    private final static ServiceInstanceFactory<InstanceMetadata> serviceInstanceFactory = new DefaultServiceInstanceFactory();
    
    @Override
    public CuratorAdvertiser<InstanceMetadata> getCuratorAdvertiser(DiscoveryFactory discoveryConfig, ServiceDiscovery<InstanceMetadata> discovery) {
        return new CuratorAdvertiser<InstanceMetadata>(discoveryConfig, discovery, serviceInstanceFactory);
    }

    @Override
    public Class<InstanceMetadata> getPayloadClass() {
        return serviceInstanceFactory.getPayloadClass();
    }

}
