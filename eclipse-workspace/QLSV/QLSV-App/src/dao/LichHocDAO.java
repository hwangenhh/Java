
package dao;

import model.LichHoc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LichHocDAO {
    private Connection conn;

    public LichHocDAO(Connection conn) {
        this.conn = conn;
    }

    public List<LichHoc> getAllLichHoc() throws SQLException {
        List<LichHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM lichhoc";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new LichHoc(
                        rs.getString("maLichHoc"),
                        rs.getString("maMH"),
                        rs.getString("maGV"),
                        rs.getString("thoiGian"),
                        rs.getString("phongHoc")
                ));
            }
        }
        return list;
    }

    public List<LichHoc> searchByMaMH(String maMH) throws SQLException {
        List<LichHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM lichhoc WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new LichHoc(
                            rs.getString("maLichHoc"),
                            rs.getString("maMH"),
                            rs.getString("maGV"),
                            rs.getString("thoiGian"),
                            rs.getString("phongHoc")
                    ));
                }
            }
        }
        return list;
    }

    public boolean insertLichHoc(LichHoc lichHoc) throws SQLException {
    	String sql = "INSERT INTO lichhoc (maLichHoc, maMH, maGV, thoiGian, phongHoc) VALUES (?, ?, ?, ?, ?)";
    	
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setString(1, lichHoc.getMaLichHoc());
            stmt.setString(2, lichHoc.getMaMH());
            stmt.setString(3, lichHoc.getMaGV());
            stmt.setString(4, lichHoc.getThoiGian());
            stmt.setString(5, lichHoc.getPhongHoc());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateLichHoc(LichHoc lichHoc) throws SQLException {
        String sql = "UPDATE lichhoc SET maMH=?, maGV=?, thoiGian=?, phongHoc=? WHERE maLichHoc=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lichHoc.getMaMH());
            stmt.setString(2, lichHoc.getMaGV());
            stmt.setString(3, lichHoc.getThoiGian());
            stmt.setString(4, lichHoc.getPhongHoc());
            stmt.setString(5, lichHoc.getMaLichHoc());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteLichHoc(String maLichHoc) throws SQLException {
        String sql = "DELETE FROM lichhoc WHERE maLichHoc = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maLichHoc);
        int affected = ps.executeUpdate();
        return affected > 0;
    }


    public Vector<String> getColumnNames() {
        Vector<String> columns = new Vector<>();
        columns.add("Mã lịch học");
        columns.add("Mã môn học");
        columns.add("Mã giảng viên");
        columns.add("Thời gian");
        columns.add("Phòng học");
        return columns;
    }
}
