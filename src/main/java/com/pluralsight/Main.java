package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

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

        // load the MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Create the connection and prepared statement
            connection = DriverManager.getConnection(url, username, password);

            // Home screen for user to choose what they want to do
            homeScreen(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void homeScreen(Connection connection){
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

            switch (option){
                case 1:
                    displayAllProducts(connection);
                    break;
                case 2:
                    displayAllCustomers(connection);
                    break;
                case 3:
                    displayAllCategories(connection);
                    break;
                case 0:
                    break;
            }
        }while(option != 0);
    }

    private static void displayAllCategories(Connection connection) {

        
    }

    private static void displayAllCustomers(Connection connection) {
        try {
            // define your query
            String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers";

            // create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Execute your query
            ResultSet results = statement.executeQuery(query);

            // process the results
            System.out.printf("%-25s %-38s %-17s %-10s\n", "ContactName", "CompanyName", "City", "Country", "Phone");
            System.out.printf("%-25s %-38s %-17s %-10s\n", "------------------------",
                    "-------------------------------------", "----------------", "------------");
            while (results.next()) {
                System.out.printf("%-25s %-38s %-17s %-10s\n", results.getString(1),
                        results.getString(2), results.getString(3),
                        results.getString(4));
            }
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void displayAllProducts(Connection connection) {
        try {
            // define your query
            String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ORDER BY Country";

            // create prepared statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Execute your query
            ResultSet results = statement.executeQuery(query);

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
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
