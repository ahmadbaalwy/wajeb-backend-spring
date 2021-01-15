package com.example.demo.repository;

import java.util.List;

import com.example.demo.models.QuizzAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizzAnswerRepository extends JpaRepository<QuizzAnswer, Long>, JpaSpecificationExecutor<QuizzAnswer> {
    @Query(
        value="SELECT question_id from quizz_answers where id =?1",
        nativeQuery = true
    )
    Long getQuestionId(Long id);

    @Query(
        value="SELECT * from quizz_answers where question_id =?1 order by sequence",
        nativeQuery = true
    )
    List<QuizzAnswer> getQuizzAnswersSortedBySequence(Long questionId);
}

