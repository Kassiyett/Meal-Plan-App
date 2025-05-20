package ui;

import exceptions.InvalidInputException;
import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the graphical user interface for the meal planner application
public class MealPlannerGUI extends JFrame implements ListSelectionListener {
    private static final String JSON_STORE_MEALS = "./data/meals.json";
    private static final String JSON_STORE_SHOPPING_LIST = "./data/shoppingList.json";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private MealDatabase mealDatabase;
    private ShoppingList shoppingList;
    private DefaultListModel<Meal> mealListModel;
    private DefaultListModel<Ingredient> ingredientListModel;

    private JList<Meal> mealJList;
    private JList<Ingredient> ingredientJList;
    private JsonReader jsonReaderMeals;
    private JsonWriter jsonWriterMeals;
    private JsonReader jsonReaderShoppingList;
    private JsonWriter jsonWriterShoppingList;

    private WindowListenerGui windowListenerGui;

    // EFFECTS: Creates and sets up the main application window
    public MealPlannerGUI() {
        super("Meal Planner");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeBackend();
        initializeMenu();
        initializeMealPanel();
        initializeButtons();
        initializeLogo();
        pack();
        setVisible(true);
        windowListenerGui = new WindowListenerGui();
        this.addWindowListener(windowListenerGui);
        startLoadPrompt();
        exitSavePrompt();


//        // Adding Image
//        ImageIcon imageIcon = new ImageIcon("data/food.png");
//        JLabel imageLabel = new JLabel(imageIcon);

//        imageLabel.setPreferredSize(new Dimension(100, 100));

//        add(imageLabel, BorderLayout.NORTH);
    }

    // EFFECTS: Initializing Image
    private void initializeLogo() {
        ImageIcon imageIcon = new ImageIcon("data/food.png");
        if (imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Image not loaded properly!");
            return;
        }
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        add(imageLabel, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: initializes main model and data fields
    private void initializeBackend() {
        jsonReaderMeals = new JsonReader(JSON_STORE_MEALS);
        jsonWriterMeals = new JsonWriter(JSON_STORE_MEALS);
        jsonReaderShoppingList = new JsonReader(JSON_STORE_SHOPPING_LIST);
        jsonWriterShoppingList = new JsonWriter(JSON_STORE_SHOPPING_LIST);
        mealDatabase = new MealDatabase();
        shoppingList = new ShoppingList();
        mealListModel = new DefaultListModel<>();
        ingredientListModel = new DefaultListModel<>();
    }

    // MODIFIES: this
    // EFFECTS: initializes menu bar with load and save options
    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> loadData());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveData());

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: initializes the panel that displays meals and ingredients
    private void initializeMealPanel() {
        JSplitPane mealPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mealPane.setOneTouchExpandable(true);
        mealPane.setDividerLocation(400);

        mealJList = new JList<>(mealListModel);
        mealJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealJList.addListSelectionListener(this);
        JScrollPane mealScrollPane = new JScrollPane(mealJList);

        ingredientJList = new JList<>(ingredientListModel);
        JScrollPane ingredientScrollPane = new JScrollPane(ingredientJList);

        mealPane.add(mealScrollPane);
        mealPane.add(ingredientScrollPane);

        add(mealPane, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: initializes buttons for adding meals, ingredients, and managing the shopping list
    private void initializeButtons() {
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());

        JButton addMealButton = new JButton("Add Meal");
        addMealButton.addActionListener(e -> addMeal());

        JButton addToShoppingListButton = new JButton("Add Meal to Shopping List");
        addToShoppingListButton.addActionListener(e -> addMealToShoppingList());

        buttonPane.add(addMealButton);
        buttonPane.add(addToShoppingListButton);

        add(buttonPane, BorderLayout.PAGE_END);



        JButton viewShoppingListButton = new JButton("View Shopping List");
        viewShoppingListButton.addActionListener(e -> showShoppingList());

        buttonPane.add(viewShoppingListButton);

        add(buttonPane, BorderLayout.PAGE_END);
    }


    // EFFECTS: prompts the user to add a new meal
    private void addMeal() {
        String mealName = JOptionPane.showInputDialog("Enter Meal Name:");
        if (mealName != null && !mealName.trim().isEmpty()) {
            Meal meal = new Meal(mealName);
            boolean continueAddingIngredients = true;
            while (continueAddingIngredients) {
                String ingredientName = JOptionPane.showInputDialog(this,
                        "Enter Ingredient Name (or cancel to finish):");
                if (ingredientName == null || ingredientName.trim().isEmpty()) {
                    continueAddingIngredients = false;
                } else {
                    // how to handle quantity?!!!!!!!!
                    Ingredient ingredient = new Ingredient(ingredientName);
                    meal.addIngredient(ingredient);
                }
            }

            if (mealDatabase.addMeal(meal)) {
                mealListModel.addElement(meal);
                updateMealList();
            } else {
                JOptionPane.showMessageDialog(this, "Meal already exists in the database.");
            }
        }
    }

    // EFFECTS: See shopping List
    private void showShoppingList() {

        JTextArea shoppingListTextArea = new JTextArea(15, 30);
        shoppingListTextArea.setEditable(false);

        StringBuilder shoppingListContent = new StringBuilder();
        shoppingList.getAllItems().forEach((itemName, quantity) ->
                shoppingListContent.append(itemName).append(": ").append(quantity).append("\n"));
        shoppingListTextArea.setText(shoppingListContent.toString());

        JScrollPane scrollPane = new JScrollPane(shoppingListTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JOptionPane.showMessageDialog(null, scrollPane, "Shopping List", JOptionPane.INFORMATION_MESSAGE);
    }



    // MODIFIES: this
    // EFFECTS: adds selected meal's ingredients to the shopping list
    private void addMealToShoppingList() {
        Meal selectedMeal = mealJList.getSelectedValue();
        if (selectedMeal != null) {
            selectedMeal.getIngredients().forEach(ingredient ->
                    shoppingList.addItem(ingredient.getName(), 1));
            JOptionPane.showMessageDialog(this, "Ingredients added to the shopping list.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a meal to add to the shopping list.");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to load data on start
    private void startLoadPrompt() {
        int loadOption = JOptionPane.showConfirmDialog(null,
                "Would you like to load the previous session?", "Load Session",
                JOptionPane.YES_NO_OPTION);
        if (loadOption == JOptionPane.YES_OPTION) {
            loadData();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to save data when closing
    private void exitSavePrompt() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int saveOption = JOptionPane.showConfirmDialog(null,
                        "Would you like to save the current session?", "Save Session",
                        JOptionPane.YES_NO_OPTION);
                if (saveOption == JOptionPane.YES_OPTION) {
                    saveData();
                }
                System.exit(0);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: loads meal database and shopping list from files
    private void loadData() {
        try {
            mealDatabase = jsonReaderMeals.readMealDatabase();
            shoppingList = jsonReaderShoppingList.readShoppingList();
            updateMealList();
            JOptionPane.showMessageDialog(this, "Data loaded successfully.");
        } catch (IOException | InvalidInputException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data.");
        }
    }

    // EFFECTS: saves meal database and shopping list to files
    private void saveData() {
        try {
            jsonWriterMeals.open();
            jsonWriterMeals.write(mealDatabase);
            jsonWriterMeals.close();

            jsonWriterShoppingList.open();
            jsonWriterShoppingList.write(shoppingList);
            jsonWriterShoppingList.close();

            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to save data.");
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the list of meals in the UI
    private void updateMealList() {
        mealListModel.clear();
        for (Meal meal : mealDatabase.getAllMeals()) {
            mealListModel.addElement(meal);
        }
    } // need to add meal name

    // MODIFIES: this
    // EFFECTS: handles list selection changes
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Meal selectedMeal = mealJList.getSelectedValue();
            if (selectedMeal != null) {
                ingredientListModel.clear();
                for (Ingredient ingredient : selectedMeal.getIngredients()) {
                    ingredientListModel.addElement(ingredient);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MealPlannerGUI::new);
    }
}

