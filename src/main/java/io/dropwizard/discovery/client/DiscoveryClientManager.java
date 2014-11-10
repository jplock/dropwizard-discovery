package io.dropwizard.discovery.client;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.lifecycle.Managed;
import javax.annotation.Nonnull;

public class DiscoveryClientManager<T> implements Managed {

    private final DiscoveryClient<T> client;

    /**
     * Constructor
     * 
     * @param client
     *            {@link DiscoveryClient}
     */
    public DiscoveryClientManager(@Nonnull final DiscoveryClient<T> client) {
        this.client = checkNotNull(client);
    }

    @Override
    public void start() throws Exception {
        client.start();
    }

    @Override
    public void stop() throws Exception {
        client.close();
    }
}
