package io.dropwizard.discovery.core;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Factory class to create <code>ServiceInstance</code> for the service.
 *
 * @param <T>
 *            payload class
 */
public interface ServiceInstanceFactory<T> {
    public Class<T> getPayloadClass();

    public ServiceInstance<T> build(String serviceName,
            CuratorAdvertiser<T> advertiser) throws Exception;

}
