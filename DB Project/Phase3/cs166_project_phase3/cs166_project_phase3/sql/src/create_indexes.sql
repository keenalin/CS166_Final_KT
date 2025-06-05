DROP INDEX IF EXISTS idx_flightinstance_flightdate;
DROP INDEX IF EXISTS idx_flightinstance_flightnumber;
DROP INDEX IF EXISTS idx_flight_flightnumber;
DROP INDEX IF EXISTS idx_flight_departure_arrival;
DROP INDEX IF EXISTS idx_schedule_flightnumber;
DROP INDEX IF EXISTS idx_reservation_customerid;
DROP INDEX IF EXISTS idx_reservation_flightinstanceid;

DROP INDEX IF EXISTS idx_repair_planeid;
DROP INDEX IF EXISTS idx_repair_repairdate;
DROP INDEX IF EXISTS idx_repair_technicianid;
DROP INDEX IF EXISTS idx_maintreq_pilotid;
DROP INDEX IF EXISTS idx_maintreq_planeid;
DROP INDEX IF EXISTS idx_maintreq_requestdate;


CREATE INDEX idx_flightinstance_flightdate ON FlightInstance(FlightDate);
CREATE INDEX idx_flightinstance_flightnumber ON FlightInstance(FlightNumber);
CREATE INDEX idx_flight_flightnumber ON Flight(FlightNumber);
CREATE INDEX idx_flight_departure_arrival ON Flight(DepartureCity, ArrivalCity);
CREATE INDEX idx_schedule_flightnumber ON Schedule(FlightNumber);
CREATE INDEX idx_reservation_customerid ON Reservation(CustomerID);
CREATE INDEX idx_reservation_flightinstanceid ON Reservation(FlightInstanceID);


CREATE INDEX idx_repair_planeid ON Repair(PlaneID);
CREATE INDEX idx_repair_repairdate ON Repair(RepairDate);
CREATE INDEX idx_repair_technicianid ON Repair(TechnicianID);
CREATE INDEX idx_maintreq_pilotid ON MaintenanceRequest(PilotID);
CREATE INDEX idx_maintreq_planeid ON MaintenanceRequest(PlaneID);
CREATE INDEX idx_maintreq_requestdate ON MaintenanceRequest(RequestDate);