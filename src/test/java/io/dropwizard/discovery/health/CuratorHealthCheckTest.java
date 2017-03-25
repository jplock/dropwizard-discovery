package io.dropwizard.discovery.health;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

public class CuratorHealthCheckTest {

    private final CuratorFramework framework = mock(CuratorFramework.class);
    private final ExistsBuilder exists = mock(ExistsBuilder.class);
    private final CuratorHealthCheck health = new CuratorHealthCheck(framework);

    @Before
    public void setUp() {
        when(framework.checkExists()).thenReturn(exists);
    }

    @Test
    public void testCheckHealthy() throws Exception {
        when(framework.getState()).thenReturn(CuratorFrameworkState.STARTED);
        when(exists.forPath(anyString())).thenReturn(new Stat());
        assertThat(health.check().isHealthy()).isTrue();
    }

    @Test
    public void testCheckNotStarted() throws Exception {
        assertThat(health.check().isHealthy()).isFalse();
    }

    @Test
    public void testCheckMissingRoot() throws Exception {
        when(framework.getState()).thenReturn(CuratorFrameworkState.STARTED);
        when(exists.forPath(anyString())).thenReturn(null);
        assertThat(health.check().isHealthy()).isFalse();
    }
}
