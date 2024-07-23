package com.example.Database;

import java.sql.*;
import java.util.ArrayList;

public class DataHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/database.db";

    private static final String CREATE_WORD_TABLE = "CREATE TABLE IF NOT EXISTS Words (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "word_target VARCHAR(255) NOT NULL, " +
            "word_explain VARCHAR(255) NOT NULL);";

    private static final String CREATE_TEXT_TO_SPEECH_REQUESTS_TABLE = "CREATE TABLE IF NOT EXISTS TextToSpeechRequests (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "text TEXT NOT NULL, " +
            "target_language VARCHAR(10) NOT NULL, " +
            "file_path TEXT NOT NULL, " +
            "request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    /**
     * Get a connection to the database
     *
     * @return A connection to the database
     * @throws RuntimeException If an error occurs while connecting to the database
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in connecting to the database");
        }
    }

    /**
     * Close the connection to the database
     *
     * @param conn The connection to be closed
     * @throws RuntimeException If an error occurs while closing the connection
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in closing the connection");
        }
    }

    /**
     * Create the tables in the database
     *
     * @throws RuntimeException If an error occurs while creating the tables
     */
    public static void createTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TEXT_TO_SPEECH_REQUESTS_TABLE);
            stmt.execute(CREATE_WORD_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in creating table: " + e.getMessage());
        }
    }

    /**
     * Insert data into the table.
     *
     * @param table   The name of the table
     * @param columns The list of columns
     * @param values  The list of values
     * @throws RuntimeException If an error occurs while inserting data into the table
     */
    public static void insertData(String table, ArrayList<String> columns, ArrayList<String> values) {
        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("Columns and values size mismatch");
        }

        String sqlSearchData = "";
        boolean isExists = false;
        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size() - 1) {
                sqlSearchData += columns.get(i) + " = '" + values.get(i) + "'";
            } else {
                sqlSearchData += columns.get(i) + " = '" + values.get(i) + "' AND ";
            }
        }
        String sqlCheckData = "SELECT * FROM " + table + " WHERE " + sqlSearchData;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheckData)) {
            if (rs.next()) {
                isExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in checking data in the table: " + e.getMessage());
        }

        if (isExists) {
            return;
        }

        String sql = "INSERT INTO " + table + " (" + String.join(", ", columns) + ") VALUES ('" + String.join("', '", values) + "')";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in inserting data into the table: " + e.getMessage() + "\nSQL: " + sql);
        }
    }

    /**
     * Update data in the table.
     *
     * @param table   The name of the table
     * @param columns The list of columns
     * @param values  The list of values
     * @throws RuntimeException If an error occurs while updating data in the table
     */
    public static void updateData(String table, ArrayList<String> columns, ArrayList<String> values) {
        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("Columns and values size mismatch");
        }

        String sql = "UPDATE " + table + " SET " + String.join(" = ?, ", columns) + " = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 1, values.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in updating data in the table: " + e.getMessage());
        }
    }

    /**
     * Update data in the table.
     *
     * @param table     The name of the table
     * @param columns   The list of columns
     * @param values    The list of values
     * @param condition The condition for updating the data
     * @throws RuntimeException If an error occurs while updating data in the table
     */
    public static void updateData(String table, ArrayList<String> columns, ArrayList<String> values, String condition) {
        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("Columns and values size mismatch");
        }

        String sql = "UPDATE " + table + " SET " + String.join(" = ?, ", columns) + " = ? WHERE " + condition;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setString(i + 1, values.get(i));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in updating data in the table: " + e.getMessage());
        }
    }

    /**
     * Delete data from the table.
     *
     * @param table The name of the table
     * @throws RuntimeException If an error occurs while deleting data from the table
     */
    public static void deleteData(String table) {
        String sql = "DELETE FROM " + table;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in deleting data from the table: " + e.getMessage());
        }
    }

    /**
     * Delete data from the table.
     *
     * @param table     The name of the table
     * @param condition The condition for deleting the data
     * @throws RuntimeException If an error occurs while deleting data from the table
     */
    public static void deleteData(String table, String condition) {
        String sql = "DELETE FROM " + table + " WHERE " + condition;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in deleting data from the table: " + e.getMessage());
        }
    }

    /**
     * Query data from the table.
     *
     * @param table   The name of the table
     * @param columns The list of columns
     * @return The list of data
     */
    public static ArrayList<ArrayList<String>> queryData(String table, ArrayList<String> columns) {
        String sql = "SELECT " + String.join(", ", columns) + " FROM " + table;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<ArrayList<String>> result = new ArrayList<>();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(rs.getString(column));
                }
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in querying data from the table: " + e.getMessage());
        }
    }

    /**
     * Query data from the table.
     *
     * @param table     The name of the table
     * @param columns   The list of columns
     * @param condition The condition for selecting the data
     * @return The list of data
     * @throws RuntimeException If an error occurs while selecting data from the table
     */
    public static ArrayList<ArrayList<String>> queryData(String table, ArrayList<String> columns, String condition) {
        String sql = "SELECT " + String.join(", ", columns) + " FROM " + table + " WHERE " + condition;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<ArrayList<String>> result = new ArrayList<>();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(rs.getString(column));
                }
                result.add(row);
            }
            System.out.println(result);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in querying data from the table: " + e.getMessage() + "\nSQL: " + sql);
        }
    }
}
