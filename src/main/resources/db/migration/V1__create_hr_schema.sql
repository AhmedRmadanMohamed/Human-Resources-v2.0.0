CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_name UNIQUE (name)
);

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(254) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    activated BOOLEAN NOT NULL DEFAULT TRUE,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id)
);
CREATE INDEX idx_users_role_id ON users (role_id);

CREATE TABLE employers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    company_name VARCHAR(120) NOT NULL,
    phone_number VARCHAR(32),
    website_url VARCHAR(255),
    CONSTRAINT pk_employers PRIMARY KEY (id),
    CONSTRAINT uk_employers_user_id UNIQUE (user_id),
    CONSTRAINT fk_employers_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE job_positions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    position_name VARCHAR(120) NOT NULL,
    CONSTRAINT pk_job_positions PRIMARY KEY (id),
    CONSTRAINT uk_job_positions_name UNIQUE (position_name)
);

CREATE TABLE job_seekers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    birth_date DATE,
    nationality_id VARCHAR(64),
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    summary VARCHAR(1000),
    current_employer_id BIGINT,
    desired_position_id BIGINT,
    CONSTRAINT pk_job_seekers PRIMARY KEY (id),
    CONSTRAINT uk_job_seekers_user_id UNIQUE (user_id),
    CONSTRAINT fk_job_seekers_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_job_seekers_employer FOREIGN KEY (current_employer_id) REFERENCES employers (id) ON DELETE SET NULL,
    CONSTRAINT fk_job_seekers_position FOREIGN KEY (desired_position_id) REFERENCES job_positions (id) ON DELETE SET NULL
);
CREATE INDEX idx_job_seekers_employer_id ON job_seekers (current_employer_id);
CREATE INDEX idx_job_seekers_position_id ON job_seekers (desired_position_id);

CREATE TABLE schools (
    id BIGINT NOT NULL AUTO_INCREMENT,
    school_name VARCHAR(200) NOT NULL,
    CONSTRAINT pk_schools PRIMARY KEY (id),
    CONSTRAINT uk_schools_name UNIQUE (school_name)
);

CREATE TABLE departments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    department_name VARCHAR(160) NOT NULL,
    school_id BIGINT NOT NULL,
    CONSTRAINT pk_departments PRIMARY KEY (id),
    CONSTRAINT uk_departments_school_name UNIQUE (school_id, department_name),
    CONSTRAINT fk_departments_school FOREIGN KEY (school_id) REFERENCES schools (id) ON DELETE CASCADE
);
CREATE INDEX idx_departments_school_id ON departments (school_id);

CREATE TABLE attended_schools (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    school_id BIGINT NOT NULL,
    start_date DATE,
    graduation_date DATE,
    CONSTRAINT pk_attended_schools PRIMARY KEY (id),
    CONSTRAINT fk_attended_schools_job_seeker FOREIGN KEY (job_seeker_id)
        REFERENCES job_seekers (id) ON DELETE CASCADE,
    CONSTRAINT fk_attended_schools_school FOREIGN KEY (school_id)
        REFERENCES schools (id) ON DELETE CASCADE,
    CONSTRAINT ck_attended_schools_dates CHECK (
        graduation_date IS NULL OR start_date IS NULL OR graduation_date >= start_date
    )
);
CREATE INDEX idx_attended_schools_job_seeker_id ON attended_schools (job_seeker_id);
CREATE INDEX idx_attended_schools_school_id ON attended_schools (school_id);

CREATE TABLE cities (
    id BIGINT NOT NULL AUTO_INCREMENT,
    city_name VARCHAR(120) NOT NULL,
    CONSTRAINT pk_cities PRIMARY KEY (id),
    CONSTRAINT uk_cities_name UNIQUE (city_name)
);

CREATE TABLE work_places (
    id BIGINT NOT NULL AUTO_INCREMENT,
    work_place_name VARCHAR(80) NOT NULL,
    CONSTRAINT pk_work_places PRIMARY KEY (id),
    CONSTRAINT uk_work_places_name UNIQUE (work_place_name)
);

CREATE TABLE work_times (
    id BIGINT NOT NULL AUTO_INCREMENT,
    work_time_name VARCHAR(80) NOT NULL,
    CONSTRAINT pk_work_times PRIMARY KEY (id),
    CONSTRAINT uk_work_times_name UNIQUE (work_time_name)
);

CREATE TABLE jobs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(2000) NOT NULL,
    minimum_salary DECIMAL(12, 2),
    maximum_salary DECIMAL(12, 2),
    open_positions INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deadline DATE,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    position_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    employer_id BIGINT NOT NULL,
    work_place_id BIGINT NOT NULL,
    work_time_id BIGINT NOT NULL,
    CONSTRAINT pk_jobs PRIMARY KEY (id),
    CONSTRAINT fk_jobs_position FOREIGN KEY (position_id) REFERENCES job_positions (id),
    CONSTRAINT fk_jobs_city FOREIGN KEY (city_id) REFERENCES cities (id),
    CONSTRAINT fk_jobs_employer FOREIGN KEY (employer_id) REFERENCES employers (id),
    CONSTRAINT fk_jobs_work_place FOREIGN KEY (work_place_id) REFERENCES work_places (id),
    CONSTRAINT fk_jobs_work_time FOREIGN KEY (work_time_id) REFERENCES work_times (id),
    CONSTRAINT ck_jobs_open_positions CHECK (open_positions >= 0),
    CONSTRAINT ck_jobs_salary CHECK (
        minimum_salary IS NULL OR maximum_salary IS NULL OR maximum_salary >= minimum_salary
    )
);
CREATE INDEX idx_jobs_position_id ON jobs (position_id);
CREATE INDEX idx_jobs_city_id ON jobs (city_id);
CREATE INDEX idx_jobs_employer_id ON jobs (employer_id);
CREATE INDEX idx_jobs_work_place_id ON jobs (work_place_id);
CREATE INDEX idx_jobs_work_time_id ON jobs (work_time_id);

CREATE TABLE work_experiences (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    position_id BIGINT,
    workplace_name VARCHAR(160) NOT NULL,
    start_date DATE,
    end_date DATE,
    CONSTRAINT pk_work_experiences PRIMARY KEY (id),
    CONSTRAINT fk_work_experiences_job_seeker FOREIGN KEY (job_seeker_id)
        REFERENCES job_seekers (id) ON DELETE CASCADE,
    CONSTRAINT fk_work_experiences_position FOREIGN KEY (position_id)
        REFERENCES job_positions (id) ON DELETE SET NULL,
    CONSTRAINT ck_work_experiences_dates CHECK (
        end_date IS NULL OR start_date IS NULL OR end_date >= start_date
    )
);
CREATE INDEX idx_work_experiences_job_seeker_id ON work_experiences (job_seeker_id);
CREATE INDEX idx_work_experiences_position_id ON work_experiences (position_id);

CREATE TABLE language_levels (
    id BIGINT NOT NULL AUTO_INCREMENT,
    level_name VARCHAR(80) NOT NULL,
    CONSTRAINT pk_language_levels PRIMARY KEY (id),
    CONSTRAINT uk_language_levels_name UNIQUE (level_name)
);

CREATE TABLE known_languages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    language_level_id BIGINT NOT NULL,
    language_name VARCHAR(80) NOT NULL,
    CONSTRAINT pk_known_languages PRIMARY KEY (id),
    CONSTRAINT uk_known_languages_job_seeker_name UNIQUE (job_seeker_id, language_name),
    CONSTRAINT fk_known_languages_job_seeker FOREIGN KEY (job_seeker_id)
        REFERENCES job_seekers (id) ON DELETE CASCADE,
    CONSTRAINT fk_known_languages_level FOREIGN KEY (language_level_id)
        REFERENCES language_levels (id)
);
CREATE INDEX idx_known_languages_level_id ON known_languages (language_level_id);

CREATE TABLE images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_seeker_id BIGINT NOT NULL,
    image_title VARCHAR(120) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    CONSTRAINT pk_images PRIMARY KEY (id),
    CONSTRAINT fk_images_job_seeker FOREIGN KEY (job_seeker_id)
        REFERENCES job_seekers (id) ON DELETE CASCADE
);
CREATE INDEX idx_images_job_seeker_id ON images (job_seeker_id);

CREATE TABLE activation_requests (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    job_id BIGINT,
    requested_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    detail VARCHAR(1000),
    CONSTRAINT pk_activation_requests PRIMARY KEY (id),
    CONSTRAINT uk_activation_requests_job_id UNIQUE (job_id),
    CONSTRAINT fk_activation_requests_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_activation_requests_job FOREIGN KEY (job_id) REFERENCES jobs (id) ON DELETE CASCADE
);
CREATE INDEX idx_activation_requests_role_id ON activation_requests (role_id);
