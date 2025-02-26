package dpannc.database;

import java.sql.*;

public class DB {
    private static final String DB_URL = "jdbc:sqlite:annc.db";
    private static Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS nodes");
                stmt.execute("DROP TABLE IF EXISTS vectors");
                stmt.execute("DROP TABLE IF EXISTS queries");
                stmt.execute("CREATE TABLE IF NOT EXISTS nodes (nodeID TEXT PRIMARY KEY, isBucket BOOLEAN, gaussian TEXT, count INTEGER)");
                stmt.execute("CREATE TABLE IF NOT EXISTS vectors (label TEXT PRIMARY KEY, vector TEXT, nodeID TEXT)");
                stmt.execute("CREATE TABLE IF NOT EXISTS queries (nodeID TEXT, label TEXT, vector TEXT, UNIQUE(nodeID, vector))");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found. Make sure it's added as a dependency.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (conn == null) {
            System.err.println("WARNING: Database connection was null! Reopening connection...");
            try {
                conn = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
    
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts to the queries table.
     *
     * @param nodeID string.
     * @param label string.
     * @param vector string.
     */
    public static void insertToQueries(String nodeID, String label, String vector) {
            try {
                Connection conn = DB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO queries (nodeID, label, vector) VALUES (?, ?, ?)");
                stmt.setString(1, nodeID);
                stmt.setString(2, label);
                stmt.setString(3, vector);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * Inserts to the vectors table.
     *
     * @param label string.
     * @param vector string.
     * @param nodeID string.
     */
    public static void insertToVectors(String label, String vector, String nodeID) {
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO vectors (label, vector, nodeID) VALUES (?, ?, ?)");
            stmt.setString(1, label);
            stmt.setString(2, vector);
            stmt.setString(3, nodeID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts to the nodes table.
     *
     * @param nodeID string.
     * @param isBucket boolean.
     * @param gaussian string.
     * @param count int.
     */
    public static void insertToNodes(String nodeID, boolean isBucket, String gaussian, int count) {
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO nodes (nodeID, isBucket, gaussian, count) VALUES (?, ?, ?, ?)");
            stmt.setString(1, nodeID);
            stmt.setBoolean(2, isBucket);
            stmt.setString(3, gaussian);
            stmt.setInt(4, count);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}