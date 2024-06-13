package com.example.productmanagerapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Product {
    private String name;
    private String manufacturer;
    private String model;
    private double price;
    private int quantity;

    public Product(String name, String manufacturer, String model, double price, int quantity) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%-20s%-20s%-20s%-10.2f%-10d", name, manufacturer, model, price, quantity);
    }
}

class FileManager {
    private static final String FILENAME = "products.txt";

    public static void writeToFile(Product product) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME, true)))) {
            writer.println(product.getName() + "," + product.getManufacturer() + "," + product.getModel() + ","
                    + product.getPrice() + "," + product.getQuantity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> readFromFile() {
        List<Product> products = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILENAME))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    String manufacturer = parts[1];
                    String model = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int quantity = Integer.parseInt(parts[4]);
                    products.add(new Product(name, manufacturer, model, price, quantity));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return products;
    }

    public static List<Product> searchProductByName(List<Product> products, String name) {
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    public void deleteAllProducts() {
        File file = new File(FILENAME);
        if (file.exists()) {
            file.delete();
            System.out.println("All products deleted.");
        }
    }
}



public class MainApp extends Application {

    private TextArea displayArea;
    private TextField nameField;
    private TextField manufacturerField;
    private TextField modelField;
    private TextField priceField;
    private TextField quantityField;
    private TextField searchField;
    private MariaDBConnection dbManager = new MariaDBConnection();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Product Manager");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Add Product Form
        HBox addProductBox = new HBox(10);
        nameField = new TextField();
        nameField.setPromptText("Name");
        manufacturerField = new TextField();
        manufacturerField.setPromptText("Manufacturer");
        modelField = new TextField();
        modelField.setPromptText("Model");
        priceField = new TextField();
        priceField.setPromptText("Price");
        quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> addProduct());

        addProductBox.getChildren().addAll(nameField, manufacturerField, modelField, priceField, quantityField, addButton);

        // Display Area
        displayArea = new TextArea();
        displayArea.setEditable(false);

        // Search Box
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search by Name");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchProductByName());
        searchBox.getChildren().addAll(searchField, searchButton);

        // Control Buttons
        HBox controlBox = new HBox(10);
        Button displayButton = new Button("Display Products");
        displayButton.setOnAction(e -> displayProducts());
        Button deleteButton = new Button("Delete All Products");
        deleteButton.setOnAction(e -> deleteAllProducts());
        controlBox.getChildren().addAll(displayButton, deleteButton);

        root.getChildren().addAll(addProductBox, displayArea, searchBox, controlBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addProduct() {
        String name = nameField.getText();
        String manufacturer = manufacturerField.getText();
        String model = modelField.getText();
        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            displayArea.setText("Price and Quantity must be valid numbers.");
            return;
        }

        Product newProduct = new Product(name, manufacturer, model, price, quantity);
        dbManager.addProduct(newProduct);
        displayArea.setText("Product added successfully.");

        // Clear input fields
        nameField.clear();
        manufacturerField.clear();
        modelField.clear();
        priceField.clear();
        quantityField.clear();
    }

    private void displayProducts() {
        List<Product> products = dbManager.getAllProducts();
        StringBuilder sb = new StringBuilder();
        sb.append("Name                Manufacturer        Model               Price     Quantity\n");
        for (Product product : products) {
            sb.append(product).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void searchProductByName() {
        String name = searchField.getText();
        List<Product> products = dbManager.searchProductByName(name);
        StringBuilder sb = new StringBuilder();
        sb.append("Name                Manufacturer        Model               Price     Quantity\n");
        for (Product product : products) {
            sb.append(product).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void deleteAllProducts() {
        dbManager.deleteAllProducts();
        displayArea.setText("All products deleted.");
    }
}

