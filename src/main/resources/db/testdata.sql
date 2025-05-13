INSERT INTO users (user_name, user_password, user_role) VALUES
('admin_user', 'adminpass', 'Admin'),
('manager_user', 'managerpass', 'ProjectManager'),
('dev_user', 'devpass', 'Developer');

INSERT INTO projects (project_name, project_description, project_start_date, project_deadline, project_time_estimate, project_time_spent, project_status, project_priority) VALUES
('Website Redesign', 'Complete redesign of the company website.', '2024-01-15', '2024-06-30', 300, 150, 'IN_PROGRESS', 'HIGH'),
('Mobile App Development', 'Develop a new mobile application for iOS and Android.', '2024-03-01', '2024-12-01', 500, 50, 'NOT_STARTED', 'HIGH'),
('Marketing Campaign Q3', 'Plan and execute Q3 marketing campaign.', '2024-07-01', '2024-09-30', 100, 0, 'NOT_STARTED', 'MEDIUM'),
('Internal CRM Update', 'Update the internal CRM system with new features.', '2023-09-01', '2024-02-28', 200, 190, 'COMPLETED', 'MEDIUM');


INSERT INTO subprojects (project_id, subproject_name, subproject_description, subproject_start_date, subproject_deadline, subproject_time_estimate, subproject_time_spent, subproject_status, subproject_priority) VALUES
(1, 'Phase 1: Design Mockups', 'Create and approve design mockups.', '2024-01-20', '2024-02-28', 80, 70, 'COMPLETED', 'HIGH'),
(1, 'Phase 2: Frontend Development', 'Develop the frontend based on mockups.', '2024-03-01', '2024-05-15', 120, 60, 'IN_PROGRESS', 'HIGH'),
(1, 'Phase 3: Backend Integration', 'Integrate frontend with backend services.', '2024-05-16', '2024-06-15', 100, 20, 'IN_PROGRESS', 'MEDIUM');

INSERT INTO subprojects (project_id, subproject_name, subproject_description, subproject_start_date, subproject_deadline, subproject_time_estimate, subproject_time_spent, subproject_status, subproject_priority) VALUES
(2, 'iOS App UI/UX', 'Design user interface and experience for iOS.', '2024-03-05', '2024-04-30', 100, 20, 'NOT_STARTED', 'HIGH'),
(2, 'Android App Development Core', 'Core feature development for Android.', '2024-03-10', '2024-07-30', 200, 30, 'NOT_STARTED', 'HIGH');


INSERT INTO tasks (subproject_id, task_name, task_description, task_start_date, task_deadline, task_time_estimate, task_time_spent, task_status, task_priority) VALUES
(2, 'Homepage Layout', 'Implement HTML/CSS for homepage.', '2024-03-01', '2024-03-15', 30, 25, 'COMPLETED', 'HIGH'),
(2, 'Product Page Template', 'Create reusable product page template.', '2024-03-16', '2024-03-30', 40, 15, 'IN_PROGRESS', 'HIGH'),
(2, 'Contact Form Implementation', 'Develop and validate the contact form.', '2024-04-01', '2024-04-10', 20, 5, 'IN_PROGRESS', 'MEDIUM'),
(2, 'Responsive Design Testing', 'Test website on various devices.', '2024-04-11', '2024-04-20', 30, 15, 'IN_PROGRESS', 'MEDIUM');

INSERT INTO tasks (subproject_id, task_name, task_description, task_start_date, task_deadline, task_time_estimate, task_time_spent, task_status, task_priority) VALUES
(4, 'User Flow Diagrams', 'Create user flow diagrams for key features.', '2024-03-05', '2024-03-15', 20, 0, 'NOT_STARTED', 'HIGH'),
(4, 'Wireframes for Main Screens', 'Develop wireframes for login, dashboard, and settings.', '2024-03-16', '2024-03-30', 30, 0, 'NOT_STARTED', 'HIGH'); 