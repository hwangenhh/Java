package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MonHocView extends JPanel {
    public JTextField txtMaMon, txtTenMon;
    public JComboBox<String> cbSoTinChi, cbMaKhoa;
    public JButton btnThem, btnSua, btnXoa;
    public JTable table;
    public DefaultTableModel tableModel;

    public MonHocView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding ngoài cùng

        // ===== Input Panel =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // padding giữa các dòng
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaMon = new JTextField(20);
        txtTenMon = new JTextField(20);
        cbSoTinChi = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        cbMaKhoa = new JComboBox<>(new String[]{
        		"CNTT", "DL","Luật", "NN","QTKD", "TDH", "TCKT"});
        int row = 0;

        // Mã môn
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Mã môn:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMaMon, gbc);

        // Tên môn
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tên môn:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenMon, gbc);

        // Số tín chỉ
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Số tín chỉ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbSoTinChi, gbc);

        // Khoa
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Khoa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbMaKhoa, gbc);

        add(formPanel, BorderLayout.NORTH);

        // ===== Table =====
        tableModel = new DefaultTableModel(new String[]{"Mã môn", "Tên môn", "Số tín chỉ", "Khoa"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
