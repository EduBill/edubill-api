package com.edubill.edubillApi.user.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "STUDENTS")
public class Student {

    protected Student() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_number")
    private String studentNumber;

    private Long studentGroupId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<AcademyStudent> academyStudents = new ArrayList<>();

}
