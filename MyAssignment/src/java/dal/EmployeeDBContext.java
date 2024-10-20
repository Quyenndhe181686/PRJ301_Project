/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.resource.Employee;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.resource.Department;
import model.resource.Salary;

/**
 *
 * @author milo9
 */
public class EmployeeDBContext extends DBContext<Employee> {

    @Override
    public void insert(Employee model) {
        PreparedStatement stm_insert = null;
        PreparedStatement stm_getId = null;
        String sql_insert = "Insert into Employees\n"
                + "([ename],"
                + "did,"
                + "phonenumber,"
                + "[address],"
                + "[sid]) \n"
                + "values (?,"
                + "?,"
                + "?,"
                + "?,"
                + "?);";

        String sql_getId = "SELECT @@IDENTITY as eid";
        try {
            connection.setAutoCommit(false);
            stm_insert = connection.prepareStatement(sql_insert);
            stm_insert.setString(1, model.getName());
            stm_insert.setInt(2, model.getDept().getId());
            stm_insert.setString(3, model.getPhoneNumber());
            stm_insert.setString(4, model.getAddress());
            stm_insert.setInt(5, model.getSalary().getId());
            stm_insert.executeUpdate();
            stm_getId = connection.prepareStatement(sql_getId);
            ResultSet rs = stm_getId.executeQuery();
            if (rs.next()) {
                model.setId(rs.getInt("eid"));
            }
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);

            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public void update(Employee model) {
        PreparedStatement stm = null;
        String sql = "UPDATE [dbo].[Employees]\n"
                + "   SET [ename] = ?\n"
                + "      ,[did] = ?\n"
                + "      ,[phonenumber] = ?\n"
                + "      ,[address] = ?\n"
                + "      ,[sid] = ?\n"
                + " WHERE eid = ?";
        try {
            stm = connection.prepareStatement(sql);
            stm.setString(1, model.getName());
            stm.setInt(2, model.getDept().getId());
            stm.setString(3, model.getPhoneNumber());
            stm.setString(4, model.getAddress());
            stm.setInt(5, model.getSalary().getId());
            stm.setInt(6, model.getId());

            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(Employee model) {
        PreparedStatement stm_update = null;
        String sql_update = "delete from Employees where eid= ?";

        try {

            stm_update = connection.prepareStatement(sql_update);
            stm_update.setInt(1, model.getId());
            stm_update.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<Employee> list() {
        PreparedStatement stm = null;
        String sql = "SELECT [eid]\n"
                + "      ,[ename]\n"
                + "      ,[did]\n"
                + "      ,[phonenumber]\n"
                + "      ,[address]\n"
                + "      ,[sid]\n"
                + "  FROM [Employees]";
        ArrayList<Employee> list = new ArrayList<>();

        try {
            stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));
                Department d = new Department();
                d.setId(rs.getInt("did"));
                e.setDept(d);
                e.setPhoneNumber(rs.getString("phonenumber"));
                e.setAddress(rs.getString("address"));
                Salary s = new Salary();
                s.setId(rs.getInt("sid"));
                e.setSalary(s);

                list.add(e);

            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        return list;

    }

    @Override
    public Employee get(int id) {
        PreparedStatement stm = null;
        String sql = "select [eid],[ename],[did],[phonenumber],[address],[sid] from Employees where eid=?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setId(id);
                e.setName(rs.getString("ename"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                e.setDept(d);

                e.setPhoneNumber(rs.getString("phonenumber"));
                e.setAddress(rs.getString("address"));

                Salary s = new Salary();
                s.setId(rs.getInt("sid"));
                e.setSalary(s);

                return e;

            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

}
