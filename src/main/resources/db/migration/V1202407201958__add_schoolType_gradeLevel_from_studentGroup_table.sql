ALTER TABLE student_group
    ADD COLUMN school_type ENUM('ELEMENTARY', 'MIDDLE', 'HIGH', 'ETC'),
    ADD COLUMN grade_level ENUM('FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH', 'SIXTH', 'ETC');
