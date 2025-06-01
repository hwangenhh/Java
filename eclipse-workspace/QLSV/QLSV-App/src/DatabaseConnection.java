
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    
    // Thông tin kết nối CSDL
    private static final String URL = "jdbc:mysql://localhost:3306/quanlysv";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Phương thức lấy kết nối
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Đăng ký driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Tạo kết nối
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối đến cơ sở dữ liệu: " + e.getMessage());
            }
        }
        return connection;
    }
    
    // Phương thức đóng kết nối
  
}