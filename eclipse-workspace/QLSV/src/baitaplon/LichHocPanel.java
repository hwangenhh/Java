package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class LichHocPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaLichHoc, tfMaMH, tfMaGV, tfThoiGian, tfPhongHoc, tfSearchMaMH;
    private Connection conn;

    public LichHocPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout(10, 10));

        // ==== Form nhập liệu ====
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin lịch học"));

        tfMaLichHoc = new JTextField();
        tfMaLichHoc.setEditable(false);
        tfMaMH = new JTextField();
        tfMaGV = new JTextField();
        tfThoiGian = new JTextField();  // yyyy-MM-dd HH:mm:ss
        tfPhongHoc = new JTextField();

        formPanel.add(new JLabel("Mã lịch học:")); formPanel.add(tfMaLichHoc);
        formPanel.add(new JLabel("Mã môn học:")); formPanel.add(tfMaMH);
        formPanel.add(new JLabel("Mã giảng viên:")); formPanel.add(tfMaGV);
        formPanel.add(new JLabel("Thời gian:")); formPanel.add(tfThoiGian);
        formPanel.add(new JLabel("Phòng học:")); formPanel.add(tfPhongHoc);

        // ==== Nút chức năng ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // ==== Bảng dữ liệu ====
        model = new DefaultTableModel(new String[]{"Mã lịch học", "Mã môn học", "Mã giảng viên", "Thời gian", "Phòng học"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // ==== Tìm kiếm theo mã môn học ====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tfSearchMaMH = new JTextField(15);
        JButton btnSearch = new JButton("Tìm theo mã môn học");
        searchPanel.add(new JLabel("Mã MH:"));
        searchPanel.add(tfSearchMaMH);
        searchPanel.add(btnSearch);

        // ==== Gộp layout ====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        loadLichHocData();

        // ==== Sự kiện ====
        btnAdd.addActionListener(e -> {
            if (addLichHoc(tfMaMH.getText(), tfMaGV.getText(), tfThoiGian.getText(), tfPhongHoc.getText())) {
                loadLichHocData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            }
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && !tfMaLichHoc.getText().isEmpty()) {
                if (updateLichHoc(Integer.parseInt(tfMaLichHoc.getText()), tfMaMH.getText(), tfMaGV.getText(), tfThoiGian.getText(), tfPhongHoc.getText())) {
                    loadLichHocData();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                if (deleteLichHoc(id)) {
                    model.removeRow(row);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                }
            }
        });

        btnClear.addActionListener(e -> clearFields());

        btnSearch.addActionListener(e -> {
            String maMH = tfSearchMaMH.getText().trim();
            if (!maMH.isEmpty()) {
                searchByMaMH(maMH);
            } else {
                loadLichHocData();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                tfMaLichHoc.setText(model.getValueAt(row, 0).toString());
                tfMaMH.setText(model.getValueAt(row, 1).toString());
                tfMaGV.setText(model.getValueAt(row, 2).toString());
                tfThoiGian.setText(model.getValueAt(row, 3).toString());
                tfPhongHoc.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadLichHocData() {
        model.setRowCount(0);
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM lichhoc");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("maLichHoc"));
                row.add(rs.getString("maMH"));
                row.add(rs.getString("maGV"));
                row.add(rs.getTimestamp("thoiGian"));
                row.add(rs.getString("phongHoc"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private boolean addLichHoc(String maMH, String maGV, String thoiGian, String phongHoc) {
        String sql = "INSERT INTO lichhoc (maMH, maGV, thoiGian, phongHoc) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH.isEmpty() ? null : maMH);
            stmt.setString(2, maGV.isEmpty() ? null : maGV);
            stmt.setTimestamp(3, thoiGian.isEmpty() ? null : Timestamp.valueOf(thoiGian));
            stmt.setString(4, phongHoc.isEmpty() ? null : phongHoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
            return false;
        }
    }

    private boolean updateLichHoc(int maLichHoc, String maMH, String maGV, String thoiGian, String phongHoc) {
        String sql = "UPDATE lichhoc SET maMH=?, maGV=?, thoiGian=?, phongHoc=? WHERE maLichHoc=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maMH.isEmpty() ? null : maMH);
            stmt.setString(2, maGV.isEmpty() ? null : maGV);
            stmt.setTimestamp(3, thoiGian.isEmpty() ? null : Timestamp.valueOf(thoiGian));
            stmt.setString(4, phongHoc.isEmpty() ? null : phongHoc);
            stmt.setInt(5, maLichHoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteLichHoc(int maLichHoc) {
        String sql = "DELETE FROM lichhoc WHERE maLichHoc=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maLichHoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
            return false;
        }
    }

    private void searchByMaMH(String maMH) {
        model.setRowCount(0);
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM lichhoc WHERE maMH = ?")) {
            stmt.setString(1, maMH);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("maLichHoc"));
                row.add(rs.getString("maMH"));
                row.add(rs.getString("maGV"));
                row.add(rs.getTimestamp("thoiGian"));
                row.add(rs.getString("phongHoc"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private void clearFields() {
        tfMaLichHoc.setText("");
        tfMaMH.setText("");
        tfMaGV.setText("");
        tfThoiGian.setText("");
        tfPhongHoc.setText("");
        tfMaMH.requestFocus();
    }
}
