package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.example.demo.models.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    @Query(
        value="SELECT quizz_id from questions where id =?1",
        nativeQuery = true
    )
    Long getQuizzId(Long id);

    @Query(
        value="SELECT * from questions where quizz_id =?1 order by sequence",
        nativeQuery = true
    )
    List<Question> getQuestionsSortedBySequence(Long quizzId);

    @Modifying
    @Query(
        value="update questions set pic_byte = ?1 where id= ?2",
            nativeQuery = true
    )
    @Transactional
    void uploadPhoto(byte[] picByte, Long questionId);

    @Query(
        value="select TO_BASE64(pic_byte) from questions where id = ?1",
        nativeQuery = true
    )
    String getQuestionPhoto(Long questionId);
}

