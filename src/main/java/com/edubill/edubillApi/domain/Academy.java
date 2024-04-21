package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ACADEMY")
public class Academy extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academy_id")
    private Long academyId;

    @Column(name = "academy_name")
    private String academyName;

    @Column(name = "academy_number")
    private String academyNumber;

    @Column(name = "business_number")
    private String businessNumber;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL)
    private List<AcademyStudent> academyStudents = new ArrayList<>();



    @Builder
    public Academy(String academyName, String academyNumber, String businessNumber, String userId) {
        this.academyName = academyName;
        this.academyNumber = academyNumber;
        this.businessNumber = businessNumber;
        this.userId = userId;
    }
}
