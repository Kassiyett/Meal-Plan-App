package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListTest {
    private ShoppingList shoppingList;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingList();
    }

    @Test
    void testAddItem() {
        shoppingList.addItem("Apples", 5);
        assertEquals(5, shoppingList.getItemQuantity("Apples"));
        assertEquals(1, shoppingList.size());
    }

    @Test
    void testRemoveItem() {
        shoppingList.addItem("Milk", 1);
        assertTrue(shoppingList.removeItem("Milk"));
        assertFalse(shoppingList.removeItem("Bread")); // Trying to remove an item that does not exist
        assertEquals(0, shoppingList.size());
    }

    @Test
    void testGetItemQuantity() {
        shoppingList.addItem("Eggs", 12);
        assertEquals(12, shoppingList.getItemQuantity("Eggs"));
    }

    @Test
    void testGetAllItems() {
        shoppingList.addItem("Bananas", 6);
        Map<String, Integer> allItems = shoppingList.getAllItems();
        assertTrue(allItems.containsKey("Bananas"));
        assertEquals(6, allItems.get("Bananas"));
        assertEquals(1, allItems.size());
    }

    @Test
    void testIsEmptyAndSize() {
        assertTrue(shoppingList.isEmpty());
        assertEquals(0, shoppingList.size());

        shoppingList.addItem("Oranges", 10);
        assertFalse(shoppingList.isEmpty());
        assertEquals(1, shoppingList.size());
    }

    @Test
    void testToJson() {
        shoppingList.addItem("Chocolate", 2);
        JSONObject json = shoppingList.toJson();
        assertEquals(2, json.getInt("Chocolate"));
//        assertEquals(1, json.length());
    }
}
