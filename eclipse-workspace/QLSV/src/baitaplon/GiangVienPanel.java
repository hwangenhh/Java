package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GiangVienPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaGV, tfTenGV, tfNgaySinh, tfGioiTinh, tfDiaChi, tfSdt, tfMaKhoa;
    private JComboBox<String> cbHocVi;
    private Connection conn;

    public GiangVienPanel(Connection conn) {
        this.conn = conn;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin giảng viên"));

        JLabel lblMaGV = new JLabel("Mã GV:");
        tfMaGV = new JTextField(20);
        JLabel lblTenGV = new JLabel("Họ tên:");
        tfTenGV = new JTextField(20);
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        tfNgaySinh = new JTextField(20);
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        tfGioiTinh = new JTextField(10);
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        tfDiaChi = new JTextField(20);
        JLabel lblSdt = new JLabel("SĐT:");
        tfSdt = new JTextField(15);
        JLabel lblMaKhoa = new JLabel("Mã khoa:");
        tfMaKhoa = new JTextField(10);
        JLabel lblHocVi = new JLabel("Học vị:");
        cbHocVi = new JComboBox<>(new String[]{"Cử nhân", "Thạc sĩ", "Tiến sĩ", "Phó giáo sư", "Giáo sư"});

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        formPanel.add(lblMaGV, gbc(row, 0));
        formPanel.add(tfMaGV, gbc(row++, 1));
        formPanel.add(lblTenGV, gbc(row, 0));
        formPanel.add(tfTenGV, gbc(row++, 1));
        formPanel.add(lblNgaySinh, gbc(row, 0));
        formPanel.add(tfNgaySinh, gbc(row++, 1));
        formPanel.add(lblGioiTinh, gbc(row, 0));
        formPanel.add(tfGioiTinh, gbc(row++, 1));
        formPanel.add(lblDiaChi, gbc(row, 0));
        formPanel.add(tfDiaChi, gbc(row++, 1));
        formPanel.add(lblSdt, gbc(row, 0));
        formPanel.add(tfSdt, gbc(row++, 1));
        formPanel.add(lblMaKhoa, gbc(row, 0));
        formPanel.add(tfMaKhoa, gbc(row++, 1));
        formPanel.add(lblHocVi, gbc(row, 0));
        formPanel.add(cbHocVi, gbc(row++, 1));

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

        model = new DefaultTableModel(new String[]{"Mã GV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã khoa", "Học vị"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách giảng viên"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadGiangVienData();

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    tfMaGV.setText(model.getValueAt(selectedRow, 0).toString());
                    tfTenGV.setText(model.getValueAt(selectedRow, 1).toString());
                    tfNgaySinh.setText(model.getValueAt(selectedRow, 2).toString());
                    tfGioiTinh.setText(model.getValueAt(selectedRow, 3).toString());
                    tfDiaChi.setText(model.getValueAt(selectedRow, 4).toString());
                    tfSdt.setText(model.getValueAt(selectedRow, 5).toString());
                    tfMaKhoa.setText(model.getValueAt(selectedRow, 6).toString());
                    cbHocVi.setSelectedItem(model.getValueAt(selectedRow, 7));
                }
            }
        });
    }

    private GridBagConstraints gbc(int row, int col) { //Tạo GridBagConstraints để bố trí label + textfield đúng dòng/cột trong formPanel.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void clearFields() {
        tfMaGV.setText("");
        tfTenGV.setText("");
        tfNgaySinh.setText("");
        tfGioiTinh.setText("");
        tfDiaChi.setText("");
        tfSdt.setText("");
        tfMaKhoa.setText("");
        cbHocVi.setSelectedIndex(0);
        tfMaGV.requestFocus();
    }

    private void loadGiangVienData() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM giangvien");
             ResultSet rs = stmt.executeQuery()) {
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maGV"),
                    rs.getString("tenGV"),
                    rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toString() : "",
                    rs.getString("gioiTinh"),
                    rs.getString("diaChi"),
                    rs.getString("sdt"),
                    rs.getString("maKhoa"),
                    rs.getString("hocVi")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu giảng viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        String maGV = tfMaGV.getText().trim();
        if (maGV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã GV không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (addGiangVien(maGV, tfTenGV.getText(), tfNgaySinh.getText(), tfGioiTinh.getText(), tfDiaChi.getText(), tfSdt.getText(), tfMaKhoa.getText(), (String) cbHocVi.getSelectedItem())) {
            loadGiangVienData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String maGV = tfMaGV.getText().trim();
            if (updateGiangVien(maGV, tfTenGV.getText(), tfNgaySinh.getText(), tfGioiTinh.getText(), tfDiaChi.getText(), tfSdt.getText(), tfMaKhoa.getText(), (String) cbHocVi.getSelectedItem())) {
                loadGiangVienData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Cập nhật giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String maGV = model.getValueAt(selectedRow, 0).toString();
            if (deleteGiangVien(maGV)) {
                loadGiangVienData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Xóa giảng viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean addGiangVien(String maGV, String tenGV, String ngaySinh, String gioiTinh, String diaChi, String sdt, String maKhoa, String hocVi) {
        String sql = "INSERT INTO giangvien (maGV, tenGV, ngaySinh, gioiTinh, diaChi, sdt, maKhoa, hocVi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maGV);
            stmt.setString(2, tenGV);
            stmt.setDate(3, ngaySinh.isEmpty() ? null : java.sql.Date.valueOf(ngaySinh));
            stmt.setString(4, gioiTinh);
            stmt.setString(5, diaChi);
            stmt.setString(6, sdt);
            stmt.setString(7, maKhoa);
            stmt.setString(8, hocVi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm giảng viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean updateGiangVien(String maGV, String tenGV, String ngaySinh, String gioiTinh, String diaChi, String sdt, String maKhoa, String hocVi) {
        String sql = "UPDATE giangvien SET tenGV = ?, ngaySinh = ?, gioiTinh = ?, diaChi = ?, sdt = ?, maKhoa = ?, hocVi = ? WHERE maGV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenGV);
            stmt.setDate(2, ngaySinh.isEmpty() ? null : java.sql.Date.valueOf(ngaySinh));
            stmt.setString(3, gioiTinh);
            stmt.setString(4, diaChi);
            stmt.setString(5, sdt);
            stmt.setString(6, maKhoa);
            stmt.setString(7, hocVi);
            stmt.setString(8, maGV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật giảng viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean deleteGiangVien(String maGV) {
        String sql = "DELETE FROM giangvien WHERE maGV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maGV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa giảng viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
