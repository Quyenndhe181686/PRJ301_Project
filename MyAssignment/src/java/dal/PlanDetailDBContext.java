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

    public List<PlanDetail> getDetailsByPlanId(int planId) {
        List<PlanDetail> details = new ArrayList<>();
        String sql = "SELECT pd.date, pd.quantity, pd.sid, pd.phid, ph.pid, ph.plid, ph.quantity as ph_quantity, ph.estimatedeffort, p.pname "
                + "FROM PlanDetails pd "
                + "JOIN PlanHeaders ph ON pd.phid = ph.phid "
                + "JOIN Products p ON ph.pid = p.pid "
                + "WHERE ph.plid = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = connection.prepareStatement(sql);
            stm.setInt(1, planId);
            rs = stm.executeQuery();

            while (rs.next()) {
                // Tạo đối tượng PlanDetail
                PlanDetail planDetail = new PlanDetail();
                planDetail.setDate(rs.getDate("date"));
                planDetail.setQuantity(rs.getInt("quantity"));

                // Tạo và thiết lập đối tượng Shift
                Shift shift = new Shift();
                shift.setId(rs.getInt("sid"));
                planDetail.setShift(shift);

                // Tạo và thiết lập đối tượng PlanHeader
                PlanHeader planHeader = new PlanHeader();
                planHeader.setId(rs.getInt("phid"));
                planHeader.setQuantity(rs.getInt("ph_quantity"));
                planHeader.setEstimatedEffort(rs.getFloat("estimatedeffort"));

                // Tạo và thiết lập đối tượng Product
                Product product = new Product();
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("pname"));
                planHeader.setProduct(product);

                planDetail.setPlanHeader(planHeader);

                // Thêm PlanDetail vào danh sách
                details.add(planDetail);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return details;
    }

    public void updatePlanDetails(ArrayList<PlanDetail> planDetails) {
        String sqlUpdate = "UPDATE PlanDetails SET quantity = ? WHERE phid = ? AND sid = ? AND date = ?";
        String sqlInsert = "INSERT INTO PlanDetails (phid, sid, date, quantity) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            PreparedStatement stmUpdate = connection.prepareStatement(sqlUpdate);
            PreparedStatement stmInsert = connection.prepareStatement(sqlInsert);

            for (PlanDetail detail : planDetails) {
                stmUpdate.setInt(1, detail.getQuantity());
                stmUpdate.setInt(2, detail.getPlanHeader().getId());
                stmUpdate.setInt(3, detail.getShift().getId());
                stmUpdate.setDate(4, new java.sql.Date(detail.getDate().getTime()));

                int rowsAffected = stmUpdate.executeUpdate();
                if (rowsAffected == 0) { // If no rows updated, insert new row
                    stmInsert.setInt(1, detail.getPlanHeader().getId());
                    stmInsert.setInt(2, detail.getShift().getId());
                    stmInsert.setDate(3, new java.sql.Date(detail.getDate().getTime()));
                    stmInsert.setInt(4, detail.getQuantity());
                    stmInsert.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

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

    public void updatePlanDetail(int phId, java.sql.Date date, int shiftId, int quantity) {
        String sql = "UPDATE PlanDetails SET quantity = ? WHERE phid = ? AND date = ? AND sid = ?";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, quantity);
            stm.setInt(2, phId);
            stm.setDate(3, date);
            stm.setInt(4, shiftId);

            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlanDetailDBContext.class.getName()).log(Level.SEVERE, null, ex);
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

    public void insertOrUpdatePlanDetails(List<PlanDetail> planDetails) {
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;

        try {
            connection.setAutoCommit(false); // Bắt đầu transaction

            // Câu lệnh kiểm tra xem `PlanDetail` có tồn tại không
            String checkSql = "SELECT COUNT(*) FROM PlanDetails WHERE phid = ? AND sid = ? AND date = ?";
            checkStmt = connection.prepareStatement(checkSql);

            // Câu lệnh `UPDATE`
            String updateSql = "UPDATE PlanDetails SET quantity = ? WHERE phid = ? AND sid = ? AND date = ?";
            updateStmt = connection.prepareStatement(updateSql);

            // Câu lệnh `INSERT`
            String insertSql = "INSERT INTO PlanDetails (phid, sid, date, quantity) VALUES (?, ?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSql);

            for (PlanDetail detail : planDetails) {
                // Thiết lập tham số cho câu lệnh kiểm tra
                checkStmt.setInt(1, detail.getPlanHeader().getId());
                checkStmt.setInt(2, detail.getShift().getId());
                checkStmt.setDate(3, detail.getDate());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu tồn tại, thực hiện `UPDATE`
                    updateStmt.setInt(1, detail.getQuantity());
                    updateStmt.setInt(2, detail.getPlanHeader().getId());
                    updateStmt.setInt(3, detail.getShift().getId());
                    updateStmt.setDate(4, detail.getDate());
                    updateStmt.addBatch();
                } else {
                    // Nếu không tồn tại, thực hiện `INSERT`
                    insertStmt.setInt(1, detail.getPlanHeader().getId());
                    insertStmt.setInt(2, detail.getShift().getId());
                    insertStmt.setDate(3, detail.getDate());
                    insertStmt.setInt(4, detail.getQuantity());
                    insertStmt.addBatch();
                }
            }

            // Thực hiện batch `UPDATE` và `INSERT`
            updateStmt.executeBatch();
            insertStmt.executeBatch();

            // Commit transaction
            connection.commit();

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback nếu có lỗi xảy ra
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                if (checkStmt != null) {
                    checkStmt.close();
                }
                if (updateStmt != null) {
                    updateStmt.close();
                }
                if (insertStmt != null) {
                    insertStmt.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    public void deleteDetails(List<PlanDetail> detailsToDelete) {
        PreparedStatement deleteStmt = null;

        try {
            connection.setAutoCommit(false); // Bắt đầu transaction

            // Câu lệnh DELETE SQL
            String deleteSql = "DELETE FROM PlanDetails WHERE phid = ? AND sid = ? AND date = ?";
            deleteStmt = connection.prepareStatement(deleteSql);

            // Duyệt qua danh sách các PlanDetail cần xóa
            for (PlanDetail detail : detailsToDelete) {
                deleteStmt.setInt(1, detail.getPlanHeader().getId());
                deleteStmt.setInt(2, detail.getShift().getId());
                deleteStmt.setDate(3, detail.getDate());
                deleteStmt.addBatch(); // Thêm vào batch
            }

            // Thực hiện batch delete
            deleteStmt.executeBatch();
            connection.commit(); // Commit transaction

        } catch (SQLException ex) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback nếu có lỗi xảy ra
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                if (deleteStmt != null) {
                    deleteStmt.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

}
