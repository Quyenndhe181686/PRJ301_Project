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
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.sql.Date;
import model.auth.User;
import model.resource.Department;
import model.resource.Plan;
import model.resource.PlanHeader;
import model.resource.Product;

/**
 *
 * @author milo9
 */
public class UpdatePlanCreateController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int planId = Integer.parseInt(req.getParameter("id"));

        // Lấy kế hoạch từ DB để cập nhật
        PlanDBContext dbPlan = new PlanDBContext();
        Plan plan = dbPlan.get(planId);

        if (plan == null) {
            resp.getWriter().println("Plan not found!");
            return;
        }
        
        // Cập nhật thông tin từ form
        plan.setName(req.getParameter("name"));
        plan.setStartDate(Date.valueOf(req.getParameter("from")));
        plan.setEndDate(Date.valueOf(req.getParameter("to")));

        Department d = new Department();
        d.setId(Integer.parseInt(req.getParameter("did")));
        plan.setDept(d);

        ArrayList<PlanHeader> headers = new ArrayList<>();
        String[] pids = req.getParameterValues("pid");
        for (String pid : pids) {
            Product p = new Product();
            p.setId(Integer.parseInt(pid));

            PlanHeader header = new PlanHeader();
            header.setProduct(p);

            // Lấy quantity và effort từ form
            String raw_quantity = req.getParameter("quantity" + pid);
            String raw_effort = req.getParameter("effort" + pid);

            header.setQuatity(raw_quantity != null && raw_quantity.length() > 0 ? Integer.parseInt(raw_quantity) : 0);
            header.setEstimatedEffort(raw_effort != null && raw_effort.length() > 0 ? Float.parseFloat(raw_effort) : 0);

            // Chỉ thêm header nếu quantity và effort hợp lệ
            if (header.getQuatity() > 0 && header.getEstimatedEffort() > 0) {
                headers.add(header);
            }
        }

        plan.setHeaders(headers);

        // Kiểm tra nếu có header mới trước khi cập nhật
        if (plan.getHeaders().size() > 0) {
            dbPlan.update(plan); // Thực hiện cập nhật kế hoạch trong DB
            resp.getWriter().println("Your plan has been updated!");
        } else {
            resp.getWriter().println("Your plan does not have any headers! It is not allowed!");
        }
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {

        int planId = Integer.parseInt(req.getParameter("id"));

        PlanDBContext dbPlan = new PlanDBContext();
        PlanHeaderDBContext dbPlanHeader = new PlanHeaderDBContext();

        Plan plan = dbPlan.get(planId);
        if (plan == null) {
            resp.getWriter().println("Plan not found!");
            return;
        }

        DepartmentDBContext dbDept = new DepartmentDBContext();
        ProductDBContext dbProduct = new ProductDBContext();

        req.setAttribute("plan", plan);
        req.setAttribute("depts", dbDept.get("Production"));
        req.setAttribute("products", dbProduct.list());
        ProductDBContext DBproducts = new ProductDBContext();
        ArrayList<Product> products = DBproducts.list();
        // Đặt quantity và effort vào request scope cho mỗi sản phẩm
        for (Product product : products) {
            int quantity = dbPlanHeader.getQuantityForPlan(planId, product.getId());
            float effort = dbPlanHeader.getEffortForPlan(planId, product.getId());

            // Sử dụng tên thuộc tính có chứa id để lưu vào request scope
            req.setAttribute("quantity" + product.getId(), quantity);
            req.setAttribute("effort" + product.getId(), effort);
        }

        req.getRequestDispatcher("../view/plan/update.jsp").forward(req, resp);

    }

}
