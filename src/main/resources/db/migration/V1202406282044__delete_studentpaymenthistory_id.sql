ALTER TABLE students
    DROP COLUMN student_payment_history_id;

ALTER TABLE payment_history
    DROP COLUMN student_payment_history_id;
