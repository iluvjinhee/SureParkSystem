USE mysql;

CREATE USER 'ohteam'@'localhost' IDENTIFIED BY 'ohteamchoigo';
#CREATE USER 'ohteam'@'%' IDENTIFIED BY 'ohteamchoigo';

CREATE DATABASE `sureparkdb`;

USE sureparkdb;

GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'localhost';
#GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'%';

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
	`password` VARBINARY(50) NOT NULL,
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	`authority_id` INT(11) NULL DEFAULT '3',
	PRIMARY KEY (`email`),
	INDEX `FK_user_authority` (`authority_id`),
	CONSTRAINT `FK_user_authority` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



CREATE TABLE `parkinglot` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`login_id` VARCHAR(255) NOT NULL,
	`login_pw` VARBINARY(255) NOT NULL,
	`name` VARCHAR(255) NOT NULL,
	`fee` VARCHAR(50) NOT NULL DEFAULT '5',
	`graceperiod` VARCHAR(50) NOT NULL DEFAULT '30',
	`user_email` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `login_id` (`login_id`),
	INDEX `FK_parkinglot_user` (`user_email`),
	CONSTRAINT `FK_parkinglot_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=13
;

INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('1st parkinglot', AES_ENCRYPT('parkinglot001', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot001');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('2nd parkinglot', AES_ENCRYPT('parkinglot002', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot002');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('3rd parkinglot', AES_ENCRYPT('parkinglot003', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot003');



CREATE TABLE `reservation` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`user_email` VARCHAR(255) NOT NULL,
	`datetime` DATETIME NOT NULL,
	`parkinglot_id` INT(11) NOT NULL,
	`payment` VARCHAR(255) NOT NULL,
	`confirmation` VARCHAR(255) NULL DEFAULT NULL,
	`state` INT(11) NOT NULL,
	`fee` VARCHAR(50) NOT NULL,
	`graceperiod` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_reservation_parkinglot` (`parkinglot_id`),
	INDEX `FK_reservation_user` (`user_email`),
	CONSTRAINT `FK_reservation_parkinglot` FOREIGN KEY (`parkinglot_id`) REFERENCES `parkinglot` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT `FK_reservation_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=23
;



CREATE TABLE `parking` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`reservation_id` BIGINT(20) NOT NULL,
	`assigned_slot` VARCHAR(255) NOT NULL,
	`parked_slot` VARCHAR(255) NULL DEFAULT NULL,
	`parking_time` DATETIME NULL DEFAULT NULL,
	`unparking_time` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_parking_reservation` (`reservation_id`),
	CONSTRAINT `FK_parking_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10
;



