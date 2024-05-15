package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "STUDENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Student extends BaseEntity{ //TODO: flyway수정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    //TODO: flyway 수정
    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_phone_number")
    private String studentPhoneNumber;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "parent_phone_number")
    private String parentPhoneNumber;

    //TODO: flyway수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<AcademyStudent> academyStudents = new ArrayList<>();

}
