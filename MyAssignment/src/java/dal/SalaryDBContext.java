/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.resource.Salary;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author milo9
 */
public class SalaryDBContext extends DBContext<Salary>{

    @Override
    public void insert(Salary model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Salary model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Salary model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Salary> list() {
         ArrayList<Salary> salarys = new ArrayList<>();
         PreparedStatement stm = null;
         String sql = "select [sid],[slevel],[salary] from Salaries;";
         
        try {
            stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while(rs.next()){
                Salary salary = new Salary();
                salary.setId(rs.getInt("sid"));
                salary.setLevel(rs.getString("slevel"));
                salary.setSalary(rs.getDouble("salary"));
                
                salarys.add(salary);
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
         finally{
             try {
                 stm.close();
                 connection.close();
             } catch (SQLException ex) {
                 Logger.getLogger(SalaryDBContext.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        return salarys;
    }

    @Override
    public Salary get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
