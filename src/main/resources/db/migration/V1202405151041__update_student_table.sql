ALTER TABLE students
    ADD COLUMN created_at DATETIME(6),
    ADD COLUMN updated_at DATETIME(6),
    ADD COLUMN parent_name varchar(255),
    ADD COLUMN parent_phone_number varchar(255),
    ADD COLUMN student_phone_number varchar(255),
    DROP COLUMN student_number;

ALTER TABLE if exists students
    add constraint FKf2lh21s6ulm2sm8d1pfdd0p97
    foreign key (student_group_id)
    references student_group (student_group_id);