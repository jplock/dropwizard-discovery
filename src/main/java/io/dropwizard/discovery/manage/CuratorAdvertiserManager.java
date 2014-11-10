package io.dropwizard.discovery.manage;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.discovery.core.CuratorAdvertiser;
import io.dropwizard.lifecycle.Managed;
import javax.annotation.Nonnull;

public class CuratorAdvertiserManager<T> implements Managed {

    private final CuratorAdvertiser<T> advertiser;

    /**
     * Constructor
     * 
     * @param advertiser
     *            {@link CuratorAdvertiser}
     */
    public CuratorAdvertiserManager(@Nonnull final CuratorAdvertiser<T> advertiser) {
        this.advertiser = checkNotNull(advertiser);
    }

    @Override
    public void start() throws Exception {
        // the {@link CuratorAdvertisementListener will register the service}
    }

    @Override
    public void stop() throws Exception {
        advertiser.unregisterAvailability();
    }
}
