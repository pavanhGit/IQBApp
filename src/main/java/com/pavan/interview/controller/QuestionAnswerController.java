package com.pavan.interview.controller;

import com.pavan.interview.dto.CreateQuestionRequest;
import com.pavan.interview.dto.QuestionResponse;
import com.pavan.interview.dto.SaveQuestionResponse;
import com.pavan.interview.dto.SimilarityResponse;
import com.pavan.interview.model.QuestionCategory;
import com.pavan.interview.service.QuestionAnswerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/questions")
public class QuestionAnswerController {

    private final QuestionAnswerService questionAnswerService;

    public QuestionAnswerController(QuestionAnswerService questionAnswerService) {
        this.questionAnswerService = questionAnswerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaveQuestionResponse create(@Valid @RequestBody CreateQuestionRequest request) {
        return questionAnswerService.save(request);
    }

    @GetMapping
    public List<QuestionResponse> findAll(@RequestParam(required = false) QuestionCategory category) {
        return questionAnswerService.findAll(category);
    }

    @GetMapping("/{id}")
    public QuestionResponse findById(@PathVariable Long id) {
        return questionAnswerService.findById(id);
    }

    @GetMapping("/similarity")
    public SimilarityResponse checkSimilarity(@RequestParam @NotBlank String question) {
        return questionAnswerService.checkSimilarity(question);
    }
}
