package com.pavan.interview.service;

import com.pavan.interview.config.AppProperties;
import com.pavan.interview.dto.CreateQuestionRequest;
import com.pavan.interview.dto.MatchResponse;
import com.pavan.interview.dto.QuestionResponse;
import com.pavan.interview.dto.SaveQuestionResponse;
import com.pavan.interview.dto.SimilarityResponse;
import com.pavan.interview.model.QuestionAnswer;
import com.pavan.interview.model.QuestionCategory;
import com.pavan.interview.repository.QuestionAnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionAnswerService {

    private final QuestionAnswerRepository repository;
    private final CategoryService categoryService;
    private final EmbeddingService embeddingService;
    private final double similarityThreshold;

    public QuestionAnswerService(
            QuestionAnswerRepository repository,
            CategoryService categoryService,
            EmbeddingService embeddingService,
            AppProperties appProperties
    ) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.embeddingService = embeddingService;
        this.similarityThreshold = appProperties.similarityThreshold();
    }

    @Transactional
    public SaveQuestionResponse save(CreateQuestionRequest request) {
        List<Double> embedding = embeddingService.generateEmbedding(request.question());
        Optional<SimilarityMatch> bestMatch = findBestMatch(embedding);

        if (bestMatch.isPresent() && bestMatch.get().score() >= similarityThreshold) {
            QuestionAnswer existing = bestMatch.get().questionAnswer();
            existing.increaseFrequency();
            QuestionAnswer saved = repository.save(existing);
            return new SaveQuestionResponse(
                    true,
                    bestMatch.get().score(),
                    QuestionResponse.from(saved, embeddingService.deserialize(saved.getEmbedding()))
            );
        }

        QuestionCategory category = categoryService.categorize(request.question(), request.answer());
        QuestionAnswer created = new QuestionAnswer(
                request.question(),
                request.answer(),
                category,
                embeddingService.serialize(embedding)
        );
        QuestionAnswer saved = repository.save(created);
        return new SaveQuestionResponse(false, 0.0, QuestionResponse.from(saved, embedding));
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findAll(QuestionCategory category) {
        List<QuestionAnswer> questions = category == null
                ? repository.findAllByOrderByUpdatedAtDesc()
                : repository.findByCategoryOrderByFrequencyCountDescUpdatedAtDesc(category);

        return questions.stream()
                .map(question -> QuestionResponse.from(question, embeddingService.deserialize(question.getEmbedding())))
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse findById(Long id) {
        QuestionAnswer questionAnswer = repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));
        return QuestionResponse.from(questionAnswer, embeddingService.deserialize(questionAnswer.getEmbedding()));
    }

    @Transactional(readOnly = true)
    public SimilarityResponse checkSimilarity(String question) {
        List<Double> embedding = embeddingService.generateEmbedding(question);
        MatchResponse matchResponse = findBestMatch(embedding)
                .map(match -> new MatchResponse(
                        match.questionAnswer().getId(),
                        match.questionAnswer().getQuestion(),
                        match.score()
                ))
                .orElse(null);

        return new SimilarityResponse(question, embedding, matchResponse);
    }

    private Optional<SimilarityMatch> findBestMatch(List<Double> embedding) {
        return repository.findAll().stream()
                .map(questionAnswer -> new SimilarityMatch(
                        questionAnswer,
                        embeddingService.cosineSimilarity(
                                embedding,
                                embeddingService.deserialize(questionAnswer.getEmbedding())
                        )
                ))
                .max(Comparator.comparingDouble(SimilarityMatch::score));
    }

    private record SimilarityMatch(QuestionAnswer questionAnswer, double score) {
    }
}
