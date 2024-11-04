/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.attendance;

import controller.auth.BaseRBACController;
import dal.AttendanceDBContext;
import dal.WorkAssignmentDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.auth.User;
import model.resource.Attendant;
import model.resource.Employee;
import model.resource.PlanDetail;
import model.resource.WorkAssignment;

/**
 *
 * @author milo9
 */
public class TakeAttendancesController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        // Lấy các tham số từ request
        String employeeIdParam = req.getParameter("employeeId");
        String quantityParam = req.getParameter("quantity");
        String pdidParam = req.getParameter("pdid"); // Lấy pdid từ request

        // Kiểm tra và xử lý tham số
        if (employeeIdParam != null && quantityParam != null && pdidParam != null) {
            try {
                int employeeId = Integer.parseInt(employeeIdParam);
                int quantity = Integer.parseInt(quantityParam);
                int pdid = Integer.parseInt(pdidParam); // Chuyển đổi pdid sang int

                // Tạo đối tượng WorkAssignment
                WorkAssignment assignment = new WorkAssignment();
                Employee e = new Employee();
                e.setId(employeeId);
                assignment.setE(e);
                assignment.setQuantity(quantity);

                PlanDetail pd = new PlanDetail();
                pd.setId(pdid);
                assignment.setPlanDetail(pd);

                // Tạo đối tượng Attendant
                Attendant attendant = new Attendant();
                attendant.setWorkAssignment(assignment);
                attendant.setActualQuantity(quantity); // Sử dụng quantity đã nhập
                attendant.setAlpha(1.0f); // Ví dụ giá trị alpha, có thể thay đổi
                attendant.setNote("Nhập thông tin attendance"); // Ghi chú

                // Lưu thông tin attendance vào cơ sở dữ liệu
                AttendanceDBContext attendanceDB = new AttendanceDBContext();
                attendanceDB.insert(attendant); // Gọi phương thức insert của AttendanceDBContext

                // Chuyển hướng trở lại trang danh sách assignments
                resp.sendRedirect(req.getContextPath() + "/plandetails/view?planId=" + pdid); // Chuyển hướng kèm theo pdid
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu không thể chuyển đổi tham số
                req.setAttribute("error", "Giá trị không hợp lệ!");
                req.getRequestDispatcher("../view/work/assignment.jsp").forward(req, resp);
            }
        } else {
            // Nếu không có tham số, chuyển hướng về trang danh sách assignments với thông báo lỗi
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            req.getRequestDispatcher("../view/work/assignment.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String dateParam = req.getParameter("date");
        int shiftId = Integer.parseInt(req.getParameter("shift")); // lấy shift từ request

        // Lấy danh sách work assignments cho date và shift cụ thể
        WorkAssignmentDBContext workAssignmentDB = new WorkAssignmentDBContext();
        ArrayList<WorkAssignment> assignments = workAssignmentDB.getAssignmentsByDateAndShift(dateParam, shiftId);

        // Đặt dữ liệu vào request scope
        req.setAttribute("assignments", assignments);
        req.setAttribute("date", dateParam);
        req.setAttribute("shift", shiftId);

        // Chuyển tiếp đến JSP
        req.getRequestDispatcher("../view/work/take.jsp").forward(req, resp);
    }

}
