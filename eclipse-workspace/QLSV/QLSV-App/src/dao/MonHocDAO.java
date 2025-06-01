package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import model.MonHoc;

public class MonHocDAO {
    private Connection conn;
    
    public Vector<Vector<Object>> search(String keyword) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM monhoc WHERE maMH LIKE ? OR tenMH LIKE ? OR maKhoa LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maMH"));
                row.add(rs.getString("tenMH"));
                row.add(rs.getString("soTinChi"));
                row.add(rs.getString("maKhoa"));
                data.add(row);
            }
        }
        return data;
    }


    public MonHocDAO(Connection conn) {
    	
        this.conn = conn;
    }
    

    // Lấy tất cả môn học
    public ArrayList<MonHoc> getAllMonHoc() {
        ArrayList<MonHoc> dsMonHoc = new ArrayList<>();
        String sql = "SELECT * FROM monhoc";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maMon = rs.getString("maMH");
                String tenMon = rs.getString("tenMH");
                String soTinChi = rs.getString("soTinChi");
                String maKhoa = rs.getString("maKhoa");
                dsMonHoc.add(new MonHoc(maMon, tenMon, soTinChi, maKhoa));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMonHoc;
    }

    // Thêm môn học
    public boolean insertMonHoc(MonHoc mh) {
        String sql = "INSERT INTO monhoc (maMH, tenMH, soTinChi, maKhoa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getMaMon());
            stmt.setString(2, mh.getTenMon());
            stmt.setString(3, mh.getSoTinChi());
            stmt.setString(4, mh.getMaKhoa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật môn học
    public boolean updateMonHoc(MonHoc mh) {
        String sql = "UPDATE monhoc SET tenMH = ?, soTinChi = ? , maKhoa = ? WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mh.getTenMon());
            stmt.setString(2, mh.getSoTinChi());
            stmt.setString(3, mh.getMaKhoa());
            stmt.setString(4, mh.getMaMon());
            

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa môn học
    public boolean deleteMonHoc(String maMon) {
        String sql = "DELETE FROM monhoc WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm môn học theo mã
    public MonHoc getMonHocById(String maMon) {
        String sql = "SELECT * FROM monhoc WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMon);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tenMon = rs.getString("tenMH");
                    String maKhoa = rs.getString("maKhoa");
                    String soTinChi = rs.getString("soTinChi");
                    return new MonHoc(maMon, tenMon, soTinChi, maKhoa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}

