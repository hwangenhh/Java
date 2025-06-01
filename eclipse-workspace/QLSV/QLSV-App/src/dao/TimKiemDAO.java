package dao;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class TimKiemDAO {
    private Connection conn;

    public TimKiemDAO(Connection conn) {
        this.conn = conn;
    }

    public Vector<String> getColumnNames(String searchType) {
        return switch (searchType) {
            case "Sinh viên" -> new Vector<>(List.of("Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã khoa"));
            case "Giảng viên" -> new Vector<>(List.of("Mã GV", "Họ tên", "Học vị", "Giới tính", "Ngày sinh", "Địa chỉ", "SĐT", "Mã khoa"));
            case "Môn học" -> new Vector<>(List.of("Mã MH", "Tên môn học", "Số tín chỉ", "Mã khoa"));
            case "Khoa" -> new Vector<>(List.of("Mã khoa", "Tên khoa"));
            case "Điểm" -> new Vector<>(List.of("Mã SV", "Tên SV", "Mã MH", "Tên MH", "Điểm"));
            default -> new Vector<>();
        };
    }


    public String buildSearchQuery(String searchType) {
        return switch (searchType) {
            case "Sinh viên" -> "SELECT maSV, tenSV, ngaySinh, gioitinh, diaChi, sdt, maKhoa FROM sinhvien " +
                    "WHERE maSV LIKE ? OR tenSV LIKE ? OR ngaySinh LIKE ? OR gioitinh LIKE ? OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ?";
            case "Giảng viên" -> "SELECT maGV, tenGV, hocVi, gioitinh, ngaySinh, diaChi, sdt, maKhoa FROM giangvien " +
                    "WHERE maGV LIKE ? OR tenGV LIKE ? OR hocVi LIKE ? OR gioitinh LIKE ? OR ngaySinh LIKE ? OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ?";
            case "Môn học" -> "SELECT maMH, tenMH, soTinChi, maKhoa FROM monhoc " +
                    "WHERE maMH LIKE ? OR tenMH LIKE ? OR maKhoa LIKE ?";
            case "Khoa" -> "SELECT maKhoa, tenKhoa FROM khoa " +
                    "WHERE maKhoa LIKE ? OR tenKhoa LIKE ?";
            case "Điểm" -> "SELECT d.maSV, sv.tenSV, d.maMH, mh.tenMH, d.diemSo FROM diem d " +
                    "JOIN sinhvien sv ON d.maSV = sv.maSV " +
                    "JOIN monhoc mh ON d.maMH = mh.maMH " +
                    "WHERE d.maSV LIKE ? OR sv.tenSV LIKE ? OR d.maMH LIKE ? OR mh.tenMH LIKE ?";
            default -> "";
        };
    }

    public Vector<Vector<Object>> search(String searchType, String keyword) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String query = buildSearchQuery(searchType);
        if (query.isEmpty()) return data;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            int paramCount = pstmt.getParameterMetaData().getParameterCount();
            for (int i = 1; i <= paramCount; i++) {
                pstmt.setString(i, "%" + keyword + "%");
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(row);
                }
            }
        }
        return data;
    }
}
