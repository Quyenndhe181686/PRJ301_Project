/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
public class ViewPlanController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDBContext planDB = new PlanDBContext();
        DepartmentDBContext deptDB = new DepartmentDBContext();
        PlanHeaderDBContext headerDB = new PlanHeaderDBContext();
        ProductDBContext proDB = new ProductDBContext();
        // Lấy danh sách các kế hoạch
        ArrayList<Plan> plans = planDB.list();

        for (Plan p : plans) {

            Department d = deptDB.get(p.getDept().getId());
            p.setDept(d);
            ArrayList<PlanHeader> list = headerDB.getHeadersByPlanId(p.getId());
            for (PlanHeader h : list) {

                h.setProduct(proDB.getProductByPlanHeaderId(h.getId()));
            }
            p.setHeaders(list);

        }
        // Ghi danh sách các kế hoạch vào request
        req.setAttribute("plans", plans);
         
       
        // Chuyển tiếp đến JSP để hiển thị
         req.getRequestDispatcher("../view/plan/view.jsp").forward(req, resp);
    }

}
