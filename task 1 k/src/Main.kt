import java.io.File

data class Product(
        val name: String,
        val manufacturer: String,
        val model: String,
        val price: Double,
        val quantity: Int
)

object FileManager {
    private const val FILENAME = "products.txt"

    fun writeToFile(product: Product) {
        File(FILENAME).appendText("${product.name},${product.manufacturer},${product.model},${product.price},${product.quantity}\n")
    }

    fun printProducts(products: List<Product>) {
        println("Name                Manufacturer        Model               Price     Quantity")
        products.forEach { println("${it.name.padEnd(20)}${it.manufacturer.padEnd(20)}${it.model.padEnd(20)}${it.price.toString().padEnd(10)}${it.quantity}") }
    }

    fun searchProductByName(products: List<Product>, name: String): List<Product> {
        return products.filter { it.name.equals(name, ignoreCase = true) }
    }

    fun searchProductByQuantity(products: List<Product>, quantity: Int): List<Product> {
        return products.filter { it.quantity < quantity }
    }

    fun displayProducts() {
        val products = readFromFile()
        if (products.isEmpty()) {
            println("No products available.")
        } else {
            printProducts(products)
        }
    }

    fun readFromFile(): List<Product> {
        val file = File(FILENAME)
        if (!file.exists()) {
            println("File not found.")
            return emptyList()
        }
        return file.useLines { lines ->
            lines.mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size == 5) {
                    Product(parts[0], parts[1], parts[2], parts[3].toDouble(), parts[4].toInt())
                } else {
                    null
                }
            }.toList()
        }
    }

    fun deleteAllProducts() {
        val file = File(FILENAME)
        if (file.exists()) {
            file.delete()
            println("All products deleted.")
        }
    }
}

fun main() {
    var choice: Int

    do {
        println("1. Add Product\n2. Display Products\n3. Search Products by Name\n4. Search Products by Quantity\n5. Delete All Products and Exit\n6. Exit")
        print("Enter your choice: ")
        choice = readLine()?.toIntOrNull() ?: 0

        when (choice) {
            1 -> {
                print("Enter product name: ")
                val name = readLine() ?: ""
                print("Enter manufacturer: ")
                val manufacturer = readLine() ?: ""
                print("Enter model: ")
                val model = readLine() ?: ""
                print("Enter price: ")
                val price = readLine()?.toDoubleOrNull() ?: 0.0
                print("Enter quantity: ")
                val quantity = readLine()?.toIntOrNull() ?: 0
                FileManager.writeToFile(Product(name, manufacturer, model, price, quantity))
                println("Product added successfully.")
            }
            2 -> FileManager.displayProducts()
            3 -> {
                print("Enter product name to search: ")
                val searchName = readLine() ?: ""
                val foundByName = FileManager.searchProductByName(FileManager.readFromFile(), searchName)
                FileManager.printProducts(foundByName)
            }
            4 -> {
                print("Enter maximum quantity to search: ")
                val maxQuantity = readLine()?.toIntOrNull() ?: 0
                val foundByQuantity = FileManager.searchProductByQuantity(FileManager.readFromFile(), maxQuantity)
                FileManager.printProducts(foundByQuantity)
            }
            5 -> {
                println("Deleting all products and exiting program.")
                FileManager.deleteAllProducts()
                clearScreen() // Clearing the screen upon exit
            }
            6 -> {
                println("Exiting program.")
                clearScreen() // Clearing the screen upon exit
            }
            else -> println("Invalid choice. Please enter a number between 1 and 6.")
        }
    } while (choice != 6)
}

// Method for clearing the screen
fun clearScreen() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        } else {
            Runtime.getRuntime().exec("clear")
        }
    } catch (ex: Exception) {
        println("Error clearing screen.")
    }
}
