package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ACADEMY")
public class Academy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academy_id")
    private Long id;

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
}
