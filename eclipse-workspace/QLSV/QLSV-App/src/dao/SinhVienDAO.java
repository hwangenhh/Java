package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import model.SinhVien;

public class SinhVienDAO {
    private Connection conn;

    public SinhVienDAO(Connection conn) {
        this.conn = conn;
    }

    public List<SinhVien> getAllSinhVien() throws SQLException {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMaSV(rs.getString("maSV"));
                sv.setTenSV(rs.getString("tenSV"));
                sv.setNgaySinh(rs.getDate("ngaySinh"));
                sv.setGioiTinh(rs.getString("gioiTinh"));
                sv.setDiaChi(rs.getString("diaChi"));
                sv.setSdt(rs.getString("sdt"));
                sv.setMaKhoa(rs.getString("maKhoa"));
                sv.setLopHanhChinh(rs.getString("lopHanhChinh"));
                sv.setKhoaHoc(rs.getString("khoaHoc"));

                list.add(sv);
            }
        }
        return list;
    }

    public boolean insertSinhVien(SinhVien sv) throws SQLException {
        String sql = "INSERT INTO sinhvien (maSV, tenSV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa, lopHanhChinh, khoaHoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sv.getMaSV());
            stmt.setString(2, sv.getTenSV());
            stmt.setDate(3, sv.getNgaySinh());
            stmt.setString(4, sv.getGioiTinh());
            stmt.setString(5, sv.getDiaChi());
            stmt.setString(6, sv.getSdt());
            stmt.setString(7, sv.getMaKhoa());
            stmt.setString(8, sv.getLopHanhChinh());
            stmt.setString(9, sv.getKhoaHoc());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateSinhVien(SinhVien sv) throws SQLException {
        String sql = "UPDATE sinhvien SET tenSV = ?, ngaySinh = ?, gioiTinh = ?, diaChi = ?, sdt = ?, maKhoa = ?, lopHanhChinh = ?, khoaHoc = ? WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sv.getTenSV());
            stmt.setDate(2, sv.getNgaySinh());
            stmt.setString(3, sv.getGioiTinh());
            stmt.setString(4, sv.getDiaChi());
            stmt.setString(5, sv.getSdt());
            stmt.setString(6, sv.getMaKhoa());
            stmt.setString(7, sv.getLopHanhChinh());
            stmt.setString(8, sv.getKhoaHoc());
            stmt.setString(9, sv.getMaSV()); // maSV là điều kiện WHERE
            return stmt.executeUpdate() > 0;
        }
    }

    public Vector<Vector<Object>> search(String keyword) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT * FROM sinhvien WHERE maSV LIKE ? OR tenSV LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("maSV"));
                    row.add(rs.getString("tenSV"));
                    row.add(rs.getDate("ngaySinh"));
                    row.add(rs.getString("gioiTinh"));
                    row.add(rs.getString("diaChi"));
                    row.add(rs.getString("sdt")); // <-- BỔ SUNG DÒNG NÀY
                    row.add(rs.getString("maKhoa"));
                    row.add(rs.getString("lopHanhChinh"));
                    row.add(rs.getString("khoaHoc"));
                    data.add(row);
                }
            }
        }

        return data;
    }

    public Vector<String> getColumnNames() {
        Vector<String> columns = new Vector<>();
        columns.add("Mã SV");
        columns.add("Họ Tên");
        columns.add("Ngày Sinh");
        columns.add("Giới Tính");
        columns.add("Địa Chỉ");
        columns.add("Khoa");
        columns.add("Lớp");
        columns.add("Khoá");
        return columns;
    }


    public boolean deleteSinhVien(String maSV) throws SQLException {
        String sql = "DELETE FROM sinhvien WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            return stmt.executeUpdate() > 0;
        }
    }
}
