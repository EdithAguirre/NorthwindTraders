package com.pluralsight;

import java.sql.*;
import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.hca.jdbc.UsingDriverManager <username> " +
                            "<password>");
            System.exit(1);
        }

        // get the username and password from the command line args
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = args[0];
        String password = args[1];

        // Create and configure the DataSource
        try (
                BasicDataSource dataSource = new BasicDataSource()
        ) {
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        } catch (SQLException e) {
            System.out.println("Fail in creating data source.");
        }

        // Create the connection and prepared statement in a try-with-resources block
        try (
                Connection connection = DriverManager.getConnection(url, username, password)
        ) {
            // Home screen for user to choose what they want to do
            homeScreen(connection);

        } catch (SQLException e) {
            System.out.println("Connection failed.");
        }
    }

    private static void homeScreen(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.print("""
                    What do you want to do?
                        1) Display all products
                        2) Display all customers
                        3) Display all categories
                        0) Exit
                    Select an option: \
                    """);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayAllProducts(connection);
                    break;
                case 2:
                    displayAllCustomers(connection);
                    break;
                case 3:
                    displayAllCategories(connection, scanner);
                    break;
                case 0:
                    break;
            }
        } while (option != 0);
    }

    private static void displayAllCategories(Connection connection, Scanner scanner) {
        // define your query
        String query = "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID";
        try (
                // create prepared statement
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            try (// Execute your query
                 ResultSet results = statement.executeQuery(query)
            ) {
                // process the results
                System.out.printf("%-12s %-10s\n", "CategoryID", "CategoryName");
                System.out.printf("%-12s %-10s\n", "-----------", "---------------");
                while (results.next()) {
                    System.out.printf("%-12s %-10s\n", results.getString(1),
                            results.getString(2));
                }
                System.out.println();

                // Prompt the user for a category id to display all products in that category
                System.out.print("Enter a category ID to display all products in that category: ");
                int idInput = scanner.nextInt();
                scanner.nextLine();

                // create prepared statement
                PreparedStatement productsPreparedStatement = connection.prepareStatement("SELECT ProductID," +
                        " ProductName, UnitPrice, UnitsInStock FROM products WHERE CategoryID = ?");

                // Set parameter for prepared statement
                productsPreparedStatement.setInt(1, idInput);

                // Execute your query
                ResultSet categoryResults = productsPreparedStatement.executeQuery();

                // process the results
                System.out.printf("%-5s %-35s %-10s %-10s\n ", "Id", "Name", "Price", "Stock");
                System.out.printf("%-5s %-35s %-10s %-10s\n", "----",
                        "----------------------------------", "---------", "---------");
                while (categoryResults.next()) {
                    System.out.printf("%-5s %-35s %-10.2f %-10s\n", categoryResults.getString(1),
                            categoryResults.getString(2), categoryResults.getFloat(3),
                            categoryResults.getString(4));
                }
                System.out.println();

            } catch (SQLException e) {
                System.out.println("Results failed.");
            }
        } catch (SQLException e) {
            System.out.println("Prepared statement failed.");
        }
    }

    private static void displayAllCustomers(Connection connection) {
        // define your query
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers";
        try (
                // create prepared statement
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            try (
                    // Execute your query
                    ResultSet results = statement.executeQuery(query)
            ) {
                // process the results
                System.out.printf("%-25s %-38s %-17s %-13s %-14s\n", "ContactName", "CompanyName", "City", "Country", "Phone");
                System.out.printf("%-25s %-38s %-17s %-13s %-14s\n", "------------------------",
                        "-------------------------------------", "----------------", "------------", "-------------");
                while (results.next()) {
                    System.out.printf("%-25s %-38s %-17s %-13s %-10s\n", results.getString(1),
                            results.getString(2), results.getString(3),
                            results.getString(4), results.getString(5));
                }
                System.out.println();

            } catch (SQLException e) {
                System.out.println("Results failed.");
            }
        } catch (SQLException e) {
            System.out.println("Prepared statement failed.");
        }
    }

    private static void displayAllProducts(Connection connection) {
        // define your query
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ORDER BY ProductID";
        try (
                // create prepared statement
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            try (
                    // Execute your query
                    ResultSet results = statement.executeQuery(query)
            ) {
                // process the results
                System.out.printf("%-5s %-35s %-10s %-10s\n", "Id", "Name", "Price", "Stock");
                System.out.printf("%-5s %-35s %-10s %-10s\n", "----",
                        "----------------------------------", "---------", "---------");
                while (results.next()) {
                    System.out.printf("%-5s %-35s %-10.2f %-10s\n", results.getString(1),
                            results.getString(2), results.getFloat(3),
                            results.getString(4));
                }
                System.out.println();
            } catch (SQLException e) {
                System.out.println("Results failed.");
            }
        } catch (SQLException e) {
            System.out.println("Prepared statement failed.");
        }
    }

}
