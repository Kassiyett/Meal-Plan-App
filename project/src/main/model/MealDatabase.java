package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Represents a database for storing and managing meals
public class MealDatabase implements Writable {
    private final List<Meal> meals;

    // EFFECTS: Constructs an empty MealDatabase
    public MealDatabase() {
        this.meals = new ArrayList<>();
    }

    // EFFECTS: Adds a meal to the database if it's not already present
    // Returns true if the meal was added, false if it was already present
    public boolean addMeal(Meal meal) {
        for (Meal existingMeal : meals) {
            if (existingMeal.equals(meal)) {
                return false;
            }
        }
        EventLog.getInstance().logEvent(new Event("Added " + meal + " to Database"));
        meals.add(meal);
        return true;
    }

    // EFFECTS: Removes a meal from the database
    // Returns true if the meal was found and removed, false otherwise
    public boolean removeMeal(Meal meal) {
        return meals.remove(meal);
    }

    // EFFECTS: Finds and returns a meal by its name
    // Returns an Optional<Meal> that is empty if no meal was found
    public Optional<Meal> findMealByName(String name) {
        return meals.stream()
                .filter(meal -> meal.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    // EFFECTS: Returns a list of all meals in the database
    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals);
    }

    // EFFECTS: Returns the number of meals in the database
    public int size() {
        return meals.size();
    }

    @Override
    public JSONObject toJson() {
        JSONArray jsonArray = new JSONArray();
        for (Meal meal : meals) {
            jsonArray.put(meal.toJson());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("meals", jsonArray);
        return jsonObject;
    }
}
