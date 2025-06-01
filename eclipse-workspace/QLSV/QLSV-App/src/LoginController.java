import model.User;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {

    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.loginView.btnLogin.addActionListener(new LoginListener());
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.txtUsername.getText().trim();
            String password = new String(loginView.txtPassword.getPassword()).trim();

            // Kiểm tra đăng nhập trống
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginView, "Vui lòng nhập đầy đủ thông tin đăng nhập.");
                return;
            }

            // Phân quyền
            if (username.equals("admin") && password.equals("123")) {
                // Admin đăng nhập
                JOptionPane.showMessageDialog(loginView, "Đăng nhập thành công với quyền Quản trị viên.");
                loginView.dispose();
                MainApp mainApp = new MainApp(username, true); // true = isAdmin
                mainApp.setVisible(true);

            } else if (password.equals("1234")) {
                // Sinh viên đăng nhập bằng mã sinh viên + mật khẩu mặc định
                // Ở đây có thể thêm validation để kiểm tra mã sinh viên có tồn tại trong DB không
                if (isValidStudentCode(username)) {
                    JOptionPane.showMessageDialog(loginView, "Đăng nhập thành công với quyền Sinh viên.");
                    loginView.dispose();
                    MainApp mainApp = new MainApp(username, false); // false = isAdmin, username là mã SV
                    mainApp.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginView, "Mã sinh viên không hợp lệ.");
                }

            } else {
                JOptionPane.showMessageDialog(loginView, "Sai tên đăng nhập hoặc mật khẩu.");
            }
        }
    }
    
    /**
     * Kiểm tra mã sinh viên có hợp lệ không
     * Có thể kết nối DB để kiểm tra hoặc validate format
     */
    private boolean isValidStudentCode(String studentCode) {
        // Ví dụ kiểm tra format mã sinh viên (có thể tùy chỉnh theo quy định)
        // Mã sinh viên thường có format như: SV001, 2021001, etc.
        
        // Kiểm tra cơ bản: không rỗng và có độ dài phù hợp
        if (studentCode == null || studentCode.length() < 3) {
            return false;
        }
        
        // Có thể thêm logic kết nối DB để kiểm tra mã SV có tồn tại không
        // Ví dụ:
        /*
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlysv", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM sinhvien WHERE maSV = ?");
            stmt.setString(1, studentCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
        */
        
        // Tạm thời return true để test
        return true;
    }
}