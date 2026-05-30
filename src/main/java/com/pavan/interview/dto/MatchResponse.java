package com.pavan.interview.dto;

public record MatchResponse(
        Long id,
        String question,
        double similarityScore
) {
}
