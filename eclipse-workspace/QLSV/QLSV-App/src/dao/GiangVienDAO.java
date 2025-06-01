package dao;

import model.GiangVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GiangVienDAO {
    private Connection conn;

    public GiangVienDAO(Connection conn) {
        this.conn = conn;
    }

    public List<GiangVien> getAllGiangVien() {
        List<GiangVien> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM giangvien");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                GiangVien gv = new GiangVien();
                gv.setMaGV(rs.getString("maGV"));
                gv.setTenGV(rs.getString("tenGV"));
                gv.setNgaySinh(rs.getDate("ngaySinh"));
                gv.setGioiTinh(rs.getString("gioiTinh"));
                gv.setDiaChi(rs.getString("diaChi"));
                gv.setSdt(rs.getString("sdt"));
                gv.setMaKhoa(rs.getString("maKhoa"));
                gv.setHocVi(rs.getString("hocVi"));
                gv.setChucVu(rs.getString("chucVu"));
                gv.setLuong(rs.getDouble("luong"));
                
                
                list.add(gv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addGiangVien(GiangVien gv) {
        String sql = "INSERT INTO giangvien (maGV, tenGV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa, hocVi, chucVu, luong) VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gv.getMaGV());
            stmt.setString(2, gv.getTenGV());
            stmt.setDate(3, gv.getNgaySinh() != null ? new java.sql.Date(gv.getNgaySinh().getTime()) : null);
            stmt.setString(4, gv.getGioiTinh());
            stmt.setString(5, gv.getDiaChi());
            stmt.setString(6, gv.getSdt());
            stmt.setString(7, gv.getMaKhoa());
            stmt.setString(8, gv.getHocVi());
            stmt.setString(9, gv.getChucVu());
            stmt.setDouble(10, gv.getLuong());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGiangVien(GiangVien gv) {
        String sql = "UPDATE giangvien SET tenGV=?, ngaySinh=?, gioiTinh=?, diaChi=?, sdt=?, maKhoa=?, hocVi=?, chucVu=?, luong=? WHERE maGV=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gv.getTenGV());

            // Chuyển Date sang java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(gv.getNgaySinh().getTime());
            ps.setDate(2, sqlDate);

            ps.setString(3, gv.getGioiTinh());
            ps.setString(4, gv.getDiaChi());
            ps.setString(5, gv.getSdt());
            ps.setString(6, gv.getMaKhoa());
            ps.setString(7, gv.getHocVi());
            ps.setString(8, gv.getChucVu());
            ps.setDouble(9, gv.getLuong());

            ps.setString(10, gv.getMaGV());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGiangVien(String maGV) {
        String sql = "DELETE FROM giangvien WHERE maGV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maGV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Vector<Vector<Object>> search(String keyword) throws SQLException {
        Vector<Vector<Object>> result = new Vector<>();

        String sql = "SELECT * FROM giangvien WHERE maGV LIKE ? OR tenGV LIKE ? OR gioiTinh LIKE ? " +
                     "OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ? OR hocVi LIKE ? OR chucVu LIKE ? OR luong LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 9; i++) {
                stmt.setString(i, searchPattern);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("maGV"));
                row.add(rs.getString("tenGV"));
                row.add(rs.getDate("ngaySinh"));
                row.add(rs.getString("gioiTinh"));
                row.add(rs.getString("diaChi"));
                row.add(rs.getString("sdt"));
                row.add(rs.getString("maKhoa"));
                row.add(rs.getString("hocVi"));
                row.add(rs.getString("chucVu"));
                row.add(rs.getDouble("luong"));
                result.add(row);
            }
        }

        return result;
    }
    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Mã GV");
        columnNames.add("Tên GV");
        columnNames.add("Ngày Sinh");
        columnNames.add("Giới Tính");
        columnNames.add("Địa Chỉ");
        columnNames.add("SĐT");
        columnNames.add("Mã Khoa");
        columnNames.add("Học Vị");
        columnNames.add("Chức Vụ");
        columnNames.add("Lương");
        return columnNames;
    }
    
    public GiangVien getGiangVienByMa(String maGV) {
        String sql = "SELECT * FROM giangvien WHERE maGV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maGV);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                GiangVien gv = new GiangVien();
                gv.setMaGV(rs.getString("maGV"));
                gv.setTenGV(rs.getString("tenGV"));
                gv.setNgaySinh(rs.getDate("ngaySinh"));
                gv.setGioiTinh(rs.getString("gioiTinh"));
                gv.setDiaChi(rs.getString("diaChi"));
                gv.setSdt(rs.getString("sdt"));
                gv.setMaKhoa(rs.getString("maKhoa"));
                gv.setHocVi(rs.getString("hocVi"));
                gv.setChucVu(rs.getString("chucVu"));
                gv.setLuong(rs.getDouble("luong"));
                
                
                rs.close();
                return gv;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//	public Object search1(String keyword) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public List<GiangVien> searchGiangVien(String keyword) {
        List<GiangVien> list = new ArrayList<>();
        String sql = "SELECT * FROM giangvien WHERE maGV LIKE ? OR tenGV LIKE ? OR gioiTinh LIKE ? " +
                "OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ? OR hocVi LIKE ? OR chucVu LIKE ? OR luong LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 9; i++) {
                stmt.setString(i, searchPattern);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    GiangVien gv = new GiangVien();
                    gv.setMaGV(rs.getString("maGV"));
                    gv.setTenGV(rs.getString("tenGV"));
                    gv.setNgaySinh(rs.getDate("ngaySinh"));
                    gv.setGioiTinh(rs.getString("gioiTinh"));
                    gv.setDiaChi(rs.getString("diaChi"));
                    gv.setSdt(rs.getString("sdt"));
                    gv.setMaKhoa(rs.getString("maKhoa"));
                    gv.setHocVi(rs.getString("hocVi"));
                    gv.setChucVu(rs.getString("chucVu"));
                    gv.setLuong(rs.getDouble("luong"));
                    list.add(gv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}