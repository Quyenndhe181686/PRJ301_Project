/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author milo9
 */
package dal;

import model.resource.Attendant;
import model.resource.WorkAssignment;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttendanceDBContext extends DBContext<Attendant> {

    @Override
    public void insert(Attendant attendant) {
        String sql = "INSERT INTO Attendances (wadid, actualQuantity, alpha, note) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, attendant.getWorkAssignment().getId()); // Lấy ID WorkAssignment
            stm.setFloat(2, attendant.getActualQuantity());
            stm.setFloat(3, attendant.getAlpha());
            stm.setString(4, attendant.getNote());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Attendant> getAttendanceByWorkAssignmentId(int wadid) {
        ArrayList<Attendant> attendants = new ArrayList<>();
        String sql = "SELECT * FROM Attendances WHERE wadid = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, wadid);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Attendant attendant = new Attendant();
                attendant.setId(rs.getInt("atid"));

                WorkAssignment workAssignment = new WorkAssignment();
                workAssignment.setId(rs.getInt("wadid"));
                attendant.setWorkAssignment(workAssignment);

                attendant.setActualQuantity(rs.getFloat("actualQuantity"));
                attendant.setAlpha(rs.getFloat("alpha"));
                attendant.setNote(rs.getString("note"));

                attendants.add(attendant);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        return attendants;
    }

    @Override
    public void update(Attendant model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Attendant model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Attendant> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Attendant get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void saveOrUpdateAttendances(ArrayList<Attendant> attendents) {
        for (Attendant attendent : attendents) {
            insert(attendent);
        }
    }
}
