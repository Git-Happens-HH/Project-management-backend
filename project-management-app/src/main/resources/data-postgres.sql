BEGIN;

-- =====================================================
-- APP_USER
-- =====================================================

INSERT INTO app_user
(app_user_id, user_name, first_name, last_name, email, password_hash, registered_at)
VALUES
(1, 'jukka-poika42', 'Jukka', 'Javalainen', 'jukkis@example.com',
 '$2a$10$ea5UZHjrlmhxMNSo0Sgan.JKKWmtAU4FWjBPHHqc3tJ.5.XDCJnNq',
 NOW()),

(2, 'p-python', 'Paula', 'Python', 'paula.python@example.com',
 '$2a$10$ea5UZHjrlmhxMNSo0Sgan.JKKWmtAU4FWjBPHHqc3tJ.5.XDCJnNq',
 NOW()),

(3, 'heikki-hacker', 'Heikki', 'Hacker', 'heikki.hacker@example.com',
 '$2a$10$ea5UZHjrlmhxMNSo0Sgan.JKKWmtAU4FWjBPHHqc3tJ.5.XDCJnNq',
 NOW()),

(4, 'pesapallomaila', 'Ismo', 'Laitela', 'ismo.laitela@example.com',
 '$2a$10$ea5UZHjrlmhxMNSo0Sgan.JKKWmtAU4FWjBPHHqc3tJ.5.XDCJnNq',
 NOW());

-- =====================================================
-- PROJECT
-- =====================================================

INSERT INTO project
(project_id, title, description, created_at)
VALUES
(1,
 'Test Project: The Six Seven App Creation Team',
 'Random description',
 NOW()),

(2,
 'Website Redesign: Make YouTube great again',
 'Second test project description',
 NOW() + INTERVAL '1 hour'),

(3,
 'Team Collaboration Project: We are so back',
 'It''s so over',
 NOW() + INTERVAL '2 hours');

-- =====================================================
-- USER_PROJECT
-- =====================================================

INSERT INTO user_project
(user_project_id, app_user_id, project_id, role, joined_at)
VALUES
(1, 1, 1, 'owner', NOW()),
(2, 3, 1, 'member', NOW()),
(3, 1, 2, 'owner', NOW()),
(4, 2, 2, 'member', NOW()),
(5, 3, 3, 'owner', NOW()),
(6, 2, 3, 'member', NOW());

-- =====================================================
-- TASK_LIST
-- =====================================================

INSERT INTO task_list
(task_list_id, project_id, title, created_at)
VALUES
(1,
 1,
 'Backlog of the super cool test project',
 NOW()),

(2,
 2,
 'In Progress',
 NOW() + INTERVAL '1 hour');

-- =====================================================
-- TASK
-- =====================================================

INSERT INTO task
(task_id,
 task_list_id,
 assigned_user,
 created_by,
 title,
 description,
 deadline,
 sort_order)
VALUES
(1,
 1,
 1,
 1,
 'Initial task',
 'This task was created by test data',
 NOW() + INTERVAL '7 days',
 0),

(2,
 2,
 2,
 2,
 'Create homepage mockup',
 'Prepare updated landing page design',
 NOW() + INTERVAL '14 days',
 0);

-- =====================================================
-- COMMENT
-- =====================================================

INSERT INTO comment
(comment_id,
 task_id,
 app_user_id,
 content,
 created_at)
VALUES
(1,
 1,
 1,
 'This is a comment in test project 1',
 NOW()),

(2,
 2,
 2,
 'Second seed comment',
 NOW() + INTERVAL '1 hour');

-- =====================================================
-- RESET IDENTITY SEQUENCES
-- =====================================================

SELECT setval(pg_get_serial_sequence('app_user', 'app_user_id'), 4, true);
SELECT setval(pg_get_serial_sequence('project', 'project_id'), 3, true);
SELECT setval(pg_get_serial_sequence('user_project', 'user_project_id'), 6, true);
SELECT setval(pg_get_serial_sequence('task_list', 'task_list_id'), 2, true);
SELECT setval(pg_get_serial_sequence('task', 'task_id'), 2, true);
SELECT setval(pg_get_serial_sequence('comment', 'comment_id'), 2, true);

COMMIT;