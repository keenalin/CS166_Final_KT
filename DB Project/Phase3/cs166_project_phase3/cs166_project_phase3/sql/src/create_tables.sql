-- Drop existing tables
DROP TABLE IF EXISTS MaintenanceRequest;
DROP TABLE IF EXISTS Repair;
DROP TABLE IF EXISTS Technician;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS FlightInstance;
DROP TABLE IF EXISTS Schedule;
DROP TABLE IF EXISTS Flight;
DROP TABLE IF EXISTS Plane;
DROP TABLE IF EXISTS Pilot;
DROP TABLE IF EXISTS Auth;
DROP TABLE IF EXISTS NewUserCode;

-- Plane Table
CREATE TABLE Plane (
    PlaneID TEXT PRIMARY KEY,
    Make TEXT,
    Model TEXT,
    Year INTEGER,
    LastRepairDate DATE
);

-- Flight Table
CREATE TABLE Flight (
    FlightNumber TEXT PRIMARY KEY,
    PlaneID TEXT,
    DepartureCity TEXT,
    ArrivalCity TEXT,
    FOREIGN KEY (PlaneID) REFERENCES Plane(PlaneID)
);

-- Schedule Table
CREATE TABLE Schedule (
    ScheduleID INTEGER PRIMARY KEY,
    FlightNumber TEXT,
    DayOfWeek TEXT,
    DepartureTime TIME,
    ArrivalTime TIME,
    FOREIGN KEY (FlightNumber) REFERENCES Flight(FlightNumber)
);

-- FlightInstance Table
CREATE TABLE FlightInstance (
    FlightInstanceID INTEGER PRIMARY KEY,
    FlightNumber TEXT,
    FlightDate DATE,
    DepartedOnTime BOOLEAN,
    ArrivedOnTime BOOLEAN,
    SeatsTotal INTEGER,
    SeatsSold INTEGER,
    NumOfStops INTEGER,
    TicketCost DECIMAL(10,2),
    FOREIGN KEY (FlightNumber) REFERENCES Flight(FlightNumber)
);

-- Customer Table
CREATE TABLE Customer (
    CustomerID INTEGER PRIMARY KEY,
    FirstName TEXT,
    LastName TEXT,
    Gender TEXT,
    DOB DATE,
    Address TEXT,
    Phone TEXT,
    Zip TEXT
    -- Username TEXT
    -- FOREIGN KEY (Username) REFERENCES Auth(Username)
);

-- Reservation Table
CREATE TABLE Reservation (
    ReservationID TEXT PRIMARY KEY,
    CustomerID INTEGER,
    FlightInstanceID INTEGER,
    Status TEXT CHECK(Status IN ('reserved', 'waitlist', 'flown')),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (FlightInstanceID) REFERENCES FlightInstance(FlightInstanceID)
);

-- Technician Table
CREATE TABLE Technician (
    TechnicianID TEXT PRIMARY KEY,
    Name TEXT
);

-- Repair Table
CREATE TABLE Repair (
    RepairID INTEGER PRIMARY KEY,
    PlaneID TEXT,
    RepairCode TEXT,
    RepairDate DATE,
    TechnicianID TEXT,
    FOREIGN KEY (PlaneID) REFERENCES Plane(PlaneID),
    FOREIGN KEY (TechnicianID) REFERENCES Technician(TechnicianID)
);

-- Pilot Table
CREATE TABLE Pilot (
    PilotID TEXT PRIMARY KEY,
    Name TEXT
);

-- MaintenanceRequest Table
CREATE TABLE MaintenanceRequest (
    RequestID INTEGER PRIMARY KEY,
    PlaneID TEXT,
    RepairCode TEXT,
    RequestDate DATE,
    PilotID TEXT,
    FOREIGN KEY (PlaneID) REFERENCES Plane(PlaneID),
    FOREIGN KEY (PilotID) REFERENCES Pilot(PilotID)
);

-- Authentication
CREATE TABLE Auth (
    Username TEXT PRIMARY KEY NOT NULL,
    Password TEXT NOT NULL,
    Type TEXT NOT NULL  -- Admin, Manager, Customer, Technician, Pilot 
);

-- NewUserCode (only for staff)
CREATE TABLE NewUserCode (
    CodeType TEXT PRIMARY KEY NOT NULL, -- Admin, Manager, Technician, Pilot 
    Code TEXT NOT NULL
);