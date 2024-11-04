/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import model.resource.WorkAssignment;
import java.sql.*;
import model.resource.Employee;
import model.resource.PlanDetail;
import java.sql.*;
import model.resource.Product;

/**
 *
 * @author milo9
 */
public class WorkAssignmentDBContext extends DBContext<WorkAssignment> {

    @Override
    public void insert(WorkAssignment assignment) {
        String sql = "INSERT INTO WorkAssignments ( pdid,eid, quantity) VALUES (?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(2, assignment.getE().getId()); // Lấy ID của nhân viên
            stm.setInt(3, assignment.getQuantity());   // Lấy số lượng
            stm.setInt(1, assignment.getPlanDetail().getId()); // Lấy ID của PlanDetail
            stm.executeUpdate();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    @Override
    public void update(WorkAssignment assignment) {
        String sql = "UPDATE WorkAssignments SET eid = ?, quantity = ?, pdid = ? WHERE waid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, assignment.getE().getId()); // Lấy ID của nhân viên
            stm.setInt(2, assignment.getQuantity());   // Lấy số lượng
            stm.setInt(3, assignment.getPlanDetail().getId()); // Lấy ID của PlanDetail
            stm.setInt(4, assignment.getId());         // Lấy ID của WorkAssignment để cập nhật
            stm.executeUpdate();                        // Thực thi câu lệnh
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(WorkAssignment assignment) {
        String sql = "DELETE FROM WorkAssignments WHERE waid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, assignment.getId()); // Lấy ID để xóa
            stm.executeUpdate();                // Thực thi câu lệnh
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<WorkAssignment> list() {
        ArrayList<WorkAssignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM WorkAssignments"; // Câu truy vấn để lấy tất cả các WorkAssignments

        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                WorkAssignment assignment = new WorkAssignment();
                assignment.setId(rs.getInt("waid")); // Lấy ID
                assignment.setQuantity(rs.getInt("quantity")); // Lấy số lượng

                // Thiết lập Employee
                Employee employee = new Employee();
                employee.setId(rs.getInt("eid")); // Lấy employeeId
                assignment.setE(employee);

                // Thiết lập PlanDetail
                PlanDetail planDetail = new PlanDetail();
                planDetail.setId(rs.getInt("pdid")); // Lấy pdid
                assignment.setPlanDetail(planDetail);

                assignments.add(assignment); // Thêm vào danh sách
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return assignments; // Trả về danh sách WorkAssignments
    }

    @Override
    public WorkAssignment get(int id) {
        WorkAssignment assignment = null;
        String sql = "SELECT * FROM WorkAssignments WHERE id = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                assignment = new WorkAssignment();
                assignment.setId(rs.getInt("waid")); // Lấy ID
                assignment.setQuantity(rs.getInt("quantity")); // Lấy số lượng

                // Thiết lập Employee
                Employee employee = new Employee();
                employee.setId(rs.getInt("eid")); // Lấy employeeId
                assignment.setE(employee);

                // Thiết lập PlanDetail
                PlanDetail planDetail = new PlanDetail();
                planDetail.setId(rs.getInt("pdid")); // Lấy pdid
                assignment.setPlanDetail(planDetail);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return assignment; // Trả về WorkAssignment
    }

    public ArrayList<WorkAssignment> getAssignmentsByPdid(int pdid) {
        EmployeeDBContext dbe = new EmployeeDBContext();
        ArrayList<WorkAssignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM WorkAssignments WHERE pdid = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, pdid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                WorkAssignment assignment = new WorkAssignment();
                assignment.setId(rs.getInt("waid"));

                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(dbe.get(rs.getInt("eid")).getName());
                assignment.setE(e);

                assignment.setQuantity(rs.getInt("quantity"));

                PlanDetail pd = new PlanDetail();
                pd.setId(rs.getInt("pdid"));
                assignment.setPlanDetail(pd);
                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return assignments;
    }

    public ArrayList<WorkAssignment> getAssignmentsByDateAndShift(String dateParam, int shiftId) {
        ArrayList<WorkAssignment> assignments = new ArrayList<>();
        String sql = "SELECT wa.*, e.*, p.* "
                + "FROM WorkAssignments wa "
                + "JOIN Employees e ON wa.eid = e.eid "
                + "JOIN PlanDetails pd ON wa.pdid = pd.pdid "
                + "JOIN PlanHeaders ph ON pd.phid = ph.phid "
                + "JOIN Products p ON ph.pid = p.pid "
                + "WHERE pd.date = ? AND pd.sid = ?"
                + "ORDER BY e.ename , p.pid ";// Lọc theo ngày và ca

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, dateParam); // Ngày từ tham số
            stm.setInt(2, shiftId); // Ca từ tham số
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                WorkAssignment assignment = new WorkAssignment();
                assignment.setId(rs.getInt("waid")); // ID công việc

                Employee e = new Employee();
                e.setId(rs.getInt("eid")); // ID nhân viên
                e.setName(rs.getString("ename")); // Tên nhân viên
                assignment.setE(e); // Đặt nhân viên cho công việc

                Product product = new Product();
                product.setId(rs.getInt("pid")); // ID sản phẩm
                product.setName(rs.getString("pname")); // Tên sản phẩm
                product.setDescription(rs.getString("description")); // Mô tả sản phẩm
                assignment.setProduct(product); // Đặt sản phẩm cho công việc

                assignment.setQuantity(rs.getInt("quantity")); // Số lượng
                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(WorkAssignmentDBContext.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return assignments;

    }
}
