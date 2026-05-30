package com.pavan.interview.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateQuestionRequest(
        @NotBlank(message = "question is required")
        String question,

        @NotBlank(message = "answer is required")
        String answer
) {
}
