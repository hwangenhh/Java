package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KhoaView extends JPanel {
    public JTextField tfMa, tfTen;
    public JButton btnAdd, btnUpdate, btnDelete, btnClear;
    public JTable table;
    public DefaultTableModel tableModel;

    public KhoaView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khoa"));

        tfMa = new JTextField(15);
        tfTen = new JTextField(15);

        formPanel.add(new JLabel("Mã khoa:"));
        formPanel.add(tfMa);
        formPanel.add(new JLabel("Tên khoa:"));
        formPanel.add(tfTen);

        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách khoa"));
        tableModel = new DefaultTableModel(new String[]{"Mã Khoa", "Tên Khoa"}, 0);
        table = new JTable(tableModel);
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(new JScrollPane(table));

        add(formPanel);
        add(buttonPanel);
        add(tablePanel);
    }
}
