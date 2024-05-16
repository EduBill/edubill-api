create table payment_key (
                             payment_key_id bigint not null auto_increment,
                             student_id bigint,
                             payment_key varchar(255),
                             primary key (payment_key_id)
) engine=InnoDB;

alter table if exists payment_key
    add constraint FKeepm7yssw25lyxuygy7m7wo5
    foreign key (student_id)
    references students (student_id);