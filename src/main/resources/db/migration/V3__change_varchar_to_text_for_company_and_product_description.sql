ALTER TABLE company
    ALTER COLUMN description TYPE TEXT USING (description::TEXT);

ALTER TABLE product
    ALTER COLUMN description TYPE TEXT USING (description::TEXT);