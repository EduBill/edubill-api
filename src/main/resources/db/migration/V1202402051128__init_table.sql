CREATE TABLE IF NOT EXISTS academy (
                         academy_id BIGINT NOT NULL AUTO_INCREMENT,
                         user_id BIGINT,
                         academy_name VARCHAR(255),
                         academy_number VARCHAR(255),
                         business_number VARCHAR(255),
                         academy_type ENUM ('MATH', 'ENGLISH', 'SCIENCE', 'ART'),
                         PRIMARY KEY (academy_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS academy_students (
                                  academy_id BIGINT,
                                  academy_student_id BIGINT NOT NULL AUTO_INCREMENT,
                                  student_id BIGINT,
                                  PRIMARY KEY (academy_student_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS accounts (
                          created_at DATETIME(6),
                          updated_at DATETIME(6),
                          user_id BIGINT,
                          account_number VARCHAR(255) NOT NULL,
                          holder_name VARCHAR(255),
                          account_status ENUM ('ACTIVE', 'INACTIVE', 'PENDING'),
                          bank_name ENUM ('SINHAN', 'KOOKMIN', 'HANA', 'WOORI', 'NONGHYUP', 'IBK'),
                          PRIMARY KEY (account_number)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS invoices (
                          created_at DATETIME(6),
                          deadline DATETIME(6),
                          invoice_id BIGINT NOT NULL AUTO_INCREMENT,
                          updated_at DATETIME(6),
                          user_id BIGINT,
                          receiver_name VARCHAR(255),
                          receiver_phone_number VARCHAR(255),
                          details TEXT,
                          PRIMARY KEY (invoice_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS payments (
                          difference INTEGER,
                          paid_amount INTEGER,
                          total_payment_amount INTEGER,
                          created_at DATETIME(6),
                          payment_id BIGINT NOT NULL AUTO_INCREMENT,
                          student_id BIGINT,
                          updated_at DATETIME(6),
                          payment_status ENUM ('PAID', 'UNPAID'),
                          PRIMARY KEY (payment_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS students (
                          student_age INTEGER,
                          student_id BIGINT NOT NULL AUTO_INCREMENT,
                          student_name VARCHAR(255),
                          student_number VARCHAR(255),
                          PRIMARY KEY (student_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
                       created_at DATETIME(6),
                       updated_at DATETIME(6),
                       user_id BIGINT NOT NULL AUTO_INCREMENT,
                       phone_number VARCHAR(255) NOT NULL,
                       user_name VARCHAR(255) NOT NULL,
                       user_role ENUM ('PARENTS', 'ACADEMY', 'ADMIN') NOT NULL,
                       PRIMARY KEY (user_id)
) ENGINE=InnoDB;

ALTER TABLE IF EXISTS academy_students
    ADD CONSTRAINT UKt5hheejdtlcwym0wd73sy5wna UNIQUE (academy_id, student_id);

ALTER TABLE IF EXISTS academy
    ADD CONSTRAINT FKqcb2296fg82hygh88mpf7kqw6
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

ALTER TABLE IF EXISTS academy_students
    ADD CONSTRAINT FKfaonalif5jvxxb8cv9vxmb573
    FOREIGN KEY (academy_id)
    REFERENCES academy (academy_id);

ALTER TABLE IF EXISTS academy_students
    ADD CONSTRAINT FKe9nltnlflw9jvtrn2tcvmrqj1
    FOREIGN KEY (student_id)
    REFERENCES students (student_id);

ALTER TABLE IF EXISTS accounts
    ADD CONSTRAINT FKnjuop33mo69pd79ctplkck40n
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

ALTER TABLE IF EXISTS invoices
    ADD CONSTRAINT FKbwr4d4vyqf2bkoetxtt8j9dx7
    FOREIGN KEY (user_id)
    REFERENCES users (user_id);

ALTER TABLE IF EXISTS payments
    ADD CONSTRAINT FK6ooq278k2bs5xi8t5o6oort1v
    FOREIGN KEY (student_id)
    REFERENCES students (student_id);