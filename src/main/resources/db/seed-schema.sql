BEGIN;

DROP TABLE IF EXISTS app_user, exercise_type, exercise, user_exercise, user_exercise_statistic;
DROP TYPE IF EXISTS user_role_enum, count_type_enum, work_time_type_enum, load_type_enum, rest_type_enum, subjective_effort_type_enum;

CREATE TYPE user_role_enum AS ENUM ('default', 'admin');

CREATE TABLE app_user (
    id                  int               GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username            varchar(30)       CONSTRAINT user_natural_key UNIQUE NOT NULL,
    displayname         varchar(30)       NOT NULL,
    pw_hash             text              NOT NULL,
    user_role           user_role_enum    DEFAULT 'default' NOT NULL,
    weight              double precision,
    are_workouts_public boolean           DEFAULT false NOT NULL
);

CREATE TYPE count_type_enum AS ENUM ('single', 'lap', 'set');
CREATE TYPE work_time_type_enum AS ENUM ('time', 'repetition');
CREATE TYPE load_type_enum AS ENUM ('distance', 'bodyweight', 'weight');
CREATE TYPE rest_type_enum AS ENUM ('optional', 'beforeCount', 'afterCount');

CREATE TABLE exercise_type (
    id                 int                 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    exercise_type_name varchar(255)        CONSTRAINT exercise_type_natural_key UNIQUE NOT NULL,
    count_type         count_type_enum     NOT NULL,
    work_time_type     work_time_type_enum NOT NULL,
    load_type          load_type_enum      NOT NULL,
    rest_type          rest_type_enum      DEFAULT 'optional' NOT NULL
);

CREATE TABLE exercise (
    id               int          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    exercise_name    varchar(255) CONSTRAINT exercise_natural_key UNIQUE NOT NULL,
    exercise_type_id int          REFERENCES exercise_type NOT NULL,
    description      text
);

CREATE TABLE user_exercise (
    id             int       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id        int       REFERENCES app_user ON DELETE CASCADE NOT NULL,
    exercise_id    int       REFERENCES exercise ON DELETE RESTRICT NOT NULL,
    datetime       timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    exercise_count smallint  DEFAULT 1 NOT NULL,
    notes          text
);

CREATE TYPE subjective_effort_type_enum AS ENUM ('CR-10', 'Borg');

CREATE TABLE user_exercise_statistic (
    user_exercise_id        int                         REFERENCES user_exercise ON DELETE CASCADE NOT NULL,
    index                   smallint                    NOT NULL,
    work_time               double precision,
    load                    double precision,
    rest_length             interval,
    subjective_effort_type  subjective_effort_type_enum DEFAULT 'CR-10' NOT NULL,
    subjective_effort_value double precision,
    PRIMARY KEY (user_exercise_id, index)
);

COMMIT;