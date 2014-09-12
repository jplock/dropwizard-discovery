package io.dropwizard.discovery.core;

import io.dropwizard.discovery.DiscoveryFactory;
import io.dropwizard.discovery.health.CuratorHealthCheck;
import io.dropwizard.discovery.manage.CuratorManager;
import io.dropwizard.setup.Environment;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

public class CuratorFactory {

    private final Environment environment;

    /**
     * Constructor
     * 
     * @param environment
     *            {@link Environment}
     */
    public CuratorFactory(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Build a new instance of a {@link CuratorFramework} and register a health
     * check and make sure it's properly managed.
     * 
     * @param config
     *            {@link DiscoveryFactory}
     * @return {@link CuratorFramework}
     */
    public CuratorFramework build(final DiscoveryFactory config) {
        final CuratorFramework framework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(
                        (int) config.getConnectionTimeout().toMilliseconds())
                .sessionTimeoutMs(
                        (int) config.getSessionTimeout().toMilliseconds())
                .retryPolicy(config.getRetryPolicy())
                .compressionProvider(config.getCompressionProvider())
                .connectString(config.getQuorumSpec())
                .canBeReadOnly(config.isReadOnly())
                .namespace(config.getNamespace()).build();

        environment.lifecycle().manage(new CuratorManager(framework));
        environment.healthChecks().register("curator",
                new CuratorHealthCheck(framework));
        return framework;
    }
}
