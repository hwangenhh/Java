package utils;

import dao.*;
import model.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BaoCaoThongKe extends JPanel { // Đổi từ JFrame sang JPanel để tích hợp vào tab
    private Connection conn;
    private JPanel chartPanel;

    // Constructor
    public BaoCaoThongKe(Connection connection) {
        this.conn = connection;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton btnShowChart = new JButton("Hiển thị biểu đồ thống kê");
        JButton btnExportExcel = new JButton("Xuất báo cáo Excel");
        JButton btnRefresh = new JButton("Làm mới dữ liệu");

        controlPanel.add(btnShowChart);
        controlPanel.add(btnExportExcel);
        controlPanel.add(btnRefresh);

        // Chart panel
        chartPanel = new JPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ thống kê"));

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(chartPanel), BorderLayout.CENTER);

        // Event listeners
        btnShowChart.addActionListener(e -> showStatisticsChart());
        btnExportExcel.addActionListener(e -> exportAllDataToExcel());
        btnRefresh.addActionListener(e -> refreshData());

        // Load initial chart
        showStatisticsChart();
    }

    // Hiển thị biểu đồ thống kê
    private void showStatisticsChart() {
        try {
            // Lấy dữ liệu thống kê
            StatisticsData stats = getStatisticsData();

            // Tạo biểu đồ
            ChartPanel chart = new ChartPanel(stats);

            chartPanel.removeAll();
            chartPanel.setLayout(new BorderLayout());
            chartPanel.add(chart, BorderLayout.CENTER);
            chartPanel.revalidate();
            chartPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tạo biểu đồ: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Lấy dữ liệu thống kê từ database
    private StatisticsData getStatisticsData() throws SQLException {
        SinhVienDAO sinhVienDAO = new SinhVienDAO(conn);
        GiangVienDAO giangVienDAO = new GiangVienDAO(conn);
        KhoaDAO khoaDAO = new KhoaDAO(conn);
        MonHocDAO monHocDAO = new MonHocDAO(conn);
        

        int soSinhVien = sinhVienDAO.getAllSinhVien().size();
        int soGiangVien = giangVienDAO.getAllGiangVien().size();
        int soKhoa = khoaDAO.getAllKhoa().size();
        int soMonHoc = monHocDAO.getAllMonHoc().size();

        return new StatisticsData(soSinhVien, soGiangVien, soKhoa, soMonHoc);
    }

    // Làm mới dữ liệu
    private void refreshData() {
        showStatisticsChart();
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!");
    }

    // Xuất tất cả dữ liệu ra Excel
    public void exportAllDataToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu báo cáo Excel");
        fileChooser.setSelectedFile(new java.io.File("BaoCaoTongHop.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                exportAllDataToExcel(filePath);
                JOptionPane.showMessageDialog(this,
                        "Xuất báo cáo Excel thành công tại:\n" + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xuất Excel: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Xuất tất cả dữ liệu vào một file Excel với nhiều sheet
    public void exportAllDataToExcel(String filePath) throws IOException, SQLException {
        Workbook workbook = new XSSFWorkbook();

        // Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
//        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Xuất từng loại dữ liệu
        exportSinhVienToSheet(workbook, headerStyle);
        exportGiangVienToSheet(workbook, headerStyle);
        exportKhoaToSheet(workbook, headerStyle);
        exportMonHocToSheet(workbook, headerStyle);
        exportStatisticsToSheet(workbook, headerStyle);

        // Ghi file
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    // Xuất sinh viên vào sheet
    private void exportSinhVienToSheet(Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet = workbook.createSheet("Danh sách Sinh viên");
        String[] headers = {"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã Khoa", "Lớp hành chính", "Khóa học"};

        SinhVienDAO dao = new SinhVienDAO(conn);
        List<SinhVien> list = dao.getAllSinhVien();

        createHeaderRow(sheet, headers, headerStyle);

        int rowIndex = 1;
        for (SinhVien sv : list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(nvl1(sv.getMaSV()));
            row.createCell(1).setCellValue(nvl1(sv.getTenSV()));
            row.createCell(2).setCellValue(sv.getNgaySinh() != null ? sv.getNgaySinh().toString() : "");
            row.createCell(3).setCellValue(nvl1(sv.getGioiTinh()));
            row.createCell(4).setCellValue(nvl1(sv.getDiaChi()));
            row.createCell(5).setCellValue(nvl1(sv.getSdt()));
            row.createCell(6).setCellValue(nvl1(sv.getMaKhoa()));
            row.createCell(7).setCellValue(nvl1(sv.getLopHanhChinh()));
            row.createCell(8).setCellValue(nvl1(sv.getKhoaHoc()));
        }

        autoSizeColumns(sheet, headers.length);
    }
//xuất giang viên
    private void exportGiangVienToSheet(Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet = workbook.createSheet("Danh sách Giảng viên");
        String[] headers = {"Mã GV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã Khoa", "Học vị", "Chức vụ", "Lương"};
        
        GiangVienDAO dao = new GiangVienDAO(conn);
        List<GiangVien> list = dao.getAllGiangVien();
        
        createHeaderRow(sheet, headers, headerStyle);
        
        int rowIndex = 1;
        for (GiangVien gv : list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(nvl1(gv.getMaGV()));
            row.createCell(1).setCellValue(nvl1(gv.getTenGV()));
            row.createCell(2).setCellValue(gv.getNgaySinh() != null ? gv.getNgaySinh().toString() : "");
            row.createCell(3).setCellValue(nvl1(gv.getGioiTinh()));
            row.createCell(4).setCellValue(nvl1(gv.getDiaChi()));
            row.createCell(5).setCellValue(nvl1(gv.getSdt()));
            row.createCell(6).setCellValue(nvl1(gv.getMaKhoa()));
            row.createCell(7).setCellValue(nvl1(gv.getHocVi()));
            row.createCell(8).setCellValue(nvl1(gv.getChucVu()));
            row.createCell(9).setCellValue(gv.getLuong());
        }
        
        autoSizeColumns(sheet, headers.length);
    }
    // Xuất khoa vào sheet
    private void exportKhoaToSheet(Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet = workbook.createSheet("Danh sách Khoa");
        String[] headers = {"Mã khoa", "Tên khoa"};

        KhoaDAO dao = new KhoaDAO(conn);
        List<Khoa> list = dao.getAllKhoa();

        createHeaderRow(sheet, headers, headerStyle);

        int rowIndex = 1;
        for (Khoa khoa : list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(nvl1(khoa.getMaKhoa()));
            row.createCell(1).setCellValue(nvl1(khoa.getTenKhoa()));
        }

        autoSizeColumns(sheet, headers.length);
    }

    // Xuất môn học vào sheet
    private void exportMonHocToSheet(Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet = workbook.createSheet("Danh sách Môn học");
        String[] headers = {"Mã môn", "Tên môn", "Số tín chỉ", "Mã khoa"};

        MonHocDAO dao = new MonHocDAO(conn);
        List<MonHoc> list = dao.getAllMonHoc();

        createHeaderRow(sheet, headers, headerStyle);

        int rowIndex = 1;
        for (MonHoc mh : list) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(nvl1(mh.getMaMon()));
            row.createCell(1).setCellValue(nvl1(mh.getTenMon()));
            row.createCell(2).setCellValue(nvl1(mh.getSoTinChi()));
            row.createCell(3).setCellValue(nvl1(mh.getMaKhoa()));
        }

        autoSizeColumns(sheet, headers.length);
    }

    // Xuất thống kê tổng hợp
    private void exportStatisticsToSheet(Workbook workbook, CellStyle headerStyle) throws SQLException {
        Sheet sheet = workbook.createSheet("Thống kê tổng hợp");
        String[] headers = {"Loại dữ liệu", "Số lượng"};

        StatisticsData stats = getStatisticsData();

        createHeaderRow(sheet, headers, headerStyle);

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Sinh viên");
        row1.createCell(1).setCellValue(stats.soSinhVien);

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Giảng viên");
        row2.createCell(1).setCellValue(stats.soGiangVien);

        Row row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue("Khoa");
        row3.createCell(1).setCellValue(stats.soKhoa);

        Row row4 = sheet.createRow(4);
        row4.createCell(0).setCellValue("Môn học");
        row4.createCell(1).setCellValue(stats.soMonHoc);

        Row row5 = sheet.createRow(5);
        row5.createCell(0).setCellValue("Tổng cộng");
        row5.createCell(1).setCellValue(stats.getTongSo());

        autoSizeColumns(sheet, headers.length);
    }

    // Helper methods
    private void createHeaderRow(Sheet sheet, String[] headers, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static String nvl1(String value) {
        return value != null ? value : "";
    }

    // Class để lưu dữ liệu thống kê
    private static class StatisticsData {
        int soSinhVien, soGiangVien, soKhoa, soMonHoc;

        StatisticsData(int soSinhVien, int soGiangVien, int soKhoa, int soMonHoc) {
            this.soSinhVien = soSinhVien;
            this.soGiangVien = soGiangVien;
            this.soKhoa = soKhoa;
            this.soMonHoc = soMonHoc;
        }

        int getTongSo() {
            return soSinhVien + soGiangVien + soKhoa + soMonHoc;
        }
    }

    // Panel hiển thị biểu đồ
    private class ChartPanel extends JPanel {
        private StatisticsData data;
        private final Color[] colors = {
                new Color(54, 162, 235),   // Xanh dương (Sinh viên)
                new Color(255, 99, 132) ,   // Đỏ (Giảng viên)
                new Color(255, 205, 86),   // Vàng (Khoa)
                new Color(75, 192, 192)
        };

        ChartPanel(StatisticsData data) {
            this.data = data;
            setPreferredSize(new Dimension(600, 400));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawBarChart(g2d);
            drawLegend(g2d);

            g2d.dispose();
        }

        private void drawBarChart(Graphics2D g2d) {
            int[] values = {data.soSinhVien, data.soGiangVien, data.soKhoa, data.soMonHoc}; // Cập nhật mảng values
            String[] labels = {"Sinh viên", "Giảng viên", "Khoa", "Môn học"};

            int maxValue = Math.max(1, java.util.Arrays.stream(values).max().orElse(1));
            int chartWidth = getWidth() - 200;
            int chartHeight = getHeight() - 150;
            int barWidth = chartWidth / (values.length * 2); // Tính lại barWidth dựa trên 4 cột
            int startX = 80;
            int startY = 50;

            // Vẽ trục
            g2d.setColor(Color.BLACK);
            g2d.drawLine(startX, startY + chartHeight, startX + chartWidth, startY + chartHeight); // Trục X
            g2d.drawLine(startX, startY, startX, startY + chartHeight); // Trục Y

            // Vẽ nhãn trục Y
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            int numTicks = 5;
            for (int i = 0; i <= numTicks; i++) {
                int y = startY + chartHeight - (i * chartHeight / numTicks);
                int tickValue = (int) ((double) i / numTicks * maxValue);
                g2d.drawString(String.valueOf(tickValue), startX - 40, y + 5);
                g2d.drawLine(startX - 5, y, startX, y); // Vạch chia
            }

            // Vẽ các cột
            for (int i = 0; i < values.length; i++) { // Vòng lặp chạy đến 4 cột
                int barHeight = (int) ((double) values[i] / maxValue * chartHeight);
                int x = startX + (i * 2 + 1) * barWidth;
                int y = startY + chartHeight - barHeight;

                // Vẽ cột
                g2d.setColor(colors[i]);
                g2d.fillRect(x, y, barWidth, barHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, barWidth, barHeight);

                // Vẽ nhãn
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(labels[i]);
                g2d.drawString(labels[i], x + (barWidth - labelWidth) / 2, startY + chartHeight + 20);

                // Vẽ giá trị trên cột
                String valueStr = String.valueOf(values[i]);
                int valueWidth = fm.stringWidth(valueStr);
                g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, y - 5);
            }

            // Vẽ tiêu đề
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String title = "BIỂU ĐỒ THỐNG KÊ SINH VIÊN, GIẢNG VIÊN, KHOA VÀ MÔN HỌC"; // Cập nhật tiêu đề
            FontMetrics titleFm = g2d.getFontMetrics();
            int titleWidth = titleFm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, 25);
        }

        private void drawLegend(Graphics2D g2d) {
            int legendX = getWidth() - 180;
            int legendY = 80;
            String[] labels = {"Sinh viên", "Giảng viên", "Khoa", "Môn học"};
            int[] values = {data.soSinhVien, data.soGiangVien, data.soKhoa, data.soMonHoc};

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(Color.BLACK);
            g2d.drawString("Chú thích:", legendX, legendY - 10);

            for (int i = 0; i < labels.length; i++) {
                int y = legendY + i * 25;
                g2d.setColor(colors[i]);
                g2d.fillRect(legendX, y, 15, 15);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(legendX, y, 15, 15);
                g2d.drawString(labels[i] + ": " + values[i], legendX + 20, y + 12);
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Tổng: " + data.getTongSo(), legendX, legendY + labels.length * 25 + 10);
        }
    }

    public static void exportSinhVien(List<SinhVien> list, String filePath) throws IOException {
        exportToExcel("Danh sách Sinh viên",
                new String[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã Khoa", "Lớp hành chính", "Khóa học"},
                list.stream().map(sv -> new String[]{
                        nvl1(sv.getMaSV()), nvl1(sv.getTenSV()),
                        sv.getNgaySinh() != null ? sv.getNgaySinh().toString() : "",
                        nvl1(sv.getGioiTinh()), nvl1(sv.getDiaChi()), nvl1(sv.getSdt()),
                        nvl1(sv.getMaKhoa()), nvl1(sv.getLopHanhChinh()), nvl1(sv.getKhoaHoc())
                }).toList(), filePath);
    }

    public static void exportGiangVien(List<GiangVien> list, String filePath) throws IOException {
        exportToExcel("Danh sách Giảng viên",
                new String[]{"Mã GV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã Khoa", "Học vị", "Chức vụ", "Lương"},
                list.stream().map(gv -> new String[]{
                        nvl1(gv.getMaGV()), nvl1(gv.getTenGV()),
                        gv.getNgaySinh() != null ? gv.getNgaySinh().toString() : "",
                        nvl1(gv.getGioiTinh()), nvl1(gv.getDiaChi()), nvl1(gv.getSdt()),
                        nvl1(gv.getMaKhoa()), nvl1(gv.getHocVi()), nvl1(gv.getChucVu()),
                        String.valueOf(gv.getLuong())
                }).toList(), filePath);
    }

    public static void exportKhoa(List<Khoa> list, String filePath) throws IOException {
        exportToExcel("Danh sách Khoa",
                new String[]{"Mã khoa", "Tên khoa"},
                list.stream().map(khoa -> new String[]{
                        nvl1(khoa.getMaKhoa()), nvl1(khoa.getTenKhoa())
                }).toList(), filePath);
    }

    public static void exportMonHoc(List<MonHoc> list, String filePath) throws IOException {
        exportToExcel("Danh sách Môn học",
                new String[]{"Mã môn", "Tên môn", "Số tín chỉ", "Mã khoa"},
                list.stream().map(mh -> new String[]{
                        nvl1(mh.getMaMon()), nvl1(mh.getTenMon()),
                        nvl1(mh.getSoTinChi()), nvl1(mh.getMaKhoa())
                }).toList(), filePath);
    }

    private static void exportToExcel(String sheetName, String[] headers, List<String[]> dataRows, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowIndex = 1;
        for (String[] rowData : dataRows) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(rowData[i] != null ? rowData[i] : "");
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String url = "jdbc:mysql://localhost:3306/quanlysv";
                String user = "root";
                String password = "";
                Connection conn = DriverManager.getConnection(url, user, password);

                JFrame frame = new JFrame("Test Báo Cáo Thống Kê");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.add(new BaoCaoThongKe(conn));
                frame.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Không thể kết nối CSDL!");
            }
        });
    }
}
    