DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS teachers_subjects CASCADE;
DROP TABLE IF EXISTS audiences CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS faculty CASCADE;



CREATE TABLE faculty(
    faculty_id SERIAL,
    faculty_name CHARACTER VARYING(40) NOT NULL,
    dean_firstname CHARACTER VARYING(30) NOT NULL,
    dean_lastname CHARACTER VARYING(30) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT faculty_pkey PRIMARY KEY (faculty_id)
);

CREATE TABLE groups (
    group_id SERIAL,
    group_name CHARACTER VARYING(10) NOT NULL,
    faculty_id INTEGER DEFAULT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id),
    CONSTRAINT faculty_fkey FOREIGN KEY (faculty_id)
        REFERENCES faculty (faculty_id) ON DELETE SET DEFAULT
);
    
CREATE TABLE students (
    student_id SERIAL,
    first_name CHARACTER VARYING(50) NOT NULL,
    last_name CHARACTER VARYING(50) NOT NULL,
    group_id INTEGER DEFAULT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT students_pkey PRIMARY KEY (student_id),
    CONSTRAINT group_fkey FOREIGN KEY (group_id)
        REFERENCES groups (group_id) ON DELETE SET DEFAULT
);

CREATE TABLE subjects (
    subject_id SERIAL,
    subject_name CHARACTER VARYING(50) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT subjects_pkey PRIMARY KEY (subject_id)
);

CREATE TABLE teachers (
    teacher_id SERIAL,
    first_name CHARACTER VARYING(50) NOT NULL,
    last_name CHARACTER VARYING(50) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT teachers_pkey PRIMARY KEY (teacher_id)
);

CREATE TABLE audiences (
    audience_id SERIAL,
    number INTEGER DEFAULT 0,
    capacity INTEGER DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT audiences_pkey PRIMARY KEY (audience_id)
);

CREATE TABLE teachers_subjects (
    teacher_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    CONSTRAINT teachers_subjects_pkey PRIMARY KEY (teacher_id, subject_id),
    CONSTRAINT teacher_fkey FOREIGN KEY (teacher_id)
        REFERENCES teachers (teacher_id) ON DELETE CASCADE,
    CONSTRAINT subject_fkey FOREIGN KEY (subject_id)
        REFERENCES subjects (subject_id) ON DELETE CASCADE
);


CREATE TABLE lessons (
    lesson_id SERIAL,
    group_id INTEGER DEFAULT NULL,
    teacher_id INTEGER DEFAULT NULL,
    day CHARACTER VARYING(10) NOT NULL,
    lesson_number CHARACTER VARYING(10) NOT NULL,
    duration INTEGER NOT NULL,
    audience_id INTEGER DEFAULT NULL,
    subject_id INTEGER DEFAULT NULL,
    
    CONSTRAINT lessons_pkey PRIMARY KEY (lesson_id),
    CONSTRAINT group_less_fkey FOREIGN KEY (group_id)
        REFERENCES groups (group_id) ON DELETE CASCADE,
    CONSTRAINT teacher_less_fkey FOREIGN KEY (teacher_id)
        REFERENCES teachers (teacher_id) ON DELETE CASCADE,
    CONSTRAINT audience_less_fkey FOREIGN KEY (audience_id)
        REFERENCES audiences (audience_id) ON DELETE CASCADE,
    CONSTRAINT subject_less_fkey FOREIGN KEY (subject_id)
        REFERENCES subjects (subject_id) ON DELETE CASCADE
);