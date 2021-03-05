package com.example.demo.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
	@Size(max = 50)
    private String classroomName;

    //needs approval to join or not
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean Private;

    //is still visible in searches
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean Active;

    private String schoolName;

    private String college;

    private String department;

    private String branch;

    private String session;

    private String category;

    private Date startDate;

    private Date endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    @NotBlank
    private Course course;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "classroom")
    @JsonManagedReference
    private Set<Quizz> quizzes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "classroom")
    @JsonManagedReference
    private Set<Enrollment> enrollments;

    public Classroom(@NotBlank @Size(max = 50) String classroomName, boolean Private, boolean Active,
            String schoolName, String category, Date startDate, Date endDate) {
        this.classroomName = classroomName;
        this.Private = Private;
        this.Active = Active;
        this.schoolName = schoolName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Classroom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public boolean isPrivate() {
        return Private;
    }

    public void setPrivate(boolean private1) {
        Private = private1;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Quizz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Set<Quizz> quizzes) {
        this.quizzes = quizzes;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Classroom(@NotBlank @Size(max = 50) String classroomName, boolean private1, boolean active,
            String schoolName, String college, String department, String branch, String session, String category,
            Date startDate, Date endDate) {
        this.classroomName = classroomName;
        Private = private1;
        Active = active;
        this.schoolName = schoolName;
        this.college = college;
        this.department = department;
        this.branch = branch;
        this.session = session;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    
}
