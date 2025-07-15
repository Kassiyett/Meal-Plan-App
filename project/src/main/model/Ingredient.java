package model;

// Represents an ingredient with a name and quantity
public class Ingredient {
    private String name;
    // Example: 2 tomatoes

    // EFFECTS: Constructs an ingredient with a name and quantity
    public Ingredient(String name) {
        this.name = name;

    }

    // EFFECTS: Returns the name of the ingredient
    public String getName() {
        return name;
    }

    // EFFECTS: Sets the name of the ingredient
    public void setName(String name) {
        this.name = name;
    }

    // EFFECTS: Returns the quantity of the ingredient
//    public String getQuantity() {
//        return quantity;
//    }

    // EFFECTS: Sets the quantity of the ingredient
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }

    // EFFECTS: Represents the ingredient as a String

    public String toString() {
        return name;
    }
}
