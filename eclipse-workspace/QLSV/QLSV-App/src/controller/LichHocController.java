package controller;

import dao.LichHocDAO;
import model.LichHoc;
import view.LichHocView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class LichHocController {
    private LichHocDAO dao;
    private LichHocView view;
    private DefaultTableModel model;

    public LichHocController(LichHocDAO dao, LichHocView view) {
        this.dao = dao;
        this.view = view;

        initView();
        initController();
        
    }
    
    private void initView() {
        setupTableColumns();
        loadTable();
        clearForm();
    }

    private void initController() {
        view.getBtnAdd().addActionListener(e -> insertLichHoc());
        view.getBtnUpdate().addActionListener(e -> updateLichHoc());
        view.getBtnDelete().addActionListener(e -> deleteLichHoc());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnSearch().addActionListener(e -> searchLichHocByMaMH());

        view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && view.getTable().getSelectedRow() != -1) {
                    fillFormFromSelectedRow();
                }
            }
        });
    }

    private void setupTableColumns() {
        model = (DefaultTableModel) view.getTable().getModel(); // Sửa tại đây
        if (model.getColumnCount() == 0) {
            model.setColumnIdentifiers(new Object[]{
                "Mã lịch học", "Mã môn học", "Mã giảng viên", "Thời gian", "Phòng học"
            });
        }
    }

    private void loadTable() {
        try {
            List<LichHoc> list = dao.getAllLichHoc();
            model.setRowCount(0);

            for (LichHoc lh : list) {
                model.addRow(new Object[]{
                    lh.getMaLichHoc(),
                    lh.getMaMH(),
                    lh.getMaGV(),
                    lh.getThoiGian(),
                    lh.getPhongHoc()
                });
            }
        } catch (SQLException e) {
            showError("Lỗi tải dữ liệu lịch học: " + e.getMessage());
        }
    }

    private void fillTable(List<LichHoc> list) {
        model.setRowCount(0);
        for (LichHoc lh : list) {
            model.addRow(new Object[]{
                lh.getMaLichHoc(),
                lh.getMaMH(),
                lh.getMaGV(),
                lh.getThoiGian(),
                lh.getPhongHoc()
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            view.getTfMaLichHoc().setText(model.getValueAt(row, 0).toString());
            view.getTfMaMH().setText(model.getValueAt(row, 1).toString());
            view.getTfMaGV().setText(model.getValueAt(row, 2).toString());

            String thoiGian = (String) model.getValueAt(row, 3);
            view.getCbThoiGian().setSelectedItem(thoiGian);

            view.getTfPhongHoc().setText(model.getValueAt(row, 4).toString());
        }
    }

    private void insertLichHoc() {
        LichHoc lh = getLichHocFromForm(false);
        if (lh == null) return;

        try {
            boolean result = dao.insertLichHoc(lh);
            if (result) {
                showMessage("Thêm lịch học thành công!");
                loadTable();
                clearForm();
            } else {
                showError("Không thể thêm lịch học!");
            }
        } catch (SQLException e) {
            showError("Lỗi thêm lịch học: " + e.getMessage());
        }
    }

    private void updateLichHoc() {
        LichHoc lh = getLichHocFromForm(true);
        if (lh == null) return;

        try {
            boolean result = dao.updateLichHoc(lh);
            if (result) {
                showMessage("Cập nhật lịch học thành công!");
                loadTable();
                clearForm();
            } else {
                showError("Không thể cập nhật lịch học!");
            }
        } catch (SQLException e) {
            showError("Lỗi cập nhật lịch học: " + e.getMessage());
        }
    }

    private void deleteLichHoc() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            showError("Vui lòng chọn lịch học cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa lịch học này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
        	String maLichHoc = view.getTfMaLichHoc().getText().trim();
        	if (maLichHoc.isEmpty()) {
        	    showError("Mã lịch học không được để trống!");
        	    return;
        	}
        	boolean result = dao.deleteLichHoc(maLichHoc);

            if (result) {
                showMessage("Xóa lịch học thành công!");
                loadTable();
                clearForm();
            }
        } catch (SQLException e) {
            showError("Lỗi xóa lịch học: " + e.getMessage());
        }
    }

    private void searchLichHocByMaMH() {
        String maMH = view.getTfSearchMaMH().getText().trim();
        if (maMH.isEmpty()) {
            loadTable();
            return;
        }

        try {
            List<LichHoc> list = dao.searchByMaMH(maMH);
            fillTable(list);
        } catch (SQLException e) {
            showError("Lỗi tìm kiếm: " + e.getMessage());
        }
    }


    private LichHoc getLichHocFromForm(boolean requireId) {
        String maLichHoc = view.getTfMaLichHoc().getText().trim();
        String maMH = view.getTfMaMH().getText().trim();
        String maGV = view.getTfMaGV().getText().trim();
        String thoiGian = (String) view.getCbThoiGian().getSelectedItem();
        String phongHoc = view.getTfPhongHoc().getText().trim();

        if (maLichHoc.isEmpty() || maMH.isEmpty() || maGV.isEmpty() || thoiGian == null || phongHoc.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin.");
            return null;
        }

        // Nếu requireId == true thì mã lịch học phải không trống (đã check ở trên)
        return new LichHoc(maLichHoc, maMH, maGV, thoiGian, phongHoc);
    }


    private void clearForm() {
        view.getTfMaLichHoc().setText("");
        view.getTfMaLichHoc().setEditable(true);
        view.getTfMaLichHoc().setEnabled(true);

        view.getTfMaMH().setText("");
        view.getTfMaGV().setText("");
        view.getCbThoiGian().setSelectedIndex(-1);
        view.getTfPhongHoc().setText("");
        view.getTfSearchMaMH().setText("");
        view.getTfMaMH().requestFocus();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

   

    public void loadLichHoc() {
    	loadTable();
    }
}
