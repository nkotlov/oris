package dz3;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "------";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    public static void main(String[] args) throws Exception {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM driver");
            while (result.next()) {
                System.out.println(result.getInt("id") + " " + result.getString("firstName") + " " + result.getString("lastName") + " " + result.getInt("age"));
            }
            addMultipleDrivers(connection);
            printDriversOlderThan(connection, 25);
        }
    }

    private static void addMultipleDrivers(Connection connection) throws SQLException {
        String sqlInsertUser = "INSERT INTO driver(firstName, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertUser)) {
            Scanner scanner = new Scanner(System.in);
            for (int i = 0; i < 6; i++) {
                System.out.println("Введите имя:");
                String firstName = scanner.nextLine();
                System.out.println("Введите фамилию:");
                String lastName = scanner.nextLine();
                System.out.println("Введите возраст:");
                int age = Integer.parseInt(scanner.nextLine());
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, age);
                preparedStatement.addBatch();
            }
            int[] affectedRows = preparedStatement.executeBatch();
            System.out.println("Было добавлено " + affectedRows.length + " строк");
        }
    }

    private static void printDriversOlderThan(Connection connection, int age) throws SQLException {
        String sqlSelectDrivers = "SELECT * FROM driver WHERE age > ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectDrivers)) {
            preparedStatement.setInt(1, age);
            ResultSet result = preparedStatement.executeQuery();
            System.out.println("Водители старше " + age + " лет:");
            while (result.next()) {
                System.out.println(result.getInt("id") + " " + result.getString("firstName") + " " + result.getString("lastName") + " " + result.getInt("age"));
            }
        }
    }
}