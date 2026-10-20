-- 1. Ingredient master list seed data
INSERT INTO ingredients (canonical_name, base_unit) VALUES
    ('egg', 'pcs'),
    ('milk', 'ml'),
    ('butter', 'g'),
    ('flour', 'g'),
    ('sugar', 'g'),
    ('salt', 'g'),
    ('black pepper', 'g'),
    ('cooking oil', 'ml'),
    ('onion', 'pcs'),
    ('garlic', 'pcs'),
    ('tomato', 'pcs'),
    ('cheddar cheese', 'g'),
    ('bread', 'pcs'),
    ('rice', 'g'),
    ('pasta', 'g'),
    ('potato', 'pcs'),
    ('carrot', 'pcs'),
    ('green pepper', 'pcs'),
    ('chicken breast', 'pcs'),
    ('mince', 'g'),
    ('tinned tomato', 'g'),
    ('baked beans', 'g'),
    ('banana', 'pcs'),
    ('oats', 'g'),
    ('honey', 'ml'),
    ('cinnamon', 'g'),
    ('baking powder', 'g'),
    ('mixed herbs', 'g'),
    ('soy sauce', 'ml'),
    ('curry powder', 'g'),
    ('chickpea', 'g'),
    ('frozen peas', 'g'),
    ('tuna', 'g'),
    ('mayonnaise', 'ml'),
    ('cucumber', 'pcs'),
    ('mushroom', 'g'),
    ('cream', 'ml'),
    ('maize meal', 'g'),
    ('cabbage', 'g'),
    ('spinach', 'g'),
    ('yoghurt', 'ml'),
    ('apple', 'pcs'),
    ('tortilla wrap', 'pcs');

-- 2. Recipes
INSERT INTO recipes (name, description, prep_minutes, steps) VALUES
    ('Tomato omelette',
     'A quick, filling breakfast for one.', 15,
     'Whisk the eggs, milk and salt together.
Dice the tomato.
Melt the butter in a pan over medium heat.
Pour in the egg mixture, scatter over the tomato and cook for 3-4 minutes.
Fold in half and serve.'),
    ('Scrambled eggs on toast',
     'Soft, creamy eggs on buttered toast.', 10,
     'Toast the bread and spread with half the butter.
Whisk the eggs, milk and salt.
Melt the remaining butter in a pan over low heat.
Add the eggs and stir gently until just set.
Spoon onto the toast.'),
    ('French toast',
     'Cinnamon-spiced eggy bread.', 15,
     'Whisk the eggs, milk, sugar and cinnamon in a shallow dish.
Soak each slice of bread for 20 seconds per side.
Melt the butter in a pan.
Fry the slices for 2 minutes per side until golden.'),
    ('Pancakes',
     'Fluffy pancakes for a weekend breakfast.', 30,
     'Mix the flour, sugar, baking powder and salt.
Whisk in the eggs and milk to make a smooth batter.
Melt a little butter in a pan over medium heat.
Cook ladlefuls of batter for 2 minutes per side.
Repeat with the remaining batter.'),
    ('Banana oat porridge',
     'Warm oats sweetened with banana and honey.', 10,
     'Bring the oats and milk to a simmer.
Stir for 5 minutes until thick and creamy.
Slice the banana on top and drizzle with honey.'),
    ('Cheese toastie',
     'A golden, melty grilled cheese sandwich.', 10,
     'Butter one side of each slice of bread.
Place the cheese between the unbuttered sides.
Fry in a pan for 3 minutes per side until golden.'),
    ('Spaghetti bolognese',
     'A classic tomato and mince pasta sauce.', 45,
     'Chop the onion and garlic and fry in the oil for 5 minutes.
Add the mince and brown it.
Stir in the tinned tomato, mixed herbs and salt; simmer for 20 minutes.
Meanwhile cook the pasta in salted water.
Serve the sauce over the pasta.'),
    ('Macaroni cheese',
     'Creamy baked pasta with cheddar sauce.', 40,
     'Cook the pasta and drain.
Melt the butter, stir in the flour and cook for 1 minute.
Gradually whisk in the milk until the sauce thickens.
Stir in most of the cheese and the salt, then mix with the pasta.
Top with the remaining cheese and bake at 200 C for 15 minutes.'),
    ('Chicken stir-fry',
     'Fast, colourful chicken and vegetables with rice.', 30,
     'Cook the rice.
Slice the chicken, pepper, carrots and onion.
Stir-fry the chicken in the oil until cooked through.
Add the vegetables and garlic and fry for 4 minutes.
Add the soy sauce and serve with the rice.'),
    ('Chicken curry',
     'A mild tomato-based curry served with rice.', 50,
     'Fry the chopped onion and garlic in the oil.
Add the curry powder and cook for 1 minute.
Add the diced chicken and brown it.
Add the tinned tomato and salt and simmer for 25 minutes.
Serve with cooked rice.'),
    ('Chickpea curry',
     'A hearty vegetarian curry.', 35,
     'Fry the chopped onion and garlic in the oil.
Stir in the curry powder.
Add the drained chickpeas and tinned tomato; simmer for 20 minutes.
Serve with cooked rice.'),
    ('Vegetable fried rice',
     'A great way to use up leftover rice.', 25,
     'Cook the rice and let it cool.
Fry the diced onion and carrot in the oil.
Push the vegetables aside and scramble the eggs in the pan.
Add the rice, peas and soy sauce and stir-fry for 5 minutes.'),
    ('Tuna mayo sandwich',
     'A simple lunchbox favourite.', 5,
     'Drain the tuna and mix with the mayonnaise.
Slice the cucumber.
Spread the tuna on one slice of bread, add cucumber, and close.'),
    ('Baked potato with beans and cheese',
     'Comfort food with a crispy skin.', 60,
     'Prick the potatoes and bake at 200 C for 50 minutes.
Warm the baked beans.
Split the potatoes and add the butter.
Top with the beans and grated cheese.'),
    ('Creamy mushroom pasta',
     'Garlicky mushrooms in a cream sauce.', 25,
     'Cook the pasta.
Fry the sliced mushrooms and garlic in the butter.
Add the cream and salt and simmer for 3 minutes.
Toss with the drained pasta.'),
    ('Pap and chakalaka',
     'A South African favourite: soft pap with spicy relish.', 45,
     'Bring 1 litre of water and the salt to the boil; whisk in the maize meal.
Cover and cook on low for 25 minutes, stirring occasionally.
Fry the onion, pepper and grated carrot in the oil.
Add the curry powder, tinned tomato and baked beans; simmer 15 minutes.
Serve the chakalaka over the pap.'),
    ('Braised cabbage',
     'Soft, buttery-style cabbage side dish.', 25,
     'Shred the cabbage and slice the onion.
Fry the onion in the oil until soft.
Add the cabbage, salt and pepper; cover and cook for 15 minutes.'),
    ('Creamed spinach',
     'A rich side for any meal.', 20,
     'Chop the onion and fry in the butter.
Stir in the flour.
Add the milk gradually to make a sauce.
Add the chopped spinach and salt; cook for 5 minutes.'),
    ('Fruit yoghurt bowl',
     'A no-cook breakfast or snack.', 5,
     'Spoon the yoghurt into a bowl.
Slice the banana and dice the apple.
Top with the fruit and oats and drizzle with honey.'),
    ('Beef mince wraps',
     'Cheesy mince and pepper wraps.', 25,
     'Fry the sliced onion and pepper in the oil.
Add the mince and cook until browned.
Divide between the wraps, top with grated cheese and roll up.');

-- 3. Which ingredients each recipe needs as well as the amount
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity)
SELECT r.recipe_id, i.ingredient_id, v.quantity
FROM (VALUES
    ('Tomato omelette', 'egg', 2),
    ('Tomato omelette', 'milk', 30),
    ('Tomato omelette', 'tomato', 1),
    ('Tomato omelette', 'salt', 1),
    ('Tomato omelette', 'butter', 10),
    ('Scrambled eggs on toast', 'egg', 3),
    ('Scrambled eggs on toast', 'milk', 50),
    ('Scrambled eggs on toast', 'butter', 15),
    ('Scrambled eggs on toast', 'bread', 2),
    ('Scrambled eggs on toast', 'salt', 1),
    ('French toast', 'bread', 4),
    ('French toast', 'egg', 2),
    ('French toast', 'milk', 100),
    ('French toast', 'cinnamon', 2),
    ('French toast', 'butter', 20),
    ('French toast', 'sugar', 10),
    ('Pancakes', 'flour', 200),
    ('Pancakes', 'egg', 2),
    ('Pancakes', 'milk', 300),
    ('Pancakes', 'sugar', 25),
    ('Pancakes', 'butter', 30),
    ('Pancakes', 'salt', 1),
    ('Pancakes', 'baking powder', 5),
    ('Banana oat porridge', 'oats', 80),
    ('Banana oat porridge', 'milk', 250),
    ('Banana oat porridge', 'banana', 1),
    ('Banana oat porridge', 'honey', 15),
    ('Cheese toastie', 'bread', 2),
    ('Cheese toastie', 'cheddar cheese', 60),
    ('Cheese toastie', 'butter', 15),
    ('Spaghetti bolognese', 'pasta', 250),
    ('Spaghetti bolognese', 'mince', 400),
    ('Spaghetti bolognese', 'onion', 1),
    ('Spaghetti bolognese', 'garlic', 2),
    ('Spaghetti bolognese', 'tinned tomato', 400),
    ('Spaghetti bolognese', 'cooking oil', 15),
    ('Spaghetti bolognese', 'mixed herbs', 3),
    ('Spaghetti bolognese', 'salt', 2),
    ('Macaroni cheese', 'pasta', 250),
    ('Macaroni cheese', 'cheddar cheese', 150),
    ('Macaroni cheese', 'milk', 400),
    ('Macaroni cheese', 'butter', 40),
    ('Macaroni cheese', 'flour', 40),
    ('Macaroni cheese', 'salt', 2),
    ('Chicken stir-fry', 'chicken breast', 2),
    ('Chicken stir-fry', 'green pepper', 1),
    ('Chicken stir-fry', 'carrot', 2),
    ('Chicken stir-fry', 'onion', 1),
    ('Chicken stir-fry', 'garlic', 2),
    ('Chicken stir-fry', 'soy sauce', 30),
    ('Chicken stir-fry', 'cooking oil', 15),
    ('Chicken stir-fry', 'rice', 200),
    ('Chicken curry', 'chicken breast', 3),
    ('Chicken curry', 'onion', 1),
    ('Chicken curry', 'garlic', 3),
    ('Chicken curry', 'tinned tomato', 400),
    ('Chicken curry', 'curry powder', 15),
    ('Chicken curry', 'cooking oil', 30),
    ('Chicken curry', 'rice', 250),
    ('Chicken curry', 'salt', 2),
    ('Chickpea curry', 'chickpea', 400),
    ('Chickpea curry', 'onion', 1),
    ('Chickpea curry', 'garlic', 2),
    ('Chickpea curry', 'tinned tomato', 400),
    ('Chickpea curry', 'curry powder', 15),
    ('Chickpea curry', 'cooking oil', 15),
    ('Chickpea curry', 'rice', 200),
    ('Vegetable fried rice', 'rice', 250),
    ('Vegetable fried rice', 'egg', 2),
    ('Vegetable fried rice', 'frozen peas', 100),
    ('Vegetable fried rice', 'carrot', 1),
    ('Vegetable fried rice', 'onion', 1),
    ('Vegetable fried rice', 'soy sauce', 30),
    ('Vegetable fried rice', 'cooking oil', 30),
    ('Tuna mayo sandwich', 'bread', 2),
    ('Tuna mayo sandwich', 'tuna', 170),
    ('Tuna mayo sandwich', 'mayonnaise', 30),
    ('Tuna mayo sandwich', 'cucumber', 1),
    ('Baked potato with beans and cheese', 'potato', 2),
    ('Baked potato with beans and cheese', 'baked beans', 410),
    ('Baked potato with beans and cheese', 'cheddar cheese', 60),
    ('Baked potato with beans and cheese', 'butter', 20),
    ('Creamy mushroom pasta', 'pasta', 250),
    ('Creamy mushroom pasta', 'mushroom', 250),
    ('Creamy mushroom pasta', 'garlic', 2),
    ('Creamy mushroom pasta', 'cream', 250),
    ('Creamy mushroom pasta', 'butter', 20),
    ('Creamy mushroom pasta', 'salt', 2),
    ('Pap and chakalaka', 'maize meal', 250),
    ('Pap and chakalaka', 'onion', 1),
    ('Pap and chakalaka', 'green pepper', 1),
    ('Pap and chakalaka', 'carrot', 2),
    ('Pap and chakalaka', 'tinned tomato', 400),
    ('Pap and chakalaka', 'baked beans', 410),
    ('Pap and chakalaka', 'curry powder', 10),
    ('Pap and chakalaka', 'cooking oil', 30),
    ('Pap and chakalaka', 'salt', 3),
    ('Braised cabbage', 'cabbage', 500),
    ('Braised cabbage', 'onion', 1),
    ('Braised cabbage', 'cooking oil', 15),
    ('Braised cabbage', 'salt', 2),
    ('Braised cabbage', 'black pepper', 1),
    ('Creamed spinach', 'spinach', 300),
    ('Creamed spinach', 'onion', 1),
    ('Creamed spinach', 'butter', 20),
    ('Creamed spinach', 'milk', 150),
    ('Creamed spinach', 'flour', 15),
    ('Creamed spinach', 'salt', 1),
    ('Fruit yoghurt bowl', 'yoghurt', 200),
    ('Fruit yoghurt bowl', 'banana', 1),
    ('Fruit yoghurt bowl', 'apple', 1),
    ('Fruit yoghurt bowl', 'honey', 15),
    ('Fruit yoghurt bowl', 'oats', 30),
    ('Beef mince wraps', 'tortilla wrap', 4),
    ('Beef mince wraps', 'mince', 300),
    ('Beef mince wraps', 'onion', 1),
    ('Beef mince wraps', 'green pepper', 1),
    ('Beef mince wraps', 'cheddar cheese', 80),
    ('Beef mince wraps', 'cooking oil', 15)
) AS v(recipe_name, ingredient_name, quantity)
JOIN recipes r ON r.name = v.recipe_name
JOIN ingredients i ON i.canonical_name = v.ingredient_name;

-- 4. Check: this should return 20 rows
SELECT r.name, COUNT(ri.ingredient_id) AS ingredient_count
FROM recipes r
LEFT JOIN recipe_ingredients ri ON ri.recipe_id = r.recipe_id
GROUP BY r.name
ORDER BY r.name;