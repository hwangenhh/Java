package controller;

import dao.DiemDAO;
import model.Diem;
import view.DiemView;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DiemController {
    private DiemView view;
    private DiemDAO dao;

    public DiemController(DiemView view, DiemDAO dao) {
        this.view = view;
        this.dao = dao;
        loadTable();
        addEventHandlers();
    }
    
   
    private void loadTable() {
        view.model.setRowCount(0);
        List<Diem> list = dao.getAll();
        for (Diem d : list) {
            view.model.addRow(new Object[]{
                d.getMaSV(), d.getTenSV(), d.getMaMH(), d.getDiemSo()
            });
        }
    }

    private void addEventHandlers() {
        view.tfMaSV.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                view.tfHoTen.setText(dao.getTenSinhVien(view.tfMaSV.getText().trim()));
            }
        });

        view.btnAdd.addActionListener(e -> {
            try {
                String maSV = view.tfMaSV.getText().trim();
                String maMH = view.tfMaMH.getText().trim();
                String diemStr = view.tfDiem.getText().trim();

                if (maSV.isEmpty() || maMH.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Mã SV và Mã MH không được trống");
                    return;
                }

                Float diem = diemStr.isEmpty() ? null : Float.parseFloat(diemStr);
                if (diem != null && (diem < 0 || diem > 10)) {
                    JOptionPane.showMessageDialog(view, "Điểm phải từ 0 đến 10");
                    return;
                }

                String tenSV = dao.getTenSinhVien(maSV);
                Diem d = new Diem(maSV, tenSV, maMH, diem);
                if (dao.insert(d)) {
                    view.model.addRow(new Object[]{maSV, tenSV, maMH, diem});
                    clearFields();
                    JOptionPane.showMessageDialog(view, "Thêm điểm thành công!");
                } else {
                    JOptionPane.showMessageDialog(view, "Lỗi khi thêm điểm!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Điểm không hợp lệ");
            }
        });
        
        view.btnEdit.addActionListener(e -> {
            try {
                String maSV = view.tfMaSV.getText().trim();
                String maMH = view.tfMaMH.getText().trim();
                String diemStr = view.tfDiem.getText().trim();

                if (maSV.isEmpty() || maMH.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Mã SV và Mã MH không được trống");
                    return;
                }

                Float diem = diemStr.isEmpty() ? null : Float.parseFloat(diemStr);
                if (diem != null && (diem < 0 || diem > 10)) {
                    JOptionPane.showMessageDialog(view, "Điểm phải từ 0 đến 10");
                    return;
                }

                String tenSV = dao.getTenSinhVien(maSV);
                Diem d = new Diem(maSV, tenSV, maMH, diem);

                if (dao.updateDiem(d)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật điểm thành công!");
                    loadTable(); // Reload lại bảng
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Không tìm thấy bản ghi để cập nhật!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Điểm không hợp lệ");
            }
        });


        view.btnDelete.addActionListener(e -> {
            String maSV = view.tfMaSV.getText().trim();
            String maMH = view.tfMaMH.getText().trim();

            if (maSV.isEmpty() || maMH.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Mã SV và Mã MH không được trống");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa điểm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteDiem(maSV, maMH)) {
                    JOptionPane.showMessageDialog(view, "Xóa điểm thành công!");
                    loadTable(); // Reload bảng
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Không tìm thấy bản ghi để xóa!");
                }
            }
        });
        view.table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = view.table.getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy dữ liệu từ bảng
                    String maSV = view.table.getValueAt(selectedRow, 0).toString();
                    String tenSV = view.table.getValueAt(selectedRow, 1).toString();
                    String maMH = view.table.getValueAt(selectedRow, 2).toString();
                    String diemStr = view.table.getValueAt(selectedRow, 3).toString();

                    // Gán vào ô nhập liệu
                    view.tfMaSV.setText(maSV);
                    view.tfHoTen.setText(tenSV);
                    view.tfMaMH.setText(maMH);
                    view.tfDiem.setText(diemStr);
                }
            }
        });



        view.btnClear.addActionListener(e -> clearFields());
    }
    

    private void clearFields() {
        view.tfMaSV.setText("");
        view.tfHoTen.setText("");
        view.tfMaMH.setText("");
        view.tfDiem.setText("");
        view.tfMaSV.requestFocus();
    }

	public void loadDiem() {
		// TODO Auto-generated method stub
		
	}
}
