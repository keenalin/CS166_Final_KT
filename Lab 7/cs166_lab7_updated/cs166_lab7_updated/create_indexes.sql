-- Indexes are named based on which problem [1,6] they are for
DROP INDEX IF EXISTS idx1;
DROP INDEX IF EXISTS idx2_nyc;
DROP INDEX IF EXISTS idx2_sfo;
DROP INDEX IF EXISTS idx2_color;
DROP INDEX IF EXISTS idx3_nyc;
DROP INDEX IF EXISTS idx3_sfo;
DROP INDEX IF EXISTS idx4;
DROP INDEX IF EXISTS idx5;
DROP INDEX IF EXISTS idx6;


CREATE INDEX idx1
ON part_nyc(on_hand);

CREATE INDEX idx2_nyc
ON part_nyc(color);
CREATE INDEX idx2_sfo
ON part_sfo(color);
CREATE INDEX idx2_color
ON color(color_name);

CREATE INDEX idx3_nyc
ON part_nyc(supplier, on_hand);
CREATE INDEX idx3_sfo
ON part_sfo(supplier, on_hand);

CREATE INDEX idx4
ON part_nyc(part_number);

CREATE INDEX idx5
ON part_sfo(on_hand);

CREATE INDEX idx6
ON part_nyc(on_hand);