package view;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import java.awt.*;

public class LoginView extends JFrame {
    public JTextField txtUsername;
    public JPasswordField txtPassword;
    public JButton btnLogin;

    public LoginView() {
        setTitle("Đăng nhập");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Font & Color
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Color primaryColor = new Color(0, 123, 255);

        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Đăng nhập hệ thống", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(font);
        panel.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField();
        txtUsername.setFont(font);
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(font);
        panel.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField();
        txtPassword.setFont(font);
        panel.add(txtPassword, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(font);
        btnLogin.setBackground(primaryColor);
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(btnLogin, gbc);

        // Thêm listener để kiểm tra và kích hoạt nút
        btnLogin.setEnabled(false); // Ban đầu vô hiệu hóa nút
        DocumentListener docListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { checkFields(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { checkFields(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { checkFields(); }

            private void checkFields() {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                btnLogin.setEnabled(!username.isEmpty() && !password.isEmpty());
            }
        };
        txtUsername.getDocument().addDocumentListener(docListener);
        txtPassword.getDocument().addDocumentListener(docListener);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}