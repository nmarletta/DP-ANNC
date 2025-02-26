package dpannc.AIMN;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dpannc.database.DB;
import dpannc.Vector;

public class DataExtractor {

    /**
     * Retrieve a list of the vectors that assigned to a particular node.
     *
     * @param nodeID as string.
     * @return list of vectors.
     */
    public static List<Vector> getVectorsForNode(String nodeId) {
        List<Vector> vectors = new ArrayList<>();
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT vector FROM vectors WHERE nodeID = ?");
            stmt.setString(1, nodeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vectors.add(Vector.fromString(rs.getString("vector")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vectors;
    }

    /**
     * Retrieve a list of the vectors that are returned by a query.
     *
     * @param query point as Vector.
     * @return list of vectors.
     */
    public static List<Vector> getQuery(Vector q) {
        List<Vector> vectors = new ArrayList<>();
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT v.vector FROM vectors v " +
                "JOIN queries q ON v.nodeID = q.nodeID " +
                "WHERE q.vector = ?");
            stmt.setString(1, q.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vectors.add(Vector.fromString(rs.getString("vector")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println("query: " + vectors.size());
        return vectors;
    }

    /**
     * Retrieve a list of all vectors in the data structure.
     *
     * @return list of vectors.
     */
    public static List<Vector> getAllVectors() {
        List<Vector> vectors = new ArrayList<>();
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT v.vector FROM vectors v ");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vectors.add(Vector.fromString(rs.getString("vector"))); // Convert back to Vector
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("returned: " + vectors.size());
        return vectors;
    }

    /**
     * Retrieve the total number of points in the data structure.
     *
     * @return number as int.
     */
    public static int getAllVectorsCount() {
        int count = 0;
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM vectors");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getQVectorsCount() {
        int count = 0;
        try {
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM vectors" +
                            "JOIN queries q ON v.nodeID = q.nodeID ");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
