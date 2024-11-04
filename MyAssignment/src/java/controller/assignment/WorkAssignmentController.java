package controller.assignment;

import controller.auth.BaseRBACController;
import dal.EmployeeDBContext;
import dal.WorkAssignmentDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.auth.User;
import model.resource.Employee;
import model.resource.PlanDetail;
import model.resource.WorkAssignment;

import java.io.IOException;
import java.util.ArrayList;

public class WorkAssignmentController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String employeeIdParam = req.getParameter("employeeId");
        String quantityParam = req.getParameter("quantity");
        String pdidParam = req.getParameter("pdid"); // Lấy pdid từ request

        // Kiểm tra và xử lý tham số
        if (employeeIdParam == null || quantityParam == null || pdidParam == null || 
            employeeIdParam.isEmpty() || quantityParam.isEmpty() || pdidParam.isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin.");
            req.getRequestDispatcher("../view/work/assignment.jsp").forward(req, resp);
            return;
        }

        try {
            int employeeId = Integer.parseInt(employeeIdParam);
            int quantity = Integer.parseInt(quantityParam);
            int pdid = Integer.parseInt(pdidParam); // Chuyển đổi pdid sang int

            WorkAssignment assignment = new WorkAssignment();
            Employee e = new Employee();
            e.setId(employeeId);
            assignment.setE(e);
            assignment.setQuantity(quantity);
            
            PlanDetail pd = new PlanDetail();
            pd.setId(pdid);
            assignment.setPlanDetail(pd);

            WorkAssignmentDBContext workAssignmentDB = new WorkAssignmentDBContext();
            workAssignmentDB.insert(assignment); // Gọi phương thức để thêm vào DB
            
            // Chuyển hướng trở lại trang danh sách assignments
            resp.sendRedirect("../work/assign?pdid=" + pdid); // Chuyển hướng kèm theo pdid

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu không thể chuyển đổi tham số
            req.setAttribute("errorMessage", "Giá trị không hợp lệ!"); 
            req.getRequestDispatcher("../view/work/assignment.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int pdid = Integer.parseInt(req.getParameter("pdid")); // Lấy pdid từ request

        // Lấy danh sách assignment từ database
        WorkAssignmentDBContext assignmentDB = new WorkAssignmentDBContext();
        ArrayList<WorkAssignment> assignments = assignmentDB.getAssignmentsByPdid(pdid);

        // Lấy danh sách employees
        EmployeeDBContext employeeDB = new EmployeeDBContext();
        ArrayList<Employee> employees = employeeDB.list();

        // Đặt dữ liệu vào request scope
        req.setAttribute("assignments", assignments);
        req.setAttribute("employees", employees);
        req.setAttribute("pdid", pdid);

        // Chuyển tiếp đến JSP
        req.getRequestDispatcher("../view/work/assignment.jsp").forward(req, resp);
    }
}