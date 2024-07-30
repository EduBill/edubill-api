ALTER TABLE groups
    ADD COLUMN school_type ENUM('ELEMENTARY', 'MIDDLE', 'HIGH', 'ETC'),
    ADD COLUMN grade_level ENUM('FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH', 'SIXTH', 'ETC'),
    ADD COLUMN group_memo VARCHAR(255);
