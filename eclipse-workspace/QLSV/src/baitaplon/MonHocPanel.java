package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonHocPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMa, tfTen, tfSoTC, tfMaKhoa;
    private Connection conn;

    public MonHocPanel(Connection conn) {
        // Lấy Connection từ Main
        try {
        	this.conn = conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==== Form nhập liệu ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));

        // Tạo các thành phần
        JLabel lblMa = new JLabel("Mã môn học:");
        tfMa = new JTextField(20);
        JLabel lblTen = new JLabel("Tên môn học:");
        tfTen = new JTextField(20);
        JLabel lblSoTC = new JLabel("Số tín chỉ:");
        tfSoTC = new JTextField(20);
        JLabel lblMaKhoa = new JLabel("Mã khoa:");
        tfMaKhoa = new JTextField(20);

        // Thêm các thành phần vào formPanel theo dạng dọc
        // Mã môn học - Label
        GridBagConstraints gbcLblMa = new GridBagConstraints();
        gbcLblMa.gridx = 0;
        gbcLblMa.gridy = 0;
        gbcLblMa.anchor = GridBagConstraints.WEST;
        gbcLblMa.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblMa, gbcLblMa);

        // Mã môn học - TextField
        GridBagConstraints gbcTfMa = new GridBagConstraints();
        gbcTfMa.gridx = 1;
        gbcTfMa.gridy = 0;
        gbcTfMa.fill = GridBagConstraints.HORIZONTAL;
        gbcTfMa.weightx = 1.0;
        gbcTfMa.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfMa, gbcTfMa);

        // Tên môn học - Label
        GridBagConstraints gbcLblTen = new GridBagConstraints();
        gbcLblTen.gridx = 0;
        gbcLblTen.gridy = 1;
        gbcLblTen.anchor = GridBagConstraints.WEST;
        gbcLblTen.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblTen, gbcLblTen);

        // Tên môn học - TextField
        GridBagConstraints gbcTfTen = new GridBagConstraints();
        gbcTfTen.gridx = 1;
        gbcTfTen.gridy = 1;
        gbcTfTen.fill = GridBagConstraints.HORIZONTAL;
        gbcTfTen.weightx = 1.0;
        gbcTfTen.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfTen, gbcTfTen);

        // Số tín chỉ - Label
        GridBagConstraints gbcLblSoTC = new GridBagConstraints();
        gbcLblSoTC.gridx = 0;
        gbcLblSoTC.gridy = 2;
        gbcLblSoTC.anchor = GridBagConstraints.WEST;
        gbcLblSoTC.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblSoTC, gbcLblSoTC);

        // Số tín chỉ - TextField
        GridBagConstraints gbcTfSoTC = new GridBagConstraints();
        gbcTfSoTC.gridx = 1;
        gbcTfSoTC.gridy = 2;
        gbcTfSoTC.fill = GridBagConstraints.HORIZONTAL;
        gbcTfSoTC.weightx = 1.0;
        gbcTfSoTC.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfSoTC, gbcTfSoTC);

        // Mã khoa - Label
        GridBagConstraints gbcLblMaKhoa = new GridBagConstraints();
        gbcLblMaKhoa.gridx = 0;
        gbcLblMaKhoa.gridy = 3;
        gbcLblMaKhoa.anchor = GridBagConstraints.WEST;
        gbcLblMaKhoa.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblMaKhoa, gbcLblMaKhoa);

        // Mã khoa - TextField
        GridBagConstraints gbcTfMaKhoa = new GridBagConstraints();
        gbcTfMaKhoa.gridx = 1;
        gbcTfMaKhoa.gridy = 3;
        gbcTfMaKhoa.fill = GridBagConstraints.HORIZONTAL;
        gbcTfMaKhoa.weightx = 1.0;
        gbcTfMaKhoa.insets = new Insets(5, 5, 5, 5);
        formPanel.add(tfMaKhoa, gbcTfMaKhoa);

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

        // Thêm khoảng cách giữa form và các nút
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==== Bảng danh sách môn học ====
        model = new DefaultTableModel(new String[]{"Mã môn học", "Tên môn học", "Số tín chỉ", "Mã khoa"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Thiết lập tiêu đề cho phần bảng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách môn học"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ==== Thêm vào layout chính ====
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Load dữ liệu từ cơ sở dữ liệu
        loadMonHocData();

        // ==== Xử lý sự kiện ====
        btnAdd.addActionListener(e -> {
            String ma = tfMa.getText().trim();
            String ten = tfTen.getText().trim();
            String soTCText = tfSoTC.getText().trim();
            String maKhoa = tfMaKhoa.getText().trim();

            if (!ma.isEmpty()) {
                try {
                    Integer soTC = soTCText.isEmpty() ? null : Integer.parseInt(soTCText);
                    if (soTC != null && soTC <= 0) {
                        JOptionPane.showMessageDialog(this, "Số tín chỉ phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (addMonHoc(ma, ten, soTC, maKhoa)) {
                        model.addRow(new Object[]{ma, ten, soTC, maKhoa});
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Thêm môn học thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi thêm môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số tín chỉ phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã môn học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String ma = tfMa.getText().trim();
                String ten = tfTen.getText().trim();
                String soTCText = tfSoTC.getText().trim();
                String maKhoa = tfMaKhoa.getText().trim();

                if (!ma.isEmpty()) {
                    try {
                        Integer soTC = soTCText.isEmpty() ? null : Integer.parseInt(soTCText);
                        if (soTC != null && soTC <= 0) {
                            JOptionPane.showMessageDialog(this, "Số tín chỉ phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (updateMonHoc(ma, ten, soTC, maKhoa)) {
                            model.setValueAt(ma, selectedRow, 0);
                            model.setValueAt(ten, selectedRow, 1);
                            model.setValueAt(soTC, selectedRow, 2);
                            model.setValueAt(maKhoa, selectedRow, 3);
                            clearFields();
                            JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Số tín chỉ phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã môn học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String ma = model.getValueAt(selectedRow, 0).toString();
                if (deleteMonHoc(ma)) {
                    model.removeRow(selectedRow);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Xóa môn học thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
                    tfSoTC.setText(model.getValueAt(selectedRow, 2) != null ? model.getValueAt(selectedRow, 2).toString() : "");
                    tfMaKhoa.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void clearFields() {
        tfMa.setText("");
        tfTen.setText("");
        tfSoTC.setText("");
        tfMaKhoa.setText("");
        tfMa.requestFocus();
    }

    private void loadMonHocData() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM monhoc");
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0); // Xóa dữ liệu cũ
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maMH"),
                    rs.getString("tenMH"),
                    rs.getInt("soTinChi"),
                    rs.getString("maKhoa")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean addMonHoc(String maMH, String tenMH, Integer soTinChi, String maKhoa) {
        String sql = "INSERT INTO monhoc (maMH, tenMH, soTinChi, maKhoa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH);
            stmt.setString(2, tenMH.isEmpty() ? null : tenMH);
            if (soTinChi == null) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, soTinChi);
            }
            stmt.setString(4, maKhoa.isEmpty() ? null : maKhoa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean updateMonHoc(String maMH, String tenMH, Integer soTinChi, String maKhoa) {
        String sql = "UPDATE monhoc SET tenMH = ?, soTinChi = ?, maKhoa = ? WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenMH.isEmpty() ? null : tenMH);
            if (soTinChi == null) {
                stmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(2, soTinChi);
            }
            stmt.setString(3, maKhoa.isEmpty() ? null : maKhoa);
            stmt.setString(4, maMH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteMonHoc(String maMH) {
        String sql = "DELETE FROM monhoc WHERE maMH = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}