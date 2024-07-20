package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassTime extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_time_id")
    private Long classTimeId;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
        studentGroup.getClassTimes().add(this);
    }
}
