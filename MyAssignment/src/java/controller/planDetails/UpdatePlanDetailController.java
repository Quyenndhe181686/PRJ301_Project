/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.planDetails;

import controller.auth.BaseRBACController;
import dal.PlanDBContext;
import dal.PlanDetailDBContext;
import dal.PlanHeaderDBContext;
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
            dateList.add(new Date(calendar.getTimeInMillis())); // Thêm vào danh sách
            calendar.add(Calendar.DATE, 1); // Thêm 1 ngày
        }

        // Lấy danh sách chi tiết kế hoạch cho planId
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        List<PlanDetail> planDetails = planDetailDB.getPlanDetailsByPlanId(planId); // Lấy các chi tiết kế hoạch

        // Lấy danh sách sản phẩm cho planId
        PlanHeaderDBContext db = new PlanHeaderDBContext();
        List<Product> products = db.getProductsByPlanId(planId);

        Map<String, Map<Integer, PlanDetail>> planDetailsMap = new HashMap<>();

        for (PlanDetail detail : planDetails) {
            String dateKey = new SimpleDateFormat("yyyy-MM-dd").format(detail.getDate());
            
            int productId = detail.getProductDetail().getId();

            if (!planDetailsMap.containsKey(dateKey)) {
                planDetailsMap.put(dateKey, new HashMap<>());
            }

            planDetailsMap.get(dateKey).put(productId, detail);
        }
        // Gán các thuộc tính cần thiết vào request
        req.setAttribute("plan", plan);
        req.setAttribute("products", products);
        req.setAttribute("dateList", dateList); // Truyền danh sách ngày vào JSP
        req.setAttribute("planDetails", planDetailsMap); // Truyền danh sách chi tiết vào JSP

        for (PlanDetail i : planDetails) {
            resp.getWriter().print(i.getId());
            resp.getWriter().print(i.getPlanHeader().getId());
            resp.getWriter().print(i.getShift().getId());
            resp.getWriter().print(i.getDate());
            resp.getWriter().println(i.getQuantity());
        }
        req.getRequestDispatcher("../view/plandetail/update.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        int planId = Integer.parseInt(req.getParameter("planId"));

        // Lấy danh sách các sản phẩm và chi tiết kế hoạch
        List<Product> products = new PlanHeaderDBContext().getProductsByPlanId(planId);
        List<PlanDetail> planDetails = planDetailDB.getPlanDetailsByPlanId(planId); // Lấy các chi tiết kế hoạch

        // Cập nhật từng chi tiết kế hoạch
        for (PlanDetail detail : planDetails) {
            for (Product product : products) {
                // Lấy quantity từ request
                int shiftId = 1; // K1
                String quantityParamK1 = "quantity_" + product.getId() + "_K1";
                String quantityParamK2 = "quantity_" + product.getId() + "_K2";
                String quantityParamK3 = "quantity_" + product.getId() + "_K3";

                // Xử lý các shift
                int quantityK1 = Integer.parseInt(req.getParameter(quantityParamK1));
                int quantityK2 = Integer.parseInt(req.getParameter(quantityParamK2));
                int quantityK3 = Integer.parseInt(req.getParameter(quantityParamK3));

                // Cập nhật quantity cho từng chi tiết
                if (detail.getShift().getId() == shiftId) {
                    detail.setQuantity(quantityK1); // K1
                } else if (detail.getShift().getId() == 2) {
                    detail.setQuantity(quantityK2); // K2
                } else if (detail.getShift().getId() == 3) {
                    detail.setQuantity(quantityK3); // K3
                }

                // Cập nhật vào DB
                planDetailDB.update(detail);
            }
        }

        // Chuyển hướng đến trang thành công
        resp.sendRedirect("success.jsp");
    }

}
