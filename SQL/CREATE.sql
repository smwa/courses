CREATE TABLE login (
    id INT PRIMARY KEY IDENTITY(1,1),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    is_temp_password BIT DEFAULT 0,
    permission_edit_users BIT DEFAULT 0,
    permission_edit_receivers BIT DEFAULT 0,
    permission_run_report BIT DEFAULT 0,
    permission_edit_classes BIT DEFAULT 0
);

CREATE TABLE receiver (
    id INT PRIMARY KEY IDENTITY(1,1),
    email VARCHAR(255)
);

CREATE TABLE class (
    id INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(255),
    description TEXT
);

CREATE TABLE requirement (
    id INT PRIMARY KEY IDENTITY(1,1),
    class_id INT,
    dependency INT,
    minimum_grade VARCHAR(1)
);
