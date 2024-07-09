ALTER TABLE student_payment_history
    DROP FOREIGN KEY IF EXISTS FK2lwbkvot1hyluov2rdtplgyre;


alter table if exists student_payment_history
    add constraint FK2lwbkvot1hyluov2rdtplgyre
    foreign key (payment_history_id)
    references payment_history (payment_history_id)
    on delete cascade;