package com.pavan.interview.repository;

import com.pavan.interview.model.QuestionAnswer;
import com.pavan.interview.model.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    List<QuestionAnswer> findByCategoryOrderByFrequencyCountDescUpdatedAtDesc(QuestionCategory category);

    List<QuestionAnswer> findAllByOrderByUpdatedAtDesc();
}
