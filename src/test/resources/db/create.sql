-- Schema creation script for H2 (Test Database)

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL
);

CREATE TABLE projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    project_description TEXT,
    project_start_date DATE,
    project_deadline DATE,
    project_time_estimate INT,
    project_time_spent INT,
    project_status VARCHAR(50),
    project_priority VARCHAR(50)
);

CREATE TABLE subprojects (
    subproject_id INT AUTO_INCREMENT PRIMARY KEY,
    subproject_name VARCHAR(255) NOT NULL,
    subproject_description TEXT,
    subproject_start_date DATE,
    subproject_deadline DATE,
    subproject_time_estimate INT,
    subproject_time_spent INT,
    subproject_status VARCHAR(50),
    subproject_priority VARCHAR(50),
    project_id INT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE
);

CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    task_description TEXT,
    task_start_date DATE,
    task_deadline DATE,
    task_time_estimate INT,
    task_time_spent INT,
    task_status VARCHAR(50),
    task_priority VARCHAR(50),
    subproject_id INT NOT NULL,
    user_id INT,
    FOREIGN KEY (subproject_id) REFERENCES subprojects(subproject_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);