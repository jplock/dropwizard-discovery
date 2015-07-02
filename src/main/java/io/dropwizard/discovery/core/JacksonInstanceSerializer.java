package io.dropwizard.discovery.core;

import java.io.ByteArrayOutputStream;
import javax.annotation.Nonnull;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JacksonInstanceSerializer<T> implements InstanceSerializer<T> {

    private final TypeReference<ServiceInstance<T>> typeRef;
    private final ObjectWriter writer;
    private final ObjectReader reader;

    /**
     * Constructor
     *
     * @param mapper
     *            {@link ObjectMapper}
     * @param typeRef
     *            {@link TypeReference}
     */
    public JacksonInstanceSerializer(@Nonnull final ObjectMapper mapper,
            @Nonnull final TypeReference<ServiceInstance<T>> typeRef) {
        this.reader = mapper.reader();
        this.writer = mapper.writer();
        this.typeRef = typeRef;
    }

    @Override
    public ServiceInstance<T> deserialize(final byte[] bytes) throws Exception {
        return reader.forType(typeRef).readValue(bytes);
    }

    @Override
    public byte[] serialize(final ServiceInstance<T> instance) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.writeValue(out, instance);
        return out.toByteArray();
    }
}
