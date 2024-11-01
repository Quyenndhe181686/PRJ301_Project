/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.planDetails;

import controller.auth.BaseRBACController;
import dal.PlanDBContext;
import dal.PlanDetailDBContext;
import dal.PlanHeaderDBContext;
import dal.ShiftDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import java.util.List;
import model.auth.User;
import model.resource.Plan;
import model.resource.PlanDetail;
import model.resource.Product;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author milo9
 */
public class CreatePlanDetailController extends BaseRBACController {

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDBContext planDB = new PlanDBContext();
        String planIdParam = req.getParameter("planId");

        // Kiểm tra xem planId có hợp lệ không
        if (planIdParam == null || planIdParam.isEmpty()) {
            resp.sendRedirect("error.jsp"); // Chuyển hướng đến trang lỗi
            return;
        }

        int planId = Integer.parseInt(planIdParam);
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        ArrayList<PlanDetail> planDetails = new ArrayList<>();

        // Lấy danh sách sản phẩm
        List<Product> products = new PlanHeaderDBContext().getProductsByPlanId(planId);

        for (Product product : products) {
            Date currentDate = plan.getStartDate();
            while (!currentDate.after(plan.getEndDate())) {
                for (int shift = 1; shift <= 3; shift++) {
                    String quantityParam = "quantity_" + product.getId() + "_K" + shift;
                    String quantityParamValue = req.getParameter(quantityParam);

                    // Kiểm tra giá trị tham số quantityParam
                    if (quantityParamValue == null || quantityParamValue.isEmpty()) {
                        System.out.println("Quantity for " + quantityParam + " is null or empty. Skipping this entry.");
                        continue; // Bỏ qua nếu không có giá trị
                    }

                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityParamValue);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity value for " + quantityParam + ": " + quantityParamValue);
                        continue; // Bỏ qua nếu giá trị không hợp lệ
                    }

                    PlanDetail planDetail = new PlanDetail();
                    planDetail.setPlanHeader(new PlanHeaderDBContext().getPlanHeaderByProductAndPlan(product.getId(), planId));
                    planDetail.setDate(currentDate);
                    planDetail.setShift(new ShiftDBContext().get(shift));
                    planDetail.setQuantity(quantity);
                    planDetails.add(planDetail);
                }
                currentDate = new Date(currentDate.getTime() + (1000 * 60 * 60 * 24)); // Move to next day
            }
        }

        if (!planDetails.isEmpty()) {
            planDetailDB.insertPlanDetails(planDetails);
        }
        
        for(PlanDetail i : planDetails){
            resp.getWriter().print(i.getId());
            resp.getWriter().print(i.getPlanHeader().getId());
            resp.getWriter().print(i.getShift().getId());
            resp.getWriter().print(i.getDate());
            resp.getWriter().println(i.getQuantity());
        }
//        resp.sendRedirect("success.jsp");
    }

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDBContext planDB = new PlanDBContext();
        int planId = Integer.parseInt(req.getParameter("planId"));
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        // Lấy StartDate và EndDate từ Plan
        java.util.Date utilStartDate = plan.getStartDate();
        java.util.Date utilEndDate = plan.getEndDate();

        // Chuyển đổi sang java.sql.Date
        Date startDate = new Date(utilStartDate.getTime());
        Date endDate = new Date(utilEndDate.getTime());

        // Tính số ngày giữa StartDate và EndDate
        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        int numberOfDays = (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;

        // Tạo danh sách các ngày
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        for (int i = 0; i < numberOfDays; i++) {
            dateList.add(new Date(calendar.getTimeInMillis())); // Thêm vào danh sách dưới dạng java.sql.Date
            calendar.add(Calendar.DATE, 1); // Thêm 1 ngày
        }

        PlanHeaderDBContext db = new PlanHeaderDBContext();
        List<Product> products = db.getProductsByPlanId(planId);

        req.setAttribute("plan", plan);
        req.setAttribute("products", products);
        req.setAttribute("dateList", dateList); // Truyền danh sách ngày vào JSP
        req.getRequestDispatcher("../view/plandetail/create.jsp").forward(req, resp);
    }
}
