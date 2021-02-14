package com.example.demo.repository;

import java.util.Optional;
import com.example.demo.models.QuestionPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPhotoRepository extends JpaRepository<QuestionPhoto, Long> {
    Optional<QuestionPhoto> findByName(String name);
}