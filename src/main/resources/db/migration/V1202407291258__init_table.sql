-- Table creation
CREATE TABLE academy (
                         academy_id BIGINT NOT NULL AUTO_INCREMENT,
                         created_at DATETIME(6),
                         updated_at DATETIME(6),
                         academy_name VARCHAR(255),
                         academy_number VARCHAR(255),
                         business_number VARCHAR(255),
                         user_id VARCHAR(255),
                         PRIMARY KEY (academy_id)
) ENGINE=InnoDB;

CREATE TABLE academy_students (
                                  academy_id BIGINT,
                                  academy_student_id BIGINT NOT NULL AUTO_INCREMENT,
                                  student_id BIGINT,
                                  PRIMARY KEY (academy_student_id)
) ENGINE=InnoDB;

CREATE TABLE excel_upload_status (
                                     is_excel_uploaded BIT,
                                     created_at DATETIME(6),
                                     excel_upload_status_id BIGINT NOT NULL AUTO_INCREMENT,
                                     updated_at DATETIME(6),
                                     user_id VARCHAR(255),
                                     year_month_str VARCHAR(255),
                                     PRIMARY KEY (excel_upload_status_id)
) ENGINE=InnoDB;

CREATE TABLE groups (
                        total_student_count INTEGER,
                        tuition INTEGER,
                        created_at DATETIME(6),
                        group_id BIGINT NOT NULL AUTO_INCREMENT,
                        updated_at DATETIME(6),
                        group_name VARCHAR(255),
                        manager_id VARCHAR(255),
                        PRIMARY KEY (group_id)
) ENGINE=InnoDB;

CREATE TABLE payment_history (
                                 paid_amount INTEGER,
                                 created_at DATETIME(6),
                                 deposit_date DATETIME(6),
                                 payment_history_id BIGINT NOT NULL AUTO_INCREMENT,
                                 updated_at DATETIME(6),
                                 bank_name VARCHAR(255),
                                 depositor_name VARCHAR(255),
                                 manager_id VARCHAR(255),
                                 memo VARCHAR(255),
                                 s3_url VARCHAR(255),
                                 payment_status ENUM('PAID', 'UNPAID'),
                                 payment_type ENUM('CASH', 'CREDIT_CARD', 'KAKAO', 'GIFT_VOUCHER', 'BANK_TRANSFER', 'ETC'),
                                 PRIMARY KEY (payment_history_id)
) ENGINE=InnoDB;

CREATE TABLE payment_key (
                             payment_key_id BIGINT NOT NULL AUTO_INCREMENT,
                             student_id BIGINT,
                             payment_key VARCHAR(255),
                             PRIMARY KEY (payment_key_id)
) ENGINE=InnoDB;

CREATE TABLE student_group (
                               created_at DATETIME(6),
                               group_id BIGINT,
                               student_group_id BIGINT NOT NULL AUTO_INCREMENT,
                               student_id BIGINT,
                               updated_at DATETIME(6),
                               PRIMARY KEY (student_group_id)
) ENGINE=InnoDB;

CREATE TABLE student_payment_history (
                                         created_at DATETIME(6),
                                         payment_history_id BIGINT,
                                         student_id BIGINT,
                                         student_payment_history_id BIGINT NOT NULL AUTO_INCREMENT,
                                         updated_at DATETIME(6),
                                         year_month_str VARCHAR(255),
                                         PRIMARY KEY (student_payment_history_id)
) ENGINE=InnoDB;

CREATE TABLE students (
                          created_at DATETIME(6),
                          student_id BIGINT NOT NULL AUTO_INCREMENT,
                          updated_at DATETIME(6),
                          memo VARCHAR(255),
                          parent_name VARCHAR(255),
                          parent_phone_number VARCHAR(255),
                          school_name VARCHAR(255),
                          student_name VARCHAR(255),
                          student_phone_number VARCHAR(255),
                          department_type ENUM('LIBERAL_ARTS', 'NATURAL_SCIENCES', 'PHYSICAL_EDUCATION', 'ETC'),
                          grade_level ENUM('FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH', 'SIXTH', 'ETC'),
                          school_type ENUM('ELEMENTARY', 'MIDDLE', 'HIGH', 'ETC'),
                          PRIMARY KEY (student_id)
) ENGINE=InnoDB;

CREATE TABLE users (
                       created_at DATETIME(6),
                       updated_at DATETIME(6),
                       phone_number VARCHAR(255) NOT NULL,
                       user_id VARCHAR(255) NOT NULL,
                       user_name VARCHAR(255) NOT NULL,
                       auth_role VARCHAR(50),
                       user_type VARCHAR(50),
                       PRIMARY KEY (user_id)
) ENGINE=InnoDB;

-- Constraints
ALTER TABLE academy_students
    ADD CONSTRAINT UKt5hheejdtlcwym0wd73sy5wna UNIQUE (academy_id, student_id);

ALTER TABLE student_group
    ADD CONSTRAINT UKq3fvnpyx8w4af039aaf4hev37 UNIQUE (student_id, group_id);

ALTER TABLE student_payment_history
    ADD CONSTRAINT UKjjq15eoqs9qp7721wqpqdj8kq UNIQUE (student_id, payment_history_id),
    ADD CONSTRAINT UK_ktohn1q0e43hjfv3uis5svhk0 UNIQUE (payment_history_id);

ALTER TABLE users
    ADD CONSTRAINT UK_9q63snka3mdh91as4io72espi UNIQUE (phone_number);

-- Foreign key constraints
ALTER TABLE academy_students
    ADD CONSTRAINT FKfaonalif5jvxxb8cv9vxmb573 FOREIGN KEY (academy_id) REFERENCES academy (academy_id),
    ADD CONSTRAINT FKe9nltnlflw9jvtrn2tcvmrqj1 FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE excel_upload_status
    ADD CONSTRAINT FKeas338yr7k715yf07wgqv2ypy FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE payment_key
    ADD CONSTRAINT FKeepm7yssw25lyxuygy7m7wo5 FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE student_group
    ADD CONSTRAINT FK8nmelqqna662np3vpq20dul8s FOREIGN KEY (group_id) REFERENCES groups (group_id),
    ADD CONSTRAINT FKop0tpvpr7c75c49sudgi1mf2 FOREIGN KEY (student_id) REFERENCES students (student_id);

ALTER TABLE student_payment_history
    ADD CONSTRAINT FK2lwbkvot1hyluov2rdtplgyre FOREIGN KEY (payment_history_id) REFERENCES payment_history (payment_history_id) ON DELETE CASCADE,
    ADD CONSTRAINT FKnd0fwxjok1qu1ylmg4th994b4 FOREIGN KEY (student_id) REFERENCES students (student_id);
