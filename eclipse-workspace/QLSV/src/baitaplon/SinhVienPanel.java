package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//View 
public class SinhVienPanel extends JPanel { 
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMa, tfTen, tfNgaySinh, tfGioiTinh, tfDiaChi, tfSdt, tfMaKhoa;
    private Connection conn;

    public SinhVienPanel(Connection conn ) { //view 
        // Lấy Connection từ Main
        try {
            this.conn = conn;
           } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==== Form nhập liệu ==== view + controller 
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        // Mã SV
        JLabel lblMa = new JLabel("Mã SV:");
        tfMa = new JTextField(20);
        GridBagConstraints gbcMaLabel = new GridBagConstraints();
        gbcMaLabel.gridx = 0;
        gbcMaLabel.gridy = 0;
        gbcMaLabel.anchor = GridBagConstraints.WEST;
        gbcMaLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblMa, gbcMaLabel);

        GridBagConstraints gbcMaField = new GridBagConstraints();
        gbcMaField.gridx = 1;
        gbcMaField.gridy = 0;
        gbcMaField.fill = GridBagConstraints.HORIZONTAL;
        gbcMaField.weightx = 1.0;
        gbcMaField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfMa, gbcMaField);

        // Họ tên
        JLabel lblTen = new JLabel("Họ tên:");
        tfTen = new JTextField(20);
        GridBagConstraints gbcTenLabel = new GridBagConstraints();
        gbcTenLabel.gridx = 0;
        gbcTenLabel.gridy = 1;
        gbcTenLabel.anchor = GridBagConstraints.WEST;
        gbcTenLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblTen, gbcTenLabel);

        GridBagConstraints gbcTenField = new GridBagConstraints();
        gbcTenField.gridx = 1;
        gbcTenField.gridy = 1;
        gbcTenField.fill = GridBagConstraints.HORIZONTAL;
        gbcTenField.weightx = 1.0;
        gbcTenField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfTen, gbcTenField);

        // Ngày sinh
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        tfNgaySinh = new JTextField(20); // Định dạng yyyy-MM-dd
        GridBagConstraints gbcNgaySinhLabel = new GridBagConstraints();
        gbcNgaySinhLabel.gridx = 0;
        gbcNgaySinhLabel.gridy = 2;
        gbcNgaySinhLabel.anchor = GridBagConstraints.WEST;
        gbcNgaySinhLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblNgaySinh, gbcNgaySinhLabel);

        GridBagConstraints gbcNgaySinhField = new GridBagConstraints();
        gbcNgaySinhField.gridx = 1;
        gbcNgaySinhField.gridy = 2;
        gbcNgaySinhField.fill = GridBagConstraints.HORIZONTAL;
        gbcNgaySinhField.weightx = 1.0;
        gbcNgaySinhField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfNgaySinh, gbcNgaySinhField);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        tfGioiTinh = new JTextField(10);
        GridBagConstraints gbcGioiTinhLabel = new GridBagConstraints();
        gbcGioiTinhLabel.gridx = 0;
        gbcGioiTinhLabel.gridy = 3;
        gbcGioiTinhLabel.anchor = GridBagConstraints.WEST;
        gbcGioiTinhLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblGioiTinh, gbcGioiTinhLabel);

        GridBagConstraints gbcGioiTinhField = new GridBagConstraints();
        gbcGioiTinhField.gridx = 1;
        gbcGioiTinhField.gridy = 3;
        gbcGioiTinhField.fill = GridBagConstraints.HORIZONTAL;
        gbcGioiTinhField.weightx = 1.0;
        gbcGioiTinhField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfGioiTinh, gbcGioiTinhField);

        // Địa chỉ
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        tfDiaChi = new JTextField(20);
        GridBagConstraints gbcDiaChiLabel = new GridBagConstraints();
        gbcDiaChiLabel.gridx = 0;
        gbcDiaChiLabel.gridy = 4;
        gbcDiaChiLabel.anchor = GridBagConstraints.WEST;
        gbcDiaChiLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblDiaChi, gbcDiaChiLabel);

        GridBagConstraints gbcDiaChiField = new GridBagConstraints();
        gbcDiaChiField.gridx = 1;
        gbcDiaChiField.gridy = 4;
        gbcDiaChiField.fill = GridBagConstraints.HORIZONTAL;
        gbcDiaChiField.weightx = 1.0;
        gbcDiaChiField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfDiaChi, gbcDiaChiField);

        // SĐT
        JLabel lblSdt = new JLabel("SĐT:");
        tfSdt = new JTextField(15);
        GridBagConstraints gbcSdtLabel = new GridBagConstraints();
        gbcSdtLabel.gridx = 0;
        gbcSdtLabel.gridy = 5;
        gbcSdtLabel.anchor = GridBagConstraints.WEST;
        gbcSdtLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblSdt, gbcSdtLabel);

        GridBagConstraints gbcSdtField = new GridBagConstraints();
        gbcSdtField.gridx = 1;
        gbcSdtField.gridy = 5;
        gbcSdtField.fill = GridBagConstraints.HORIZONTAL;
        gbcSdtField.weightx = 1.0;
        gbcSdtField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfSdt, gbcSdtField);

        // Mã khoa
        JLabel lblMaKhoa = new JLabel("Mã khoa:");
        tfMaKhoa = new JTextField(20);
        GridBagConstraints gbcMaKhoaLabel = new GridBagConstraints();
        gbcMaKhoaLabel.gridx = 0;
        gbcMaKhoaLabel.gridy = 6;
        gbcMaKhoaLabel.anchor = GridBagConstraints.WEST;
        gbcMaKhoaLabel.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblMaKhoa, gbcMaKhoaLabel);

        GridBagConstraints gbcMaKhoaField = new GridBagConstraints();
        gbcMaKhoaField.gridx = 1;
        gbcMaKhoaField.gridy = 6;
        gbcMaKhoaField.fill = GridBagConstraints.HORIZONTAL;
        gbcMaKhoaField.weightx = 1.0;
        gbcMaKhoaField.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfMaKhoa, gbcMaKhoaField);

        // ==== Các nút chức năng ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==== Bảng danh sách sinh viên ====
        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã khoa"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ==== Thêm vào layout chính ====
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu từ cơ sở dữ liệu
        loadSinhVienData();

        // ==== Controller Xử lý sự kiện ====
        btnAdd.addActionListener(e -> {
            String ma = tfMa.getText().trim();
            String ten = tfTen.getText().trim();
            String ngaySinh = tfNgaySinh.getText().trim();
            String gioiTinh = tfGioiTinh.getText().trim();
            String diaChi = tfDiaChi.getText().trim();
            String sdt = tfSdt.getText().trim();
            String maKhoa = tfMaKhoa.getText().trim();

            if (!ma.isEmpty()) {
                if (addSinhVien(ma, ten, ngaySinh, gioiTinh, diaChi, sdt, maKhoa)) {
                    model.addRow(new Object[]{ma, ten, ngaySinh, gioiTinh, diaChi, sdt, maKhoa});
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String ma = tfMa.getText().trim();
                String ten = tfTen.getText().trim();
                String ngaySinh = tfNgaySinh.getText().trim();
                String gioiTinh = tfGioiTinh.getText().trim();
                String diaChi = tfDiaChi.getText().trim();
                String sdt = tfSdt.getText().trim();
                String maKhoa = tfMaKhoa.getText().trim();

                if (!ma.isEmpty()) {
                    if (updateSinhVien(ma, ten, ngaySinh, gioiTinh, diaChi, sdt, maKhoa)) {
                        model.setValueAt(ma, selectedRow, 0);
                        model.setValueAt(ten, selectedRow, 1);
                        model.setValueAt(ngaySinh, selectedRow, 2);
                        model.setValueAt(gioiTinh, selectedRow, 3);
                        model.setValueAt(diaChi, selectedRow, 4);
                        model.setValueAt(sdt, selectedRow, 5);
                        model.setValueAt(maKhoa, selectedRow, 6);
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String ma = model.getValueAt(selectedRow, 0).toString();
                if (deleteSinhVien(ma)) {
                    model.removeRow(selectedRow);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> {
            clearFields();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tfMa.setText(model.getValueAt(selectedRow, 0).toString());
                    tfTen.setText(model.getValueAt(selectedRow, 1).toString());
                    tfNgaySinh.setText(model.getValueAt(selectedRow, 2).toString());
                    tfGioiTinh.setText(model.getValueAt(selectedRow, 3).toString());
                    tfDiaChi.setText(model.getValueAt(selectedRow, 4).toString());
                    tfSdt.setText(model.getValueAt(selectedRow, 5).toString());
                    tfMaKhoa.setText(model.getValueAt(selectedRow, 6).toString());
                }
            }
        });
    }

    //view - hàm tiện ích xóa trắng các trường nhập liêu
    private void clearFields() {
        tfMa.setText("");
        tfTen.setText("");
        tfNgaySinh.setText("");
        tfGioiTinh.setText("");
        tfDiaChi.setText("");
        tfSdt.setText("");
        tfMaKhoa.setText("");
        tfMa.requestFocus();
    }

  //hàm làm việc với CSDL - model 
    private void loadSinhVienData() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sinhvien");
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0); // Xóa dữ liệu cũ
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maSV"),
                    rs.getString("tenSV"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toString() : "",
                    rs.getString("gioiTinh"),
                    rs.getString("diaChi"),
                    rs.getString("sdt"),
                    rs.getString("maKhoa")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private boolean addSinhVien(String maSV, String tenSV, String ngaySinh, String gioiTinh, String diaChi, String sdt, String maKhoa) {
        String sql = "INSERT INTO sinhvien (maSV, tenSV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            stmt.setString(2, tenSV);
            stmt.setString(3, ngaySinh); // Nếu DB là DATE thì định dạng yyyy-MM-dd
            stmt.setString(4, gioiTinh);
            stmt.setString(5, diaChi);
            stmt.setString(6, sdt);
            stmt.setString(7, maKhoa);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


    private boolean updateSinhVien(String maSV, String tenSV, String ngaySinh, String gioiTinh, String diaChi, String sdt, String maKhoa) {
        String sql = "UPDATE sinhvien SET tenSV = ?, ngaySinh = ?, gioiTinh = ?, diaChi = ?, sdt = ?, maKhoa = ? WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenSV.isEmpty() ? null : tenSV);
            stmt.setDate(2, ngaySinh.isEmpty() ? null : java.sql.Date.valueOf(ngaySinh));
            stmt.setString(3, gioiTinh.isEmpty() ? null : gioiTinh);
            stmt.setString(4, diaChi.isEmpty() ? null : diaChi);
            stmt.setString(5, sdt.isEmpty() ? null : sdt);
            stmt.setString(6, maKhoa.isEmpty() ? null : maKhoa);
            stmt.setString(7, maSV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteSinhVien(String maSV) {
        String sql = "DELETE FROM sinhvien WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}