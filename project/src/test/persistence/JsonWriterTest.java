package persistence;

import model.Meal;
import model.MealDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private Meal mealOne, mealTwo, mealThree;
    private MealDatabase mealDatabase;

    @BeforeEach
    public void setup() throws Exception {
        mealOne = new Meal("Chicken Salad");
        mealTwo = new Meal("Beef Tacos");
        mealThree = new Meal("Pizza");

        mealDatabase = new MealDatabase();
        mealDatabase.addMeal(mealOne);
        mealDatabase.addMeal(mealTwo);
        mealDatabase.addMeal(mealThree);
    }

    @Test
    public void testWriterInvalidFile() {
        try {
            MealDatabase emptyDatabase = new MealDatabase();
            JsonWriter writer = new JsonWriter("./data/bad:\0invalidFileName2.json");
            writer.open();
            fail("IOException expected but not thrown");
        } catch(IOException e) {
            //excepted
        }
    }

    @Test
    public void testWriterEmptyDatabase() {
        try {
            MealDatabase emptyDatabase = new MealDatabase();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDatabase.json");

            writer.open();
            writer.write(emptyDatabase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmpty.json");
            MealDatabase result = reader.readMealDatabase();
            assertTrue(result.getAllMeals().isEmpty());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterFullDatabase() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterFullDatabase.json");
            writer.open();
            writer.write(mealDatabase);
            writer.close();

            JsonReader reader = new JsonReader("./data/testFull.json");
            MealDatabase result = reader.readMealDatabase();
            assertEquals(2, result.getAllMeals().size());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

//    @Test
//    public void testFileContent() throws IOException {
//        JsonWriter writer = new JsonWriter();
//        writer.open();
//        writer.write(mealDatabase);
//        writer.close();
//
//        String content = Files.readString(Paths.get());
//        assertNotNull(content);
//    }
}
