package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DiemPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaSV, tfMaMH, tfDiem, tfHoTen;
    private JButton btnAdd, btnEdit, btnDelete, btnClear;
    private Connection conn;

    public DiemPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin điểm"));

        JLabel lblMaSV = new JLabel("Mã SV:");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblMaSV, gbc);

        tfMaSV = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(tfMaSV, gbc);

        JLabel lblHoTen = new JLabel("Họ tên:");
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblHoTen, gbc);

        tfHoTen = new JTextField(15);
        tfHoTen.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(tfHoTen, gbc);

        JLabel lblMaMH = new JLabel("Mã môn học:");
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblMaMH, gbc);

        tfMaMH = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(tfMaMH, gbc);

        JLabel lblDiem = new JLabel("Điểm:");
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblDiem, gbc);

        tfDiem = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(tfDiem, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Thêm Điểm");
        btnEdit = new JButton("Sửa Điểm");
        btnDelete = new JButton("Xóa Điểm");
        btnClear = new JButton("Làm mới");

        buttonPanel.add(btnAdd); buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete); buttonPanel.add(btnClear);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Mã môn học", "Điểm"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách điểm"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        loadDiemData();

        tfMaSV.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String tenSV = getTenSinhVien(tfMaSV.getText().trim());
                tfHoTen.setText(tenSV);
            }
        });

        btnAdd.addActionListener(e -> {
            String maSV = tfMaSV.getText().trim();
            String maMH = tfMaMH.getText().trim();
            String diemText = tfDiem.getText().trim();
            if (!maSV.isEmpty() && !maMH.isEmpty()) {
                try {
                    Float diemSo = diemText.isEmpty() ? null : Float.parseFloat(diemText);
                    if (diemSo != null && (diemSo < 0 || diemSo > 10)) {
                        JOptionPane.showMessageDialog(this, "Điểm phải nằm trong khoảng từ 0 đến 10!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (addDiem(maSV, maMH, diemSo)) {
                        String tenSV = getTenSinhVien(maSV);
                        model.addRow(new Object[]{maSV, tenSV, maMH, diemSo});
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Thêm điểm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi thêm điểm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Điểm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã sinh viên và mã môn học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearFields() {
        tfMaSV.setText(""); tfHoTen.setText(""); tfMaMH.setText(""); tfDiem.setText(""); tfMaSV.requestFocus();
    }

    private void loadDiemData() {
        String sql = "SELECT d.maSV, sv.tenSV, d.maMH, d.diemSo FROM diem d JOIN sinhvien sv ON d.maSV = sv.maSV";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maSV"),
                    rs.getString("tenSV"),
                    rs.getString("maMH"),
                    rs.getFloat("diemSo")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu điểm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTenSinhVien(String maSV) {
        String sql = "SELECT tenSV FROM sinhvien WHERE maSV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenSV");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn tên sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return "Không rõ";
    }

    private boolean addDiem(String maSV, String maMH, Float diemSo) {
        String sql = "INSERT INTO diem (maSV, maMH, diemSo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSV);
            stmt.setString(2, maMH);
            if (diemSo == null) {
                stmt.setNull(3, java.sql.Types.FLOAT);
            } else {
                stmt.setFloat(3, diemSo);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm điểm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
