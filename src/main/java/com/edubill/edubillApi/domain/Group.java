package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.GradeLevel;
import com.edubill.edubillApi.domain.enums.SchoolType;
import com.edubill.edubillApi.dto.student.GroupInfoRequestDto;

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
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "school_type")
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Column(name = "tuition")
    private Integer tuition;

    @Column(name = "group_memo")
    private String groupMemo;

    @Column(name = "total_student_count")
    private Integer totalStudentCount;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudentGroup> studentGroups = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClassTime> classTimes = new ArrayList<>();

    public Group(GroupInfoRequestDto groupInfoRequestDto, String userId) {
        this.groupName = groupInfoRequestDto.getGroupName();
        this.managerId = userId;
        this.schoolType = groupInfoRequestDto.getSchoolType();
        this.gradeLevel = groupInfoRequestDto.getGradeLevel();
        this.tuition = groupInfoRequestDto.getTuition();
        this.groupMemo = groupInfoRequestDto.getGroupMemo();
        this.studentGroups = new ArrayList<>();
        this.classTimes = new ArrayList<>();
    }

    public static Group from(GroupInfoRequestDto groupInfoRequestDto, String userId) {
        return new Group(groupInfoRequestDto, userId);
    }

    //학생수 추가 로직
    public void addStudent() {
        if (this.totalStudentCount != null) {
            this.totalStudentCount = this.totalStudentCount + 1;
        } else {
            this.totalStudentCount = 1; // 예외 처리 또는 기본값 설정 등
        }
    }

    //학생수 감소 로직
    public void removeStudent() {
        if (this.totalStudentCount != null) {
            if (this.totalStudentCount > 0) {
                this.totalStudentCount = this.totalStudentCount - 1;
            } else {
                throw new IllegalStateException("Student count cannot be less than zero.");
            }
        } else {
            throw new IllegalStateException("Total student count is not set.");
        }
    }
}
