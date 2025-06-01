package controller;

import dao.MonHocDAO;
import model.MonHoc;
import view.MonHocView;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MonHocController {
    private MonHocView view;
    private MonHocDAO dao;

    public MonHocController(MonHocView view, MonHocDAO dao) {
        this.view = view;
        this.dao = dao;

        loadData();
        addEventHandlers();
    }

    private void loadData() {
        view.tableModel.setRowCount(0);
        ArrayList<MonHoc> ds = dao.getAllMonHoc();
        for (MonHoc mh : ds) {
            view.tableModel.addRow(new Object[]{
                mh.getMaMon(), mh.getTenMon(), mh.getSoTinChi(), mh.getMaKhoa()
            });
        }
    }

    private void addEventHandlers() {
        view.btnThem.addActionListener(e -> themMonHoc());
        view.btnSua.addActionListener(e -> suaMonHoc());
        view.btnXoa.addActionListener(e -> xoaMonHoc());

        view.table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.table.getSelectedRow();
                view.txtMaMon.setText(view.tableModel.getValueAt(row, 0).toString());
                view.txtTenMon.setText(view.tableModel.getValueAt(row, 1).toString());
                view.cbSoTinChi.setSelectedItem(view.tableModel.getValueAt(row, 2).toString()); // fix here
                view.cbMaKhoa.setSelectedItem(view.tableModel.getValueAt(row, 3).toString());
            }
        });
    }

    private void themMonHoc() {
        try {
            String ma = view.txtMaMon.getText();
            String ten = view.txtTenMon.getText();
            String soTC = view.cbSoTinChi.getSelectedItem().toString(); // fix here
            String khoa = view.cbMaKhoa.getSelectedItem().toString(); 

            MonHoc mh = new MonHoc(ma, ten, soTC, khoa);
            if (dao.insertMonHoc(mh)) {
                loadData();
                JOptionPane.showMessageDialog(view, "Thêm thành công");
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void suaMonHoc() {
        try {
            String ma = view.txtMaMon.getText();
            String ten = view.txtTenMon.getText();
            String soTC = view.cbSoTinChi.getSelectedItem().toString(); // fix here
            String khoa = view.cbMaKhoa.getSelectedItem().toString(); 

            MonHoc mh = new MonHoc(ma, ten, soTC, khoa);
            if (dao.updateMonHoc(mh)) {
                loadData();
                JOptionPane.showMessageDialog(view, "Sửa thành công");
            } else {
                JOptionPane.showMessageDialog(view, "Sửa thất bại");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void xoaMonHoc() {
        String ma = view.txtMaMon.getText();
        if (dao.deleteMonHoc(ma)) {
            loadData();
            JOptionPane.showMessageDialog(view, "Xóa thành công");
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại");
        }
    }

    public void loadMonHoc() {
        loadData();
    }
}
