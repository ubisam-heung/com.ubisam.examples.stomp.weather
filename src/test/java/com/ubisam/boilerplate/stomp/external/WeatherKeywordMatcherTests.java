package com.ubisam.boilerplate.stomp.external;

import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WeatherKeywordMatcherTests {
    @Test
    void containsKeyword_null_returnsFalse() {
        WeatherKeywordMatcher matcher = new WeatherKeywordMatcher();
        assertFalse(matcher.containsKeyword(null, "key"));
    }

    @Test
    void containsKeyword_textual_true() {
        WeatherKeywordMatcher matcher = new WeatherKeywordMatcher();
        JsonNode node = Mockito.mock(JsonNode.class);
        Mockito.when(node.isTextual()).thenReturn(true);
        Mockito.when(node.asText()).thenReturn("abc-key-xyz");
        assertTrue(matcher.containsKeyword(node, "key"));
    }

    @Test
    void containsKeyword_textual_false() {
        WeatherKeywordMatcher matcher = new WeatherKeywordMatcher();
        JsonNode node = Mockito.mock(JsonNode.class);
        Mockito.when(node.isTextual()).thenReturn(true);
        Mockito.when(node.asText()).thenReturn("abc-xyz");
        assertFalse(matcher.containsKeyword(node, "key"));
    }

    @Test
    void containsKeyword_object_true() {
        WeatherKeywordMatcher matcher = new WeatherKeywordMatcher();
        JsonNode node = Mockito.mock(JsonNode.class);
        Mockito.when(node.isTextual()).thenReturn(false);
        Mockito.when(node.isNumber()).thenReturn(false);
        Mockito.when(node.isObject()).thenReturn(true);
        Iterator<String> it = Arrays.asList("field-key").iterator();
        Mockito.when(node.fieldNames()).thenReturn(it);
        Mockito.when(node.get("field-key")).thenReturn(null);
        assertTrue(matcher.containsKeyword(node, "key"));
    }

    @Test
    void containsKeyword_array_true() {
        WeatherKeywordMatcher matcher = new WeatherKeywordMatcher();
        JsonNode node = Mockito.mock(JsonNode.class);
        Mockito.when(node.isTextual()).thenReturn(false);
        Mockito.when(node.isNumber()).thenReturn(false);
        Mockito.when(node.isObject()).thenReturn(false);
        Mockito.when(node.isArray()).thenReturn(true);
        List<JsonNode> list = Arrays.asList(Mockito.mock(JsonNode.class));
        Mockito.when(node.iterator()).thenReturn(list.iterator());
        WeatherKeywordMatcher matcherSpy = Mockito.spy(matcher);
        Mockito.doReturn(true).when(matcherSpy).containsKeyword(list.get(0), "key");
        assertTrue(matcherSpy.containsKeyword(node, "key"));
    }
}

