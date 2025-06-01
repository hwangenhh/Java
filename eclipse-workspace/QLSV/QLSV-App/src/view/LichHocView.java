package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LichHocView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaLichHoc, tfMaMH, tfMaGV, tfPhongHoc, tfSearchMaMH;
    private JComboBox<String> cbThoiGian;  
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    public LichHocView() {
        setLayout(new BorderLayout(10, 10));

        // ============ FORM NHẬP LIỆU ===============
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin lịch học"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        tfMaLichHoc = new JTextField(20);
        tfMaMH = new JTextField(20);
        tfMaGV = new JTextField(20);
        cbThoiGian = new JComboBox<>(new String[] {"Ca 1","Ca 2", "Ca 3", "Ca 4"});  
      
        tfPhongHoc = new JTextField(20);
        tfMaLichHoc.setEditable(false);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Mã lịch học:"), gbc);
        gbc.gridx = 1; formPanel.add(tfMaLichHoc, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Mã môn học:"), gbc);
        gbc.gridx = 1; formPanel.add(tfMaMH, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Mã giảng viên:"), gbc);
        gbc.gridx = 1; formPanel.add(tfMaGV, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Thời gian:"), gbc);
        gbc.gridx = 1; formPanel.add(cbThoiGian, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Phòng học:"), gbc);
        gbc.gridx = 1; formPanel.add(tfPhongHoc, gbc);

        // ============ CÁC NÚT CHỨC NĂNG ===============
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // ============ BẢNG DỮ LIỆU ===============
        model = new DefaultTableModel(new Object[]{"Mã lịch học", "Mã môn học", "Mã giảng viên", "Thời gian", "Phòng học"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // ============ TÌM KIẾM ===============
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tfSearchMaMH = new JTextField(15);
        btnSearch = new JButton("Tìm theo mã môn học");
        searchPanel.add(new JLabel("Mã môn học:"));
        searchPanel.add(tfSearchMaMH);
        searchPanel.add(btnSearch);

        // ============ GỘP GIAO DIỆN ===============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
    
    // ===== GETTERS để Controller sử dụng =====
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }

    public JTextField getTfMaLichHoc() { return tfMaLichHoc; }
    public JTextField getTfMaMH() { return tfMaMH; }
    public JTextField getTfMaGV() { return tfMaGV; }
    public JComboBox<String> getCbThoiGian() { return cbThoiGian; }
    public JTextField getTfPhongHoc() { return tfPhongHoc; }
    public JTextField getTfSearchMaMH() { return tfSearchMaMH; }

    
   
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnSearch() { return btnSearch; }
}
