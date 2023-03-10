DROP DATABASE IF EXISTS `pocapi`;
CREATE DATABASE `pocapi`;

USE pocapi;

DROP TABLE IF EXISTS `hospital`;
CREATE TABLE `hospital` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `free_beds` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `specialty`;
CREATE TABLE `specialty` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `hospital_specialty`;
CREATE TABLE `hospital_specialty` (
  `idhospital` int NOT NULL,
  `idspecialty` int NOT NULL,
  PRIMARY KEY (`idhospital`,`idspecialty`),
  constraint fk_type_hp
        foreign key(idhospital)
            references hospital(id),
    constraint fk_type_sp
        foreign key(idspecialty)
            references specialty(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
