/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import dal.ProductDBContext;
import model.auth.User;
import model.auth.Feature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.resource.Product;

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

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        ProductDBContext productDB = new ProductDBContext();
        List<Product> products = productDB.list(); // Giả sử có phương thức list() để lấy tất cả sản phẩm

        // Đưa danh sách sản phẩm vào request
        req.setAttribute("products", products);
        req.setAttribute("userName", user.getDisplayName());

        // Tạo danh sách đường dẫn dựa trên vai trò của người dùng
        List<String> links = new ArrayList<>();
        user.getRoles().forEach(role -> {
            switch (role.getName()) {
                case "Role 1":
                    links.add("../employee/add");
                    links.add("../employee/view");
                    break;
                case "Role 2":
                    links.add("../plan/create");
                    links.add("../plan/view");
                    break;
                case "Role 4":
                    links.add("../employee/view");
                    links.add("../plan/view");
                    links.add("../attendance/view");
                    links.add("../salary/report");
                    break;
                case "Role 5":
                    links.add("../plan/view");
                    break;
                default:
                    break;
            }
        });
        Date date = new Date();
        
        req.setAttribute("date", date);
        req.setAttribute("links", links);

        // Chuyển tiếp tới trang home.jsp
        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }

}
