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
        Plan plan = new Plan();
        plan.setName(req.getParameter("name"));
        plan.setStartDate(Date.valueOf(req.getParameter("from")));
        plan.setEndDate(Date.valueOf(req.getParameter("to")));
        
        Department d = new Department();
        d.setId(Integer.parseInt(req.getParameter("did")));
        
        plan.setDept(d);
        
        String[] pids = req.getParameterValues("pid");
        for (String pid : pids) {
            Product p = new Product();
            p.setId(Integer.parseInt(pid));
            
            PlanHeader header = new PlanHeader();
            header.setProduct(p);
            String raw_quantity = req.getParameter("quantity"+pid);
            String raw_effort = req.getParameter("effort"+pid);
            header.setQuatity(raw_quantity!=null && raw_quantity.length()>0?Integer.parseInt(raw_quantity):0);
            header.setEstimatedEffort(raw_effort!=null && raw_effort.length()>0?Float.parseFloat(raw_effort):0);
            
            if(header.getQuatity()>0&& header.getEstimatedEffort()>0)
                plan.getHeaders().add(header);
        }
        
        if(plan.getHeaders().size() >0)
        {
            PlanDBContext db = new PlanDBContext();
            db.insert(plan);
            resp.getWriter().println("your plan has been added!");
        }
        else
        {
            resp.getWriter().println("your plan does not have any headers! it is not allowed!");
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
