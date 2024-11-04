/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.plan;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.PlanDBContext;
import dal.ProductDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import model.auth.User;
import model.resource.Department;
import model.resource.Plan;
import model.resource.PlanHeader;
import model.resource.Product;

/**
 *
 * @author milo9
 */
public class ProductionPlanCreateController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String raw_name = req.getParameter("name");
        String raw_from = req.getParameter("from");
        String raw_to = req.getParameter("to");
        String raw_did = req.getParameter("did");
        String[] pids = req.getParameterValues("pid");

        StringBuilder errors = new StringBuilder();

        // Validation
        if (raw_name == null || raw_name.isEmpty()) {
            errors.append("Tên kế hoạch không được để trống.<br>");
        }

        if (raw_from == null || raw_from.isEmpty() || raw_to == null || raw_to.isEmpty()) {
            errors.append("Ngày bắt đầu và ngày kết thúc không được để trống.<br>");
        } else {
            try {
                Date startDate = Date.valueOf(raw_from);
                Date endDate = Date.valueOf(raw_to);
                if (startDate.after(endDate)) {
                    errors.append("Ngày bắt đầu phải trước ngày kết thúc.<br>");
                }
            } catch (IllegalArgumentException e) {
                errors.append("Ngày không hợp lệ.<br>");
            }
        }

        if (raw_did == null || raw_did.isEmpty()) {
            errors.append("ID phòng ban không được để trống.<br>");
        }

        if (pids == null || pids.length == 0) {
            errors.append("Phải chọn ít nhất một sản phẩm.<br>");
        }

        // Nếu có lỗi, chuyển tiếp về trang tạo kế hoạch và hiển thị thông báo lỗi
        if (errors.length() > 0) {
            req.setAttribute("errors", errors.toString());
            req.setAttribute("depts", new DepartmentDBContext().get("Production"));
            req.setAttribute("products", new ProductDBContext().list());
            req.getRequestDispatcher("../view/plan/create.jsp").forward(req, resp);
            return;
        }

        // Tạo kế hoạch
        Plan plan = new Plan();
        plan.setName(raw_name);
        plan.setStartDate(Date.valueOf(raw_from));
        plan.setEndDate(Date.valueOf(raw_to));

        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        plan.setDept(d);

        for (String pid : pids) {
            Product p = new Product();
            p.setId(Integer.parseInt(pid));

            PlanHeader header = new PlanHeader();
            header.setProduct(p);

            String raw_quantity = req.getParameter("quantity" + pid);
            String raw_effort = req.getParameter("effort" + pid);
            header.setQuantity(raw_quantity != null && raw_quantity.length() > 0 ? Integer.parseInt(raw_quantity) : 0);
            header.setEstimatedEffort(raw_effort != null && raw_effort.length() > 0 ? Float.parseFloat(raw_effort) : 0);

            if (header.getQuantity() > 0 && header.getEstimatedEffort() > 0) {
                plan.getHeaders().add(header);
            }
        }

        if (plan.getHeaders().size() > 0) {
            PlanDBContext db = new PlanDBContext();
            db.insert(plan);
            resp.sendRedirect("../plan/view");
        } else {
            resp.sendError(404, errors.toString());
        }
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        DepartmentDBContext dbDept = new DepartmentDBContext();
        ProductDBContext dbProduct = new ProductDBContext();

        req.setAttribute("depts", dbDept.get("Production"));
        req.setAttribute("products", dbProduct.list());

        req.getRequestDispatcher("../view/plan/create.jsp").forward(req, resp);

    }

}
