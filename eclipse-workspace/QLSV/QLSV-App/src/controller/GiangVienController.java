package controller;

import dao.GiangVienDAO;
import model.GiangVien;
import view.GiangVienView;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GiangVienController {
    private GiangVienDAO giangVienDAO;
    private GiangVienView giangVienView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GiangVienController(GiangVienDAO giangVienDAO, GiangVienView giangVienView) {
        this.giangVienDAO = giangVienDAO;
        this.giangVienView = giangVienView;

        // Khởi tạo listeners cho view
        this.giangVienView.setAddButtonListener(e -> handleAddGiangVien());
        this.giangVienView.setUpdateButtonListener(e -> handleUpdateGiangVien());
        this.giangVienView.setDeleteButtonListener(e -> handleDeleteGiangVien());
        this.giangVienView.setClearButtonListener(e -> giangVienView.clearFields());
        this.giangVienView.setTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });

        // Tải dữ liệu ban đầu
        loadGiangVienData();
    }

    public void loadGiangVienData() {
        List<GiangVien> giangVienList = giangVienDAO.getAllGiangVien();
        giangVienView.displayGiangVienList(giangVienList);
    }

    private void handleAddGiangVien() {
        GiangVien gv = getGiangVienFromView();
        if (gv == null) return;

        if (gv.getMaGV().isEmpty()) {
            JOptionPane.showMessageDialog(giangVienView, "Mã GV không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (giangVienDAO.addGiangVien(gv)) {
            loadGiangVienData();
            giangVienView.clearFields();
            JOptionPane.showMessageDialog(giangVienView, "Thêm giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(giangVienView, "Thêm giảng viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateGiangVien() {
        int selectedRow = giangVienView.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(giangVienView, "Vui lòng chọn giảng viên cần cập nhật!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        GiangVien gv = getGiangVienFromView();
        if (gv == null) return;

        if (giangVienDAO.updateGiangVien(gv)) {
            loadGiangVienData();
            giangVienView.clearFields();
            JOptionPane.showMessageDialog(giangVienView, "Cập nhật giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(giangVienView, "Cập nhật giảng viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteGiangVien() {
        int selectedRow = giangVienView.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(giangVienView, "Vui lòng chọn giảng viên cần xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maGV = giangVienView.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(giangVienView,
                "Bạn có chắc chắn muốn xóa giảng viên có mã " + maGV + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (giangVienDAO.deleteGiangVien(maGV)) {
                loadGiangVienData();
                giangVienView.clearFields();
                JOptionPane.showMessageDialog(giangVienView, "Xóa giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(giangVienView, "Xóa giảng viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleSearchGiangVien(String keyword) {
        // Nếu DAO chưa có searchGiangVien trả về List<GiangVien> thì dùng cách này:
        // Vector<Vector<Object>> results = giangVienDAO.search(keyword);
        // giangVienView.displayDataVector(results, giangVienDAO.getColumnNames());

        // Nếu bạn muốn dùng List<GiangVien>, nên cài đặt trong DAO.
        List<GiangVien> result = giangVienDAO.searchGiangVien(keyword);
        giangVienView.displayGiangVienList(result);
    }

    private void handleTableSelection() {
        int selectedRow = giangVienView.getSelectedRow();
        if (selectedRow >= 0) {
            String maGV = giangVienView.getValueAt(selectedRow, 0).toString();
            String tenGV = giangVienView.getValueAt(selectedRow, 1).toString();
            String ngaySinh = giangVienView.getValueAt(selectedRow, 2).toString();
            String gioiTinh = giangVienView.getValueAt(selectedRow, 3).toString();
            String diaChi = giangVienView.getValueAt(selectedRow, 4).toString();
            String sdt = giangVienView.getValueAt(selectedRow, 5).toString();
            String maKhoa = giangVienView.getValueAt(selectedRow, 6).toString();
            String hocVi = giangVienView.getValueAt(selectedRow, 7).toString();
            String chucVu = giangVienView.getValueAt(selectedRow, 8).toString();
            String luong = giangVienView.getValueAt(selectedRow, 9).toString();

            giangVienView.setFields(maGV, tenGV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa, hocVi, chucVu, luong);
        }
    }

    private GiangVien getGiangVienFromView() {
        String maGV = giangVienView.getMaGV();
        String tenGV = giangVienView.getTenGV();
        String ngaySinh = giangVienView.getNgaySinh();
        String gioiTinh = giangVienView.getGioiTinh();
        String diaChi = giangVienView.getDiaChi();
        String sdt = giangVienView.getSdt();
        String maKhoa = giangVienView.getMaKhoa();
        String hocVi = giangVienView.getHocVi();
        String chucVu = giangVienView.getChucVu();
        String luongStr = giangVienView.getLuong();

        Date dateNgaySinh = null;
        if (ngaySinh != null && !ngaySinh.isEmpty()) {
            try {
                dateNgaySinh = dateFormat.parse(ngaySinh);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(giangVienView,
                        "Định dạng ngày sinh không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        double luong = 0;
        if (luongStr != null && !luongStr.isEmpty()) {
            try {
                luong = Double.parseDouble(luongStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(giangVienView,
                        "Lương phải là số hợp lệ!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        GiangVien gv = new GiangVien();
        gv.setMaGV(maGV);
        gv.setTenGV(tenGV);
        gv.setNgaySinh(dateNgaySinh);
        gv.setGioiTinh(gioiTinh);
        gv.setDiaChi(diaChi);
        gv.setSdt(sdt);
        gv.setMaKhoa(maKhoa);
        gv.setHocVi(hocVi);
        gv.setChucVu(chucVu);
        gv.setLuong(luong);

        return gv;
    }

    public void loadGiangVien() {
        loadGiangVienData();
    }
}
