-- DB: traffic_management_system
create database traffic_management_system;
use traffic_management_system;


CREATE TABLE account (
  id varchar(12) NOT NULL,
  username varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role enum('USER','ADMIN','MODERATOR') NOT NULL DEFAULT 'USER',
  last_login datetime DEFAULT NULL,
  created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY username (username),
  UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE person (
  id varchar(12) NOT NULL,
  full_name varchar(100) NOT NULL,
  birth_date datetime(6) DEFAULT NULL,
  gender enum('MALE','FEMALE') DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  phone_number varchar(20) DEFAULT NULL,
  face_path varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE car (
  id int NOT NULL AUTO_INCREMENT,
  license_plate varchar(20) NOT NULL,
  brand varchar(50) DEFAULT NULL,
  color varchar(30) DEFAULT NULL,
  owner_id varchar(12) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY license_plate (license_plate),
  KEY idx_car_owner (owner_id),
  CONSTRAINT car_ibfk_1 FOREIGN KEY (owner_id) REFERENCES person (id)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE car_scan_logs (
  id int NOT NULL AUTO_INCREMENT,
  scan_timestamp datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  license_plate varchar(20) NOT NULL,
  operator_id varchar(12) NOT NULL,
  car_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY operator_id (operator_id),
  KEY car_id (car_id),
  KEY idx_car_scan_time (scan_timestamp),
  KEY idx_car_scan_plate (license_plate),
  CONSTRAINT car_scan_logs_ibfk_1 FOREIGN KEY (operator_id) REFERENCES account (id),
  CONSTRAINT car_scan_logs_ibfk_2 FOREIGN KEY (car_id) REFERENCES car (id)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE car_violations_report (
  id int NOT NULL AUTO_INCREMENT,
  license_plate varchar(20) NOT NULL,
  violator_id varchar(12) NOT NULL,
  officer_id varchar(12) NOT NULL,
  report_time datetime NOT NULL,
  report_location text NOT NULL,
  penalty_type text NOT NULL,
  resolution_deadline datetime NOT NULL,
  resolution_status tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  KEY license_plate (license_plate),
  KEY violator_id (violator_id),
  KEY officer_id (officer_id),
  CONSTRAINT car_violations_report_ibfk_1 FOREIGN KEY (license_plate) REFERENCES car (license_plate),
  CONSTRAINT car_violations_report_ibfk_2 FOREIGN KEY (violator_id) REFERENCES person (id),
  CONSTRAINT car_violations_report_ibfk_3 FOREIGN KEY (officer_id) REFERENCES account (id)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE car_violation_details (
  id int NOT NULL AUTO_INCREMENT,
  violation_id int NOT NULL,
  violation_type varchar(100) NOT NULL,
  fine_amount decimal(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY violation_id (violation_id),
  CONSTRAINT car_violation_details_ibfk_1 FOREIGN KEY (violation_id) REFERENCES car_violations_report (id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE login_history (
  id int NOT NULL AUTO_INCREMENT,
  account_id varchar(12) NOT NULL,
  login_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  logout_time datetime DEFAULT NULL,
  ip_address varchar(50) DEFAULT NULL,
  device_info varchar(255) DEFAULT NULL,
  login_status enum('SUCCESS','FAILED') NOT NULL,
  PRIMARY KEY (id),
  KEY account_id (account_id),
  CONSTRAINT login_history_ibfk_1 FOREIGN KEY (account_id) REFERENCES account (id)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE motorcycle (
  id int NOT NULL AUTO_INCREMENT,
  license_plate varchar(20) NOT NULL,
  brand varchar(50) DEFAULT NULL,
  color varchar(30) DEFAULT NULL,
  owner_id varchar(12) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY license_plate (license_plate),
  KEY idx_motorcycle_owner (owner_id),
  CONSTRAINT motorcycle_ibfk_1 FOREIGN KEY (owner_id) REFERENCES person (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE motorcycle_scan_logs (
  id int NOT NULL AUTO_INCREMENT,
  scan_timestamp datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  license_plate varchar(20) NOT NULL,
  operator_id varchar(12) NOT NULL,
  motorcycle_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY operator_id (operator_id),
  KEY motorcycle_id (motorcycle_id),
  KEY idx_mc_scan_time (scan_timestamp),
  KEY idx_mc_scan_plate (license_plate),
  CONSTRAINT motorcycle_scan_logs_ibfk_1 FOREIGN KEY (operator_id) REFERENCES account (id),
  CONSTRAINT motorcycle_scan_logs_ibfk_2 FOREIGN KEY (motorcycle_id) REFERENCES motorcycle (id)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




CREATE TABLE motorcycle_violations_report (
  id int NOT NULL AUTO_INCREMENT,
  license_plate varchar(20) NOT NULL,
  violator_id varchar(12) NOT NULL,
  officer_id varchar(12) NOT NULL,
  report_time datetime NOT NULL,
  report_location text NOT NULL,
  penalty_type text NOT NULL,
  resolution_deadline datetime NOT NULL,
  resolution_status tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  KEY license_plate (license_plate),
  KEY violator_id (violator_id),
  KEY officer_id (officer_id),
  CONSTRAINT motorcycle_violations_report_ibfk_1 FOREIGN KEY (license_plate) REFERENCES motorcycle (license_plate),
  CONSTRAINT motorcycle_violations_report_ibfk_2 FOREIGN KEY (violator_id) REFERENCES person (id),
  CONSTRAINT motorcycle_violations_report_ibfk_3 FOREIGN KEY (officer_id) REFERENCES account (id)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE motorcycle_violation_details (
  id int NOT NULL AUTO_INCREMENT,
  violation_id int NOT NULL,
  violation_type varchar(100) NOT NULL,
  fine_amount decimal(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY violation_id (violation_id),
  CONSTRAINT motorcycle_violation_details_ibfk_1 FOREIGN KEY (violation_id) REFERENCES motorcycle_violations_report (id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TRUYỀN DATA
INSERT INTO person
VALUES
('058205002155', 'Nguyễn Hữu Tuấn Khang', '2005-12-04', 'MALE', 'Binh Duong', '0366408263', '058205002155.jpg');

INSERT INTO motorcycle(license_plate,brand,color,owner_id)
VALUES 
("85C140689", "Honda","Red", "058205002155");
 

