-- NEW INDEXES
DROP INDEX IF EXISTS idx_flightinstance_flightnum_date; -- 1, 2, 3, 4, 5, 10, 11
CREATE INDEX idx_flightinstance_flightnum_date ON FlightInstance(FlightNumber, FlightDate);

DROP INDEX IF EXISTS idx_reservation_customer -- 6, 14
CREATE INDEX idx_reservation_customer ON Reservation(ReservationID, CustomerID);

DROP INDEX IF EXISTS idx_customer_customerid -- 5, 6
CREATE INDEX idx_customer_customerid ON Customer(CustomerID);

-- DROP INDEX IF EXISTS idx_plane_make;
-- DROP INDEX IF EXISTS idx_plane_model;
-- DROP INDEX IF EXISTS idx_plane_lastrepair;

-- DROP INDEX IF EXISTS idx_flight_departure;
-- DROP INDEX IF EXISTS idx_flight_arrival;
-- DROP INDEX IF EXISTS idx_flight_planeid;

-- DROP INDEX IF EXISTS idx_schedule_flightnum;
-- DROP INDEX IF EXISTS idx_schedule_dayofweek;

-- DROP INDEX IF EXISTS idx_flightinstance_flightnum;
-- DROP INDEX IF EXISTS idx_flightinstance_date;
-- DROP INDEX IF EXISTS idx_flightinstance_ontime;

-- DROP INDEX IF EXISTS idx_customer_name;
-- DROP INDEX IF EXISTS idx_customer_zip;
-- DROP INDEX IF EXISTS idx_customer_phone;

-- DROP INDEX IF EXISTS idx_reservation_customer;
-- DROP INDEX IF EXISTS idx_reservation_flightinstance;
-- DROP INDEX IF EXISTS idx_reservation_status;

-- DROP INDEX IF EXISTS idx_technician_name;

-- DROP INDEX IF EXISTS idx_repair_plane;
-- DROP INDEX IF EXISTS idx_repair_tech;
-- DROP INDEX IF EXISTS idx_repair_code_date;

-- DROP INDEX IF EXISTS idx_pilot_name;

-- DROP INDEX IF EXISTS idx_request_plane;
-- DROP INDEX IF EXISTS idx_request_pilot;
-- DROP INDEX IF EXISTS idx_request_code_date;

-- CREATE INDEX idx_newusercode_codetype ON NewUserCode(CodeType);

-- CREATE INDEXES
-- CREATE INDEX idx_plane_make ON Plane(Make);
-- CREATE INDEX idx_plane_model ON Plane(Model);
-- CREATE INDEX idx_plane_lastrepair ON Plane(LastRepairDate);

-- CREATE INDEX idx_flight_departure ON Flight(DepartureCity);
-- CREATE INDEX idx_flight_arrival ON Flight(ArrivalCity);
-- CREATE INDEX idx_flight_planeid ON Flight(PlaneID);

-- CREATE INDEX idx_schedule_flightnum ON Schedule(FlightNumber);
-- CREATE INDEX idx_schedule_dayofweek ON Schedule(DayOfWeek);

-- CREATE INDEX idx_flightinstance_flightnum ON FlightInstance(FlightNumber);
-- CREATE INDEX idx_flightinstance_date ON FlightInstance(FlightDate);
-- CREATE INDEX idx_flightinstance_ontime ON FlightInstance(DepartedOnTime, ArrivedOnTime);

-- CREATE INDEX idx_customer_name ON Customer(LastName, FirstName);
-- CREATE INDEX idx_customer_zip ON Customer(Zip);
-- CREATE INDEX idx_customer_phone ON Customer(Phone);

-- CREATE INDEX idx_reservation_customer ON Reservation(CustomerID);
-- CREATE INDEX idx_reservation_flightinstance ON Reservation(FlightInstanceID);
-- CREATE INDEX idx_reservation_status ON Reservation(Status);

-- CREATE INDEX idx_technician_name ON Technician(Name);

-- CREATE INDEX idx_repair_plane ON Repair(PlaneID);
-- CREATE INDEX idx_repair_tech ON Repair(TechnicianID);
-- CREATE INDEX idx_repair_code_date ON Repair(RepairCode, RepairDate);

-- CREATE INDEX idx_pilot_name ON Pilot(Name);

-- CREATE INDEX idx_request_plane ON MaintenanceRequest(PlaneID);
-- CREATE INDEX idx_request_pilot ON MaintenanceRequest(PilotID);
-- CREATE INDEX idx_request_code_date ON MaintenanceRequest(RepairCode, RequestDate);