package view;

import model.GiangVien;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class GiangVienView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfMaGV, tfTenGV, tfDiaChi, tfSdt, tfLuong;
    private JComboBox<String> cbHocVi, cbGioiTinh, cbMaKhoa, cbChucVu;
    private JDateChooser dateChooserNgaySinh;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public GiangVienView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin giảng viên"));

        JLabel lblMaGV = new JLabel("Mã GV:");
        tfMaGV = new JTextField(20);
        JLabel lblTenGV = new JLabel("Họ tên:");
        tfTenGV = new JTextField(20);
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        dateChooserNgaySinh = new JDateChooser();
        dateChooserNgaySinh.setDateFormatString("yyyy-MM-dd");
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        tfDiaChi = new JTextField(20);
        JLabel lblSdt = new JLabel("SĐT:");
        tfSdt = new JTextField(15);
        JLabel lblMaKhoa = new JLabel("Khoa:");
        cbMaKhoa = new JComboBox<>(new String[] {"CNTT", "DL","Luật", "NN","QTKD", "TDH", "TCKT"});
        JLabel lblHocVi = new JLabel("Học vị:");
        cbHocVi = new JComboBox<>(new String[]{"Thạc sĩ", "Tiến sĩ", "Phó giáo sư", "Giáo sư"});
        JLabel lblChucVu = new JLabel("Chức vụ:");
        cbChucVu = new JComboBox<>(new String[]{"Giảng viên", "Trưởng bộ môn", "Phó khoa", "Trưởng khoa"});
        JLabel lblLuong = new JLabel("Lương:");
        tfLuong = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        formPanel.add(lblMaGV, createGBC(row, 0));
        formPanel.add(tfMaGV, createGBC(row++, 1));
        formPanel.add(lblTenGV, createGBC(row, 0));
        formPanel.add(tfTenGV, createGBC(row++, 1));
        formPanel.add(lblNgaySinh, createGBC(row, 0));
        formPanel.add(dateChooserNgaySinh, createGBC(row++, 1));
        formPanel.add(lblGioiTinh, createGBC(row, 0));
        formPanel.add(cbGioiTinh, createGBC(row++, 1));
        formPanel.add(lblDiaChi, createGBC(row, 0));
        formPanel.add(tfDiaChi, createGBC(row++, 1));
        formPanel.add(lblSdt, createGBC(row, 0));
        formPanel.add(tfSdt, createGBC(row++, 1));
        formPanel.add(lblMaKhoa, createGBC(row, 0));
        formPanel.add(cbMaKhoa, createGBC(row++, 1));
        formPanel.add(lblHocVi, createGBC(row, 0));
        formPanel.add(cbHocVi, createGBC(row++, 1));
        formPanel.add(lblChucVu, createGBC(row, 0));
        formPanel.add(cbChucVu, createGBC(row++, 1));
        formPanel.add(lblLuong, createGBC(row, 0));
        formPanel.add(tfLuong, createGBC(row++, 1));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{
                "Mã GV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Khoa", "Học vị", "Chức vụ", "Lương"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trong bảng
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách giảng viên"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Ví dụ: kiểm tra định dạng lương khi nhấn nút Thêm
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isValidLuong()) {
                    JOptionPane.showMessageDialog(GiangVienView.this,
                            "Lương phải là số thực hợp lệ và không âm!",
                            "Lỗi định dạng",
                            JOptionPane.ERROR_MESSAGE);
                    tfLuong.requestFocus();
                    return;
                }
                // Thực hiện thêm giảng viên nếu hợp lệ
                // ... (gọi controller hoặc xử lý khác)
            }
        });
    }

    private GridBagConstraints createGBC(int row, int col) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    // Phương thức kiểm tra định dạng lương nhập
    public boolean isValidLuong() {
        String luongStr = tfLuong.getText().trim();
        if (luongStr.isEmpty()) {
            return false; // hoặc có thể cho phép trống tùy ý
        }
        try {
            double luong = Double.parseDouble(luongStr);
            return luong >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Các getters/setters và phương thức khác giữ nguyên
    public String getMaGV() {
        return tfMaGV.getText().trim();
    }

    public String getTenGV() {
        return tfTenGV.getText().trim();
    }

    public String getNgaySinh() {
        Date date = dateChooserNgaySinh.getDate();
        return (date != null) ? dateFormat.format(date) : "";
    }

    public String getGioiTinh() {
        return (String) cbGioiTinh.getSelectedItem();
    }

    public String getDiaChi() {
        return tfDiaChi.getText().trim();
    }

    public String getSdt() {
        return tfSdt.getText().trim();
    }

    public String getMaKhoa() {
        return (String) cbMaKhoa.getSelectedItem();
    }

    public String getHocVi() {
        return (String) cbHocVi.getSelectedItem();
    }

    public String getChucVu() {
        return (String) cbChucVu.getSelectedItem();
    }

    public String getLuong() {
        return tfLuong.getText().trim();
    }
    public String getValueAtAsString(int row, int column) {
        Object val = getValueAt(row, column);
        return (val != null) ? val.toString() : "";
    }

    public void setAddButtonListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }

    public void setUpdateButtonListener(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }

    public void setDeleteButtonListener(ActionListener listener) {
        btnDelete.addActionListener(listener);
    }

    public void setClearButtonListener(ActionListener listener) {
        btnClear.addActionListener(listener);
    }

    public void setTableSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    public void clearFields() {
        tfMaGV.setText("");
        tfTenGV.setText("");
        dateChooserNgaySinh.setDate(null);
        cbGioiTinh.setSelectedIndex(0);
        tfDiaChi.setText("");
        tfSdt.setText("");
        cbMaKhoa.setSelectedIndex(0);
        cbHocVi.setSelectedIndex(0);
        cbChucVu.setSelectedIndex(0);
        tfLuong.setText("");
        tfMaGV.requestFocus();
    }

    public void setMaGVEditable(boolean editable) {
        tfMaGV.setEditable(editable);
    }

    public void setFields(String maGV, String tenGV, String ngaySinh, String gioiTinh,
                          String diaChi, String sdt, String maKhoa, String hocVi, String chucVu, String luong) {
        tfMaGV.setText(maGV);
        tfTenGV.setText(tenGV);

        try {
            if (ngaySinh != null && !ngaySinh.isEmpty()) {
                Date date = dateFormat.parse(ngaySinh);
                dateChooserNgaySinh.setDate(date);
            } else {
                dateChooserNgaySinh.setDate(null);
            }
        } catch (Exception e) {
            dateChooserNgaySinh.setDate(null);
        }
        cbGioiTinh.setSelectedItem(gioiTinh);
        tfDiaChi.setText(diaChi);
        tfSdt.setText(sdt);
        cbMaKhoa.setSelectedItem(maKhoa);
        cbHocVi.setSelectedItem(hocVi);
        cbChucVu.setSelectedItem(chucVu);
        tfLuong.setText(luong);
    }

    public Object getValueAt(int row, int column) {
        return model.getValueAt(row, column);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public void displayGiangVienList(List<GiangVien> giangVienList) {
        model.setRowCount(0);
        for (GiangVien gv : giangVienList) {
            String ngaySinhStr = "";
            if (gv.getNgaySinh() != null) {
                ngaySinhStr = dateFormat.format(gv.getNgaySinh());
            }
            model.addRow(new Object[]{
                    gv.getMaGV(),
                    gv.getTenGV(),
                    ngaySinhStr,
                    gv.getGioiTinh(),
                    gv.getDiaChi(),
                    gv.getSdt(),
                    gv.getMaKhoa(),
                    gv.getHocVi(),
                    gv.getChucVu(),
                    gv.getLuong()
            });
        }
    }
}
