USE mysql;

CREATE USER 'ohteam'@'localhost' IDENTIFIED BY 'ohteam';

CREATE DATABASE `sureparkdb`;

USE sureparkdb;

GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'localhost';

CREATE TABLE `authority` (
	`id` INT(11) NOT NULL,
	`type` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

INSERT INTO `sureparkdb`.`authority` VALUES (1, 'OWNER');
INSERT INTO `sureparkdb`.`authority` VALUES (2, 'ATTENDANT');
INSERT INTO `sureparkdb`.`authority` VALUES (3, 'DRIVER');


CREATE TABLE `user` (
	`username` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`create_time` DATETIME NULL DEFAULT NULL,
	`authority_id` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`email`),
	INDEX `FK_user_authority` (`authority_id`),
	CONSTRAINT `FK_user_authority` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `parkinglot` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`login_id` VARCHAR(45) NOT NULL,
	`login_pw` VARCHAR(50) NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`fee` FLOAT NOT NULL DEFAULT '5',
	`graceperiod` INT(11) NOT NULL DEFAULT '30',
	`user_email` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `login_id` (`login_id`),
	INDEX `FK_parkinglot_user` (`user_email`),
	CONSTRAINT `FK_parkinglot_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON UPDATE CASCADE ON DELETE SET NULL
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10
;

INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('1st parkinglot', 'parkinglot001', 'parkinglot001');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('2nd parkinglot', 'parkinglot002', 'parkinglot002');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('3rd parkinglot', 'parkinglot003', 'parkinglot003');


CREATE TABLE `reservation` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`user_email` VARCHAR(255) NOT NULL,
	`datetime` DATETIME NOT NULL,
	`parkinglot_id` INT(11) NOT NULL,
	`payment` VARCHAR(45) NOT NULL,
	`confirmation` VARCHAR(45) NOT NULL,
	`state` INT(11) NOT NULL,
	`fee` FLOAT NOT NULL,
	`graceperiod` INT(11) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_reservation_parkinglot` (`parkinglot_id`),
	INDEX `FK_reservation_user` (`user_email`),
	CONSTRAINT `FK_reservation_parkinglot` FOREIGN KEY (`parkinglot_id`) REFERENCES `parkinglot` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT `FK_reservation_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=20
;

CREATE TABLE `parking` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`reservation_id` BIGINT(20) NOT NULL,
	`assigned_slot` INT(11) NOT NULL,
	`parked_slot` INT(11) NULL DEFAULT NULL,
	`parking_time` DATETIME NULL DEFAULT NULL,
	`unparking_time` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_parking_reservation` (`reservation_id`),
	CONSTRAINT `FK_parking_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



