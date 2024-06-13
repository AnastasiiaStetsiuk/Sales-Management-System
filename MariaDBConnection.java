package com.example.productmanagerapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/product_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MariaDB database!");
            // Виконайте запити до бази даних тут
        } catch (SQLException e) {
            System.out.println("Error connecting to MariaDB database");
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO products (name, manufacturer, model, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getManufacturer());
            statement.setString(3, product.getModel());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, product.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String manufacturer = resultSet.getString("manufacturer");
                String model = resultSet.getString("model");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                products.add(new Product(name, manufacturer, model, price, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> searchProductByName(String name) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String productName = resultSet.getString("name");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");
                    products.add(new Product(productName, manufacturer, model, price, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void deleteAllProducts() {
        String query = "DELETE FROM products";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
