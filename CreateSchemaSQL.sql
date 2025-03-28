use car_rental_db;

CREATE TABLE `user` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(25) UNIQUE NOT NULL,
    password VARCHAR(150) NOT NULL,
    name VARCHAR(50) NOT NULL,
    familyName VARCHAR(50),
    phoneNumber VARCHAR(10) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    avatar VARCHAR(255),
    dateOfBirth DATE NOT NULL, 
    isStaff TINYINT NOT NULL DEFAULT 0
);


CREATE TABLE partner (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    detail VARCHAR(255)
);


CREATE TABLE contract (
    id INT PRIMARY KEY AUTO_INCREMENT,
    detail VARCHAR(255),
    createDate DATE NOT NULL,
    status ENUM("SIGNED", "LIQUIDATED") NOT NULL,
    partnerId INT NOT NULL,
    FOREIGN KEY (partnerId) REFERENCES partner(id)
);

CREATE TABLE partner_payment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price DOUBLE NOT NULL,
    createDate DATE NOT NULL,
    verifyDate DATE,
    detail VARCHAR(255),
    partnerId INT NOT NULL,
    staffId INT NOT NULL,
    FOREIGN KEY (partnerId) REFERENCES partner(id),
    FOREIGN KEY (staffId) REFERENCES user(id)
);

CREATE TABLE car (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    avatar VARCHAR(25) NOT NULL,
    brand VARCHAR(25) NOT NULL,
    type ENUM('FOUR_SEAT', 'SIX_SEAT', 'NINE_SEAT', 'TWELVE_SEAT', 'SIXTEEN_SEAT') NOT NULL,
    licensePlate VARCHAR(25) UNIQUE NOT NULL,
    detail VARCHAR(255),
    price FLOAT NOT NULL,
    partnerRentalPricePerMonth FLOAT NOT NULL,
    rentalPricePerDay FLOAT NOT NULL,
    status ENUM("FREE", "ON RENTING", "ON REPAIRING", "RETURNED") NOT NULL,
    partnerId INT NOT NULL,
    contractId INT NOT NULL,
    FOREIGN KEY (partnerId) REFERENCES partner(id),
    FOREIGN KEY (contractId) REFERENCES contract(id)
);

CREATE TABLE video (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link VARCHAR(25) UNIQUE NOT NULL,
    detail VARCHAR(255),
    carId INT NOT NULL,
    FOREIGN KEY (carId) REFERENCES car(id)
);

CREATE TABLE car_image (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link VARCHAR(25) UNIQUE NOT NULL,
    detail VARCHAR(255),
    carId INT NOT NULL,
    FOREIGN KEY (carId) REFERENCES car(id)
);

CREATE TABLE collateral (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price FLOAT,
    createDate DATE NOT NULL,
    detail VARCHAR(255),
    status ENUM("WAITING EVALUATE", "RECEIVE SUCCESSFULLY", "RETURN SUCCESSFULLY", "SEIZED") NOT NULL,
    customerId INT,
    FOREIGN KEY (customerId) REFERENCES user(id)
);

CREATE TABLE collateral_image (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link VARCHAR(25) UNIQUE NOT NULL,
    detail VARCHAR(255),
    collateralId INT NOT NULL,
    FOREIGN KEY (collateralId) REFERENCES collateral(id)
);

CREATE TABLE broken_report (
    id INT PRIMARY KEY AUTO_INCREMENT,
    detail VARCHAR(255),
    price FLOAT NOT NULL,
    carId INT NOT NULL,
    FOREIGN KEY (carId) REFERENCES car(id)
);

CREATE TABLE broken_report_image (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link VARCHAR(25) UNIQUE NOT NULL,
    detail VARCHAR(255),
    brokenReportId INT NOT NULL,
    FOREIGN KEY (brokenReportId) REFERENCES broken_report(id)
);

CREATE TABLE rental_order (
    id INT PRIMARY KEY AUTO_INCREMENT,
    createDate DATE NOT NULL,
    rentalBeginDate DATE NOT NULL,
    rentalEndDate DATE NOT NULL,
    price DOUBLE NOT NULL,
    status ENUM("WAITING VERIFY ORDER", "WAITING VERIFY COLLATERAL", 
    "VERIFY SUCCESSFULLY", "BEGIN", "END") NOT NULL,
    receiveDate DATE,
    returnDate DATE,
    carId INT NOT NULL,
    brokenReportId INT,
    staffId INT NOT NULL,
    customerId INT NOT NULL,
    collateralId INT,
    FOREIGN KEY (carId) REFERENCES car(id),
    FOREIGN KEY (brokenReportId) REFERENCES broken_report(id),
    FOREIGN KEY (staffId) REFERENCES user(id),
    FOREIGN KEY (customerId) REFERENCES user(id),
    FOREIGN KEY (collateralId) REFERENCES collateral(id)
);

CREATE TABLE Bill (
    id INT PRIMARY KEY AUTO_INCREMENT,
    detail VARCHAR(255),
    createDate DATE NOT NULL,
    rentalPrice FLOAT NOT NULL,
    lateFee FLOAT NOT NULL,
    repairFee FLOAT NOT NULL,
    totalPrice FLOAT NOT NULL,
    paymentDate DATE,
    rentalOrderId INT NOT NULL,
    staffId INT NOT NULL,
    customerId INT NOT NULL,
    FOREIGN KEY (rentalOrderId) REFERENCES rental_order(id),
    FOREIGN KEY (staffId) REFERENCES user(id),
    FOREIGN KEY (customerId) REFERENCES user(id)
);