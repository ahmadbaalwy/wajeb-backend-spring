package com.example.demo.repository;

import java.util.List;

import com.example.demo.models.Enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query(
        value="SELECT" +
        " enrollments.status," +
        " classrooms.id as 'classroomId'," +
        " classrooms.classroom_name," +
        " classrooms.school_name," +
        " courses.course_name," +
        " profiles.full_name," +
        " DATE_FORMAT(enrollments.request_date, '%d %m %Y') as 'request_date'," +
        " DATE_FORMAT(enrollments.approval_date, '%d %m %Y') as 'approval_date'" +
    " FROM" +
        " enrollments" +
            " JOIN" +
        " classrooms ON enrollments.classroom_id = classrooms.id" +
            " JOIN" +
        " courses ON classrooms.course_id = courses.id" +
            " JOIN" +
        " profiles ON courses.user = profiles.username" +
    " WHERE" +
        " enrollments.username = ?1",
        nativeQuery = true
    )
    List<enrollmentsDetails> getMyEnrollments(String username);

    @Query(
        value="SELECT" +
        " enrollments.status," +
        " enrollments.id as 'enrollmentId'," +
        " classrooms.id as 'classroomId'," +
        " classrooms.classroom_name," +
        " classrooms.school_name," +
        " courses.course_name," +
        " profiles.full_name," +
        " DATE_FORMAT(enrollments.request_date, '%d %m %Y') as 'request_date'," +
        " DATE_FORMAT(enrollments.approval_date, '%d %m %Y') as 'approval_date'" +
    " FROM" +
        " enrollments" +
            " JOIN" +
        " classrooms ON enrollments.classroom_id = classrooms.id" +
            " JOIN" +
        " courses ON classrooms.course_id = courses.id" +
            " JOIN" +
        " profiles ON enrollments.username = profiles.username" +
    " WHERE" +
        " enrollments.classroom_id = ?1 and enrollments.status='pending'",
        nativeQuery = true
    )
    List<enrollmentsDetails> getPendingEnrollments(Long classroomId);

    @Query(
        value="SELECT" +
        " enrollments.status," +
        " enrollments.id as 'enrollmentId'," +
        " classrooms.id as 'classroomId'," +
        " classrooms.classroom_name," +
        " classrooms.school_name," +
        " courses.course_name," +
        " profiles.full_name," +
        " DATE_FORMAT(enrollments.request_date, '%d %m %Y') as 'request_date'," +
        " DATE_FORMAT(enrollments.approval_date, '%d %m %Y') as 'approval_date'" +
    " FROM" +
        " enrollments" +
            " JOIN" +
        " classrooms ON enrollments.classroom_id = classrooms.id" +
            " JOIN" +
        " courses ON classrooms.course_id = courses.id" +
            " JOIN" +
        " profiles ON enrollments.username = profiles.username" +
    " WHERE" +
        " enrollments.classroom_id = ?1 and enrollments.status='approved'",
        nativeQuery = true
    )
    List<enrollmentsDetails> getApprovedEnrollments(Long classroomId);


    
    public static interface enrollmentsDetails{
        String getStatus();
        Long getEnrollmentId();
        Long getClassroomId();
        String getClassroom_name();
        String getSchool_name();
        String getCourse_name();
        String getFull_name();
        String getRequest_date();
        String getApproval_date();
    }
}

