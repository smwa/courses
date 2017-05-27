INSERT INTO login(firstname, lastname, email, password, is_temp_password, permission_edit_users, permission_edit_receivers, permission_run_report, permission_edit_classes)
VALUES ('admin', 'admin', 'admin@example.com', '', 1, 1, 1, 1, 1),
 ('Michael', 'Smith', 'michael.smith.ok@gmail.com', '', 1, 1, 1, 1, 1);

INSERT INTO receiver (email) VALUES 
('michael.smith.ok@gmail.com');

INSERT INTO class (name, description) VALUES
('MSIS-1000', 'First course'),
('MSIS-1001', 'Second course'),
('MSIS-1002', 'Third course'),
('MSIS-1003', 'Fourth course');

INSERT INTO requirement (class_id, dependency, minimum_grade) VALUES
(2, 1, 'C'),
(3, 1, 'C'),
(3, 2, 'B'),
(4, 2, 'C');
