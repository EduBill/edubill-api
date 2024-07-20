package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
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

    @Column(name = "tuition")
    private Integer tuition;

    @Column(name = "school_type")
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Column(name = "total_student_count")
    private Integer totalStudentCount;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudentGroup> studentGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClassTime> classTimes = new ArrayList<>();


    //====비즈니스 로직=====//
    public void addStudent() {
        if (this.totalStudentCount != null) {
            this.totalStudentCount = this.totalStudentCount + 1;
        } else {
            this.totalStudentCount = 1; // 예외 처리 또는 기본값 설정 등
        }
    }
}
