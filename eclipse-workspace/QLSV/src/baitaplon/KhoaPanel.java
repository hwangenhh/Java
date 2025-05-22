package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KhoaPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMa, tfTen;
    private Connection conn;

    public KhoaPanel(Connection conn) {
        // Lấy Connection từ Main
        try {
        	this.conn = conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout(10, 10));

        // ==== Form nhập liệu ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khoa"));

        JLabel lblMa = new JLabel("Mã Khoa:");
        tfMa = new JTextField(20);
        JLabel lblTen = new JLabel("Tên Khoa:");
        tfTen = new JTextField(20);

        // Sử dụng đối tượng GridBagConstraints riêng cho từng thành phần
        GridBagConstraints gbcLblMa = new GridBagConstraints();
        gbcLblMa.insets = new Insets(5, 5, 5, 5);
        gbcLblMa.anchor = GridBagConstraints.WEST;
        gbcLblMa.gridx = 0;
        gbcLblMa.gridy = 0;
        formPanel.add(lblMa, gbcLblMa);

        GridBagConstraints gbcTfMa = new GridBagConstraints();
        gbcTfMa.insets = new Insets(5, 5, 5, 5);
        gbcTfMa.fill = GridBagConstraints.HORIZONTAL;
        gbcTfMa.weightx = 1.0;
        gbcTfMa.gridx = 1;
        gbcTfMa.gridy = 0;
        formPanel.add(tfMa, gbcTfMa);

        GridBagConstraints gbcLblTen = new GridBagConstraints();
        gbcLblTen.insets = new Insets(5, 5, 5, 5);
        gbcLblTen.anchor = GridBagConstraints.WEST;
        gbcLblTen.gridx = 0;
        gbcLblTen.gridy = 1;
        formPanel.add(lblTen, gbcLblTen);

        GridBagConstraints gbcTfTen = new GridBagConstraints();
        gbcTfTen.insets = new Insets(5, 5, 5, 5);
        gbcTfTen.fill = GridBagConstraints.HORIZONTAL;
        gbcTfTen.weightx = 1.0;
        gbcTfTen.gridx = 1;
        gbcTfTen.gridy = 1;
        formPanel.add(tfTen, gbcTfTen);

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

        // ==== Bảng danh sách khoa ====
        model = new DefaultTableModel(new String[]{"Mã Khoa", "Tên Khoa"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách khoa"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu từ cơ sở dữ liệu
        loadKhoaData();

        // ==== Xử lý sự kiện ====
        btnAdd.addActionListener(e -> {
            String ma = tfMa.getText().trim();
            String ten = tfTen.getText().trim();
            if (!ma.isEmpty()) {
                if (addKhoa(ma, ten)) {
                    model.addRow(new Object[]{ma, ten});
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Thêm khoa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm khoa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã khoa không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String ma = tfMa.getText().trim();
                String ten = tfTen.getText().trim();
                if (!ma.isEmpty()) {
                    if (updateKhoa(ma, ten)) {
                        model.setValueAt(ma, selectedRow, 0);
                        model.setValueAt(ten, selectedRow, 1);
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Cập nhật khoa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật khoa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã khoa không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khoa để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String ma = model.getValueAt(selectedRow, 0).toString();
                if (deleteKhoa(ma)) {
                    model.removeRow(selectedRow);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Xóa khoa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa khoa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khoa để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
                }
            }
        });
    }

    private void clearFields() {
        tfMa.setText("");
        tfTen.setText("");
        tfMa.requestFocus();
    }

    private void loadKhoaData() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM khoa");
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0); // Xóa dữ liệu cũ
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maKhoa"),
                    rs.getString("tenKhoa")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khoa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addKhoa(String maKhoa, String tenKhoa) {
        String sql = "INSERT INTO khoa (maKhoa, tenKhoa) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKhoa);
            stmt.setString(2, tenKhoa.isEmpty() ? null : tenKhoa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khoa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean updateKhoa(String maKhoa, String tenKhoa) {
        String sql = "UPDATE khoa SET tenKhoa = ? WHERE maKhoa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenKhoa.isEmpty() ? null : tenKhoa);
            stmt.setString(2, maKhoa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật khoa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteKhoa(String maKhoa) {
        String sql = "DELETE FROM khoa WHERE maKhoa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKhoa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa khoa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}