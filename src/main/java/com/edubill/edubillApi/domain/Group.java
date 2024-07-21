package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "GROUPS")
public class Group extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "manager_id")
    private String managerId; //user

    private Integer tuition;

    @Column(name = "total_student_count")
    private Integer totalStudentCount;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<StudentGroup> studentGroups = new ArrayList<>();


    //====비즈니스 로직=====//
    public void addStudent() {
        if (this.totalStudentCount != null) {
            this.totalStudentCount = this.totalStudentCount + 1;
        } else {
            this.totalStudentCount = 1; // 예외 처리 또는 기본값 설정 등
        }
    }
}
