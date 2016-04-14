package io.dropwizard.discovery.manage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorManagerTest {

    private final CuratorFramework framework = mock(CuratorFramework.class);
    private final CreateBuilder create = mock(CreateBuilder.class);
    private final ExistsBuilder exists = mock(ExistsBuilder.class);
    @SuppressWarnings("unchecked")
    private final ProtectACLCreateModePathAndBytesable<String> acl = mock(
            ProtectACLCreateModePathAndBytesable.class);
    private final CuratorManager manager = new CuratorManager(framework);

    @Before
    public void setUp() {
        when(framework.create()).thenReturn(create);
        when(framework.checkExists()).thenReturn(exists);
        when(create.creatingParentsIfNeeded()).thenReturn(acl);
    }

    @After
    public void tearDown() {
        reset(framework);
    }

    @Test
    public void testStartRootExists() throws Exception {
        when(exists.forPath("/")).thenReturn(new Stat());
        manager.start();
        verify(framework, never()).create();
    }

    @Test
    public void testStartRootMissing() throws Exception {
        when(exists.forPath("/")).thenReturn(null);
        manager.start();
        verify(framework).create();
    }

    @Test
    public void testStop() throws Exception {
        manager.stop();
        verify(framework).close();
    }
}
