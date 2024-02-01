package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "academy_type")
    @Enumerated(EnumType.STRING)
    private AcademyType academyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
