package persistence;

import model.Ingredient;
import model.Meal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {

    // EFFECTS: Checks given meal(s) against expected data
    protected void checkMealTest(String name, List<Ingredient> expectedIngredients, Meal... meals) {
        for (Meal meal : meals) {
            assertEquals(name, meal.getName());
            List<Ingredient> ingredients = meal.getIngredients();

            for (Ingredient expectedIngredient : expectedIngredients) {
                boolean found = ingredients.stream().anyMatch(ingredient ->
                        ingredient.getName().equals(expectedIngredient.getName()));
                assertTrue(found, "Expected ingredient not found: " + expectedIngredient.getName());
            }

            assertEquals(expectedIngredients.size(), ingredients.size(), "Unexpected ingredient found in meal.");
        }
    }
}
