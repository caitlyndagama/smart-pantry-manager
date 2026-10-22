# Smart Pantry Manager

A Java Android application that tracks the ingredients a user already has at home and suggests
recipes they can cook **using only those ingredients**, helping to reduce household food waste.

This assignment forms part of the practical assessment for Mobile App Development 700. Building on the module content covering Android Studio, the Activity lifecycle, layouts, intents, adapters, and persistent data storage, you are required to design, build, document, and demonstrate a fully functional Android application written in Java.

Author: Caitlyn Tahlia Da Gama, 402312832

---

## Features

Pantry management - the user can add, edit, and delete pantry items (e.g. ingredient name, quantity, unit, and optionally an expiry date).
• A pantry list screen showing all current ingredients, using a RecyclerView/ListView bound to your database.
• A recipe collection - a set of at least 15-20 recipes stored in your database (pre-loaded/seeded on first run), each recipe consisting of a name, a list of required ingredients, and simple preparation steps.
• A “Suggested Recipes” screen that runs the strict-matching logic (Section 2.3) against the user's current pantry and lists only the recipes the user can make right now.
Mobile App Development 700 - Practical Assignment
Page 3 of 8
• A recipe detail screen showing the full ingredient list and method for a selected recipe.
• A settings or profile screen (e.g. toggle for expiring-soon alerts, or units preference), satisfying the minimum-screens requirement in Section 3.1.
• Basic feedback when zero recipes match the current pantry (e.g. “No recipes match your pantry yet - add more ingredients”), rather than a blank or broken screen.

## Database choice

**Chosen option:** PostgreSQL, hosted on Supabase, accessed through the REST API that Supabase
generates for the tables (PostgREST).

**Why:** The data involved in this project is naturally relational, for example, the recipes and ingredients hold a many-to-many relationship. PostgreSQL is a powerful open-source RDBMS, it is intuitive and helps visualise the structure of the data very clearly. 

## Strict-matching approach

Ingredient names are normalised before saving (trimmed, lower-cased, last word singularised,
common synonyms mapped, e.g. "Canned tomatoes" -> "tinned tomato"), and every quantity is
converted to a base unit (g, ml or pcs). Pantry items and recipes therefore share ingredient IDs
and comparable quantities. A recipe is suggested only if no required ingredient is missing or
short. See `logic/RecipeMatcher.java` and its unit tests.

## Tech stack

- Language: Java | IDE: Android Studio
- Minimum SDK: API 24 (Android 7.0) | Target SDK: [e.g. API 35]
- Retrofit 2 + Gson for HTTP and JSON
- PostgreSQL on Supabase (free tier)
- **No Google Maps, mapping SDKs or location services are used (out of scope per the brief)**

## Setup and run instructions

1. Clone the repository:
  https://github.com/caitlyndagama/smart-pantry-manager.git
2. Open Android Studio, then **File > Open** and select the cloned folder. Let Gradle sync.
3. The app is already connected to the project's Supabase database
   (`app/src/main/java/.../data/SupabaseConfig.java`). To use your own database instead:
   create a Supabase project, run `database/schema.sql` and then `database/seed.sql` in its
   SQL Editor, and paste your project URL and publishable key into `SupabaseConfig.java`.
4. Select an emulator or device (API 24+) with internet access and click **Run**.
5. Add pantry items, then open **Recipes** to see suggestions.

Note: free Supabase projects pause after a week without activity. If the app shows a
connection error, the project can be restored from the Supabase dashboard.

## Project structure

```
app/src/main/java/com/smartpantrymanager/
├── activities/   PantryList, AddEditIngredient, SuggestRecipes, RecipeDetail, Settings, ScreenHelper
├── adapters/     PantryAdapter, RecipeAdapter
├── data/         SupabaseConfig, SupabaseApi, ApiClient, PantryRepository, SettingsManager
├── logic/        IngredientNormaliser, UnitConverter, RecipeMatcher, ExpiryUtils
└── model/        Ingredient, PantryItem, Recipe, RecipeIngredient
app/src/test/...  JUnit tests for the matching logic
database/         schema.sql, seed.sql
```

## Demonstration video
Included in the submission ZIP (5–7 minutes).
