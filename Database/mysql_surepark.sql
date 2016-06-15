USE mysql;

CREATE USER 'team5'@'localhost' IDENTIFIED BY 'team5';

CREATE DATABASE `sureparkdb` /*!40100 COLLATE 'utf8_general_ci' */;

USE sureparkdb;

GRANT ALL PRIVILEGES ON sureparkdb.* TO 'team5'@'localhost';

CREATE TABLE `account_info` (
	`login_id` VARCHAR(50) NOT NULL,
	`login_pw` VARCHAR(50) NOT NULL,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`authority` VARCHAR(50) NULL DEFAULT 'USER',
	`credit_number` VARCHAR(50) NULL DEFAULT NULL,
	`credit_name` VARCHAR(50) NULL DEFAULT NULL,
	`credit_expired` VARCHAR(50) NULL DEFAULT NULL,
	UNIQUE INDEX `login_id` (`login_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('admin', 'admin', 'admin', 'ADMIN');
INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('parkinglot01', 'team5', 'parkinglot', 'PARKINGLOT');
INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('parkinglot02', 'team5', 'parkinglot', 'PARKINGLOT');
INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('parkinglot03', 'team5', 'parkinglot', 'PARKINGLOT');
INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('parkinglot04', 'team5', 'parkinglot', 'PARKINGLOT');
INSERT INTO `sureparkdb`.`account_info`(`login_id`, `login_pw`, `name`, `authority`) VALUES ('parkinglot05', 'team5', 'parkinglot', 'PARKINGLOT');

CREATE TABLE `reservation_info` (
	`login_id` VARCHAR(50) NOT NULL,
	`reservation_time` DATETIME NOT NULL,
	`parkinglot_id` VARCHAR(50) NOT NULL,
	`parking_fee` INT(11) NOT NULL,
	`grace_period` INT(11) NOT NULL,
	`arrival_time` DATETIME NOT NULL,
	`departure_time` DATETIME NOT NULL,
	`confirmation_code` VARCHAR(50) NOT NULL,
	`assigned_slot` INT(11) NOT NULL
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `usage_info` (
	`date` DATE NULL DEFAULT NULL
)
ENGINE=InnoDB
;

