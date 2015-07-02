package io.dropwizard.discovery.manage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import io.dropwizard.discovery.core.CuratorAdvertiser;
import io.dropwizard.discovery.core.InstanceMetadata;
import org.junit.Test;

public class CuratorAdvertiserManagerTest {

    private final CuratorAdvertiser<InstanceMetadata> advertiser = mock(CuratorAdvertiser.class);
    private final CuratorAdvertiserManager<InstanceMetadata> manager = new CuratorAdvertiserManager<InstanceMetadata>(
            advertiser);

    @Test
    public void testStop() throws Exception {
        manager.stop();
        verify(advertiser).unregisterAvailability();
    }
}
