package com.pavan.interview.dto;

import com.pavan.interview.model.QuestionAnswer;
import com.pavan.interview.model.QuestionCategory;

import java.time.Instant;
import java.util.List;

public record QuestionResponse(
        Long id,
        String question,
        String answer,
        QuestionCategory category,
        int frequencyCount,
        List<Double> embedding,
        Instant createdAt,
        Instant updatedAt
) {

    public static QuestionResponse from(QuestionAnswer questionAnswer, List<Double> embedding) {
        return new QuestionResponse(
                questionAnswer.getId(),
                questionAnswer.getQuestion(),
                questionAnswer.getAnswer(),
                questionAnswer.getCategory(),
                questionAnswer.getFrequencyCount(),
                embedding,
                questionAnswer.getCreatedAt(),
                questionAnswer.getUpdatedAt()
        );
    }
}
