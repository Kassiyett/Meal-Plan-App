package persistence;

import exceptions.InvalidInputException;
import model.Ingredient;
import model.Meal;
import model.MealDatabase;
import model.ShoppingList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        assertThrows(IOException.class, reader::readMealDatabase);
        assertThrows(IOException.class, reader::readShoppingList);
    }

    @Test
    public void testReaderEmptyMealDatabase() {
        JsonReader reader = new JsonReader("./data/testEmpty.json");
        try {
            MealDatabase mdb = reader.readMealDatabase();
            assertEquals(0, mdb.size());
        } catch (IOException | InvalidInputException e) {
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testReaderValidMealDatabase() {
        JsonReader reader = new JsonReader("./data/testFull.json");
        try {
            MealDatabase mdb = reader.readMealDatabase();
            List<Meal> meals = mdb.getAllMeals();
            assertEquals(2, meals.size());

            Meal meal1 = meals.get(0);
            assertEquals("Chicken Salad", meal1.getName());
            assertEquals(3, meal1.getIngredients().size());
            Ingredient ingredient1 = new Ingredient("Chicken");
            assertFalse(meal1.getIngredients().contains(ingredient1));
            assertFalse(meal1.getIngredients().contains(new Ingredient("Curry Sauce")));

            Meal meal2 = meals.get(1);
            assertEquals("Beef Tacos", meal2.getName());
            assertEquals(3, meal2.getIngredients().size());
            assertFalse(meal2.getIngredients().contains(new Ingredient("Beef")));
            assertFalse(meal2.getIngredients().contains(new Ingredient("Taco Shell")));
            assertFalse(meal2.getIngredients().contains(new Ingredient("Salsa")));

        } catch (IOException | InvalidInputException e) {
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testReaderEmptyShoppingList() {
        JsonReader reader = new JsonReader("./data/testEmpty.json");
        JsonReader readerShopping = new JsonReader("./data/emptyShoppingListTest.json");
        try {
            ShoppingList shoppingList = readerShopping.readShoppingList();
            assertTrue(shoppingList.isEmpty());
        } catch (IOException e) {
            fail("Shouldn't have thrown an exception");
        }
    }

    @Test
    public void testReaderValidShoppingList() {
        JsonReader reader = new JsonReader("./data/testFull.json");
        JsonReader readerShopping = new JsonReader("./data/shoppingListFullTest.json");
        try {
            ShoppingList shoppingList = readerShopping.readShoppingList();
            assertEquals(1, shoppingList.getItemQuantity("Milk"));
            assertEquals(2, shoppingList.getItemQuantity("Bread"));
            assertEquals(12, shoppingList.getItemQuantity("Eggs"));
        } catch (IOException e) {
            fail("Shouldn't have thrown an exception");
        }
    }
}
