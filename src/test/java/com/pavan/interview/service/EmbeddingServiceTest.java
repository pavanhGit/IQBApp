package com.pavan.interview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavan.interview.config.AppProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class EmbeddingServiceTest {

    private final EmbeddingService embeddingService = new EmbeddingService(
            new ObjectMapper(),
            new AppProperties(0.82, 64)
    );

    @Test
    void generatesDeterministicEmbedding() {
        List<Double> first = embeddingService.generateEmbedding("Explain Java streams");
        List<Double> second = embeddingService.generateEmbedding("Explain Java streams");

        assertThat(first).hasSize(64);
        assertThat(first).isEqualTo(second);
    }

    @Test
    void identicalTextHasMaximumSimilarity() {
        List<Double> first = embeddingService.generateEmbedding("Spring dependency injection");
        List<Double> second = embeddingService.generateEmbedding("Spring dependency injection");

        assertThat(embeddingService.cosineSimilarity(first, second)).isCloseTo(1.0, within(0.000000001));
    }
}
