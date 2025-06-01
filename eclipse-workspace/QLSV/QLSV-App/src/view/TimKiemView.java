package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimKiemView extends JPanel {
    private JComboBox<String> cboSearchType;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnReset;
    private JTable tableResult;
    private DefaultTableModel tableModel;

    public TimKiemView() {
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

        tableModel = new DefaultTableModel();
        tableResult = new JTable(tableModel);
        tableResult.setFont(new Font("Arial", Font.PLAIN, 14));
        tableResult.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableResult.setRowHeight(25);
        tableResult.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tableResult);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Các getter để Controller dùng truy cập:
    public JComboBox<String> getCboSearchType() {
        return cboSearchType;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnReset() {
        return btnReset;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
