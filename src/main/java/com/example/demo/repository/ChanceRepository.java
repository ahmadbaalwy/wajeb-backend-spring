package com.example.demo.repository;

import java.util.List;

import com.example.demo.models.Chance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChanceRepository extends JpaRepository<Chance,Long> {
    @Query(
        value="select * from chances where username=?1 and quizz_id=?2",
        nativeQuery = true
    )
    List<Chance> getStudentChances(String username, Long quizzId);

    @Query(
        value="select * from chances where status='pending' and username=?1 and quizz_id=?2",
        nativeQuery = true
    )
    List<Chance> getStudentPendingChances(String username, Long quizzId);

    @Query(
        value="select id, status, update_date, username, quizz_id, grade, DATE_FORMAT(grade_date, '%Y-%m-%dT%TZ') as 'grade_date'" +
        " from chances"+
        " where status='submitted' and username=?1 and quizz_id=?2",
        nativeQuery = true
    )
    List<chanceData> getStudentSubmittedChances(String username, Long quizzId);

    @Query(
        value="select quizz_id from chances where id=?1",
        nativeQuery = true
    )
    Long getQuizzId(Long chanceId);

    @Query(
        value="SELECT" + 
                    " SUM(answer_score)" +
                " FROM" +
                    " (SELECT" + 
                        " chances.id AS 'chance_id'," +
                            " questions.id AS 'question_id'," +
                            " IF(SUM(IF(chance_answers.selected = quizz_answers.correct, 1, 0)) = COUNT(quizz_answers.id), questions.score, 0) AS 'answer_score'" +
                    " FROM" +
                        " chance_answers" +
                    " JOIN quizz_answers ON chance_answers.quizz_answer_id = quizz_answers.id" +
                    " JOIN questions ON quizz_answers.question_id = questions.id" +
                    " JOIN chances ON chance_answers.chance_id = chances.id" +
                    " WHERE" +
                        " chances.id = ?1" +
                    " GROUP BY questions.id) AS scoreOfAnswers",
            nativeQuery = true
    )
    int calculateChanceGrade(Long chanceId);

    @Query(
        value="select MAX(grade) as score, profiles.full_name" +
        " from chances" +
        " join profiles on chances.username = profiles.username" +
        " where chances.quizz_id=?1" +
        " group by chances.username, profiles.full_name;",
        nativeQuery = true
    )
    List<studentsScores> getStudentsScores(Long quizzId);

    public static interface chanceData{
        Long getId();
        String getStatus();
        String getUpdate_date();
        String getUsername();
        Long getQuizz_id();
        int getGrade();
        String getGrade_date();
    }

    public static interface studentsScores{
        String getFull_name();
        int getScore();
    }


}
