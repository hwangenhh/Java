package dao;

import model.Diem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dao.DatabaseConnection;

public class DiemDAO {
    private Connection conn;

    // Constructor mặc định lấy connection từ DatabaseConnection
    public DiemDAO() {
        this.conn = DatabaseConnection.getConnection();
        if (this.conn == null) {
            System.err.println("Không thể tạo kết nối DB trong DiemDAO!");
        } else {
            System.out.println("Kết nối DB thành công trong DiemDAO");
        }
    }

    // Constructor truyền connection ngoài vào
    public DiemDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Connection được truyền vào DiemDAO là null!");
        }
    }

    // Lấy tất cả điểm
    public List<Diem> getAll() {
        List<Diem> list = new ArrayList<>();
        if (conn == null) {
            System.err.println("Lỗi: Kết nối CSDL null trong getAll()");
            return list;
        }

        String sql = "SELECT d.maSV, sv.tenSV, d.maMH, d.diemSo " +
                     "FROM diem d JOIN sinhvien sv ON d.maSV = sv.maSV";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Diem(
                        rs.getString("maSV"),
                        rs.getString("tenSV"),
                        rs.getString("maMH"),
                        rs.getFloat("diemSo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getAll(): " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Thêm điểm mới
    public boolean insert(Diem diem) {
        if (conn == null) {
            System.err.println("Lỗi: Kết nối CSDL null trong insert()");
            return false;
        }

        String sql = "INSERT INTO diem (maSV, maMH, diemSo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, diem.getMaSV());
            stmt.setString(2, diem.getMaMH());
            if (diem.getDiemSo() == null) {
                stmt.setNull(3, Types.FLOAT);
            } else {
                stmt.setFloat(3, diem.getDiemSo());
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong insert(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateDiem(Diem diem) {
        if (conn == null) return false;
        String sql = "UPDATE diem SET diemSo = ? WHERE maSV = ? AND maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (diem.getDiemSo() == null) {
                stmt.setNull(1, Types.FLOAT);
            } else {
                stmt.setFloat(1, diem.getDiemSo());
            }
            stmt.setString(2, diem.getMaSV());
            stmt.setString(3, diem.getMaMH());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean deleteDiem(String maSV, String maMH) {
        if (conn == null) return false;
        String sql = "DELETE FROM diem WHERE maSV = ? AND maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            stmt.setString(2, maMH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    // Lấy tên sinh viên theo mã SV
    public String getTenSinhVien(String maSV) {
        if (conn == null) {
            System.err.println("Lỗi: Kết nối CSDL null trong getTenSinhVien()");
            return "Không rõ";
        }

        String sql = "SELECT tenSV FROM sinhvien WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenSV");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getTenSinhVien(): " + e.getMessage());
            e.printStackTrace();
        }
        return "Không rõ";
    }
    public Vector<Vector<Object>> search(String keyword) throws SQLException {
        Vector<Vector<Object>> result = new Vector<>();

        String sql = "SELECT * FROM diem WHERE maSV LIKE ? OR maMH LIKE ? OR CAST(diemSo AS CHAR) LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maSV"));
                row.add(rs.getString("maMH"));
                row.add(rs.getFloat("diemSo"));
                result.add(row);
            }
        }

        return result;
    }
    public Vector<String> getColumnNames() {
        Vector<String> columns = new Vector<>();
        columns.add("Mã Sinh Viên");
        columns.add("Mã Môn Học");
        columns.add("Điểm");
        return columns;
    }

    // Nếu cần, thêm phương thức đóng connection (nếu không dùng connection chung)
    public void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Đã đóng kết nối DB trong DiemDAO");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối DB: " + e.getMessage());
            }
        }
    }
}
