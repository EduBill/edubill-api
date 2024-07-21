package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
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
public class Student extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_phone_number")
    private String studentPhoneNumber;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "parent_phone_number")
    private String parentPhoneNumber;

    @Column(name = "school_type")
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Column(name = "department_type")
    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "memo")
    private String memo;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentGroup> studentGroups = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<PaymentKey> paymentKeyList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<AcademyStudent> academyStudents = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentPaymentHistory> studentPaymentHistories = new ArrayList<>();
}
