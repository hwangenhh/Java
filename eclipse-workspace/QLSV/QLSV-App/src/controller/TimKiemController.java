package controller;

import dao.*;
import view.TimKiemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Vector;

public class TimKiemController {
    private TimKiemView view;

    // Các DAO được truyền từ MainApp
    private SinhVienDAO svDAO;
    private GiangVienDAO gvDAO;
    private MonHocDAO mhDAO;
    private KhoaDAO khoaDAO;
    private DiemDAO diemDAO;

    public TimKiemController(TimKiemView view, SinhVienDAO svDAO, GiangVienDAO gvDAO,
                             MonHocDAO mhDAO, KhoaDAO khoaDAO, DiemDAO diemDAO) {
        this.view = view;
        this.svDAO = svDAO;
        this.gvDAO = gvDAO;
        this.mhDAO = mhDAO;
        this.khoaDAO = khoaDAO;
        this.diemDAO = diemDAO;

        init();
    }

    private void init() {
        updateTableColumns(getCurrentSearchType());

        view.getCboSearchType().addActionListener(e -> {
            updateTableColumns(getCurrentSearchType());
            clearResults();
        });

        view.getBtnSearch().addActionListener(e -> performSearch());

        view.getBtnReset().addActionListener(e -> {
            view.getTxtSearch().setText("");
            clearResults();
        });
    }

    private String getCurrentSearchType() {
        return (String) view.getCboSearchType().getSelectedItem();
    }

    private void updateTableColumns(String searchType) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);

        Vector<String> columns = switch (searchType) {
            case "Sinh viên" -> new Vector<>(java.util.List.of("Mã SV", "Tên SV", "Giới tính", "Ngày sinh", "Mã Khoa"));
            case "Giảng viên" -> new Vector<>(java.util.List.of("Mã GV", "Tên GV", "Học vị", "Mã Khoa"));
            case "Môn học" -> new Vector<>(java.util.List.of("Mã MH", "Tên MH", "Số tín chỉ"));
            case "Khoa" -> new Vector<>(java.util.List.of("Mã Khoa", "Tên Khoa"));
            case "Điểm" -> new Vector<>(java.util.List.of("Mã SV", "Mã MH", "Điểm"));
            default -> new Vector<>();
        };

        model.setColumnIdentifiers(columns);
    }

    private void clearResults() {
        view.getTableModel().setRowCount(0);
    }

    private void performSearch() {
        String keyword = view.getTxtSearch().getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập từ khóa tìm kiếm", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String searchType = getCurrentSearchType();
        clearResults();

        try {
            Vector<Vector<Object>> results = switch (searchType) {
                case "Sinh viên" -> svDAO.search(keyword);
                case "Giảng viên" -> gvDAO.search(keyword);
                case "Môn học" -> mhDAO.search(keyword);
                case "Khoa" -> khoaDAO.search(keyword);
                case "Điểm" -> diemDAO.search(keyword);
                default -> new Vector<>();
            };

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy kết quả phù hợp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Vector<Object> row : results) {
                    view.getTableModel().addRow(row);
                }
                JOptionPane.showMessageDialog(view, "Tìm thấy " + results.size() + " kết quả.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi truy vấn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public void prepareSearch() {
        updateTableColumns(getCurrentSearchType());
    }
}
