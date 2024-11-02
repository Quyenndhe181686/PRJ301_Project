/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.plan;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.PlanDBContext;
import dal.PlanHeaderDBContext;
import dal.ProductDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.auth.User;
import model.resource.Department;
import model.resource.Plan;
import model.resource.PlanHeader;
import model.resource.Product;
import java.util.ArrayList;
import java.sql.*;

public class UpdatePlanController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int planId = Integer.parseInt(req.getParameter("planId"));

        PlanDBContext planDB = new PlanDBContext();
        DepartmentDBContext deptDB = new DepartmentDBContext();
        ProductDBContext productDB = new ProductDBContext();
        PlanHeaderDBContext headerDB = new PlanHeaderDBContext();

        // Lấy dữ liệu của kế hoạch cần cập nhật
        Plan plan = planDB.get(planId);
        ArrayList<Department> departments = deptDB.list();
        ArrayList<Product> products = productDB.list();
        ArrayList<PlanHeader> headers = headerDB.getHeadersByPlanId(planId);
        for (PlanHeader ph : headers) {
            Product d = productDB.getProductByPlanHeaderId(ph.getId());
            ph.setProduct(d);
        }
        plan.setHeaders(headers);

        // Đặt dữ liệu vào request scope để hiển thị trên `update.jsp`
        req.setAttribute("plan", plan);
        req.setAttribute("depts", departments);
        req.setAttribute("products", products);
        req.setAttribute("headers", headers);

        req.getRequestDispatcher("../view/plan/update.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        // Lấy thông tin kế hoạch từ form
        Plan plan = new Plan();
        plan.setId(Integer.parseInt(req.getParameter("planId")));
        plan.setName(req.getParameter("name"));
        plan.setStartDate(Date.valueOf(req.getParameter("from")));
        plan.setEndDate(Date.valueOf(req.getParameter("to")));

        // Lấy thông tin phòng ban
        Department dept = new Department();
        dept.setId(Integer.parseInt(req.getParameter("did")));
        plan.setDept(dept);

        ArrayList<PlanHeader> headers = new ArrayList<>();

        // Giả sử bạn có danh sách tất cả các sản phẩm (`products`) đã được load từ cơ sở dữ liệu
        ProductDBContext productDB = new ProductDBContext();
        ArrayList<Product> products = productDB.list();  // Lấy danh sách tất cả sản phẩm

        // Duyệt qua tất cả sản phẩm và lấy dữ liệu từ form
        for (Product product : products) {
            String quantityStr = req.getParameter("quantity" + product.getId());
            String effortStr = req.getParameter("effort" + product.getId());
            String phidStr = req.getParameter("phid" + product.getId());

            // Kiểm tra nếu quantity, effort và phid không null và không rỗng
            if (phidStr != null && !phidStr.trim().isEmpty()
                    && quantityStr != null && !quantityStr.trim().isEmpty()
                    && effortStr != null && !effortStr.trim().isEmpty()) {

                int phid = Integer.parseInt(phidStr);
                int quantity = Integer.parseInt(quantityStr);
                float estimatedEffort = Float.parseFloat(effortStr);

                // Tạo mới PlanHeader với phid và thêm vào danh sách headers
                PlanHeader header = new PlanHeader();
                header.setId(phid);  // Sử dụng phid để xác định PlanHeader cần cập nhật
                header.setProduct(product);
                header.setQuantity(quantity);
                header.setEstimatedEffort(estimatedEffort);
                headers.add(header);
            }
        }

        // Gán danh sách headers vào plan
        plan.setHeaders(headers);

        // Cập nhật kế hoạch vào cơ sở dữ liệu
        PlanDBContext db = new PlanDBContext();
        db.update(plan);

        // Chuyển hướng sau khi cập nhật thành công
        resp.sendRedirect("../plan/view");
    }
}