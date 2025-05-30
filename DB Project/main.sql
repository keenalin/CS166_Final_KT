-- CS166 Database Project Phase 2

-- Drop tables
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Flight;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS FlightInstance;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS MaintenanceRequest;
DROP TABLE IF EXISTS Pilot;
DROP TABLE IF EXISTS Repairs;
DROP TABLE IF EXISTS Technician;
DROP TABLE IF EXISTS Plane;


-- 1. Tables without dependencies
CREATE TABLE Plane (
    PlaneID INTEGER NOT NULL,
    Make TEXT,
    Model TEXT,
    Year INTEGER,
    LastRepairDate DATE,
    PRIMARY KEY (PlaneID)
);

CREATE TABLE Technician (
    TechnicianID INTEGER NOT NULL,
    Name TEXT,
    PRIMARY KEY (TechnicianID)
);

CREATE TABLE Repairs (
    RepairID INTEGER NOT NULL,
    RepairCode INTEGER,
    RepairDate DATE,
    PRIMARY KEY (RepairID)
);

CREATE TABLE Pilot (
    PilotID INTEGER NOT NULL,
    Name TEXT,
    PRIMARY KEY (PilotID)
);

CREATE TABLE MaintenanceRequest (
    RequestID INTEGER NOT NULL,
    RepairCode INTEGER,
    RequestDate DATE,
    PRIMARY KEY (RequestID)
);

CREATE TABLE Schedule (
    ScheduleID INTEGER NOT NULL,
    DayOfWeek TEXT,
    DepartureTime INTEGER,
    ArrivalTime INTEGER,
    PRIMARY KEY (ScheduleID)
);

CREATE TABLE FlightInstance (
    FlightInstanceID INTEGER NOT NULL,
    FlightDate DATE,
    DepartedOnTime BOOLEAN,
    SeatsTotal INTEGER,
    SeatsSold INTEGER,
    NumOfStops INTEGER,
    TicketCost FLOAT,
    PRIMARY KEY (FlightInstanceID)
);

CREATE TABLE Customer (
    CustomerID INTEGER NOT NULL,
    FirstName TEXT,
    LastName TEXT,
    Phone CHAR(13),
    Gender TEXT,
    DOB DATE,
    Address TEXT,
    Zip INTEGER,
    PRIMARY KEY (CustomerID)
);

-- 2. Tables with dependencies
CREATE TABLE Reservation (
    ReservationID INTEGER NOT NULL,
    Status TEXT,
    CustomerID INTEGER,
    FlightInstanceID INTEGER,
    PRIMARY KEY (ReservationID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (FlightInstanceID) REFERENCES FlightInstance(FlightInstanceID)
);

CREATE TABLE Flight (
    FlightNumber INTEGER NOT NULL,
    DepartureCity TEXT,
    ArrivalCity TEXT,
    PlaneID INTEGER,
    ScheduleID INTEGER,
    FlightInstanceID INTEGER,
    PRIMARY KEY (FlightNumber),
    FOREIGN KEY (PlaneID) REFERENCES Plane(PlaneID),
    FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID),
    FOREIGN KEY (FlightInstanceID) REFERENCES FlightInstance(FlightInstanceID)
);


-- Print tables
SELECT * FROM Flight;
SELECT * FROM Plane;
SELECT * FROM Technician;
SELECT * FROM Repairs;
SELECT * FROM Pilot;
SELECT * FROM MaintenanceRequest;
SELECT * FROM Schedule;
SELECT * FROM FlightInstance;
SELECT * FROM Customer;
SELECT * FROM Reservation;
