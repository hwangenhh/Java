package baitaplon;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DangNhap extends JFrame {
    public DangNhap() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel chính
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        getContentPane().add(panel, BorderLayout.CENTER);

        // Tiêu đề
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        titlePanel.add(lblTitle);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Panel nhập liệu
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        panel.add(formPanel, BorderLayout.CENTER);

        // Tạo GridBagConstraints cho từng thành phần
        // Nhãn Tên đăng nhập
        GridBagConstraints gbcLblUser = new GridBagConstraints();
        gbcLblUser.insets = new Insets(10, 10, 10, 10);
        gbcLblUser.fill = GridBagConstraints.HORIZONTAL;
        gbcLblUser.gridx = 0;
        gbcLblUser.gridy = 0;

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUser.setForeground(new Color(0, 51, 102));
        formPanel.add(lblUser, gbcLblUser);

        // Ô nhập Tên đăng nhập
        GridBagConstraints gbcTxtUser = new GridBagConstraints();
        gbcTxtUser.insets = new Insets(10, 10, 10, 10);
        gbcTxtUser.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtUser.gridx = 1;
        gbcTxtUser.gridy = 0;

        JTextField txtUser = new JTextField(15);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUser.setBackground(new Color(245, 245, 245));
        txtUser.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        formPanel.add(txtUser, gbcTxtUser);

        // Nhãn Mật khẩu
        GridBagConstraints gbcLblPass = new GridBagConstraints();
        gbcLblPass.insets = new Insets(10, 10, 10, 10);
        gbcLblPass.fill = GridBagConstraints.HORIZONTAL;
        gbcLblPass.gridx = 0;
        gbcLblPass.gridy = 1;

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPass.setForeground(new Color(0, 51, 102));
        formPanel.add(lblPass, gbcLblPass);

        // Ô nhập Mật khẩu
        GridBagConstraints gbcTxtPass = new GridBagConstraints();
        gbcTxtPass.insets = new Insets(10, 10, 10, 10);
        gbcTxtPass.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtPass.gridx = 1;
        gbcTxtPass.gridy = 1;

        JPasswordField txtPass = new JPasswordField(15);
        txtPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPass.setBackground(new Color(245, 245, 245));
        txtPass.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        formPanel.add(txtPass, gbcTxtPass);

        // Panel nút Đăng nhập
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));

        // Hover effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 120, 60));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 153, 76));
            }
        });

        buttonPanel.add(btnLogin);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý đăng nhập
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUser.getText();
                String pass = new String(txtPass.getPassword());
                if (user.equals("admin") && pass.equals("123")) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                    dispose();
                    new Main(user, true); // mở giao diện Main cho admin
                } else if (pass.equals("1234")) {
                    dispose();
                    new Main(user, false); // mở giao diện Main cho sinh viên
                } else {
                    JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangNhap().setVisible(true));
    }
}