/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.assignment;

import controller.auth.BaseRBACController;
import dal.WorkAssignmentDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.auth.User;
import model.resource.WorkAssignment;

/**
 *
 * @author milo9
 */
public class DeleteAssignmentController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {

        int assignmentId = Integer.parseInt(req.getParameter("assignmentId")); // Lấy ID của assignment từ request

        WorkAssignmentDBContext workAssignmentDB = new WorkAssignmentDBContext();
        WorkAssignment assignment = new WorkAssignment();
        assignment.setId(assignmentId); // Thiết lập ID cho assignment

        workAssignmentDB.delete(assignment); // Gọi phương thức xóa trong DBContext

        // Chuyển hướng trở lại trang danh sách assignments
        int pdid = Integer.parseInt(req.getParameter("pdid"));/* Lấy pdid từ session hoặc request nếu cần */ ;
        resp.sendRedirect("assign?pdid=" + pdid); // Chuyển hướng trở lại trang danh sách
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
