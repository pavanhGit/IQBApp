package com.pavan.interview.dto;

public record SaveQuestionResponse(
        boolean duplicate,
        double similarityScore,
        QuestionResponse question
) {
}
