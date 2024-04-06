package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class StudentGroup extends BaseEntity {

    protected StudentGroup() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_group_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "manager_id")
    private String managerId; //user

    private Integer tuition;

    @Column(name = "total_student_count")
    private Integer totalStudentCount;

    @Builder
    public StudentGroup(String groupName, String managerId, Integer tuition, Integer totalStudentCount) {
        this.groupName = groupName;
        this.managerId = managerId;
        this.tuition = tuition;
        this.totalStudentCount = totalStudentCount;
    }
}
