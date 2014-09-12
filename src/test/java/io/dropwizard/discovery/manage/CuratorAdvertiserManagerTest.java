package io.dropwizard.discovery.manage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import io.dropwizard.discovery.core.CuratorAdvertiser;
import org.junit.Test;

public class CuratorAdvertiserManagerTest {

    private final CuratorAdvertiser advertiser = mock(CuratorAdvertiser.class);
    private final CuratorAdvertiserManager manager = new CuratorAdvertiserManager(
            advertiser);

    @Test
    public void testStop() throws Exception {
        manager.stop();
        verify(advertiser).unregisterAvailability();
    }
}
