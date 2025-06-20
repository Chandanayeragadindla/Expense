package org.example.dailyexpensetrackerproject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ViewExpenseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        if (userEmail == null) {
            response.sendRedirect("login.jsp?msg=Please login first");
            return;
        }

        HashMap<String, List<Expense>> expensesByDate = new HashMap<>();
        HashMap<String, Double> totalByDate = new HashMap<>();
        double totalAmount = 0.0;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM expenses WHERE user_email = ? ORDER BY date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Expense exp = new Expense();
                exp.setCategory(rs.getString("category"));
                exp.setSubcategory(rs.getString("subcategory"));
                exp.setDescription(rs.getString("description"));
                exp.setQuantity(rs.getInt("quantity"));
                exp.setPrice(rs.getDouble("price"));
                exp.setDate(rs.getString("date"));

                totalAmount += exp.getPrice();

                // Group by date
                String date = exp.getDate();
                expensesByDate.putIfAbsent(date, new ArrayList<>());
                expensesByDate.get(date).add(exp);

                totalByDate.put(date, totalByDate.getOrDefault(date, 0.0) + exp.getPrice());
            }

            // Set data to request
            request.setAttribute("expensesByDate", expensesByDate);
            request.setAttribute("totalByDate", totalByDate);
            request.setAttribute("totalAmount", totalAmount);

            RequestDispatcher rd = request.getRequestDispatcher("viewExpense.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?msg=Error loading expenses");
        }
    }
}
