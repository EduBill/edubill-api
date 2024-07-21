-- 기존 STUDENT_GROUP 테이블 이름을 GROUPS로 변경
RENAME TABLE student_group TO `GROUPS`;

-- 기존 GROUPS 테이블에서 student_group_id 컬럼을 group_id로 변경
ALTER TABLE `GROUPS`
    CHANGE COLUMN `student_group_id` `group_id` BIGINT AUTO_INCREMENT;


-- STUDENT_GROUP 테이블 생성
CREATE TABLE STUDENT_GROUP (
                               student_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               student_id BIGINT NOT NULL,
                               group_id BIGINT NOT NULL,
    -- 기타 컬럼 (예: 생성일, 수정일 등 BaseEntity에서 상속된 컬럼)
                               created_at   DATETIME(6),
                               updated_at   DATETIME(6),

    -- 외래 키 제약조건
                               CONSTRAINT FK_STUDENT_STUDENT_GROUP FOREIGN KEY (student_id) REFERENCES STUDENTS(student_id) ON DELETE CASCADE,
                               CONSTRAINT FK_GROUP_STUDENT_GROUP FOREIGN KEY (group_id) REFERENCES GROUPS(group_id) ON DELETE CASCADE,

    -- 유니크 제약조건: student_id와 group_id 조합의 유니크 제약조건
                               CONSTRAINT UK_STUDENT_GROUP_STUDENT_ID_GROUP_ID UNIQUE (student_id, group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE payment_history
    DROP COLUMN student_group_id;
