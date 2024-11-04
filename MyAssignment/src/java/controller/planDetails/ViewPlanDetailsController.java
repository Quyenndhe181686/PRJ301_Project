/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.planDetails;

import controller.auth.BaseRBACController;
import dal.PlanDBContext;
import dal.PlanDetailDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.auth.User;
import model.resource.Plan;
import model.resource.PlanDetail;

/**
 *
 * @author milo9
 */
public class ViewPlanDetailsController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        String planId = req.getParameter("planId");

        if (planId != null) {
            // Chuyển hướng đến trang chỉnh sửa chi tiết với planId
            resp.sendRedirect("../plandetails/create?planId=" + planId);
        } else {
            resp.getWriter().println("Plan ID is missing.");
        }
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
 int planId = Integer.parseInt(req.getParameter("planId")); // Get planId from request

        // Fetch plan details from database
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        List<PlanDetail> planDetails = planDetailDB.getDetailsByPlanId(planId);
        
        // Create a list of dates
        List<Date> dateList = new ArrayList<>();
        if (!planDetails.isEmpty()) {
            for (PlanDetail detail : planDetails) {
                dateList.add(detail.getDate()); // Assuming detail.getDate() returns java.sql.Date
            }
        }

        // Store both lists in the session
        HttpSession session = req.getSession();
        session.setAttribute("planDetails", planDetails);
        session.setAttribute("dateList", dateList);
        
//        for(Date i : dateList){
//            resp.getWriter().println(i.toString());
//        }
        
        // Forward to view.jsp
        req.getRequestDispatcher("../view/plandetail/view.jsp").forward(req, resp);
    }

}
