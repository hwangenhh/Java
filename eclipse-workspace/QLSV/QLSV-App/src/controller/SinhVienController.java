package controller;

import dao.KhoaDAO;
import dao.SinhVienDAO;
import model.SinhVien;
import view.SinhVienView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

public class SinhVienController {
    private SinhVienView view;
    private SinhVienDAO sinhVienDAO;
    private KhoaDAO khoaDAO;

    public SinhVienController(SinhVienView view, Connection conn) {
        this.view = view;
        this.sinhVienDAO = new SinhVienDAO(conn);
        this.khoaDAO = new KhoaDAO(conn);

        initController();
        
        loadTable();
    }
    

    private void initController() {
        view.getBtnAdd().addActionListener(e -> addSinhVien());
        view.getBtnUpdate().addActionListener(e -> updateSinhVien());
        view.getBtnDelete().addActionListener(e -> deleteSinhVien());
        view.getBtnClear().addActionListener(e -> view.clearForm());

        view.getTable().getSelectionModel().addListSelectionListener(e -> fillForm());
    }

    private void loadTable() {
        try {
            List<SinhVien> list = sinhVienDAO.getAllSinhVien();
            DefaultTableModel model = view.getModel();
            model.setRowCount(0);
            for (SinhVien sv : list) {
                model.addRow(new Object[]{
                        sv.getMaSV(),
                        sv.getTenSV(),
                        sv.getNgaySinh(),
                        sv.getGioiTinh(),
                        sv.getDiaChi(),
                        sv.getSdt(),
                        sv.getMaKhoa(),
                        sv.getLopHanhChinh(),
                        sv.getKhoaHoc()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void addSinhVien() {
        try {
            SinhVien sv = getSinhVienFromForm();
            if (sinhVienDAO.insertSinhVien(sv)) {
                JOptionPane.showMessageDialog(view, "Thêm sinh viên thành công!");
                loadTable();
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Thêm sinh viên thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void updateSinhVien() {
        try {
            SinhVien sv = getSinhVienFromForm();
            if (sinhVienDAO.updateSinhVien(sv)) {
                JOptionPane.showMessageDialog(view, "Cập nhật sinh viên thành công!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteSinhVien() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sinh viên để xóa!");
            return;
        }
        String maSV = (String) view.getModel().getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa sinh viên " + maSV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (sinhVienDAO.deleteSinhVien(maSV)) {
                    JOptionPane.showMessageDialog(view, "Xóa sinh viên thành công!");
                    loadTable();
                    view.clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void fillForm() {
        int row = view.getTable().getSelectedRow();
        if (row != -1) {
            DefaultTableModel model = view.getModel();

            view.getTfMa().setText((String) model.getValueAt(row, 0));
            view.getTfTen().setText((String) model.getValueAt(row, 1));

            java.util.Date utilDate = (java.util.Date) model.getValueAt(row, 2);
            if (utilDate != null) {
                view.getDcNgaySinh().setDate(utilDate);
            } else {
                view.getDcNgaySinh().setDate(null);
            }

            String gioiTinh = (String) model.getValueAt(row, 3);
            view.getCbGioiTinh().setSelectedItem(gioiTinh);

            view.getTfDiaChi().setText((String) model.getValueAt(row, 4));
            view.getTfSdt().setText((String) model.getValueAt(row, 5));

            String maKhoa = (String) model.getValueAt(row, 6);
            view.getCbMaKhoa().setSelectedItem(maKhoa);
            
            view.getTfLopHanhChinh().setText((String) model.getValueAt(row, 7));
            
            String khoaHoc = (String) model.getValueAt(row, 8);
            view.getCbKhoaHoc().setSelectedItem(khoaHoc);
        }
    }

    private SinhVien getSinhVienFromForm() throws Exception {
        String maSV = view.getTfMa().getText().trim();
        String tenSV = view.getTfTen().getText().trim();
        java.util.Date ngaySinhUtil = view.getDcNgaySinh().getDate();

        if (maSV.isEmpty() || tenSV.isEmpty()) {
            throw new Exception("Mã sinh viên và Họ tên không được để trống!");
        }
        if (ngaySinhUtil == null) {
            throw new Exception("Ngày sinh không được để trống!");
        }

        String gioiTinh = (String) view.getCbGioiTinh().getSelectedItem();
        String diaChi = view.getTfDiaChi().getText().trim();
        String sdt = view.getTfSdt().getText().trim();
        String maKhoa = (String) view.getCbMaKhoa().getSelectedItem();
        String lopHanhChinh = view.getTfLopHanhChinh().getText().trim();
        String khoaHoc = (String) view.getCbKhoaHoc().getSelectedItem();

        if (!Pattern.matches("\\d{9,11}", sdt)) {
            throw new Exception("Số điện thoại không hợp lệ! Phải gồm 9 đến 11 chữ số.");
        }

        Date ngaySinh = new Date(ngaySinhUtil.getTime());
        return new SinhVien(maSV, tenSV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa, lopHanhChinh, khoaHoc);
    }

    public void loadTableData() {
        try {
            List<SinhVien> list = sinhVienDAO.getAllSinhVien();
            DefaultTableModel model = view.getModel();
            model.setRowCount(0); // xóa dữ liệu cũ

            for (SinhVien sv : list) {
                model.addRow(new Object[]{
                    sv.getMaSV(),
                    sv.getTenSV(),
                    sv.getNgaySinh(),
                    sv.getGioiTinh(),
                    sv.getDiaChi(),
                    sv.getSdt(),
                    sv.getMaKhoa(),
                    sv.getLopHanhChinh(),
                    sv.getKhoaHoc() // => dòng này hiển thị thông tin "Khóa"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu sinh viên");
        }
        loadTableData();

    }
    

    public void loadSinhVien() {
    	try {
            List<SinhVien> list = sinhVienDAO.getAllSinhVien();
            DefaultTableModel model = view.getModel();
            model.setRowCount(0);

            for (SinhVien sv : list) {
                model.addRow(new Object[]{
                    sv.getMaSV(),
                    sv.getTenSV(),
                    sv.getNgaySinh(),
                    sv.getGioiTinh(),
                    sv.getDiaChi(),
                    sv.getSdt(),
                    sv.getMaKhoa(),
                    sv.getLopHanhChinh(),
                    sv.getKhoaHoc()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu sinh viên");
        }
    }
}
