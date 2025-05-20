package persistence;

import exceptions.InvalidInputException;
import model.Ingredient;
import model.Meal;
import model.MealDatabase;
import model.ShoppingList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    // EFFECTS: construct
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads and returns a MealDatabase from file
    public MealDatabase readMealDatabase() throws IOException, InvalidInputException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMealDatabase(jsonObject);

        //return parseMealDatabase(jsonObject.getJSONObject());
    }

    // EFFECTS: reads and returns a ShoppingList from file
    public ShoppingList readShoppingList() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
//        return parseShoppingList(jsonObject.getJSONObject("shoppingList"));
        return parseShoppingList(jsonObject);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MealDatabase from JSONObject and returns it
    private MealDatabase parseMealDatabase(JSONObject jsonObject) throws InvalidInputException {
        MealDatabase mealDatabase = new MealDatabase();
        JSONArray jsonArray = jsonObject.getJSONArray("meals");
        for (Object json : jsonArray) {
            JSONObject nextMeal = (JSONObject) json;
            mealDatabase.addMeal(returnJsonMeal(nextMeal));
        }
        return mealDatabase;
    }

    // EFFECTS: parses ShoppingList from JSONObject and returns it
    private ShoppingList parseShoppingList(JSONObject jsonObject) {
        ShoppingList shoppingList = new ShoppingList();
        jsonObject.keys().forEachRemaining(key -> {
            int quantity = jsonObject.getInt(key);
            shoppingList.addItem(key, quantity);
        });
        return shoppingList;
    }

    // EFFECTS: parses Meal from JSONObject and returns it
    private Meal returnJsonMeal(JSONObject jsonObject) throws InvalidInputException {
        String name = jsonObject.getString("name");
        JSONArray ingredientsJsonArray = jsonObject.getJSONArray("ingredients");

        Meal meal = new Meal(name);

        // Iterate over each entry in the JSONArray to create Ingredient objects
//        for (int i = 0; i < ingredientsJsonArray.length(); i++) {
//            String ingredientName = ingredientsJsonArray.getString(i);
//            Ingredient ingredient = new Ingredient(ingredientName);
//
//            meal.addIngredient(ingredient);
//        }


        for (int i = 0; i < ingredientsJsonArray.length(); i++) {
            JSONObject ingredientJson = ingredientsJsonArray.getJSONObject(i);
            String ingredientName = ingredientJson.getString("name");
            Ingredient ingredient = new Ingredient(ingredientName);
            meal.addIngredient(ingredient);
        }

        return meal;
    }
}
