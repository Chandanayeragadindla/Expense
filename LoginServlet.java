package org.example.dailyexpensetrackerproject;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        boolean isValidUser = userDAO.validateUser(email, password);

        if (isValidUser) {
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", email);
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?msg=Credentials not found, please register.");
        }
    }
}
