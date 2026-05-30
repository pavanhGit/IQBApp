package com.pavan.interview.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        double similarityThreshold,
        int embeddingDimensions
) {
}
