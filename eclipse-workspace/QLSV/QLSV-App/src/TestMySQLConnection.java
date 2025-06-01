import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMySQLConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanlysinhvien";
        String user = "root";
        String password = ""; // Đổi nếu bạn đặt mật khẩu

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("✅ Kết nối thành công!");
                conn.close();
            } else {
                System.out.println("❌ Kết nối thất bại!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}
