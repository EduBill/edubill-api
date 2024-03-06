ALTER TABLE users
    MODIFY COLUMN user_id varchar(255) NOT NULL,
    MODIFY COLUMN user_role enum ('PARENTS','STUDENT','ACADEMY','ADMIN');

