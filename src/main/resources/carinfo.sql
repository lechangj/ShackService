CREATE TABLE car (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `serial` INT,
    `make` VARCHAR(20) CHARACTER SET utf8,
    `model` VARCHAR(20) CHARACTER SET utf8,
    `color` VARCHAR(20) CHARACTER SET utf8,
    `year` INT,
    `engine_type` VARCHAR(20) CHARACTER SET utf8,
    `list_price` DECIMAL(10,2),
    `image` VARCHAR(30) CHARACTER SET utf8,
   	PRIMARY KEY (id),
   	UNIQUE (serial)
);
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (154204,'TESLA','MRX','red',2014,'V6',57838,'pic0.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (857088,'LANDROVER','XKR','red',2013,'V4',63617,'pic1.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (948559,'MERCEDES','RX4','white',2008,'V8',47805,'pic2.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (921244,' HONDA','CSX','gray',2008,'V4',36437,'pic3.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (200501,'ACURA','XKR','black',2012,'V12',61434,'pic4.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (584342,'MERCEDES','RX4','white',2007,'V12',34641,'pic5.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (224277,'MERCEDES','RX4','red',2015,'V6',66182,'pic6.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (393361,'TESLA','DU','gray',2015,'V4',45861,'pic7.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (185951,'MERCEDES','RX4','black',2008,'V8',62893,'pic8.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (307146,'MERCEDES','XKR','white',2011,'V6',68684,'pic9.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (103487,'LANDROVER','XKR','gray',2011,'V6',45043,'pic10.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (743475,'TESLA','RX4','black',2010,'V6',50242,'pic11.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (659103,' HONDA','MRX','gray',2007,'V12',54825,'pic12.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (355030,'LANDROVER','XKR','red',2010,'V4',69859,'pic13.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (404423,'MERCEDES','RX4','yellow',2013,'V6',50531,'pic14.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (158810,'ACURA','XKR','black',2015,'V8',65493,'pic15.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (687604,' HONDA','CSX','yellow',2010,'V8',47543,'pic16.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (602656,'LANDROVER','MRX','red',2016,'V8',38261,'pic17.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (678519,'TOYOTA','MRX','gray',2012,'V6',61883,'pic18.jpg');
INSERT INTO car (`serial`,`make`,`model`,`color`,`year`,`engine_type`,`list_price`,`image`) VALUES (637231,' HONDA','MRX','yellow',2007,'V4',36892,'pic19.jpg');