BEGIN;

CREATE TYPE user_role_enum AS ENUM ('default', 'admin');

CREATE TABLE IF NOT EXISTS users (
    id                  int            GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username            varchar(30)    CONSTRAINT user_natural_key UNIQUE NOT NULL,
    displayname         varchar(30)    NOT NULL,
    pw_hash             text           NOT NULL,
    user_role           user_role_enum DEFAULT 'default' NOT NULL,
    weight              real,
    are_workouts_public boolean        DEFAULT false NOT NULL
);

CREATE TYPE count_type_enum AS ENUM ('single', 'lap', 'set');
CREATE TYPE work_time_type_enum AS ENUM ('time', 'repetition');
CREATE TYPE load_type_enum AS ENUM ('distance', 'bodyweight', 'weight');
CREATE TYPE rest_type_enum AS ENUM ('optional', 'beforeCount', 'afterCount');

CREATE TABLE IF NOT EXISTS exercise_types (
    id                 int                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    exercise_type_name varchar(255)        CONSTRAINT exercise_type_natural_key UNIQUE NOT NULL,
    count_type         count_type_enum     NOT NULL,
    work_time_type     work_time_type_enum NOT NULL,
    load_type          load_type_enum      NOT NULL,
    rest_type          rest_type_enum      DEFAULT 'optional' NOT NULL
);

CREATE TABLE IF NOT EXISTS exercises (
    id               int          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    exercise_name    varchar(255) CONSTRAINT exercise_natural_key UNIQUE NOT NULL,
    exercise_type_id int          REFERENCES exercise_types NOT NULL,
    description      text
);

CREATE TABLE IF NOT EXISTS user_exercises (
    id             int       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id        int       REFERENCES users ON DELETE CASCADE NOT NULL,
    exercise_id    int       REFERENCES exercises ON DELETE RESTRICT NOT NULL,
    datetime       timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    exercise_count smallint  DEFAULT 1 NOT NULL,
    notes          text
);

CREATE TYPE subjective_effort_type_enum AS ENUM ('CR-10', 'Borg');

CREATE TABLE IF NOT EXISTS user_exercise_statistics (
    user_exercise_id        int                         REFERENCES user_exercises ON DELETE CASCADE NOT NULL,
    index                   smallint                    NOT NULL,
    work_time               real,
    load                    real,
    rest_length             interval,
    subjective_effort_type  subjective_effort_type_enum DEFAULT 'CR-10' NOT NULL,
    subjective_effort_value real,
    PRIMARY KEY (user_exercise_id, index)
);

COMMIT;