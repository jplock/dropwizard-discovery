package io.dropwizard.discovery.core;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.dropwizard.validation.PortRange;

@Immutable
public final class InstanceMetadata {

    @NotNull
    private final UUID instanceId;

    @NotEmpty
    private final String listenAddress;

    @PortRange
    private final int listenPort;

    @Nullable
    @PortRange
    private final Integer adminPort;

    @JsonCreator
    public InstanceMetadata(@JsonProperty("instanceId") final UUID instanceId,
            @JsonProperty("listenAddress") final String listenAddress,
            @JsonProperty("listenPort") final int listenPort,
            @JsonProperty("adminPort") final Optional<Integer> adminPort) {
        this.instanceId = instanceId;
        this.listenAddress = listenAddress;
        this.listenPort = listenPort;
        if (adminPort == null) {
            this.adminPort = null;
        } else {
            this.adminPort = adminPort.orElse(null);
        }
    }

    @JsonProperty
    public UUID getInstanceId() {
        return instanceId;
    }

    @JsonProperty
    public String getListenAddress() {
        return listenAddress;
    }

    @JsonProperty
    public int getListenPort() {
        return listenPort;
    }

    @JsonProperty
    public Optional<Integer> getAdminPort() {
        return Optional.ofNullable(adminPort);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final InstanceMetadata other = (InstanceMetadata) obj;
        return Objects.equals(instanceId, other.instanceId)
                && Objects.equals(listenAddress, other.listenAddress)
                && Objects.equals(listenPort, other.listenPort)
                && Objects.equals(adminPort, other.adminPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, listenAddress, listenPort, adminPort);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("instanceId", instanceId)
                .add("listenAddress", listenAddress)
                .add("listenPort", listenPort).add("adminPort", adminPort)
                .toString();
    }
}
