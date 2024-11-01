/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.resource.Shift;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author milo9
 */
public class ShiftDBContext extends DBContext<Shift> {

    @Override
    public void insert(Shift model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Shift model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Shift model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Shift> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Shift get(int shiftId) {
        Shift shift = null;
        String sql = "SELECT sid, sname, starttime, endtime "
                + "FROM Shifts "
                + "WHERE sid = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, shiftId);
            rs = stm.executeQuery();

            if (rs.next()) {
                shift = new Shift();
                shift.setId(rs.getInt("sid"));
                shift.setName(rs.getString("sname"));
                shift.setStartTime(rs.getTime("starttime"));
                shift.setEndTime(rs.getTime("endtime"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShiftDBContext.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ShiftDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return shift;
    }
}


