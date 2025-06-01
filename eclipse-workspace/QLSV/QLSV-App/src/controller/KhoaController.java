package controller;

import dao.KhoaDAO;
import model.Khoa;
import view.KhoaView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KhoaController {
    private KhoaView view;
    private KhoaDAO dao;

    public KhoaController(KhoaView view, KhoaDAO dao) {
        this.view = view;
        this.dao = dao;

        loadData();

        view.btnAdd.addActionListener(e -> addKhoa());
        view.btnUpdate.addActionListener(e -> updateKhoa());
        view.btnDelete.addActionListener(e -> deleteKhoa());
        view.btnClear.addActionListener(e -> clearFields());

        view.table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
    }

    private void loadData() {
        List<Khoa> ds = dao.getAllKhoa();
        view.tableModel.setRowCount(0);
        for (Khoa k : ds) {
            view.tableModel.addRow(new Object[]{k.getMaKhoa(), k.getTenKhoa()});
        }
    }

    private void addKhoa() {
        String ma = view.tfMa.getText().trim();
        String ten = view.tfTen.getText().trim();
        if (ma.isEmpty() || ten.isEmpty()) {
            showError("Không được để trống thông tin.");
            return;
        }
        if (dao.addKhoa(new Khoa(ma, ten))) {
            view.tableModel.addRow(new Object[]{ma, ten});
            clearFields();
        } else {
            showError("Thêm khoa thất bại.");
        }
    }

    private void updateKhoa() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            showError("Vui lòng chọn một dòng để sửa.");
            return;
        }

        String ma = view.tfMa.getText().trim();
        String ten = view.tfTen.getText().trim();
        if (dao.updateKhoa(new Khoa(ma, ten))) {
            view.tableModel.setValueAt(ma, row, 0);
            view.tableModel.setValueAt(ten, row, 1);
            clearFields();
        } else {
            showError("Sửa khoa thất bại.");
        }
    }

    private void deleteKhoa() {
        int row = view.table.getSelectedRow();
        if (row == -1) {
            showError("Vui lòng chọn một dòng để xóa.");
            return;
        }

        String ma = view.tableModel.getValueAt(row, 0).toString();
        if (dao.deleteKhoa(ma)) {
            view.tableModel.removeRow(row);
            clearFields();
        } else {
            showError("Xóa khoa thất bại.");
        }
    }

    private void clearFields() {
        view.tfMa.setText("");
        view.tfTen.setText("");
        view.table.clearSelection();
        view.tfMa.requestFocus();
    }

    private void fillFormFromTable() {
        int row = view.table.getSelectedRow();
        if (row >= 0) {
            view.tfMa.setText(view.tableModel.getValueAt(row, 0).toString());
            view.tfTen.setText(view.tableModel.getValueAt(row, 1).toString());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

	public void loadKhoa() {
		// TODO Auto-generated method stub
		
	}
}
