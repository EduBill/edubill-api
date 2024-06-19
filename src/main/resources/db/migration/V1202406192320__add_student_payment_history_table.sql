create table student_payment_history (
                                     created_at datetime(6),
                                     student_payment_history_id bigint not null auto_increment,
                                     updated_at datetime(6),
                                     student_id bigint,
                                     payment_history_id bigint,
                                     year_month_str varchar(255),
                                     primary key (student_payment_history_id)
) engine=InnoDB;