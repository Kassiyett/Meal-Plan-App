package ui;

import exceptions.InvalidInputException;
import model.Ingredient;
import model.Meal;
import model.MealDatabase;
import model.ShoppingList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

// Represents the main user interface console for Meal Planner
public class MealPlannerApp {
    private static final String JSON_STORE_MEALS = "./data/meals.json";
    private static final String JSON_STORE_SHOPPING_LIST = "./data/shoppingList.json";
    private MealDatabase mealDatabase;
    private ShoppingList shoppingList;
    private Scanner input;
    private JsonReader jsonReaderMeals;
    private JsonWriter jsonWriterMeals;
    private JsonReader jsonReaderShoppingList;
    private JsonWriter jsonWriterShoppingList;

    // EFFECTS: runs the meal planner application
    public MealPlannerApp() {
        jsonReaderMeals = new JsonReader(JSON_STORE_MEALS);
        jsonWriterMeals = new JsonWriter(JSON_STORE_MEALS);
        jsonReaderShoppingList = new JsonReader(JSON_STORE_SHOPPING_LIST);
        jsonWriterShoppingList = new JsonWriter(JSON_STORE_SHOPPING_LIST);
        mealDatabase = new MealDatabase();
        shoppingList = new ShoppingList();
        input = new Scanner(System.in);
        runMealPlanner();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runMealPlanner() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            command = input.nextLine();

            if (command.equals("quit")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tnew - create a new meal");
        System.out.println("\tlist - list all meals");
        System.out.println("\tadd - add ingredient to shopping list");
        System.out.println("\tshow - show shopping list");
        System.out.println("\tsave - save meals and shopping list to file");
        System.out.println("\tload - load meals and shopping list from file");
        System.out.println("\tquit - quit application");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "new":
                createMeal();
                break;
            case "list":
                listMeals();
                break;
            case "add":
                addMealToShoppingList();
                break;
            case "show":
                showShoppingList();
                break;
            case "save":
                saveData();
                break;
            case "load":
                loadData();
                break;
            default:
                System.out.println("Invalid command...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to create a new meal and add it to the database
    private void createMeal() {
        System.out.println("Enter meal name:");
        String name = input.nextLine();
        Meal meal = new Meal(name);

        System.out.println("Add ingredients (type 'done' when finished):");


        String ingredientName = input.nextLine();
        while (!ingredientName.equalsIgnoreCase("done")) {
            Ingredient ingredient = new Ingredient(ingredientName);
            meal.addIngredient(ingredient);
            ingredientName = input.nextLine();
        }

        mealDatabase.addMeal(meal);
        System.out.println("Meal added successfully.");
    }


    // EFFECTS: lists all meals in the database
    private void listMeals() {
        List<Meal> meals = mealDatabase.getAllMeals();
        for (Meal meal : meals) {
            System.out.println(meal.getName());
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a selected meal's ingredients to the shopping list
    private void addMealToShoppingList() {
        System.out.println("Enter meal name to add to shopping list:");
        String name = input.nextLine();

        // find the meal in the meal database
        Optional<Meal> mealOptional = mealDatabase.findMealByName(name);

        if (mealOptional.isPresent()) {
            Meal meal = mealOptional.get();
            List<Ingredient> ingredients = meal.getIngredients();
            for (Ingredient ingredient : ingredients) {
                shoppingList.addItem(ingredient.getName(), 1);
            }
            System.out.println("Ingredients for " + name + " have been added to the shopping list.");
        } else {
            System.out.println("Meal not found in the database.");
        }
    }


    // EFFECTS: shows the shopping list
    private void showShoppingList() {
        Map<String, Integer> items = shoppingList.getAllItems();
        for (String item : items.keySet()) {
            System.out.println(item + ": " + items.get(item));
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the meal database and shopping list to files
    private void saveData() {
        try {
            jsonWriterMeals.open();
            jsonWriterMeals.write(mealDatabase);
            jsonWriterMeals.close();

            jsonWriterShoppingList.open();
            jsonWriterShoppingList.write(shoppingList);
            jsonWriterShoppingList.close();

            System.out.println("Data saved to files.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the meal database and shopping list from files
    private void loadData() {
        try {
            mealDatabase = jsonReaderMeals.readMealDatabase();
            shoppingList = jsonReaderShoppingList.readShoppingList();
            System.out.println("Data loaded from files.");
        } catch (IOException e) {
            System.out.println("Unable to read from file.");
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
    }
}
