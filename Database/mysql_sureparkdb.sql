-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        5.7.13-log - MySQL Community Server (GPL)
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  9.3.0.5093
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE USER 'ohteam'@'localhost' IDENTIFIED BY 'ohteamchoigo';
--CREATE USER 'ohteam'@'%' IDENTIFIED BY 'ohteamchoigo';

-- sureparkdb 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `sureparkdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sureparkdb`;

GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'localhost';
--GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'%';

-- 테이블 sureparkdb.authority 구조 내보내기
CREATE TABLE IF NOT EXISTS `authority` (
  `id` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sureparkdb`.`authority` VALUES (1, 'OWNER');
INSERT INTO `sureparkdb`.`authority` VALUES (2, 'ATTENDANT');
INSERT INTO `sureparkdb`.`authority` VALUES (3, 'DRIVER');
-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.change_history 구조 내보내기
CREATE TABLE IF NOT EXISTS `change_history` (
  `parkinglot_id` int(11) NOT NULL,
  `changed_time` datetime NOT NULL,
  `changed_type` varchar(50) NOT NULL,
  `changed_value` varchar(50) NOT NULL,
  KEY `paringlot_id` (`parkinglot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.occupancy_rate 구조 내보내기
CREATE TABLE IF NOT EXISTS `occupancy_rate` (
  `id` bigint(20) NOT NULL,
  `month` tinyint(4) NOT NULL,
  `day` tinyint(4) NOT NULL,
  `hour` tinyint(4) NOT NULL,
  `parkinglot_id` int(11) NOT NULL,
  `rate` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.parking 구조 내보내기
CREATE TABLE IF NOT EXISTS `parking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint(20) NOT NULL,
  `assigned_slot` varchar(255) NOT NULL,
  `parked_slot` varchar(255) DEFAULT NULL,
  `parking_time` datetime DEFAULT NULL,
  `unparking_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_parking_reservation` (`reservation_id`),
  CONSTRAINT `FK_parking_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.parkinglot 구조 내보내기
CREATE TABLE IF NOT EXISTS `parkinglot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(255) NOT NULL,
  `login_pw` varbinary(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `fee` varchar(50) NOT NULL DEFAULT '5',
  `graceperiod` varchar(50) NOT NULL DEFAULT '30',
  `user_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_id` (`login_id`),
  KEY `FK_parkinglot_user` (`user_email`),
  CONSTRAINT `FK_parkinglot_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('1st parkinglot', AES_ENCRYPT('parkinglot001', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot001');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('2nd parkinglot', AES_ENCRYPT('parkinglot002', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot002');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `login_pw`, `name`) VALUES ('3rd parkinglot', AES_ENCRYPT('parkinglot003', UNHEX(SHA2('SureparksystemByOhteam',256))), 'parkinglot003');


-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.reservation 구조 내보내기
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_email` varchar(255) NOT NULL,
  `reserve_time` datetime NOT NULL,
  `parkinglot_id` int(11) NOT NULL,
  `credit_info` varchar(255) NOT NULL,
  `confirmation_info` varchar(255) DEFAULT NULL,
  `parking_fee` varchar(50) NOT NULL,
  `graceperiod` varchar(50) NOT NULL,
  `reserve_state` int(11) NOT NULL,
  `payment` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_reservation_parkinglot` (`parkinglot_id`),
  KEY `FK_reservation_user` (`user_email`),
  CONSTRAINT `FK_reservation_parkinglot` FOREIGN KEY (`parkinglot_id`) REFERENCES `parkinglot` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_reservation_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;


-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sureparkdb.user 구조 내보내기
CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varbinary(255) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `authority_id` int(11) DEFAULT '3',
  PRIMARY KEY (`email`),
  KEY `FK_user_authority` (`authority_id`),
  CONSTRAINT `FK_user_authority` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;



