package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;


import java.time.YearMonth;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelUploadStatus extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "excel_upload_status_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Builder.Default
    private Boolean isExcelUploaded = false; // 엑셀업로드 유무

    @Column
    private YearMonth yearMonth;
}
