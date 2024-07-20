CREATE TABLE class_time (
                              class_time_id BIGINT NOT NULL AUTO_INCREMENT,
                              day_of_week ENUM('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN') NOT NULL,
                              start_time TIME NOT NULL,
                              end_time TIME NOT NULL,
                              student_group_id BIGINT,
                              created_at   DATETIME(6),
                              updated_at   DATETIME(6),
                              PRIMARY KEY (class_time_id)
) ENGINE=InnoDB;

alter table if exists class_time
    add constraint fk_student_group_and_class_time
    foreign key (student_group_id)
    references student_group (student_group_id);