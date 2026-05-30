package com.pavan.interview.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "question_answers")
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String question;

    @Column(nullable = false, columnDefinition = "text")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private QuestionCategory category;

    @Column(nullable = false)
    private int frequencyCount = 1;

    @Column(nullable = false, columnDefinition = "text")
    private String embedding;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    protected QuestionAnswer() {
    }

    public QuestionAnswer(String question, String answer, QuestionCategory category, String embedding) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.embedding = embedding;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public String getEmbedding() {
        return embedding;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void increaseFrequency() {
        this.frequencyCount++;
        this.updatedAt = Instant.now();
    }
}
