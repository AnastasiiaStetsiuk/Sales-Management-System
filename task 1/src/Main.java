import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.File;


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

    public static void printProducts(List<Product> products) {
        System.out.println("Name                Manufacturer        Model               Price     Quantity");
        for (Product product : products) {
            System.out.println(product);
        }
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

    public static List<Product> searchProductByQuantity(List<Product> products, int quantity) {
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() < quantity) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    public void displayProducts() {
        List<Product> products = readFromFile();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("Name                Manufacturer        Model               Price     Quantity");
        for (Product product : products) {
            System.out.println(product);
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

    public void deleteAllProducts() {
        File file = new File(FILENAME);
        if (file.exists()) {
            file.delete();
            System.out.println("All products deleted.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager(); // Оголошення та ініціалізація змінної fileManager
        Scanner scanner = new Scanner(System.in);
        // Ініціалізуємо змінну products
        List<Product> products = new ArrayList<>();

        int choice;
        do {
            System.out.println("1. Add Product\n2. Display Products\n3. Search Products by Name\n4. Search Products by Quantity\n5. Delete All Products and Exit\n6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    Product newProduct = new Product(name, manufacturer, model, price, quantity);
                    FileManager.writeToFile(newProduct);
                    System.out.println("Product added successfully.");
                    break;
                case 2:
                    products = fileManager.readFromFile(); // Оновлення списку продуктів
                    FileManager.printProducts(products);
                    break;
                case 3:
                    System.out.print("Enter product name to search: ");
                    String searchName = scanner.nextLine();
                    products = fileManager.readFromFile(); // Оновлення списку продуктів
                    List<Product> foundByName = FileManager.searchProductByName(products, searchName);
                    FileManager.printProducts(foundByName);
                    break;
                case 4:
                    System.out.print("Enter maximum quantity to search: ");
                    int maxQuantity = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    products = fileManager.readFromFile(); // Оновлення списку продуктів
                    List<Product> foundByQuantity = FileManager.searchProductByQuantity(products, maxQuantity);
                    FileManager.printProducts(foundByQuantity);
                    break;
                case 5:
                    System.out.println("Deleting all products and exiting program.");
                    fileManager.deleteAllProducts();
                    clearScreen(); // Очищення екрану при виході
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    clearScreen(); // Очищення екрану при виході
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        } while (choice != 6);
        scanner.close();
    }
    // Метод для очищення екрану
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception ex) {
            System.out.println("Error clearing screen.");
        }
    }
}
