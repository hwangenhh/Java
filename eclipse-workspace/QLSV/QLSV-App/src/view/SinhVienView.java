package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import com.toedter.calendar.JDateChooser; // cần thêm thư viện jcalendar

public class SinhVienView extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    private JTextField tfMa, tfTen, tfDiaChi, tfSdt, tfLopHanhChinh;
    private JDateChooser dcNgaySinh;
    private JComboBox<String> cbGioiTinh, cbMaKhoa, cbKhoaHoc;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public SinhVienView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels
        String[] labels = {"Mã SV:", "Họ tên:", "Ngày sinh:", "Giới tính:", "Địa chỉ:", "SĐT:", "Khoa:","Lớp hành chính: ", "Khóa: "};

        // Tạo các component input
        tfMa = new JTextField(20);
        tfTen = new JTextField(20);
        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("yyyy-MM-dd");
        dcNgaySinh.setPreferredSize(new Dimension(200, 24));

        cbGioiTinh = new JComboBox<>(new String[] {"Nam", "Nữ", "Khác"});
        cbGioiTinh.setPreferredSize(new Dimension(200, 24));

        tfDiaChi = new JTextField(20);
        tfSdt = new JTextField(20);

        // Ví dụ mã khoa tĩnh, có thể thay bằng danh sách từ DB truyền vào constructor
        cbMaKhoa = new JComboBox<>(new String[] {"CNTT", "DL","Luật", "NN","QTKD", "TDH", "TCKT"});
        cbMaKhoa.setPreferredSize(new Dimension(200, 24));
        
        tfLopHanhChinh = new JTextField(20);
        
        
        cbKhoaHoc = new JComboBox<>(new String[] {"10", "11", "12","13","14","15"});
        cbKhoaHoc.setPreferredSize(new Dimension(200, 24));
        

        Component[] inputs = {tfMa, tfTen, dcNgaySinh, cbGioiTinh, tfDiaChi, tfSdt, cbMaKhoa,tfLopHanhChinh, cbKhoaHoc};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            formPanel.add(inputs[i], gbc);
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
        }

        // Các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Bảng danh sách
        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Khoa", "Lớp hành chính", "Khóa"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Kết hợp layout
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Getters để Controller sử dụng
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }

    public JTextField getTfMa() { return tfMa; }
    public JTextField getTfTen() { return tfTen; }
    public JDateChooser getDcNgaySinh() { return dcNgaySinh; }
    public JComboBox<String> getCbGioiTinh() { return cbGioiTinh; }
    public JTextField getTfDiaChi() { return tfDiaChi; }
    public JTextField getTfSdt() { return tfSdt; }
    public JComboBox<String> getCbMaKhoa() { return cbMaKhoa; }
    public JTextField getTfLopHanhChinh() { return tfLopHanhChinh; }
    public JComboBox<String> getCbKhoaHoc() { return cbKhoaHoc; }

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnClear() { return btnClear; }

    

    // Hàm xóa form nhập liệu
    public void clearForm() {
        tfMa.setText("");
        tfTen.setText("");
        dcNgaySinh.setDate(null);
        cbGioiTinh.setSelectedIndex(0);
        tfDiaChi.setText("");
        tfSdt.setText("");
        cbMaKhoa.setSelectedIndex(0);
        tfLopHanhChinh.setText("");
        cbKhoaHoc.setSelectedIndex(0);
    }
    
}
