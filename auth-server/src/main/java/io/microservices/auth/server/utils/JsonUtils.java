package io.microservices.auth.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public final class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> List<T> readList(InputStream stream, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(stream, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, type));
        } catch(Exception e) {
            LOG.error("Stream to List json conversion error", e);
            return null;
        }
    }

    private JsonUtils() {}
}
