package com.seeds.account.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 *
 * @author milo
 *
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    public static String writeValue(Object obj)  {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch(Exception e) {
            throw new IllegalArgumentException("not able to convert object to json", e);
        }
    }

    public static <T> T readValue(String s, Class<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(s, ref);
        } catch(Exception e) {
            throw new IllegalArgumentException("not able to convert json to object " + ref, e);
        }
    }

    public static <T> T readValue(String s, TypeReference<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(s, ref);
        } catch(Exception e) {
            throw new IllegalArgumentException("not able to convert json to object " + ref, e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
