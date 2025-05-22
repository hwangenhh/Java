package baitaplon;

import com.itextpdf.text.*; // thư viện dùng để tạo file PDF 
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.sql.*;

public class BaoCaoThongKe {

    public static void xuatBaoCao(String filePath, Connection conn) {
        Document document = new Document();

        try { //khởi tạo file pdf 
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            //Định nghĩa phông chữ và tiêu đề 
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph title = new Paragraph("BÁO CÁO THỐNG KÊ ỨNG DỤNG QUẢN LÝ SINH VIÊN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            //Truy vấn thống kê số lượng 
            // 1. Tổng số sinh viên
            int soSV = getCount("SELECT COUNT(*) FROM sinhvien", conn);
            document.add(new Paragraph("Tổng số sinh viên: " + soSV, normalFont));

            // 2. Tổng số giảng viên
            int soGV = getCount("SELECT COUNT(*) FROM giangvien", conn);
            document.add(new Paragraph("Tổng số giảng viên: " + soGV, normalFont));

            // 3. Tổng số môn học
            int soMH = getCount("SELECT COUNT(*) FROM monhoc", conn);
            document.add(new Paragraph("Tổng số môn học: " + soMH, normalFont));

            // 4. Tổng số khoa
            int soKhoa = getCount("SELECT COUNT(*) FROM khoa", conn);
            document.add(new Paragraph("Tổng số khoa: " + soKhoa, normalFont));

            document.add(new Paragraph(" ", normalFont));

            // 5. Bảng thống kê số sinh viên theo khoa
            document.add(new Paragraph("Thống kê số sinh viên theo khoa:", headerFont));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            table.addCell(new PdfPCell(new Phrase("Tên khoa", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Số sinh viên", headerFont)));

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT k.tenKhoa, COUNT(s.maSV) AS soLuong " +
                "FROM khoa k LEFT JOIN sinhvien s ON k.maKhoa = s.maKhoa " +
                "GROUP BY k.tenKhoa"
            );
            while (rs.next()) {
                table.addCell(new PdfPCell(new Phrase(rs.getString("tenKhoa"), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getInt("soLuong")), normalFont)));
            }
            document.add(table);

            document.close();
            System.out.println("✅ Xuất báo cáo thành công: " + filePath); //kết thúc và đóng file pdf 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hàm phụ trợ truy vấn câu SQL và trả về kết quả có số lượng 
    private static int getCount(String sql, Connection conn) {
    	//sử dụng try-with -recouse để tự động đóng Statement và ResultSet
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        try {
            Connection conn = connectMySQL.getConnection();
            xuatBaoCao("BaoCao_ThongKe.pdf", conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
