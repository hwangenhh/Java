import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BaoCaoThongKe {

    // Hàm xuất dữ liệu chung
    private static void exportToExcel(String sheetName, String[] headers, List<String[]> dataRows, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Định dạng header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Ghi header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu
        int rowIndex = 1;
        for (String[] rowData : dataRows) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.length; i++) {
                row.createCell(i).setCellValue(rowData[i]);
            }
        }

        // Tự động co giãn cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi ra file
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    // Xuất danh sách sinh viên
    public static void exportSinhVien(List<model.SinhVien> list, String filePath) throws IOException {
        String[] headers = {"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Khoa","Lớp hành chính ", "Khóa "};
        List<String[]> rows = list.stream().map(sv -> new String[]{
                sv.getMaSV(), sv.getTenSV(), sv.getGioiTinh(), sv.getNgaySinh().toString(), sv.getMaKhoa()
        }).toList();

        exportToExcel("Danh sách Sinh viên", headers, rows, filePath);
    }

    // Xuất danh sách giảng viên
    public static void exportGiangVien(List<model.GiangVien> list, String filePath) throws IOException {
        String[] headers = {"Mã GV", "Họ Tên", "Ngày sinh", "Giới tính","Địa chỉ","SĐT", "Khoa","Học vị", "Chức vụ","Lương" };
        List<String[]> rows = list.stream().map(gv -> new String[]{
                gv.getMaGV(), gv.getTenGV(), gv.getChucVu(), gv.getHocVi(), gv.getMaKhoa()
        }).toList();

        exportToExcel("Danh sách Giảng viên", headers, rows, filePath);
    }

    // Xuất danh sách khoa
    public static void exportKhoa(List<model.Khoa> list, String filePath) throws IOException {
        String[] headers = {"Mã khoa", "Tên khoa"};
        List<String[]> rows = list.stream().map(khoa -> new String[]{
                khoa.getMaKhoa(), khoa.getTenKhoa()
        }).toList();

        exportToExcel("Danh sách Khoa", headers, rows, filePath);
    }

    // Xuất danh sách môn học
    public static void exportMonHoc(List<model.MonHoc> list, String filePath) throws IOException {
        String[] headers = {"Mã môn", "Tên môn", "Số tín chỉ", "Khoa"};
        List<String[]> rows = list.stream().map(mh -> new String[]{
                mh.getMaMon(), mh.getTenMon(), String.valueOf(mh.getSoTinChi()), mh.getMaKhoa()
        }).toList();

        exportToExcel("Danh sách Môn học", headers, rows, filePath);
    }
}
