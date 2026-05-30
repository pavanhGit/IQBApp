package com.pavan.interview.dto;

import java.util.List;

public record SimilarityResponse(
        String inputQuestion,
        List<Double> embedding,
        MatchResponse bestMatch
) {
}
