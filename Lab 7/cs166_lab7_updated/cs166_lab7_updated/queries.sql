-- Exercise 1: Write the following SQL queries in queries.sql both with and without using indexes:
-- 1. Count how many parts in NYC have more than 70 parts on_hand
    -- Without index
SELECT COUNT(part_number)
FROM (SELECT part_number, on_hand
    FROM part_nyc
    WHERE on_hand > 70
) AS parts_with_gt_70_on_hand;

-- 2. Count how many total parts on_hand, in both NYC and SFO, are Red
    -- Without index
SELECT SUM(on_hand)
FROM (
    SELECT on_hand, color_name
    FROM part_nyc
    JOIN color ON part_nyc.color = color.color_id

    UNION ALL

    SELECT on_hand, color_name
    FROM part_sfo
    JOIN color ON part_sfo.color = color.color_id
) AS parts_combined
WHERE color_name != 'Red';

-- 3. List all the suppliers that have more total on_hand parts in NYC than they do in SFO.
    -- Without index
CREATE TEMP TABLE supplier_on_hand_totals AS
SELECT NYC.supplier, NYC.total_on_hand AS nyc_on_hand, SFO.total_on_hand AS sfo_on_hand
FROM
    (SELECT supplier, SUM(on_hand) AS total_on_hand
     FROM part_nyc
     GROUP BY supplier) AS NYC
JOIN
    (SELECT supplier, SUM(on_hand) AS total_on_hand
     FROM part_sfo
     GROUP BY supplier) AS SFO
ON NYC.supplier = SFO.supplier;
SELECT supplier
FROM supplier_on_hand_totals
WHERE NYC_on_hand > SFO_on_hand;

-- 4. List all suppliers that supply parts in NYC that aren’t supplied by anyone in SFO.
    -- Without index

SELECT DISTINCT supplier
FROM part_nyc
WHERE (part_number) NOT IN (
    SELECT part_number
    FROM part_sfo
);

-- 5. Update all of the NYC on_hand values to on_hand - 10.
    -- Without index
UPDATE part_nyc
SET on_hand = -10;

-- 6. Delete all parts from NYC which have less than 30 parts on_hand.
DELETE FROM part_nyc WHERE on_hand < 30;