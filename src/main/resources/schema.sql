CREATE TABLE `hospital` (
  `idhospital` int NOT NULL AUTO_INCREMENT,
  `hosp_name` varchar(255) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `free_beds` int NOT NULL,
  PRIMARY KEY (`idhospital`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `specialty` (
  `idspecialty` int NOT NULL AUTO_INCREMENT,
  `spec_name` varchar(255) NOT NULL,
  PRIMARY KEY (`idspecialty`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `hospital_specialty` (
  `idhospital` int NOT NULL,
  `idspecialty` int NOT NULL,
  PRIMARY KEY (`idhospital`,`idspecialty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
