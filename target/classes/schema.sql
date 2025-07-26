CREATE TABLE IF NOT EXISTS  `department` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `budget` DECIMAL(10, 3) NOT NULL DEFAULT '0.000',
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS  `employee` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `department_id` INT NOT NULL,
    `date_of_joining` DATE NOT NULL,
    `salary` DECIMAL(18, 2) NOT NULL,
    `manager_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`),
    KEY `department_id` (`department_id`),
    KEY `manager_id` (`manager_id`),
    CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
    CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`id`)
);

CREATE TABLE IF NOT EXISTS  `project` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `start_date` TIMESTAMP NULL DEFAULT NULL,
    `end_date` TIMESTAMP NULL DEFAULT NULL,
    `department_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `department_id` (`department_id`),
    CONSTRAINT `project_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
);

CREATE TABLE IF NOT EXISTS  `performance_review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `employee_id` BIGINT NOT NULL,
    `review_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `score` DECIMAL(2, 2) NOT NULL,
    `review_comments` VARCHAR(200) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `employee_id` (`employee_id`),
    CONSTRAINT `performance_review_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
    CONSTRAINT `performance_review_chk_1` CHECK (
        (
            (`score` >= 0)
            AND (`score` <= 10)
        )
    )
);

CREATE TABLE IF NOT EXISTS  `employee_project` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `employee_id` BIGINT NOT NULL,
    `project_id` INT NOT NULL,
    `assigned_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `role` VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `employee_id` (`employee_id`),
    KEY `project_id` (`project_id`),
    CONSTRAINT `employee_project_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
    CONSTRAINT `employee_project_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
);