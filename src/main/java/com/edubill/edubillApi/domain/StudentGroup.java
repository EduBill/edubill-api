package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "STUDENT_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "group_id"}))
public class StudentGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_group_id")
    private Long studentGroupId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    //==연관관계 메서드==//
    public void setStudent(Student student) {
        this.student = student;
        student.getStudentGroups().add(this);
    }

    public void setGroup(Group group) {
        this.group = group;
        group.getStudentGroups().add(this);
    }
}
