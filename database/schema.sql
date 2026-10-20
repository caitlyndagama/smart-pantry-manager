DROP TABLE IF EXISTS recipe_ingredients;
DROP TABLE IF EXISTS pantry_items;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS ingredients;
--1. Ingredient list
-- ---------------------------------------------------------------------
CREATE TABLE ingredients (
ingredient_id SERIAL PRIMARY KEY,
canonical_name VARCHAR(60) NOT NULL UNIQUE,
base_unit VARCHAR(5) NOT NULL
CHECK (base_unit IN ('g', 'ml', 'pcs'))
);

 --2. Pantry
-- ---------------------------------------------------------------------
CREATE TABLE pantry_items (
pantry_item_id SERIAL PRIMARY KEY,
ingredient_id INTEGER NOT NULL UNIQUE
REFERENCES ingredients (ingredient_id) ON DELETE RESTRICT,
display_name VARCHAR(60) NOT NULL,
quantity NUMERIC(10,2) NOT NULL CHECK (quantity > 0),
expiry_date DATE NULL,
created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

--3. Recipes (seeded with 20 rows)
-- ---------------------------------------------------------------------
CREATE TABLE recipes (
recipe_id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL UNIQUE,
description VARCHAR(255),
steps TEXT NOT NULL,
prep_minutes INTEGER CHECK (prep_minutes > 0)
);

--4. Recipe (many-to-many, with required quantity)
-- ---------------------------------------------------------------------
CREATE TABLE recipe_ingredients (
recipe_id INTEGER NOT NULL
REFERENCES recipes (recipe_id) ON DELETE CASCADE,
ingredient_id INTEGER NOT NULL
REFERENCES ingredients (ingredient_id) ON DELETE RESTRICT,
quantity NUMERIC(10,2) NOT NULL CHECK (quantity > 0),
PRIMARY KEY (recipe_id, ingredient_id)
);
--------------------------------------------------------------------------

GRANT USAGE ON SCHEMA public TO anon;
GRANT SELECT, INSERT, UPDATE, DELETE ON pantry_items TO anon;
GRANT SELECT, INSERT ON ingredients TO anon;
GRANT SELECT ON recipes TO anon;
GRANT SELECT ON recipe_ingredients TO anon;
GRANT USAGE, SELECT ON SEQUENCE pantry_items_pantry_item_id_seq TO anon;
GRANT USAGE, SELECT ON SEQUENCE ingredients_ingredient_id_seq TO anon;
ALTER TABLE ingredients ENABLE ROW LEVEL SECURITY;
ALTER TABLE pantry_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE recipes ENABLE ROW LEVEL SECURITY;
ALTER TABLE recipe_ingredients ENABLE ROW LEVEL SECURITY;
CREATE POLICY "app reads ingredients" ON ingredients FOR SELECT TO anon USING (true);
CREATE POLICY "app adds ingredients" ON ingredients FOR INSERT TO anon WITH CHECK (true);
CREATE POLICY "app reads pantry" ON pantry_items FOR SELECT TO anon USING (true);
CREATE POLICY "app adds pantry" ON pantry_items FOR INSERT TO anon WITH CHECK (true);
CREATE POLICY "app updates pantry" ON pantry_items FOR UPDATE TO anon USING (true) WITH CHECK (true);
CREATE POLICY "app deletes pantry" ON pantry_items FOR DELETE TO anon USING (true);
CREATE POLICY "app reads recipes" ON recipes FOR SELECT TO anon USING (true);
CREATE POLICY "app reads recipe links" ON recipe_ingredients FOR SELECT TO anon USING (true);