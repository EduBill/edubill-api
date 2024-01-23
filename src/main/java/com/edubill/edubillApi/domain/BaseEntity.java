package com.edubill.edubillApi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass //Entity 클래스들이 해당 추상 클래스를 상속할 경우 created_at, updated_at를 컬럼으로 인식
@Getter
@EntityListeners(AuditingEntityListener.class)
//@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

    @CreatedDate //Entity가 생성되어 저장될 때 시간이 자동 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate //조회한 Entity의 값을 변경할 때 시간이 자동 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
