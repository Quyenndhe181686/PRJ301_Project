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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.auth.User;
import model.resource.Plan;
import model.resource.PlanDetail;
import model.resource.Product;

/**
 *
 * @author milo9
 */
public class UpdatePlanDetailController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDBContext planDB = new PlanDBContext();
        int planId = Integer.parseInt(req.getParameter("planId"));
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        // Lấy danh sách sản phẩm theo planId
        PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
        List<Product> products = planHeaderDB.getProductsByPlanId(planId);

        // Lấy danh sách chi tiết kế hoạch từ cơ sở dữ liệu
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        List<PlanDetail> planDetails = planDetailDB.getPlanDetailsByPlanId(planId);

        // Tạo danh sách các ngày giữa startDate và endDate
        Date startDate = plan.getStartDate();
        Date endDate = plan.getEndDate();
        List<Date> dateList = getDateList(startDate, endDate);

        // Tạo bản đồ để lưu trữ số lượng theo sản phẩm, ngày, và ca
        Map<Integer, Map<Date, Map<Integer, Integer>>> quantityMap = new HashMap<>();

// Lưu trữ số lượng vào quantityMap
        for (PlanDetail detail : planDetails) {
            int productId = detail.getPlanHeader().getProduct().getId();
            Date date = detail.getDate();
            int shiftId = detail.getShift().getId();

            System.out.println(detail.getQuantity());

            quantityMap
                    .computeIfAbsent(productId, k -> new HashMap<>())
                    .computeIfAbsent(date, k -> new HashMap<>())
                    .put(shiftId, detail.getQuantity());
        }

        // Truyền các dữ liệu đến JSP
        req.setAttribute("plan", plan);
        req.setAttribute("products", products);
        req.setAttribute("dateList", dateList);
        req.setAttribute("quantityMap", quantityMap); // Truyền quantityMap vào JSP
        req.getRequestDispatcher("../view/plandetail/update.jsp").forward(req, resp);
    }

// Phương thức để tạo danh sách ngày
    private List<Date> getDateList(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        return dateList;
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        int planId = Integer.parseInt(req.getParameter("planId"));

        // Lấy kế hoạch để có StartDate và EndDate
        PlanDBContext planDB = new PlanDBContext();
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        // Lấy danh sách các sản phẩm
        List<Product> products = new PlanHeaderDBContext().getProductsByPlanId(planId);

        // Lấy danh sách các ngày
        List<Date> dateList = getDateList(plan.getStartDate(), plan.getEndDate());

        for (Product product : products) {
            for (Date date : dateList) {
                for (int shift = 1; shift <= 3; shift++) {
                    String quantityParam = "quantity_" + product.getId() + "_K" + shift;
                    String quantityStr = req.getParameter(quantityParam);

                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        int quantity = Integer.parseInt(quantityStr);

                        // Chuyển đổi java.util.Date thành java.sql.Date
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        // Cập nhật dữ liệu vào DB
                        PlanDetail planDetail = new PlanDetail();
                        planDetail.setPlanHeader(new PlanHeaderDBContext().getPlanHeaderByProductAndPlan(product.getId(), planId));
                        planDetail.setDate(sqlDate);
                        planDetail.setShift(new ShiftDBContext().get(shift));
                        planDetail.setQuantity(quantity);

                        // Cập nhật chi tiết kế hoạch
                        planDetailDB.update(planDetail);
                    }
                }
            }
        }

        resp.sendRedirect("success.jsp");
    }


}
