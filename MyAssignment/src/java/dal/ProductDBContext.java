/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.resource.Product;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author milo9
 */
public class ProductDBContext extends DBContext<Product> {

    // Phương thức để lấy Product theo phid của PlanHeader
    public Product getProductByPlanHeaderId(int phid) {
        Product product = null;
        String sql = "SELECT p.pid, p.pname, p.description "
                + "FROM PlanHeaders ph "
                + "JOIN Products p ON ph.pid = p.pid "
                + "WHERE ph.phid = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, phid);
            rs = stm.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDescription(rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanHeaderDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return product;
    }

    @Override
    public void insert(Product model) {
        String sql = "INSERT INTO Products (pname, description) VALUES (?, ?)";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, model.getName());
            stm.setString(2, model.getDescription());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Product model) {
        String sql = "UPDATE Products SET pname = ?, description = ? WHERE pid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, model.getName());
            stm.setString(2, model.getDescription());
            stm.setInt(3, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Product model) {
        String sql = "DELETE FROM Products WHERE pid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Product> list() {
        ArrayList<Product> products = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "SELECT pid, pname FROM Products";  // Ví dụ về câu truy vấn

        try {
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                products.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 

        return products;
    }

    @Override
    public Product get(int id) {
        Product product = null;
        String sql = "SELECT p.pid, p.pname, p.description FROM Products p WHERE p.pid = ?";

        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                product.setDescription(rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ProductDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return product;

    }

}
