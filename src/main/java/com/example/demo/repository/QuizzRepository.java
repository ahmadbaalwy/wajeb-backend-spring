package com.example.demo.repository;

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
}

