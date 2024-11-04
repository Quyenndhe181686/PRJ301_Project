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
import java.util.ArrayList;
import model.auth.User;
import model.resource.Department;
import model.resource.Employee;
import model.resource.Salary;

/**
 *
 * @author milo9
 */
public class UpdateEmployeeController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String raw_eid = req.getParameter("eid");
        String raw_name = req.getParameter("name");
        String raw_did = req.getParameter("dept");
        String raw_phoneNumber = req.getParameter("pNumber");
        String raw_address = req.getParameter("address");
        String raw_salaryid = req.getParameter("salary");

        StringBuilder errors = new StringBuilder();

        // Validation
        if (raw_name == null || raw_name.isEmpty()) {
            errors.append("Tên nhân viên không được để trống.<br>");
        }

        if (raw_did == null || raw_did.isEmpty()) {
            errors.append("ID phòng ban không được để trống.<br>");
        }

        if (raw_phoneNumber == null || raw_phoneNumber.isEmpty()) {
            errors.append("Số điện thoại không được để trống.<br>");
        } else if (!raw_phoneNumber.matches("\\d{10}")) { // Kiểm tra định dạng số điện thoại (10 số)
            errors.append("Số điện thoại không hợp lệ, vui lòng nhập 10 số.<br>");
        }

        if (raw_address == null || raw_address.isEmpty()) {
            errors.append("Địa chỉ không được để trống.<br>");
        }

        if (raw_salaryid == null || raw_salaryid.isEmpty()) {
            errors.append("ID lương không được để trống.<br>");
        }

        // Nếu có lỗi, chuyển tiếp về trang cập nhật nhân viên và hiển thị thông báo lỗi
        if (errors.length() > 0) {
            req.setAttribute("errors", errors.toString());
            req.getRequestDispatcher("../view/employee/update.jsp").forward(req, resp);
            return;
        }

        // Nếu không có lỗi, tiến hành cập nhật thông tin nhân viên
        Employee e = new Employee();
        e.setId(Integer.parseInt(raw_eid));
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
        db.update(e);

        // Chuyển hướng về trang danh sách nhân viên sau khi cập nhật
        resp.sendRedirect("../employee/view");

    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        EmployeeDBContext dbEmp = new EmployeeDBContext();
        Employee e = dbEmp.get(id);
        if (e != null) {
            DepartmentDBContext dbDept = new DepartmentDBContext();
            SalaryDBContext dbSalary = new SalaryDBContext();
            ArrayList<Salary> salarys = dbSalary.list();
            ArrayList<Department> depts = dbDept.list();
            req.setAttribute("e", e);
            req.setAttribute("depts", depts);
            req.setAttribute("salarys", salarys);

            req.getRequestDispatcher("../view/employee/update.jsp").forward(req, resp);
        } else {
            resp.sendError(404, "employee does not exist!");
        }
    }

}
