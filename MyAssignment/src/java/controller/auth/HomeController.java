/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.auth;

import model.auth.User;
import model.auth.Feature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy người dùng từ session
        User user = (User) req.getSession().getAttribute("account");

        if (user == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            resp.sendRedirect("login.html");
            return;
        }

        // Tạo danh sách các feature từ các role của user
        Set<Feature> features = new HashSet<>(); // Dùng Set để tránh trùng lặp
        user.getRoles().forEach(role -> features.addAll(role.getFeatures()));

        // Đưa danh sách features vào request để hiển thị trong home.jsp
        req.setAttribute("features", new ArrayList<>(features));
        req.setAttribute("userName", user.getDisplayName());

        // Chuyển tiếp tới trang home.jsp
        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }
}
