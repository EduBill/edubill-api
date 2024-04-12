package com.edubill.edubillApi.user.domain;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "academy_id", insertable = false, updatable = false)
    private Academy academy;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

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
