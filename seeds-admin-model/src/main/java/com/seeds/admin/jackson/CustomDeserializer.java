package com.seeds.admin.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CustomDeserializer extends JsonDeserializer<Map<Long, Long>> {
    @Override
    public Map<Long, Long> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        String autoIds = node.asText();
        Map<Long, Long> map = new HashMap<>();
        try {
            map = mapper.readValue(autoIds, new TypeReference<Map<Long,Long>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }
}
