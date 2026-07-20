INSERT INTO app_user (username, displayname, user_role, weight, are_workouts_public, pw_hash) VALUES ('frank', 'Frank', 'admin', 65.2, true, 'asfdeas');

INSERT INTO exercise_type (exercise_type_name, count_type, work_time_type, load_type, rest_type)
    VALUES ('Distance run', 'single', 'time', 'distance', DEFAULT),
           ('Weightlifting', 'set', 'repetition', 'weight', 'afterCount');

INSERT INTO exercise (exercise_name, description, exercise_type_id)
    VALUES ('Road run', 'An easy training run around the streets', 1),
           ('Bench press', 'Lying on a bench, press a barbell up', 2);

INSERT INTO user_workout (user_id, exercise_id, datetime, workout_count, notes, subjective_effort_type) 
    VALUES (1, 1, '2026-07-15T08:40:17.751209Z', DEFAULT, 'Saw a cool rock', DEFAULT);
INSERT INTO user_workout_statistic (user_workout_id, index, work_time, load, rest_length, subjective_effort_value)
    VALUES (1, 0, 2700000, 8.2, null, 6);

INSERT INTO user_workout (user_id, exercise_id, datetime, workout_count, notes, subjective_effort_type) 
    VALUES (1, 1, '2026-07-15T08:40:17.751209Z', DEFAULT, 'Saw a cool rock', DEFAULT);
INSERT INTO user_workout_statistic (user_workout_id, index, work_time, load, rest_length, subjective_effort_value)
    VALUES (2, 0, 2700000, 8.2, null, 6);

INSERT INTO user_workout (user_id, exercise_id, datetime, workout_count, notes, subjective_effort_type) 
    VALUES (1, 1, '2026-07-15T08:40:17.751209Z', DEFAULT, 'Saw a cool rock', DEFAULT);
INSERT INTO user_workout_statistic (user_workout_id, index, work_time, load, rest_length, subjective_effort_value)
    VALUES (3, 0, 2700000, 8.2, null, 6);

INSERT INTO user_workout (user_id, exercise_id, datetime, workout_count, notes, subjective_effort_type) 
    VALUES (1, 1, '2026-07-15T08:40:17.751209Z', DEFAULT, 'Saw a cool rock', DEFAULT);
INSERT INTO user_workout_statistic (user_workout_id, index, work_time, load, rest_length, subjective_effort_value)
    VALUES (4, 0, 2700000, 8.2, null, 6);

INSERT INTO user_workout (user_id, exercise_id, datetime, workout_count, notes, subjective_effort_type)
    VALUES (1, 1, '2026-07-15T08:40:17.751209Z', DEFAULT, 'Saw a cool rock', DEFAULT);
INSERT INTO user_workout_statistic (user_workout_id, index, work_time, load, rest_length, subjective_effort_value)
    VALUES (5, 0, 2700000, 8.2, null, 6);