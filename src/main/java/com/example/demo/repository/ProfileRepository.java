package com.example.demo.repository;


import com.example.demo.models.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    @Query(
        value="SELECT role from profiles where username =?1",
        nativeQuery = true
    )
    String getRole(String user);

    @Query(
        value="SELECT full_name from profiles where username =?1",
        nativeQuery = true
    )
    String getFullName(String user);

    
}

