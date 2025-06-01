package dao;

import model.Khoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class KhoaDAO {
    private Connection conn;

    public KhoaDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Khoa> getAllKhoa() {
        List<Khoa> list = new ArrayList<>();
        String sql = "SELECT * FROM khoa";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Khoa(rs.getString("maKhoa"), rs.getString("tenKhoa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addKhoa(Khoa khoa) {
        String sql = "INSERT INTO khoa (maKhoa, tenKhoa) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, khoa.getMaKhoa());
            stmt.setString(2, khoa.getTenKhoa());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKhoa(Khoa khoa) {
        String sql = "UPDATE khoa SET tenKhoa = ? WHERE maKhoa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, khoa.getTenKhoa());
            stmt.setString(2, khoa.getMaKhoa());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKhoa(String maKhoa) {
        String sql = "DELETE FROM khoa WHERE maKhoa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKhoa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Vector<Vector<Object>> search(String keyword) throws SQLException {
        Vector<Vector<Object>> result = new Vector<>();

        String sql = "SELECT * FROM khoa WHERE maKhoa LIKE ? OR tenKhoa LIKE ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 2; i++) {
                stmt.setString(i, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maKhoa"));
                row.add(rs.getString("tenKhoa"));
                
                result.add(row);
            }
        }

        return result;
    }
    public Vector<String> getColumnNames() {
        Vector<String> columns = new Vector<>();
        columns.add("Mã Khoa");
        columns.add("Tên Khoa");
        
        return columns;
    }
}
