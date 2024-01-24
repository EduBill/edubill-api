package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ACADEMY_STUDENTS", uniqueConstraints = @UniqueConstraint(columnNames = {"academy_id", "student_id"}))
public class AcademyStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academy_student_id")
    private Long academyStudentId;

    @Column(name = "academy_id")
    private Long academyId;

    @Column(name = "student_id")
    private Long studentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "academy_id", insertable = false, updatable = false)
    private Academy academy;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;


    public AcademyStudent(Long academyId, Long studentId) {
        this.academyId = academyId;
        this.studentId = studentId;
    }

    //==연관관계 메서드==//
    public void setAcademy(Academy academy) {
        this.academy = academy;
        academy.getAcademyStudents().add(this);
    }

    public void setStudent(Student student) {
        this.student = student;
        student.getAcademyStudents().add(this);
    }
}
