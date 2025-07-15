package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealDatabaseTest {
    private MealDatabase database;
    private Meal meal1;
    private Meal meal2;

    @BeforeEach
    void setUp() {
        database = new MealDatabase();
        meal1 = new Meal("Pizza");
        meal2 = new Meal("Salad");
    }

    @Test
    void testAddMeal() {
        assertTrue(database.addMeal(meal1));
        assertFalse(database.addMeal(meal1));
        assertEquals(1, database.size());
    }

    @Test
    void testRemoveMeal() {
        database.addMeal(meal1);
        assertTrue(database.removeMeal(meal1));
        assertFalse(database.removeMeal(meal1));
        assertEquals(0, database.size());
    }

    @Test
    void testFindMealByName() {
        database.addMeal(meal1);
        database.addMeal(meal2);
        assertTrue(database.findMealByName("Pizza").isPresent());
        assertFalse(database.findMealByName("Burger").isPresent());
    }

//    @Test
//    void testGetAllMeals() {
//        database.addMeal(meal1);
//        database.addMeal(meal2);
//        List<Meal> allMeals = database.getAllMeals();
//        assertEquals(2, allMeals.size());
//        assertTrue(allMeals.contains(meal1));
//        assertTrue(allMeals.contains(meal2));
//    }
}
