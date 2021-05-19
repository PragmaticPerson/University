INSERT INTO faculty(faculty_name, dean_firstname, dean_lastname) VALUES
    ('FacultyName1', 'Name1', 'Surname1'),
    ('FacultyName2', 'Name2', 'Surname2');
    
INSERT INTO groups(group_name, faculty_id) VALUES
    ('Group1', 1),
    ('Group2', 1),
    ('Group3', 2),
    ('Group4', 2);
    
INSERT INTO students(first_name, last_name, group_id) VALUES
    ('Name1', 'Surname1', 1),
    ('Name2', 'Surname2', 1),
    ('Name3', 'Surname3', 2),
    ('Name4', 'Surname4', 3);
    
INSERT INTO subjects(subject_name) VALUES
    ('Subject1'), ('Subject2'), ('Subject3'), ('Subject4'), ('Subject5'),
    ('Subject6'), ('Subject7'), ('Subject8'), ('Subject9'), ('Subject10');
    
INSERT INTO teachers(first_name, last_name) VALUES
    ('Name1', 'Surname1'), ('Name2', 'Surname2'), ('Name3', 'Surname3'),
    ('Name4', 'Surname4'), ('Name5', 'Surname5');
    
INSERT INTO teachers_subjects(teacher_id, subject_id) VALUES 
    (1, 2), (1, 5), (1, 6), (2, 1), (2, 3), (2, 8), (2, 10), (3, 1),
    (3, 7), (3, 9), (4, 1), (4, 2), (4, 6), (4, 9), (5, 1), (5, 2),
    (5, 5), (5, 8), (5, 10);
    
INSERT INTO audiences(number, capacity) VALUES
    (101, 29), (102, 40), (103, 40), (104, 20), (105, 36), (106, 50), (107, 60);
    
INSERT INTO lessons(group_id, teacher_id, day, lesson_number, duration, audience_id, subject_id) VALUES
    (1, 1, 'MONDAY', 'FIRST', 90, 1, 2),
    (1, 4, 'MONDAY', 'SECOND', 90, 1, 1),
    (2, 2, 'MONDAY', 'FIRST', 60, 2, 1),
    (2, 5, 'MONDAY', 'THIRD', 90, 5, 10),
    (3, 3, 'TUESDAY', 'FOURTH', 90, 3, 9),
    (1, 1, 'WEDNESDAY', 'FIRST', 60, 1, 2),
    (1, 5, 'WEDNESDAY', 'FOURTH', 90, 2, 1),
    (3, 4, 'WEDNESDAY', 'FIRST', 60, 4, 6),
    (3, 2, 'WEDNESDAY', 'SECOND', 90, 3, 9);