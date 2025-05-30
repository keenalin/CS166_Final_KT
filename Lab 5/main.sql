-- Query 1
SELECT pid
FROM catalog
WHERE cost < 10;

-- Query 2
SELECT pname
FROM catalog
    JOIN parts on catalog.pid = parts.pid
    WHERE cost < 10;

-- Query 3
SELECT address
FROM catalog
    JOIN parts on catalog.pid = parts.pid
    JOIN suppliers on catalog.sid = suppliers.sid
    WHERE pname = 'Fire Hydrant Cap';

-- Query 4
SELECT sname
FROM catalog
    JOIN parts on catalog.pid = parts.pid
    JOIN suppliers on catalog.sid = suppliers.sid
    WHERE color = 'Green';

-- Query 5
SELECT sname, pname
FROM catalog
    JOIN parts on catalog.pid = parts.pid
    JOIN suppliers on catalog.sid = suppliers.sid
    GROUP BY suppliers;
