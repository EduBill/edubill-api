CREATE TABLE IF NOT EXISTS academy(
    academy_id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id         VARCHAR(255),
    academy_name    VARCHAR(255),
    academy_number  VARCHAR(255),
    business_number VARCHAR(255),
    PRIMARY KEY (academy_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS academy_students(
    academy_id         BIGINT,
    academy_student_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id         BIGINT,
    PRIMARY KEY (academy_student_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS students(
    student_id     BIGINT NOT NULL AUTO_INCREMENT,
    student_name   VARCHAR(255),
    student_number VARCHAR(255),
    student_group_id BIGINT,
    PRIMARY KEY (student_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS users(
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    user_id      VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    user_name    VARCHAR(255) NOT NULL,
    user_type    VARCHAR(50),
    auth_role    VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS payment_history (
    payment_history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bank_name  VARCHAR(255),
    deposit_date DATE,
    depositor_name VARCHAR(255),
    memo varchar(255),
    paid_amount INT,
    student_group_id BIGINT,
    created_at   DATETIME(6),
    updated_at   DATETIME(6)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS student_group (
    student_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_name   VARCHAR(50),
    manager_id   VARCHAR(50),
    total_student_count INT,
    tuition INT,
    created_at   DATETIME(6),
    updated_at   DATETIME(6)
) ENGINE=InnoDB;

ALTER TABLE IF EXISTS academy_students
    ADD CONSTRAINT UKt5hheejdtlcwym0wd73sy5wna UNIQUE (academy_id, student_id);