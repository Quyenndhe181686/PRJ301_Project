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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.resource.PlanDetail;
import model.resource.PlanHeader;
import model.resource.Product;
import model.resource.Shift;

/**
 *
 * @author milo9
 */
public class PlanDetailDBContext extends DBContext<PlanDetail> {

    public List<PlanDetail> getPlanDetailsByPlanId(int planId) {
        ProductDBContext db = new ProductDBContext();
        List<PlanDetail> planDetails = new ArrayList<>();
        String sql = "SELECT pd.pdid, pd.phid, pd.sid, pd.date, pd.quantity "
                + "FROM PlanDetails pd "
                + "JOIN PlanHeaders ph ON pd.phid = ph.phid "
                + "WHERE ph.plid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, planId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                PlanDetail planDetail = new PlanDetail();
                planDetail.setId(rs.getInt("pdid"));

                PlanHeader planHeader = new PlanHeader();
                planHeader.setId(rs.getInt("phid"));
                Product product = db.getProductByPlanHeaderId(rs.getInt("phid"));                
                planHeader.setProduct(product);
                planDetail.setPlanHeader(planHeader);

                Shift shift = new Shift();
                shift.setId(rs.getInt("sid"));
                planDetail.setShift(shift);

                planDetail.setDate(rs.getDate("date"));
                planDetail.setQuantity(rs.getInt("quantity"));

                planDetails.add(planDetail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return planDetails;
    }

    public void insertPlanDetails(ArrayList<PlanDetail> planDetails) {
        String sql = "INSERT INTO PlanDetails (phid, sid, date, quantity) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement(sql);

            for (PlanDetail detail : planDetails) {
                stm.setInt(1, detail.getPlanHeader().getId());
                stm.setInt(2, detail.getShift().getId());
                stm.setDate(3, new java.sql.Date(detail.getDate().getTime()));
                stm.setInt(4, detail.getQuantity());
                stm.addBatch();
            }
            stm.executeBatch();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void insert(PlanDetail planDetail) {
        PreparedStatement stm = null;
        String sql = "INSERT INTO[PlanDetails]\n"
                + "           ([phid]\n"
                + "           ,[sid]\n"
                + "           ,[date]\n"
                + "           ,[quantity])\n"
                + "     VALUES\n"
                + "           (?,?,?,?)";
        try {
            stm = connection.prepareStatement(sql);

            // add vaid 
            stm.setInt(1, planDetail.getPlanHeader().getId());
            stm.setInt(2, planDetail.getShift().getId());
            stm.setDate(3, (Date) planDetail.getDate());
            stm.setInt(4, planDetail.getQuantity());

            stm.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void update(PlanDetail model) {
        PreparedStatement stm = null;
        String sql = "UPDATE [PlanDetails]\n"
                + "   SET [phid] = ? \n"
                + "      ,[sid] = ? \n"
                + "      ,[date] = ? \n"
                + "      ,[quantity] = ? \n"
                + " WHERE [pdid] = ?";

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getPlanHeader().getId());
            stm.setInt(2, model.getShift().getId());
            stm.setDate(3, (Date) model.getDate());
            stm.setInt(4, model.getQuantity());
            stm.setInt(5, model.getId());

            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void delete(PlanDetail model) {
        String sql = "DELETE FROM PlanDetails WHERE pdid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, model.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<PlanDetail> list() {
        ArrayList<PlanDetail> planDetails = new ArrayList<>();
        String sql = "SELECT pdid, phid, sid, date, quantity FROM PlanDetails";

        try (PreparedStatement stm = connection.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                PlanDetail pd = new PlanDetail();
                pd.setId(rs.getInt("pdid"));

                PlanHeader ph = new PlanHeader();
                ph.setId(rs.getInt("phid"));
                pd.setPlanHeader(ph);

                Shift s = new Shift();
                s.setId(rs.getInt("sid"));
                pd.setShift(s);

                pd.setDate(rs.getDate("date"));
                pd.setQuantity(rs.getInt("quantity"));

                planDetails.add(pd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return planDetails;
    }

    @Override
    public PlanDetail get(int id) {
        PlanDetail pd = new PlanDetail();
        String sql = "select * from PlanDetails where pdid = ?";
        PreparedStatement stm = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                pd.setId(id);

                PlanHeader ph = new PlanHeader();
                ph.setId(rs.getInt("phid"));
                pd.setPlanHeader(ph);

                Shift s = new Shift();
                s.setId(rs.getInt("sid"));
                pd.setShift(s);

                pd.setDate(rs.getDate("date"));

                pd.setQuantity(rs.getInt("quantity"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return pd;
    }

}
