package baitaplon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;
    private SinhVienPanel sinhVienPanel;
    private GiangVienPanel giangVienPanel;
    private KhoaPanel khoaPanel;
    private DiemPanel diemPanel;
    private MonHocPanel monHocPanel;
    private LichHocPanel lichHocPanel;
    private TimKiemPanel timKiemPanel;
    private Connection conn;

    private final String username;
    private final boolean isAdmin;

    public Main(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;

        initializeDatabaseConnection();
        initializeUI();
        setVisible(true);
    }

    private void initializeDatabaseConnection() {
        try {
            conn = connectMySQL.getConnection();
            showMessage("Kết nối cơ sở dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | ClassNotFoundException e) {
            showMessage("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeUI() {
        setTitle("Ứng Dụng Quản Lý Sinh Viên");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createTabbedPane(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JLabel createHeader() {
        JLabel headerLabel = new JLabel("Ứng Dụng Quản Lý Sinh Viên - Người dùng: " + username, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0, 102, 204));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return headerLabel;
    }

    private JTabbedPane createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        try {
            sinhVienPanel = new SinhVienPanel(conn);
            giangVienPanel = new GiangVienPanel(conn);
            khoaPanel = new KhoaPanel(conn);
            diemPanel = new DiemPanel(conn);
            monHocPanel = new MonHocPanel(conn);
            lichHocPanel = new LichHocPanel(conn);
            timKiemPanel = new TimKiemPanel(conn);
        } catch (Exception e) {
            showMessage("Lỗi khởi tạo panel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        tabbedPane.addTab("Quản Lý Sinh Viên", sinhVienPanel);
        tabbedPane.addTab("Quản Lý Giảng Viên", giangVienPanel);
        tabbedPane.addTab("Quản Lý Khoa", khoaPanel);
        tabbedPane.addTab("Quản Lý Điểm", diemPanel);
        tabbedPane.addTab("Quản Lý Môn Học", monHocPanel);
        tabbedPane.addTab("Quản Lý Lịch Học", lichHocPanel);
        tabbedPane.addTab("Tìm Kiếm", timKiemPanel);

        setSearchTabIcon();
        applyRolePermissions();

        return tabbedPane;
    }

    private void setSearchTabIcon() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("search_icon.png"));
            if (icon.getIconWidth() > 0) {
                ImageIcon scaledIcon = new ImageIcon(getScaledImage(icon.getImage(), 16, 16));
                tabbedPane.setIconAt(tabbedPane.getTabCount() - 1, scaledIcon);
            }
        } catch (Exception e) {
            System.out.println("Không thể tải biểu tượng tìm kiếm: " + e.getMessage());
        }
    }

    private void applyRolePermissions() {
        if (!isAdmin) {
            for (int i = 1; i < tabbedPane.getTabCount() - 1; i++) {
                tabbedPane.setEnabledAt(i, false);
            }
            showMessage("Bạn đang đăng nhập với quyền người dùng thường. Một số tính năng bị hạn chế.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Bạn đang đăng nhập với quyền quản trị viên.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogout.setBackground(new Color(204, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(120, 30));
        btnLogout.addActionListener(e -> logout());

        JButton btnExportReport = new JButton("Xuất báo cáo");
        btnExportReport.setFont(new Font("Arial", Font.PLAIN, 14));
        btnExportReport.setBackground(new Color(0, 153, 76));
        btnExportReport.setForeground(Color.WHITE);
        btnExportReport.setPreferredSize(new Dimension(140, 30));
        btnExportReport.addActionListener(e -> exportReport());

        footerPanel.add(btnLogout);
        footerPanel.add(btnExportReport);

        return footerPanel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new DangNhap().setVisible(true);
        }
    }

    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu báo cáo");
        fileChooser.setSelectedFile(new File("BaoCao_ThongKe.pdf"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            BaoCaoThongKe.xuatBaoCao(path, conn);
            showMessage("Đã xuất báo cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Image getScaledImage(Image srcImg, int width, int height) {
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();

        return resizedImg;
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public Connection getConnection() {
        return conn;
    }

    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main("admin", true));
    }
}