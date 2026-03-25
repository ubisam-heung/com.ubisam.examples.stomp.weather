package com.ubisam.boilerplate.stomp.external;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
@Data
public class WeatherKeywordMatcher {

    protected Log logger = LogFactory.getLog(getClass());
    
    public boolean containsKeyword(JsonNode node, String keyword) {
        logger.debug("[containsKeyword]: "+ keyword);
        if (node == null) return false;
        if (node.isTextual() || node.isNumber()) {
            return node.asText().contains(keyword);
        }
        if (node.isObject()) {
            for (java.util.Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
                String field = it.next();
                if (field.contains(keyword) || containsKeyword(node.get(field), keyword)) return true;
            }
        }
        if (node.isArray()) {
            for (JsonNode item : node) {
                if (containsKeyword(item, keyword)) return true;
            }
        }
        return false;
    }
}
