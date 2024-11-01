/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.resource.Department;
import model.resource.Plan;
import model.resource.PlanHeader;
import model.resource.Product;
import java.sql.*;

/**
 *
 * @author milo9
 */
public class PlanDBContext extends DBContext<Plan> {

    @Override
    public void insert(Plan model) {
        try {
            connection.setAutoCommit(false);
            String sql_insert_plan = "INSERT INTO [Plans]\n"
                    + "           ([plname]\n"
                    + "           ,[startdate]\n"
                    + "           ,[enddate]\n"
                    + "           ,[did])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";

            PreparedStatement stm_insert_plan = connection.prepareStatement(sql_insert_plan);
            stm_insert_plan.setString(1, model.getName());
            stm_insert_plan.setDate(2, (Date) model.getStartDate());
            stm_insert_plan.setDate(3, (Date) model.getEndDate());
            stm_insert_plan.setInt(4, model.getDept().getId());
            stm_insert_plan.executeUpdate();

            String sql_select_plan = "SELECT @@IDENTITY as plid";
            PreparedStatement stm_select_plan = connection.prepareStatement(sql_select_plan);
            ResultSet rs = stm_select_plan.executeQuery();
            if (rs.next()) {
                model.setId(rs.getInt("plid"));
            }

            String sql_insert_header = "INSERT INTO [PlanHeaders]\n"
                    + "           ([plid]\n"
                    + "           ,[pid]\n"
                    + "           ,[quantity]\n"
                    + "           ,[estimatedeffort])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)";

            for (PlanHeader header : model.getHeaders()) {
                PreparedStatement stm_insert_header = connection.prepareStatement(sql_insert_header);
                stm_insert_header.setInt(1, model.getId());
                stm_insert_header.setInt(2, header.getProduct().getId());
                stm_insert_header.setInt(3, header.getQuatity());
                stm_insert_header.setFloat(4, header.getEstimatedEffort());
                stm_insert_header.executeUpdate();
            }

            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void update(Plan plan) {
        PreparedStatement stmPlan = null;
        PreparedStatement stmUpdateHeader = null;
        PreparedStatement stmInsertHeader = null;

        String sqlPlan = "UPDATE Plans SET plname = ?, startdate = ?, enddate = ?, did = ? WHERE plid = ?";
        String sqlUpdateHeader = "UPDATE PlanHeaders SET quantity = ?, estimatedeffort = ? WHERE plid = ? AND pid = ?";
        String sqlInsertHeader = "INSERT INTO PlanHeaders (plid, pid, quantity, estimatedeffort) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Cập nhật thông tin kế hoạch
            stmPlan = connection.prepareStatement(sqlPlan);
            stmPlan.setString(1, plan.getName());
            stmPlan.setDate(2, plan.getStartDate());
            stmPlan.setDate(3, plan.getEndDate());
            stmPlan.setInt(4, plan.getDept().getId());
            stmPlan.setInt(5, plan.getId());
            stmPlan.executeUpdate();

            // Cập nhật hoặc thêm mới các header
            for (PlanHeader header : plan.getHeaders()) {
                // Kiểm tra nếu header đã tồn tại
                stmUpdateHeader = connection.prepareStatement(sqlUpdateHeader);
                stmUpdateHeader.setInt(1, header.getQuatity());
                stmUpdateHeader.setFloat(2, header.getEstimatedEffort());
                stmUpdateHeader.setInt(3, plan.getId());
                stmUpdateHeader.setInt(4, header.getProduct().getId());

                int rowsAffected = stmUpdateHeader.executeUpdate();

                // Nếu header chưa tồn tại, chèn mới
                if (rowsAffected == 0) {
                    stmInsertHeader = connection.prepareStatement(sqlInsertHeader);
                    stmInsertHeader.setInt(1, plan.getId());
                    stmInsertHeader.setInt(2, header.getProduct().getId());
                    stmInsertHeader.setInt(3, header.getQuatity());
                    stmInsertHeader.setFloat(4, header.getEstimatedEffort());
                    stmInsertHeader.executeUpdate();
                }
            }

            // Commit transaction
            connection.commit();

        } catch (SQLException ex) {
            // Rollback if there is an error
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, "Rollback failed", rollbackEx);
            }
            Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            // Reset auto-commit and close resources
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException autoCommitEx) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, "Failed to reset auto-commit", autoCommitEx);
            }

            // Close PreparedStatement objects
            try {
                if (stmPlan != null) {
                    stmPlan.close();
                }
                if (stmUpdateHeader != null) {
                    stmUpdateHeader.close();
                }
                if (stmInsertHeader != null) {
                    stmInsertHeader.close();
                }
            } catch (SQLException closeEx) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, "Failed to close PreparedStatements", closeEx);
            }

            // Close connection
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException closeEx) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, "Failed to close connection", closeEx);
            }
        }
    }

    @Override
    public void delete(Plan model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Plan> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Plan get(int id) {
        Plan plan = null;
        try {
            // Truy vấn để lấy thông tin của kế hoạch
            String sql_plan = "SELECT plid, plname, startdate, enddate, did FROM Plans WHERE plid = ?";
            PreparedStatement stm_plan = connection.prepareStatement(sql_plan);
            stm_plan.setInt(1, id);
            ResultSet rs_plan = stm_plan.executeQuery();

            if (rs_plan.next()) {
                plan = new Plan();
                plan.setId(rs_plan.getInt("plid"));
                plan.setName(rs_plan.getString("plname"));
                plan.setStartDate(rs_plan.getDate("startdate"));
                plan.setEndDate(rs_plan.getDate("enddate"));
                // Set department ID or object if you have a Department model
                // Assuming you set only ID for simplicity
                Department d = new Department();
                d.setId(rs_plan.getInt("did"));
                plan.setDept(d);

                // Truy vấn để lấy các `PlanHeader` liên quan đến kế hoạch này
                String sql_headers = "SELECT phid, pid, quantity, estimatedeffort FROM PlanHeaders WHERE plid = ?";
                PreparedStatement stm_headers = connection.prepareStatement(sql_headers);
                stm_headers.setInt(1, id);
                ResultSet rs_headers = stm_headers.executeQuery();

                ArrayList<PlanHeader> headers = new ArrayList<>();
                while (rs_headers.next()) {
                    PlanHeader header = new PlanHeader();
                    header.setId(rs_headers.getInt("phid"));

                    // Tạo đối tượng Product và set ID sản phẩm từ bảng Products
                    Product product = new Product();
                    product.setId(rs_headers.getInt("pid"));
                    header.setProduct(product);

                    header.setQuatity(rs_headers.getInt("quantity"));
                    header.setEstimatedEffort(rs_headers.getFloat("estimatedeffort"));

                    headers.add(header);
                }
                plan.setHeaders(headers);

                rs_headers.close();
                stm_headers.close();
            }

            rs_plan.close();
            stm_plan.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PlanDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return plan;
    }

}
