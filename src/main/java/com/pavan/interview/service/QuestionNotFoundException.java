package com.pavan.interview.service;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(Long id) {
        super("Question not found with id: " + id);
    }
}
