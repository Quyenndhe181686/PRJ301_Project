/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.employee;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.EmployeeDBContext;
import dal.SalaryDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.auth.User;
import model.resource.Department;
import model.resource.Employee;
import model.resource.Salary;

/**
 *
 * @author milo9
 */
public class CreateEmployeeController extends BaseRBACController {
   

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        
        String raw_name=req.getParameter("name");
        String raw_did=req.getParameter("dept");
        String raw_phoneNumber = req.getParameter("pNumber");
        String raw_address = req.getParameter("address");
        String raw_salaryid = req.getParameter("salary");
        Employee e = new Employee();
        //validation        
       
                
        //add
        
        e.setName(raw_name);
        
        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        e.setDept(d);
        
        e.setAddress(raw_address);
        e.setPhoneNumber(raw_phoneNumber);
        
        Salary s = new Salary();        
        s.setId(Integer.parseInt(raw_salaryid));
        e.setSalary(s);        
        
        EmployeeDBContext db = new EmployeeDBContext();        
        db.insert(e);
        
        resp.getWriter().println("New Eid:" + e.getId());
        
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        DepartmentDBContext deptsdb = new DepartmentDBContext();
        SalaryDBContext salarydb = new SalaryDBContext();
        
        req.setAttribute("depts", deptsdb.list());
        req.setAttribute("salarys", salarydb.list());
        
        req.getRequestDispatcher("../view/employee/create.jsp").forward(req, resp);
    }

}
