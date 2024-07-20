CREATE TABLE class_time (
                              class_time_id BIGINT NOT NULL AUTO_INCREMENT,
                              day_of_week ENUM('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN') NOT NULL,
                              start_time TIME NOT NULL,
                              end_time TIME NOT NULL,
                              group_id BIGINT,
                              created_at   DATETIME(6),
                              updated_at   DATETIME(6),
                              PRIMARY KEY (class_time_id),
    -- 외래 키 제약조건
                              constraint fk_group_and_class_time foreign key (group_id) references groups(group_id) on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;