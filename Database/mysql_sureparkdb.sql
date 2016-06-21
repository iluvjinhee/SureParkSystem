-- --------------------------------------------------------
-- ȣ��Ʈ:                          127.0.0.1
-- ���� ����:                        5.7.13-log - MySQL Community Server (GPL)
-- ���� OS:                        Win64
-- HeidiSQL ����:                  9.3.0.5093
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- CREATE USER 'ohteam'@'localhost' IDENTIFIED BY 'ohteamchoigo';
-- CREATE USER 'ohteam'@'%' IDENTIFIED BY 'ohteamchoigo';

-- sureparkdb �����ͺ��̽� ���� ��������
CREATE DATABASE IF NOT EXISTS `sureparkdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sureparkdb`;

-- GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'localhost';
-- GRANT ALL PRIVILEGES ON sureparkdb.* TO 'ohteam'@'%';

-- ���̺� sureparkdb.authority ���� ��������
CREATE TABLE IF NOT EXISTS `authority` (
  `id` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sureparkdb`.`authority` VALUES (1, 'OWNER');
INSERT INTO `sureparkdb`.`authority` VALUES (2, 'ATTENDANT');
INSERT INTO `sureparkdb`.`authority` VALUES (3, 'DRIVER');
-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.change_history ���� ��������
CREATE TABLE IF NOT EXISTS `change_history` (
  `parkinglot_id` varchar(50) NOT NULL,
  `changed_time` datetime NOT NULL,
  `changed_type` int(11) NOT NULL,
  `changed_value` varchar(50) NOT NULL,
  KEY `paringlot_id` (`parkinglot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.occupancy_rate ���� ��������
CREATE TABLE IF NOT EXISTS `occupancy_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `month` smallint(6) NOT NULL,
  `day` smallint(6) NOT NULL,
  `hour` smallint(6) NOT NULL,
  `parkinglot_id` varchar(50) NOT NULL,
  `occupancy_rate` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.parking ���� ��������
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.parkinglot ���� ��������
CREATE TABLE IF NOT EXISTS `parkinglot` (
  `login_id` varchar(50) NOT NULL,
  `password` varbinary(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `fee` varchar(50) NOT NULL DEFAULT '5',
  `grace_period` varchar(50) NOT NULL DEFAULT '30',
  `user_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`login_id`),
  KEY `FK_parkinglot_user` (`user_email`),
  CONSTRAINT `FK_parkinglot_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `password`, `address`) 
VALUES ('SP001', AES_ENCRYPT('SP001', UNHEX(SHA2('SureparksystemByOhteam',256))), 'SurePark Parkinglot, 5000 Forbes Avenue,Pittsburgh, PA 15213 USA');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `password`, `address`) 
VALUES ('SP002', AES_ENCRYPT('SP002', UNHEX(SHA2('SureparksystemByOhteam',256))), 'SurePark Parkinglot, 2000 Fifth Avenue,Pittsburgh, PA 15213 USA');
INSERT INTO `sureparkdb`.`parkinglot`(`login_id`, `password`, `address`) 
VALUES ('SP003', AES_ENCRYPT('SP003', UNHEX(SHA2('SureparksystemByOhteam',256))), 'SurePark Parkinglot, 1000 Centre Avenue,Pittsburgh, PA 15213 USA');


-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.reservation ���� ��������
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_email` varchar(255) NOT NULL,
  `reservation_time` datetime NOT NULL,
  `parkinglot_id` varchar(50) NOT NULL,
  `credit_info` varbinary(50) NOT NULL,
  `confirmation_info` varchar(255) DEFAULT NULL,
  `parking_fee` varchar(50) NOT NULL,
  `grace_period` varchar(50) NOT NULL,
  `reservation_state` int(11) NOT NULL,
  `payment` float DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_reservation_user` (`user_email`),
  KEY `FK_reservation_parkinglot` (`parkinglot_id`),
  CONSTRAINT `FK_reservation_parkinglot` FOREIGN KEY (`parkinglot_id`) REFERENCES `parkinglot` (`login_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_reservation_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.statistics_info ���� ��������
CREATE TABLE IF NOT EXISTS `statistics_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parkinglot_id` varchar(50) NOT NULL,
  `date_time` datetime NOT NULL,
  `year` int(11) NOT NULL,
  `month` smallint(6) NOT NULL,
  `day` smallint(6) NOT NULL,
  `revenue` float NOT NULL,
  `occupancy_rate` float NOT NULL,
  `cancel_rate` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
-- ���̺� sureparkdb.user ���� ��������
CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varbinary(255) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `authority_id` int(11) DEFAULT '3',
  PRIMARY KEY (`email`),
  KEY `FK_user_authority` (`authority_id`),
  CONSTRAINT `FK_user_authority` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `sureparkdb`.`user`(`username`, `email`, `password`, `authority_id`) 
VALUES ('Tony', 'antony@cmu.edu', AES_ENCRYPT('1234567890', UNHEX(SHA2('SureparksystemByOhteam',256))), 1);
-- INSERT INTO `sureparkdb`.`user`(`username`, `email`, `password`, `authority_id`) 
-- VALUES ('daedon', 'daedon.jeon@lge.com', AES_ENCRYPT('1234567891', UNHEX(SHA2('SureparksystemByOhteam',256))), 2);
-- INSERT INTO `sureparkdb`.`user`(`username`, `email`, `password`, `authority_id`) 
-- VALUES ('sanghee', 'sanghee3.lee@lge.com', AES_ENCRYPT('1234567892', UNHEX(SHA2('SureparksystemByOhteam',256))), 3);

-- ������ �����Ͱ� ���õǾ� ���� �ʽ��ϴ�.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
