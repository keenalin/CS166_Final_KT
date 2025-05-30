-- 4
SELECT DISTINCT s. supplier_name
FROM supplier AS s, part_nyc AS n
WHERE s.supplier_id = n.supplier
AND n.part_number NOT IN (SELECT part_number FROM part_sfo);