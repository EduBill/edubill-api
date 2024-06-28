ALTER TABLE IF EXISTS student_payment_history
    ADD CONSTRAINT UKjjq15eoqs9qp7721wqpqdj8kq UNIQUE (student_id, payment_history_id),
    ADD CONSTRAINT UK_ktohn1q0e43hjfv3uis5svhk0 UNIQUE (payment_history_id),
    ADD CONSTRAINT FK2lwbkvot1hyluov2rdtplgyre FOREIGN KEY (payment_history_id) REFERENCES payment_history (payment_history_id),
    ADD CONSTRAINT FKnd0fwxjok1qu1ylmg4th994b4 FOREIGN KEY (student_id) REFERENCES students (student_id);
