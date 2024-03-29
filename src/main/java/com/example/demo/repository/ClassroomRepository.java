package com.example.demo.repository;

import java.util.List;

import com.example.demo.models.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>, JpaSpecificationExecutor<Classroom> {
    @Query(
        value="SELECT course_id from classrooms where id =?1",
        nativeQuery = true
    )
    Long getCourseId(Long id);

    @Query(
        value="select courses.id as courseId, course_name, classrooms.id as classroomId," +
        " classroom_name, school_name, profiles.full_name, enrollments.status, " +
        " classrooms.college, classrooms.department, classrooms.branch, classrooms.session, classrooms.category," +
        " DATE_FORMAT(classrooms.start_date, '%d %m %Y') as 'start_date'," +
        " DATE_FORMAT(classrooms.end_date, '%d %m %Y') as 'end_date'"+
        " from courses" +
        " join profiles on courses.user = profiles.username" +
        " join classrooms on courses.id=classrooms.course_id" +
        " left join  enrollments on classrooms.id = enrollments.classroom_id and enrollments.username= ?1" +
        " where course_name like ?2" +
        " and classrooms.school_name like ?3" +
        " and profiles.full_name like ?4" +
        " and classrooms.active = true",
        nativeQuery = true
    )
    List<classroomSearch> searchForClassroom(String username, String courseName, String schoolName, String teacherName);

    public static interface classroomSearch{
        Long getCourseId();
        String getCourse_name();
        Long getClassroomId();
        String getClassroom_name();
        String getSchool_name();
        String getFull_name();
        String getStatus();
        String getCollege();
        String getDepartment();
        String getBranch();
        String getSession();
        String getCategory();
        String getStart_date();
        String getEnd_date();
    }

    
}

