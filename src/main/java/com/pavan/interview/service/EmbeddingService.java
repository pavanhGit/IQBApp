package com.pavan.interview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavan.interview.config.AppProperties;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class EmbeddingService {

    private static final TypeReference<List<Double>> DOUBLE_LIST = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final int dimensions;

    public EmbeddingService(ObjectMapper objectMapper, AppProperties appProperties) {
        this.objectMapper = objectMapper;
        this.dimensions = appProperties.embeddingDimensions();
    }

    public List<Double> generateEmbedding(String text) {
        double[] vector = new double[dimensions];
        tokenize(text).forEach(token -> vector[indexFor(token)] += weightFor(token));
        normalize(vector);
        return Arrays.stream(vector).boxed().toList();
    }

    public double cosineSimilarity(List<Double> first, List<Double> second) {
        int size = Math.min(first.size(), second.size());
        double dotProduct = 0.0;
        double firstNorm = 0.0;
        double secondNorm = 0.0;

        for (int i = 0; i < size; i++) {
            double firstValue = first.get(i);
            double secondValue = second.get(i);
            dotProduct += firstValue * secondValue;
            firstNorm += firstValue * firstValue;
            secondNorm += secondValue * secondValue;
        }

        if (firstNorm == 0.0 || secondNorm == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(firstNorm) * Math.sqrt(secondNorm));
    }

    public String serialize(List<Double> embedding) {
        try {
            return objectMapper.writeValueAsString(embedding);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize embedding", ex);
        }
    }

    public List<Double> deserialize(String embedding) {
        try {
            return objectMapper.readValue(embedding, DOUBLE_LIST);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to deserialize embedding", ex);
        }
    }

    private List<String> tokenize(String text) {
        String normalized = text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9+#]+", " ");
        if (normalized.isBlank()) {
            return List.of();
        }
        return Arrays.stream(normalized.split("\\s+"))
                .filter(token -> token.length() > 1)
                .toList();
    }

    private int indexFor(String token) {
        byte[] digest = sha256(token);
        int value = ((digest[0] & 0xff) << 24)
                | ((digest[1] & 0xff) << 16)
                | ((digest[2] & 0xff) << 8)
                | (digest[3] & 0xff);
        return Math.floorMod(value, dimensions);
    }

    private double weightFor(String token) {
        byte[] digest = sha256("weight:" + token);
        return (digest[0] & 1) == 0 ? 1.0 : -1.0;
    }

    private byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }

    private void normalize(double[] vector) {
        double norm = 0.0;
        for (double value : vector) {
            norm += value * value;
        }
        if (norm == 0.0) {
            return;
        }
        double length = Math.sqrt(norm);
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] / length;
        }
    }
}
