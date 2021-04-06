package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import com.example.demo.models.Quizz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, Long>, JpaSpecificationExecutor<Quizz> {
    @Query(
        value="SELECT classroom_id from quizzes where id =?1",
        nativeQuery = true
    )
    Long getClassroomId(Long id);

    @Query(
        value="select quizzes.max_chances from quizzes where quizzes.id = ?1",
        nativeQuery = true
    )
    Long getMaxAllowedChances(Long id);

    @Query(
        value="select SUM(score) from questions where quizz_id = ?1",
        nativeQuery = true
    )
    Long getMaxScore(Long quizz_id);

    @Query(
        value="SELECT" +
    " courses.course_name AS 'courseName'," +
    " classrooms.id AS 'classroomId'," +
    " classrooms.classroom_name as 'classroomName'," +
    " quizzes.id AS 'quizzId'," +
    " quizzes.quizz_name AS 'quizzName'," +
    " quizzes.creation_date AS 'creationDate'," +
    " enrollments.username," +
    " chances.status AS 'chanceStatus'," +
    " chances.id AS 'chanceId'" +
" FROM" +
    " courses JOIN classrooms ON classrooms.course_id = courses.id" +
        " JOIN" +
    " quizzes ON classrooms.id = quizzes.classroom_id" +
        " JOIN" +
    " enrollments ON classrooms.id = enrollments.classroom_id" +
        " LEFT JOIN" +
    " chances ON quizzes.id = chances.quizz_id" +
        " AND enrollments.username = chances.username" +
" WHERE" +
    " enrollments.username = ?1" +
        " AND quizzes.id NOT IN (SELECT" + 
            " chances.quizz_id" +
        " FROM" +
            " chances" +
        " WHERE" +
            " chances.username = ?1" +
                " AND chances.status = 'submitted')",
            nativeQuery = true
    )
    List<newQuizzes> getNewQuizzes(String username);

    public static interface newQuizzes{
        String getCourseName();
        Long getClassroomId();
        String getClassroomName();
        Long getQuizzId();
        String getQuizzName();
        Date getCreationDate();
        String getUsername();
        String getChanceStatus();
        Long getChanceId();
    }
}

