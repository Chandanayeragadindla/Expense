package org.example.dailyexpensetrackerproject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddExpenseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userEmail = (String) request.getSession().getAttribute("userEmail");
        if (userEmail == null) {
            response.sendRedirect("login.jsp?msg=Please login first");
            return;
        }

        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String description = request.getParameter("description");
        String date = request.getParameter("date");

        int quantity = 1;
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            quantity = Integer.parseInt(quantityStr);
        }

        double price = Double.parseDouble(priceStr);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO expenses (user_email, category, subcategory, price, quantity, description, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, userEmail);
            ps.setString(2, category);
            ps.setString(3, subcategory);
            ps.setDouble(4, price);
            ps.setInt(5, quantity);
            ps.setString(6, description);
            ps.setString(7, date);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("dashboard.jsp?msg=Expense added successfully");
            } else {
                response.sendRedirect("addExpense.jsp?msg=Failed to add expense");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addExpense.jsp?msg=Error: " + e.getMessage());
        }
    }
}
