-- Lab 6 Exercise 1

-- Find the total number of parts supplied by each supplier.
SELECT S.sid, S.sname, COUNT(C.pid) AS num_parts
FROM suppliers S
JOIN catalog C ON S.sid = C.sid
GROUP BY S.sid, S.sname;


-- Find the total number of parts supplied by each supplier who supplies at least 3 parts.
SELECT S.sid, S.sname, COUNT(C.pid) AS num_parts
FROM suppliers S
JOIN catalog C ON S.sid = C.sid
GROUP BY S.sid, S.sname
HAVING COUNT(C.pid) > 2;


-- For every supplier that supplies only green parts, print the name of the supplier and the total number of parts that he supplies.
SELECT S.sname, COUNT(C.pid) AS num_green_parts
FROM suppliers S
JOIN catalog C on S.sid = C.sid
JOIN parts P on P.pid = C.pid
WHERE color = 'Green'
GROUP BY S.sname;


-- For every supplier that supplies green part and red part, print the name of the supplier and the price of the most expensive part that he supplies.
SELECT S.sname, MAX(C.pid) AS most_expensive_part
FROM suppliers S
JOIN catalog C on S.sid = C.sid
JOIN parts P on P.pid = C.pid
WHERE color IN ('Green', 'Red')
GROUP BY S.sname;