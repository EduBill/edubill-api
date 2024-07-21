-- 1. 외래 키 제약 조건 삭제
ALTER TABLE students
    DROP FOREIGN KEY FKf2lh21s6ulm2sm8d1pfdd0p97;

-- 2. 열 삭제
ALTER TABLE students
    DROP COLUMN student_group_id;
