/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.auth;

import dal.UserDBContext;
import model.auth.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập tên đăng nhập và mật khẩu.");
            req.getRequestDispatcher("login.html").forward(req, resp);
            return;
        }

        UserDBContext db = new UserDBContext();
        User account = db.get(username, password);

        if (account != null) {
            req.getSession().setAttribute("account", account);
            req.getRequestDispatcher("/home").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "Sai tên đăng nhập hoặc mật khẩu!");
            resp.sendRedirect("home");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.html").forward(req, resp);
    }
}
