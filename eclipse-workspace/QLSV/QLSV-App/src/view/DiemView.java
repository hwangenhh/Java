package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DiemView extends JPanel {
    public JTextField tfMaSV, tfHoTen, tfMaMH, tfDiem;
    public JButton btnAdd, btnEdit, btnDelete, btnClear;
    public JTable table;
    public DefaultTableModel model;

    public DiemView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin điểm"));
        formPanel.setBackground(Color.WHITE);

        tfMaSV = new JTextField(15);
        tfHoTen = new JTextField(15); tfHoTen.setEditable(false);
        tfMaMH = new JTextField(15);
        tfDiem = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Mã SV:", "Họ tên:", "Mã môn học:", "Điểm:"};
        JTextField[] fields = {tfMaSV, tfHoTen, tfMaMH, tfDiem};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        // Bảng hiển thị điểm
        model = new DefaultTableModel(new String[]{"Mã SV", "Họ Tên", "Mã MH", "Điểm"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Nút chức năng
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Thêm Điểm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
