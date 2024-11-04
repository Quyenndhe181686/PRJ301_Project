package controller.planDetails;

import controller.auth.BaseRBACController;
import dal.PlanDBContext;
import dal.PlanDetailDBContext;
import dal.PlanHeaderDBContext;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import model.auth.User;
import model.resource.Plan;
import model.resource.PlanDetail;
import model.resource.Product;
import model.resource.Shift;

public class CreatePlanDetailController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int planId = Integer.parseInt(req.getParameter("planId"));
        PlanDBContext planDB = new PlanDBContext();
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        // Lấy danh sách các ngày từ startDate đến endDate của kế hoạch
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(plan.getStartDate());

        while (!calendar.getTime().after(plan.getEndDate())) {
            dateList.add(new Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DATE, 1);
        }

        PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
        List<Product> products = planHeaderDB.getProductsByPlanId(planId);

        // Lấy các `PlanDetail` nếu đã tồn tại
        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        List<PlanDetail> existingDetails = planDetailDB.getDetailsByPlanId(planId);

        // Map lưu trữ các giá trị `PlanDetail` theo định dạng [date + "_" + productId + "_" + shiftId]
        Map<String, Integer> detailMap = new HashMap<>();
        for (PlanDetail detail : existingDetails) {
            String key = detail.getDate() + "_" + detail.getPlanHeader().getProduct().getId() + "_" + detail.getShift().getId();
            detailMap.put(key, detail.getQuantity());
        }

        // Lưu `dateList` vào session để sử dụng trong `POST`
        HttpSession session = req.getSession();
        session.setAttribute("dateList", dateList);

        // Truyền dữ liệu vào request để hiển thị trong JSP
        req.setAttribute("plan", plan);
        req.setAttribute("products", products);
        req.setAttribute("detailMap", detailMap);

        req.getRequestDispatcher("../view/plandetail/create.jsp").forward(req, resp);

    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int planId = Integer.parseInt(req.getParameter("planId"));
        PlanDBContext planDB = new PlanDBContext();
        Plan plan = planDB.get(planId);

        if (plan == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        PlanDetailDBContext planDetailDB = new PlanDetailDBContext();
        ArrayList<PlanDetail> detailsToInsertOrUpdate = new ArrayList<>();
        ArrayList<PlanDetail> detailsToDelete = new ArrayList<>();

        // Lấy `dateList` từ session
        HttpSession session = req.getSession();
        List<Date> dateList = (List<Date>) session.getAttribute("dateList");

        PlanHeaderDBContext planHeaderDB = new PlanHeaderDBContext();
        List<Product> products = planHeaderDB.getProductsByPlanId(planId);

        StringBuilder errors = new StringBuilder();
        boolean hasError = false;

        for (Product product : products) {
            String productName = product.getName();
            for (Date date : dateList) {
                String dateStr = date.toString();
                for (int shift = 1; shift <= 3; shift++) {
                    String paramName = "shift" + shift + "_quantity_" + dateStr + "_" + productName;
                    String quantityRaw = req.getParameter(paramName);
                    int quantity = (quantityRaw != null && !quantityRaw.isEmpty()) ? Integer.parseInt(quantityRaw) : 0;

                    // Validation
                    if (quantity < 0) {
                        errors.append("Số lượng không được âm cho sản phẩm " + productName + " vào ngày " + dateStr + " ca " + shift + ".<br>");
                        hasError = true;
                    }

                    Shift s = new Shift();
                    s.setId(shift);

                    PlanDetail detail = new PlanDetail();
                    detail.setPlanHeader(planHeaderDB.getPlanHeaderByProductAndPlan(product.getId(), planId));
                    detail.setShift(s);
                    detail.setDate(date);
                    detail.setQuantity(quantity);

                    if (quantity > 0) {
                        detailsToInsertOrUpdate.add(detail);
                    } else {
                        detailsToDelete.add(detail);
                    }
                }
            }
        }

        // Nếu có lỗi, thông báo lỗi và quay lại trang
        if (hasError) {
            req.setAttribute("errors", errors.toString());
            req.setAttribute("plan", plan);
            req.setAttribute("products", products);
            
            req.getRequestDispatcher("../view/plandetail/create.jsp").forward(req, resp);
            return;
        }

        // Chỉ thực hiện insert hoặc delete nếu không có lỗi
        if (!detailsToInsertOrUpdate.isEmpty()) {
            planDetailDB.insertOrUpdatePlanDetails(detailsToInsertOrUpdate);
        }

        if (!detailsToDelete.isEmpty()) {
            planDetailDB.deleteDetails(detailsToDelete);
        }

        // Xóa `dateList` khỏi session sau khi sử dụng
        session.removeAttribute("dateList");

        String notification = "Cập nhật thành công!";
        resp.sendRedirect("../plandetails/create?planId=" + planId + "&notification=" + notification);
    }

}
