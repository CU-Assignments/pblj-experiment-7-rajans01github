import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {

    static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASS = "your_password";

    public static void main(String[] args) {
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Scanner scanner = new Scanner(System.in)
        ) {
            conn.setAutoCommit(false); // Begin transaction

            while (true) {
                System.out.println("\n==== PRODUCT MANAGEMENT SYSTEM ====");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        createProduct(conn, scanner);
                        break;
                    case 2:
                        readProducts(conn);
                        break;
                    case 3:
                        updateProduct(conn, scanner);
                        break;
                    case 4:
                        deleteProduct(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, quantity);

                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Product created successfully.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Transaction rolled back. Error: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void readProducts(Connection conn) {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Product List ---");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Price: %.2f, Quantity: %d%n",
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading products: " + e.getMessage());
        }
    }

    private static void updateProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new Product Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new Price: ");
            double price = scanner.nextDouble();

            System.out.print("Enter new Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, id);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println("Product updated successfully.");
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Transaction rolled back. Error: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void deleteProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM Product WHERE ProductID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println("Product deleted successfully.");
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Transaction rolled back. Error: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
// Note: Make sure to replace "your_database", "your_username", and "your_password" with your actual database credentials.
// Also, ensure that the Product table exists in your database with the appropriate columns.
// You can create the table using the following SQL command:
// CREATE TABLE Product (ProductID INT PRIMARY KEY, ProductName VARCHAR(100), Price DECIMAL(10, 2), Quantity INT);
// This code is a simple console-based CRUD application for managing products in a database.
// It uses JDBC for database connectivity and handles transactions with commit and rollback.