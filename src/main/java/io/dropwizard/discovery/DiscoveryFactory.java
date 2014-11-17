package io.dropwizard.discovery;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MinDuration;
import io.dropwizard.validation.PortRange;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

public class DiscoveryFactory {

    /**
     * An enumeration of the available compression codecs available for
     * compressed entries.
     * 
     * @see #getCompressionProvider()
     * @see CompressionProvider
     */
    enum CompressionCodec {

        /**
         * GZIP compression.
         * 
         * @see GzipCompressionProvider
         */
        GZIP(new GzipCompressionProvider());

        final private CompressionProvider provider;

        CompressionCodec(@Nonnull final CompressionProvider provider) {
            this.provider = provider;
        }

        /**
         * Gets the {@link CompressionProvider} for this codec.
         * 
         * @return the provider for this codec.
         */
        public CompressionProvider getProvider() {
            return provider;
        }
    }

    @NotEmpty
    private String[] hosts = new String[] { "localhost" };

    @PortRange
    private int port = 2181;

    @NotEmpty
    private String serviceName;

    @NotNull
    private String listenAddress = "";

    @NotEmpty
    private String namespace = "dropwizard";

    @NotEmpty
    private String basePath = "service";

    @NotNull
    @MinDuration(value = 1, unit = TimeUnit.MILLISECONDS)
    private Duration connectionTimeout = Duration.seconds(6);

    @NotNull
    @MinDuration(value = 1, unit = TimeUnit.MILLISECONDS)
    private Duration sessionTimeout = Duration.seconds(6);

    @NotNull
    @MinDuration(value = 1, unit = TimeUnit.MILLISECONDS)
    private Duration baseSleepTime = Duration.seconds(1);

    @Min(0)
    @Max(29)
    private int maxRetries = 5;

    @NotNull
    private CompressionCodec compression = CompressionCodec.GZIP;

    @NotNull
    private Boolean isReadOnly = false;

    @NotNull
    private Boolean isDisabled = false;

    @JsonProperty
    public String[] getHosts() {
        return hosts;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    /**
     * Retrieves a formatted specification of the ZooKeeper quorum..
     * 
     * The specification is formatted as: host1:port,host2:port[,hostN:port]
     * 
     * @return a specification of the ZooKeeper quorum, formatted as a String
     */
    @JsonIgnore
    public String getQuorumSpec() {
        return Joiner.on(":" + getPort() + ",").skipNulls()
                .appendTo(new StringBuilder(), getHosts()).append(':')
                .append(getPort()).toString();
    }

    @JsonProperty
    public String getServiceName() {
        return serviceName;
    }

    @JsonProperty
    public void setServiceName(@Nonnull final String serviceName) {
        this.serviceName = checkNotNull(serviceName);
    }

    @JsonProperty
    public String getNamespace() {
        return namespace;
    }

    @JsonProperty
    public void setNamespace(@Nonnull final String namespace) {
        this.namespace = checkNotNull(namespace);
    }

    @JsonProperty
    public String getBasePath() {
        return basePath;
    }

    @JsonProperty
    public void setBasePath(@Nonnull final String basePath) {
        this.basePath = checkNotNull(basePath);
    }

    @JsonProperty
    public String getListenAddress() {
        return listenAddress;
    }

    @JsonProperty
    public void setListenAddress(@Nonnull final String listenAddress) {
        this.listenAddress = checkNotNull(listenAddress);
    }

    @JsonProperty
    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    @JsonProperty
    public void setConnectionTimeout(@Nonnull final Duration connectionTimeout) {
        this.connectionTimeout = checkNotNull(connectionTimeout);
    }

    @JsonProperty
    public Duration getSessionTimeout() {
        return sessionTimeout;
    }

    @JsonProperty
    public void setSessionTimeout(@Nonnull final Duration sessionTimeout) {
        this.sessionTimeout = checkNotNull(sessionTimeout);
    }

    @JsonProperty
    public boolean isDisabled() {
        return isDisabled;
    }

    @JsonProperty("isDisabled")
    public void setIsDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @JsonProperty
    public boolean isReadOnly() {
        return isReadOnly;
    }

    @JsonProperty("isReadOnly")
    public void setIsReadOnly(final boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    @JsonProperty
    public int getMaxRetries() {
        return maxRetries;
    }

    @JsonProperty
    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @JsonProperty
    public Duration getBaseSleepTime() {
        return baseSleepTime;
    }

    @JsonProperty
    public void setBaseSleepTime(@Nonnull final Duration baseSleepTime) {
        this.baseSleepTime = checkNotNull(baseSleepTime);
    }

    /**
     * Returns a {@link RetryPolicy} for handling failed connection attempts.
     * 
     * Always configures an {@link ExponentialBackoffRetry} based on the
     * {@link #getMaxRetries() maximum retries} and {@link #getBaseSleepTime()
     * initial back-off} configured.
     * 
     * @return a {@link RetryPolicy} for handling failed connection attempts.
     * 
     * @see #getMaxRetries()
     * @see #getBaseSleepTime()
     */
    @JsonIgnore
    public RetryPolicy getRetryPolicy() {
        return new ExponentialBackoffRetry((int) getBaseSleepTime()
                .toMilliseconds(), getMaxRetries());
    }

    /**
     * Returns a {@link CompressionProvider} to compress values with.
     * 
     * @return the compression provider used to compress values.
     * 
     * @see #CompressionCodec
     */
    @JsonIgnore
    public CompressionProvider getCompressionProvider() {
        return compression.getProvider();
    }
}
