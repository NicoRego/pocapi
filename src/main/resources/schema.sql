DROP DATABASE IF EXISTS `pocapi`;
CREATE DATABASE `pocapi`;

USE pocapi;

DROP TABLE IF EXISTS `hospital`;
CREATE TABLE `hospital` (
  `idhospital` int NOT NULL AUTO_INCREMENT,
  `hosp_name` varchar(255) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `free_beds` int NOT NULL,
  PRIMARY KEY (`idhospital`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `specialty`;
CREATE TABLE `specialty` (
  `idspecialty` int NOT NULL AUTO_INCREMENT,
  `spec_name` varchar(255) NOT NULL,
  PRIMARY KEY (`idspecialty`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `hospital_specialty`;
CREATE TABLE `hospital_specialty` (
  `hospitalid` int NOT NULL,
  `specialtyid` int NOT NULL,
  PRIMARY KEY (`hospitalid`,`specialtyid`)
  constraint fk_type_hp
        foreign key(hospitalid)
            references hospital(id),
    constraint fk_type_sp
        foreign key(specialityid)
            references speciality(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
