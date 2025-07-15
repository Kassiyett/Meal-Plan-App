package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    private Meal meal;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        meal = new Meal("Spaghetti");
        ingredient1 = new Ingredient("Tomato Sauce");
        ingredient2 = new Ingredient("Spaghetti Noodles");

        meal.addIngredient(ingredient1);
        meal.addIngredient(ingredient2);
    }

    @Test
    void testConstructor() {
        assertEquals("Spaghetti", meal.getName());
        assertEquals(2, meal.getIngredients().size());
    }

    @Test
    void testAddIngredient() {
        Ingredient ingredient3 = new Ingredient("Meatballs");
        meal.addIngredient(ingredient3);

        assertEquals(3, meal.getIngredients().size());
        assertTrue(meal.getIngredients().contains(ingredient3));
    }

    @Test
    void testEquals() {
        Meal anotherMeal = new Meal("Spaghetti");
        anotherMeal.addIngredient(ingredient1);
        anotherMeal.addIngredient(ingredient2);

        assertEquals(meal, anotherMeal);
    }

    @Test
    void testHashCode() {
        Meal anotherMeal = new Meal("Spaghetti");
        anotherMeal.addIngredient(ingredient1);
        anotherMeal.addIngredient(ingredient2);

        assertEquals(meal.hashCode(), anotherMeal.hashCode());
    }
}
