package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.models.ChanceAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChanceAnswerRepository extends JpaRepository<ChanceAnswer, Long>{
    @Modifying
    @Query(
        value="insert into chance_answers(chance_id, quizz_answer_id)" +  
        " (SELECT" +
            " chances.id as 'chance_id', quizz_answers.id as 'quizzAnswer_id'" +
        " FROM" +
            " chances" +
               " JOIN" +
            " quizzes on quizzes.id = chances.quizz_id" +
            " join" +
            " questions on questions.quizz_id = quizzes.id" +
            " join" +
            " quizz_answers on quizz_answers.question_id = questions.id" +
            " where chances.id = ?1);",
            nativeQuery = true
    )
    @Transactional
    void newChanceAnswers(Long chanceId);

    @Query(
        value="select chance_answers.id as 'chanceAnswerId', quizz_answers.text as 'chanceAnswerText' , chance_answers.selected as 'chanceAnswerSelected'" +
        " from chance_answers" +
        " join quizz_answers on chance_answers.quizz_answer_id = quizz_answers.id" +
        " join questions on quizz_answers.question_id = questions.id" +
        " join quizzes on questions.quizz_id = quizzes.id" +
        " where chance_answers.chance_id = ?1 and questions.id = ?2",
        nativeQuery = true
    )
    List<chanceAnswerData> getChanceAnswerData(Long chanceId, Long questionId);

    public static interface chanceAnswerData{
        Long getChanceAnswerId();
        String getChanceAnswerText();
        boolean getChanceAnswerSelected();
    }

}
