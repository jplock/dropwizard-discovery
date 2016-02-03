package io.dropwizard.discovery.manage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.api.ProtectACLCreateModeStatPathAndBytesable;
import org.junit.Before;
import org.junit.Test;

public class CuratorManagerTest {

    private final CuratorFramework framework = mock(CuratorFramework.class);
    private final CreateBuilder builder = mock(CreateBuilder.class);
    @SuppressWarnings("unchecked")
    private final ProtectACLCreateModeStatPathAndBytesable<String> acl = mock(
            ProtectACLCreateModeStatPathAndBytesable.class);
    private final CuratorManager manager = new CuratorManager(framework);

    @Before
    public void setUp() {

        when(framework.create()).thenReturn(builder);
        when(builder.creatingParentsIfNeeded()).thenReturn(acl);
    }

    @Test
    public void testStart() throws Exception {
        manager.start();
        verify(framework).create();
    }

    @Test
    public void testStop() throws Exception {
        manager.stop();
        verify(framework).close();
    }
}
