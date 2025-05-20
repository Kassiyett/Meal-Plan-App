package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;

// Represents a shopping list with items to purchase
public class ShoppingList implements Writable {
    private final Map<String, Integer> items; // Maps item names to their quantities

    // EFFECTS: Constructs an empty ShoppingList
    public ShoppingList() {
        this.items = new HashMap<>();
    }

    // EFFECTS: Adds an item to the shopping list with its quantity If the item already exists, its quantity is updated.
    public void addItem(String itemName, int quantity) {
        if (items.containsKey(itemName)) {
            int existingQuantity = items.get(itemName);
            int updatedQuantity = existingQuantity + quantity;
            items.put(itemName, updatedQuantity);
        } else {
            items.put(itemName, quantity);
        }
        EventLog.getInstance().logEvent(new Event("Added Ingredient " + itemName + " to Shopping List"));
    }


    // EFFECTS: Removes an item from the shopping list
    // Returns true if the item was found and removed, false otherwise
    public boolean removeItem(String itemName) {
        if (items.containsKey(itemName)) {
            items.remove(itemName);
            return true;
        }
        return false;
    }

    // EFFECTS: Returns the quantity of a specific item in the shopping list
    public int getItemQuantity(String itemName) {
        return items.get(itemName);
    }

    // EFFECTS: Returns a map of all items in the shopping list
    public Map<String, Integer> getAllItems() {
        EventLog.getInstance().logEvent(new Event("Vied Shopping List "));
        return new HashMap<>(items);
    }

    // EFFECTS: Returns the number of items in the shopping list
    public int size() {
        return items.size();
    }

    // EFFECTS: Checks if the shopping list is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject;
    }
}
