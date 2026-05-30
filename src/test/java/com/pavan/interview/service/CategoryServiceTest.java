package com.pavan.interview.service;

import com.pavan.interview.model.QuestionCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryServiceTest {

    private final CategoryService categoryService = new CategoryService();

    @Test
    void categorizesSpringQuestions() {
        QuestionCategory category = categoryService.categorize(
                "What is dependency injection in Spring Boot?",
                "Spring injects bean dependencies into classes."
        );

        assertThat(category).isEqualTo(QuestionCategory.SPRING);
    }

    @Test
    void categorizesDatabaseQuestions() {
        QuestionCategory category = categoryService.categorize(
                "What is an index in PostgreSQL?",
                "An index improves SQL query lookup performance."
        );

        assertThat(category).isEqualTo(QuestionCategory.DATABASE);
    }
}
