/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.employee;

import controller.auth.BaseRBACController;
import dal.DepartmentDBContext;
import dal.EmployeeDBContext;
import dal.SalaryDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.auth.User;
import model.resource.Department;
import model.resource.Employee;
import model.resource.Salary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeController extends BaseRBACController {
   
    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String raw_name = req.getParameter("name");
        String raw_did = req.getParameter("dept");
        String raw_phoneNumber = req.getParameter("pNumber");
        String raw_address = req.getParameter("address");
        String raw_salaryid = req.getParameter("salary");

        // Danh sách lỗi
        List<String> errors = new ArrayList<>();

        // Validation
        if (raw_name == null || raw_name.trim().isEmpty()) {
            errors.add("Tên nhân viên không được để trống.");
        }

        if (raw_did == null || raw_did.trim().isEmpty()) {
            errors.add("Bạn phải chọn bộ phận.");
        }

        if (raw_phoneNumber == null || raw_phoneNumber.trim().isEmpty()) {
            errors.add("Số điện thoại không được để trống.");
        } else if (!raw_phoneNumber.matches("\\d{10}")) { // Kiểm tra định dạng số điện thoại (10 chữ số)
            errors.add("Số điện thoại phải là 10 chữ số.");
        }

        if (raw_address == null || raw_address.trim().isEmpty()) {
            errors.add("Địa chỉ không được để trống.");
        }

        if (raw_salaryid == null || raw_salaryid.trim().isEmpty()) {
            errors.add("Bạn phải chọn mức lương.");
        }

        // Nếu có lỗi, quay lại trang tạo nhân viên với thông báo lỗi
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            // Lấy lại danh sách các bộ phận và mức lương để hiển thị trên trang
            DepartmentDBContext deptsdb = new DepartmentDBContext();
            SalaryDBContext salarydb = new SalaryDBContext();
            req.setAttribute("depts", deptsdb.list());
            req.setAttribute("salarys", salarydb.list());
            req.getRequestDispatcher("../view/employee/create.jsp").forward(req, resp);
            return;
        }

        // Nếu không có lỗi, thực hiện thêm nhân viên
        Employee e = new Employee();
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
        
        // Chuyển hướng về danh sách nhân viên
        resp.sendRedirect("../employee/view");
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

