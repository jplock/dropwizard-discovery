package io.dropwizard.discovery.core;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import javax.annotation.Nonnull;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuratorAdvertisementListener<T> implements ServerLifecycleListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CuratorAdvertisementListener.class);
    private static final String APPLICATION_CONNECTOR = "application";
    private final CuratorAdvertiser<T> advertiser;

    /**
     * Constructor
     * 
     * @param advertiser
     *            {@link CuratorAdvertiser}
     */
    public CuratorAdvertisementListener(
            @Nonnull final CuratorAdvertiser<T> advertiser) {
        this.advertiser = checkNotNull(advertiser);
    }

    @Override
    public void serverStarted(final Server server) {
        // Detect the port Jetty is listening on - works with configured and
        // random port
        for (final Connector connector : server.getConnectors()) {
            if (APPLICATION_CONNECTOR.equals(connector.getName())) {
                final ServerSocketChannel channel = (ServerSocketChannel) connector
                        .getTransport();

                try {
                    final InetSocketAddress socket = (InetSocketAddress) channel
                            .getLocalAddress();
                    advertiser.initListenInfo(socket.getPort());
                    advertiser.registerAvailability();
                    return;
                } catch (final Exception e) {
                    LOGGER.error("Unable to register service in ZK", e);
                }
            }
        }
    }
}
