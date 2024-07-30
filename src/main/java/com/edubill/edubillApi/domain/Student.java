package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.DepartmentType;
import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.student.StudentInfoRequestDto;
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
    @Builder.Default
    private List<StudentGroup> studentGroups = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PaymentKey> paymentKeyList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Builder.Default
    private List<AcademyStudent> academyStudents = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudentPaymentHistory> studentPaymentHistories = new ArrayList<>();

    public Student(StudentInfoRequestDto studentInfoRequestDto) {
        this.studentName = studentInfoRequestDto.getStudentName();
        this.studentPhoneNumber = studentInfoRequestDto.getStudentPhoneNumber();
        this.parentName = studentInfoRequestDto.getParentName();
        this.parentPhoneNumber = studentInfoRequestDto.getParentPhoneNumber();
        this.schoolType = studentInfoRequestDto.getSchoolType();
        this.gradeLevel = studentInfoRequestDto.getGradeLevel();
        this.departmentType = studentInfoRequestDto.getDepartmentType();
        this.schoolName = studentInfoRequestDto.getSchoolName();
        this.memo = studentInfoRequestDto.getMemo();
        this.studentGroups = new ArrayList<>(); //빌더패턴을 사용하지 않기 때문에 직접 초기화
    }
    public static Student from(StudentInfoRequestDto studentInfoRequestDto) {
      return new Student(studentInfoRequestDto);
    }
}
