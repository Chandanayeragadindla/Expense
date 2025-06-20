package org.example.dailyexpensetrackerproject;

import java.sql.*;

public class UserDAO {

    // DB credentials
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "chandu"; // update this

    // INSERT user (for registration)
    public boolean insertUser(User user) {
        boolean isInserted = false;

        try {
            // Step 1: Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Get Connection
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Step 3: Prepare query
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            // Step 4: Execute and check
            int rows = ps.executeUpdate();
            if (rows > 0) {
                isInserted = true;
            }

            // Step 5: Close
            ps.close();
            conn.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("User already exists.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInserted;
    }

    public boolean validateUser(String email, String password) {
        boolean isValid = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isValid = true;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

}
