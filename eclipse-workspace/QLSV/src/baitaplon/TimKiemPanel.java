package baitaplon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class TimKiemPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JComboBox<String> cboSearchType;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReset;
    private JTable tableResult;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private Connection conn;

    public TimKiemPanel(Connection conn) {
        this.conn = conn; // kết nối CSDL 
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lblSearchType = new JLabel("Tìm kiếm theo:");
        lblSearchType.setFont(new Font("Arial", Font.BOLD, 14));

        String[] searchTypes = {"Sinh viên", "Giảng viên", "Môn học", "Khoa", "Điểm"};
        cboSearchType = new JComboBox<>(searchTypes);
        cboSearchType.setPreferredSize(new Dimension(150, 30));

        JLabel lblKeyword = new JLabel("Từ khóa:");
        lblKeyword.setFont(new Font("Arial", Font.BOLD, 14));

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 30));

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSearch.setBackground(new Color(0, 102, 204));
        btnSearch.setForeground(Color.WHITE);

        btnReset = new JButton("Làm mới");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReset.setBackground(new Color(128, 128, 128));
        btnReset.setForeground(Color.WHITE);

        searchPanel.add(lblSearchType);
        searchPanel.add(cboSearchType);
        searchPanel.add(lblKeyword);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);

        add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(); // tạo bảng và model điều khiển dữ liệu hiển thị 
        tableResult = new JTable(tableModel);
        tableResult.setFont(new Font("Arial", Font.PLAIN, 14));
        tableResult.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableResult.setRowHeight(25);
        tableResult.setAutoCreateRowSorter(true);

        scrollPane = new JScrollPane(tableResult);
        add(scrollPane, BorderLayout.CENTER);

        updateTableColumns("Sinh viên");

        cboSearchType.addActionListener(e -> { // xử lý sự kiện tìm kiếm 
            String selectedType = (String) cboSearchType.getSelectedItem();
            updateTableColumns(selectedType);
        });

        btnSearch.addActionListener(e -> performSearch()); 

        btnReset.addActionListener(e -> { // nút làm mới 
            txtSearch.setText("");
            tableModel.setRowCount(0);
        });
    }

    private void updateTableColumns(String searchType) { //cập nhật bảng kết quả theo loại dữ liệu tìm kiếm 
        tableModel.setRowCount(0);
        switch (searchType) {
            case "Sinh viên" -> tableModel.setColumnIdentifiers(new Object[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã khoa"});
            case "Giảng viên" -> tableModel.setColumnIdentifiers(new Object[]{"Mã GV", "Họ tên", "Học vị", "Giới tính", "Ngày sinh", "Địa chỉ", "SĐT", "Mã khoa"});
            case "Môn học" -> tableModel.setColumnIdentifiers(new Object[]{"Mã MH", "Tên môn học", "Số tín chỉ", "Mã khoa"});
            case "Khoa" -> tableModel.setColumnIdentifiers(new Object[]{"Mã khoa", "Tên khoa"});
            case "Điểm" -> tableModel.setColumnIdentifiers(new Object[]{"Mã SV", "Tên SV", "Mã MH", "Tên MH", "Điểm"});
        }
    }

    private void performSearch() { // thực hiện truy vấn tìm kiếm với từ khóa 
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String searchType = (String) cboSearchType.getSelectedItem();
        tableModel.setRowCount(0);

        try {
            String query = buildSearchQuery(searchType); //lấy câu truy vấn tương ứng từ SQL 
            PreparedStatement pstmt = conn.prepareStatement(query);

            int paramCount = pstmt.getParameterMetaData().getParameterCount();
            for (int i = 1; i <= paramCount; i++) {
                pstmt.setString(i, "%" + keyword + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả phù hợp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tìm thấy " + tableModel.getRowCount() + " kết quả.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String buildSearchQuery(String searchType) { // phương thức Trả về câu SQL tìm kiếm phù hợp với loại tìm kiếm


        return switch (searchType) {
            case "Sinh viên" -> "SELECT maSV, tenSV, ngaySinh, gioitinh, diaChi, sdt, maKhoa FROM sinhvien " +
                    "WHERE maSV LIKE ? OR tenSV LIKE ? OR ngaySinh LIKE ? OR gioitinh LIKE ? OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ?";
            case "Giảng viên" -> "SELECT maGV, tenGV, hocVi, gioitinh, ngaySinh, diaChi, sdt, maKhoa FROM giangvien " +
                    "WHERE maGV LIKE ? OR tenGV LIKE ? OR hocVi LIKE ? OR gioitinh LIKE ? OR ngaySinh LIKE ? OR diaChi LIKE ? OR sdt LIKE ? OR maKhoa LIKE ?";
            case "Môn học" -> "SELECT maMH, tenMH, soTinChi, maKhoa FROM monhoc " +
                    "WHERE maMH LIKE ? OR tenMH LIKE ? OR maKhoa LIKE ?";
            case "Khoa" -> "SELECT maKhoa, tenKhoa FROM khoa " +
                    "WHERE maKhoa LIKE ? OR tenKhoa LIKE ?";
            case "Điểm" -> {
            	// Xử lý các trường số với cách xử lý LIKE thích hợp chỉ dành cho các trường văn bản
                String query = "SELECT d.maSV, sv.tenSV, d.maMH, mh.tenMH, d.diemSo FROM diem d " +
                        "JOIN sinhvien sv ON d.maSV = sv.maSV " +
                        "JOIN monhoc mh ON d.maMH = mh.maMH " +
                        "WHERE d.maSV LIKE ? OR sv.tenSV LIKE ? OR d.maMH LIKE ? OR mh.tenMH LIKE ?";
                yield query; // Trả về truy vấn tìm kiếm Điểm
            }
            default -> "";
        };
    }
}
