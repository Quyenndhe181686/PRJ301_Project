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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.auth.User;
import model.resource.Plan;
import model.resource.PlanDetail;
import model.resource.Product;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdatePlanDetailController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDBContext planDB = new PlanDBContext();
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();

        int planId = Integer.parseInt(req.getParameter("planId"));
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        // Lấy danh sách ngày
        Date startDate = plan.getStartDate();
        Date endDate = plan.getEndDate();
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            dateList.add(new Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE, 1);
        }

        // Lấy danh sách sản phẩm và chi tiết kế hoạch
        PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
        List<Product> products = planHeaderDB.getProductsByPlanId(planId);
        List<PlanDetail> planDetails = planDetailDB.getPlanDetailsByPlanId(planId);
        
        
        
        // Tạo Map để lưu trữ quantity dựa trên key
        Map<String, Integer> detailMap = new HashMap<>();
        for (PlanDetail detail : planDetails) {
            String key = detail.getDate().toString() + "_" + detail.getPlanHeader().getProduct().getId() + "_" + detail.getShift().getId();
            detailMap.put(key, detail.getQuantity());
        }

        // Truyền dữ liệu sang JSP
        req.setAttribute("plan", plan);
        req.setAttribute("products", products);
        req.setAttribute("dateList", dateList);
        req.setAttribute("detailMap", detailMap); // Truyền Map sang JSP

     
        req.getRequestDispatcher("../view/plandetail/update.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        ArrayList<PlanDetail> planDetails = new ArrayList<>();

        int planId = Integer.parseInt(req.getParameter("planId"));

        // Lấy danh sách sản phẩm
        List<Product> products = new PlanHeaderDBContext().getProductsByPlanId(planId);
        Date startDate = Date.valueOf(req.getParameter("startDate"));
        Date endDate = Date.valueOf(req.getParameter("endDate"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date currentDate = new Date(calendar.getTimeInMillis());
            for (Product product : products) {
                for (int shift = 1; shift <= 3; shift++) {
                    String quantityParam = "quantity_" + product.getId() + "_K" + shift;
                    String quantityParamValue = req.getParameter(quantityParam);

                    // Kiểm tra giá trị quantity
                    if (quantityParamValue == null || quantityParamValue.isEmpty()) {
                        continue;
                    }

                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityParamValue);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    PlanDetail planDetail = new PlanDetail();
                    planDetail.setPlanHeader(new PlanHeaderDBContext().getPlanHeaderByProductAndPlan(product.getId(), planId));
                    planDetail.setDate(currentDate);
                    planDetail.setShift(new ShiftDBContext().get(shift));
                    planDetail.setQuantity(quantity);
                    planDetails.add(planDetail);
                }
            }
            calendar.add(Calendar.DATE, 1);
        }

        if (!planDetails.isEmpty()) {
            planDetailDB.updatePlanDetails(planDetails);
        }

        resp.sendRedirect("success.jsp");
    }
}
