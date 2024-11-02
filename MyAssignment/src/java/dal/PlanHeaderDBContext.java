package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.resource.PlanHeader;
import model.resource.Product;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanHeaderDBContext extends DBContext<PlanHeader> {

    public ArrayList<PlanHeader> getHeadersByPlanId(int planId) {
        ArrayList<PlanHeader> headers = new ArrayList<>();
        String sql = "SELECT ph.phid, ph.pid, ph.quantity, ph.estimatedeffort "
                + "FROM PlanHeaders ph "
                + "WHERE ph.plid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, planId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                PlanHeader header = new PlanHeader();
                header.setId(rs.getInt("phid"));

                // Tạo đối tượng Product và thiết lập ID sản phẩm từ bảng Products
                Product product = new Product();
                product.setId(rs.getInt("pid"));
                header.setProduct(product);

                header.setQuantity(rs.getInt("quantity"));
                header.setEstimatedEffort(rs.getFloat("estimatedeffort"));

                headers.add(header);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        return headers;
    }

    // Phương thức để lấy số lượng của sản phẩm cho một kế hoạch cụ thể
    public int getQuantityForPlan(int planId, int productId) {
        int quantity = 0;
        String sql = "SELECT quantity FROM PlanHeaders WHERE plid = ? AND pid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, planId);
            stm.setInt(2, productId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                quantity = rs.getInt("quantity");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return quantity;
    }

    // Phương thức để lấy estimated effort của sản phẩm cho một kế hoạch cụ thể
    public float getEffortForPlan(int planId, int productId) {
        float effort = 0;
        String sql = "SELECT estimatedeffort FROM PlanHeaders WHERE plid = ? AND pid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, planId);
            stm.setInt(2, productId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                effort = rs.getFloat("estimatedeffort");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return effort;
    }
// Phương thức để lấy PlanHeader dựa vào productId và planId

    public PlanHeader getPlanHeaderByProductAndPlan(int productId, int planId) {
        PlanHeader planHeader = null;
        String sql = "SELECT phid, pid, plid, quantity, estimatedeffort "
                + "FROM PlanHeaders "
                + "WHERE pid = ? AND plid = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, productId);
            stm.setInt(2, planId);
            rs = stm.executeQuery();

            if (rs.next()) {
                planHeader = new PlanHeader();
                planHeader.setId(rs.getInt("phid"));

                Product product = new Product();
                product.setId(rs.getInt("pid"));
                planHeader.setProduct(product);

                planHeader.setQuantity(rs.getInt("quantity"));
                planHeader.setEstimatedEffort(rs.getFloat("estimatedeffort"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return planHeader;
    }

    public List<Product> getProductsByPlanId(int planId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.pid, p.pname, p.description FROM PlanHeaders ph "
                + "JOIN Products p ON ph.pid = p.pid "
                + "WHERE ph.plid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDescription(rs.getString("description"));
                products.add(product);
            }

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, e);
        }

        return products;
    }

    @Override
    public void insert(PlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(PlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(PlanHeader model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<PlanHeader> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PlanHeader get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
