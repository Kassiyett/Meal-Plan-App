package model;

import exceptions.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Represents a single meal with a name and list of ingredients
public class Meal implements Writable {
    private String name;
    private List<Ingredient> ingredients;

    // EFFECTS: creates basic meal entry
    public Meal(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an ingredient to a meal entry
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        EventLog.getInstance().logEvent(new Event("Added ingredient " + ingredient + " to meal " + this));
    }

    // EFFECTS: overrides equals method to compare different meal objects with the exact same information
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meal meal = (Meal) o;
        return name.equals(meal.name) && Objects.equals(ingredients, meal.ingredients);
    }

    // EFFECTS: a standard override of hashCode method to override equals method for a meal
    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients);
    }

    public String getName() {
        return this.name;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    // EFFECTS: converts a meal into a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("ingredients", ingredientsToJson());
        return json;
    }

    // EFFECTS: returns ingredients in this meal as a JSON array
    private JSONArray ingredientsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Ingredient ingredient : ingredients) {
            JSONObject json = new JSONObject();
            json.put("name", ingredient.getName());
//            json.put("quantity", ingredient.getQuantity());
            jsonArray.put(json);
        }
        return jsonArray;
    }

    @Override
    public String toString() {
        return name;
        // "Meal{" + "name='" + name + '\'' + '}'
    }
}
