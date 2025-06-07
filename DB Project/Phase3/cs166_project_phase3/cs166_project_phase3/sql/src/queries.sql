-- Feature 1 | 1
SELECT * FROM FlightInstance
WHERE FlightNumber = 'F100' AND flightdate >= '2025-05-05' AND flightdate < DATE '2025-05-05' + INTERVAL '7 days'
ORDER BY flightdate;

-- Feature 2 | 2
SELECT SeatsTotal, SeatsSold
FROM FlightInstance
WHERE FlightNumber = 'F100' AND flightdate = '2025-05-05'
ORDER BY flightdate;

-- Feature 3 | 3
SELECT DepartedOnTime, ArrivedOnTime
FROM FlightInstance
WHERE FlightNumber = 'F100' AND flightdate = '2025-05-05'
ORDER BY flightdate;

-- Feature 4 | 4
SELECT FlightInstanceID, FlightNumber
FROM FlightInstance
WHERE flightdate = '2025-05-05'
ORDER BY flightdate;

-- Feature 5 | 5
SELECT R.ReservationID, C.CustomerID, C.FirstName, C.LastName, R.Status
FROM Reservation R
JOIN Customer C ON R.CustomerID = C.CustomerID
JOIN FlightInstance FI ON R.FlightInstanceID = FI.FlightInstanceID
WHERE FI.FlightNumber = 'F100' AND FI.FlightDate = '2025-05-05'
ORDER BY R.Status;

-- Feature 6 | 6
SELECT C.FirstName, C.LastName, C.Gender, C.DOB, C.Address, C.Phone, C.Zip
FROM Customer C
JOIN Reservation R ON R.CustomerID = C.CustomerID
WHERE R.ReservationID = 'R0001'
ORDER BY C.CustomerID;

-- Feature 7 | 7
SELECT *
FROM Plane
WHERE PlaneID = 'PL001';

-- Feature 8 | 8
SELECT T.TechnicianID, T.Name, R.RepairID, R.PlaneID, R.RepairCode, R.RepairDate
FROM Technician T
JOIN Repair R ON R.TechnicianID = T.TechnicianID
WHERE T.TechnicianID = 'T001'
ORDER BY R.RepairDate;

-- Feature 9 | 9
SELECT R.RepairDate, R.RepairCode
FROM Repair R
WHERE R.PlaneID = 'PL001' AND R.RepairDate >= '2025-01-01' AND R.RepairDate <= '2025-12-31'
ORDER BY R.RepairDate;

-- Feature 10 | 10
SELECT COUNT(FlightNumber) AS DaysFlown, 
       SUM(SeatsSold) AS TotalSeatsSold, 
       SUM(SeatsTotal - SeatsSold) AS TotalSeatsUnsold
FROM FlightInstance
WHERE FlightNumber = 'F100' AND FlightDate >= '2025-05-01' AND FlightDate <= '2025-05-31'
GROUP BY FlightNumber;

-- Feature 11 | 11
SELECT F.FlightNumber, S.DepartureTime, S.ArrivalTime,
       CAST(AVG(FI.NumOfStops) AS INT) AS NumOfStops,
       ROUND(100.0 * SUM(CASE WHEN FI.DepartedOnTime IS TRUE AND FI.ArrivedOnTime IS TRUE THEN 1 ELSE 0 END) / COUNT(*), 2) AS OnTimePercentage
FROM Flight F
JOIN FlightInstance FI ON F.FlightNumber = FI.FlightNumber
JOIN Schedule S ON F.FlightNumber = S.FlightNumber
WHERE F.DepartureCity = 'DEPARTURE_CITY'
  AND F.ArrivalCity = 'ARRIVAL_CITY'
  AND FI.FlightDate = '2025-05-05'
  AND S.DayOfWeek = TRIM(TO_CHAR(DATE '2025-05-05', 'Day'))
GROUP BY F.FlightNumber, S.DepartureTime, S.ArrivalTime;

-- Feature 12 | 12
SELECT FI.FlightInstanceID, FI.FlightDate, FI.TicketCost
FROM FlightInstance FI
WHERE FI.FlightNumber = 'F100'
ORDER BY FI.FlightDate;

-- Feature 13 | 13
SELECT DISTINCT P.Make, P.Model
FROM Flight F
JOIN Plane P ON F.PlaneID = P.PlaneID
WHERE F.FlightNumber = 'F100';

-- Feature 14 | 14 15
SELECT SeatsSold, SeatsTotal
FROM FlightInstance
WHERE FlightInstanceID = '1';

INSERT INTO Reservation (ReservationID, CustomerID, FlightInstanceID, Status)
VALUES ('R9999', '200', '1', 'reserved');

-- Feature 15 | 16
SELECT RepairDate, RepairCode
FROM Repair
WHERE PlaneID = 'PL001'
  AND RepairDate BETWEEN '2025-01-01' AND '2025-12-31'
ORDER BY RepairDate;

-- Feature 16 | 17
SELECT MR.RequestID, MR.PlaneID, MR.RepairCode, MR.RequestDate
FROM MaintenanceRequest MR
WHERE MR.PilotID = 'P001'
ORDER BY MR.RequestDate DESC;

-- Feature 17 | 18
INSERT INTO Repair (RepairID, PlaneID, RepairCode, RepairDate, TechnicianID)
VALUES ('20', 'PL001', 'RC002', '2025-06-01', 'T002');

-- Feature 18 | 19
INSERT INTO MaintenanceRequest (RequestID, PlaneID, RepairCode, RequestDate, PilotID)
VALUES ('25', 'PL001', 'RC003', '2025-06-02', 'P001');

-- Feature 19 | 20 21
DELETE FROM NewUserCode WHERE CodeType = 'Technician';
INSERT INTO NewUserCode (CodeType, Code)
VALUES ('Technician', '12345');

