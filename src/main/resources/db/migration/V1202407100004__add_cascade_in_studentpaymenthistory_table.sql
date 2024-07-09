alter table if exists student_payment_history
    add constraint FK_my_custom_fk_name
    foreign key (payment_history_id)
    references payment_history (payment_history_id)
    on delete cascade