package org.example.dailyexpensetrackerproject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


import java.io.IOException;
import java.sql.*;
import java.util.*;

public class FilterExpenseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = (String) request.getSession().getAttribute("userEmail");

        if (userEmail == null) {
            response.sendRedirect("login.jsp?msg=Please login first");
            return;
        }

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String category = request.getParameter("category");

        if ((startDate == null || startDate.trim().isEmpty()) && (category == null || category.trim().isEmpty())) {
            request.setAttribute("errorMessage", "Please provide at least a start date or a category to filter.");
            request.getRequestDispatcher("filterExpense.jsp").forward(request, response);
            return;
        }

        List<Expense> filteredExpenses = new ArrayList<>();
        HashMap<String, List<Expense>> expensesByDate = new HashMap<>();
        HashMap<String, Double> totalByDate = new HashMap<>();
        HashMap<String, Double> categoryTotals = new HashMap<>();
        double totalAmount = 0.0;

        try (Connection conn = DBConnection.getConnection()) {

            StringBuilder sql = new StringBuilder("SELECT * FROM expenses WHERE user_email = ?");
            List<Object> params = new ArrayList<>();
            params.add(userEmail);

            if (startDate != null && !startDate.trim().isEmpty()) {
                sql.append(" AND date >= ?");
                params.add(startDate);
            }

            if (endDate != null && !endDate.trim().isEmpty()) {
                sql.append(" AND date <= ?");
                params.add(endDate);
            }

            if (category != null && !category.equals("all")) {
                sql.append(" AND category = ?");
                params.add(category);
            }

            sql.append(" ORDER BY date DESC");

            PreparedStatement ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Expense exp = new Expense();
                exp.setCategory(rs.getString("category"));
                exp.setSubcategory(rs.getString("subcategory"));
                exp.setDescription(rs.getString("description"));
                exp.setQuantity(rs.getInt("quantity"));
                exp.setPrice(rs.getDouble("price"));
                exp.setDate(rs.getString("date"));

                filteredExpenses.add(exp);
                totalAmount += exp.getPrice();

                // Group by date
                String date = exp.getDate();
                expensesByDate.putIfAbsent(date, new ArrayList<>());
                expensesByDate.get(date).add(exp);

                totalByDate.put(date, totalByDate.getOrDefault(date, 0.0) + exp.getPrice());

                // Category totals
                categoryTotals.put(exp.getCategory(),
                        categoryTotals.getOrDefault(exp.getCategory(), 0.0) + exp.getPrice());
            }

            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("filteredExpenses", filteredExpenses);
            resultMap.put("expensesByDate", expensesByDate);
            resultMap.put("totalByDate", totalByDate);
            resultMap.put("categoryTotals", categoryTotals);
            resultMap.put("totalAmount", totalAmount);
            resultMap.put("startDate", startDate);
            resultMap.put("endDate", endDate);
            resultMap.put("selectedCategory", category);

            request.setAttribute("hasResults", true);
            request.setAttribute("filterResults", resultMap);
            RequestDispatcher rd = request.getRequestDispatcher("filterExpense.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?msg=Error filtering expenses");
        }
    }
}
