package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient("Tomato");
    }

    @Test
    void testConstructor() {
        assertEquals("Tomato", testIngredient.getName());
    }

    @Test
    void testSetName() {
        testIngredient.setName("Potato");
        assertEquals("Potato", testIngredient.getName());
    }

    @Test
    void testToString() {
        assertEquals("Tomato", testIngredient.toString());
    }
}
