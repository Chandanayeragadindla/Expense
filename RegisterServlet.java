package org.example.dailyexpensetrackerproject;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Get data from form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Step 2: Create a user object
        User user = new User(email, password);

        // Step 3: Call DAO to insert user
        UserDAO userDAO = new UserDAO();
        boolean isSuccess = userDAO.insertUser(user);

        // Step 4: Redirect with message
        if (isSuccess) {
            response.sendRedirect("login.jsp?msg=Registration successful. Please login.");
        } else {
            response.sendRedirect("register.jsp?msg=Email already exists or something went wrong.");
        }
    }
}
