import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Workbook;

import controller.*;
import dao.*;
import view.*;
import model.GiangVien;
import model.Khoa;
import model.MonHoc;
import model.SinhVien;
import utils.BaoCaoThongKe;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainApp extends JFrame {

    private Connection conn;
    private SinhVienView svView;
    private DiemView diemView;
    private JTabbedPane tabbedPane;

    // Controllers
    private SinhVienController svController;
    private GiangVienController gvController;
    private LichHocController lhController;
    private MonHocController mhController;
    private DiemController diemController;
    private KhoaController khoaController;
    private TimKiemController timKiemController;

    private String username;
    private boolean isAdmin;
    private String maSVLoggedIn;

    public MainApp(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.maSVLoggedIn = null;

        setTitle("Hệ thống quản lý sinh viên - " + username + (isAdmin ? " (Admin)" : ""));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectDB();

        // DAO
        SinhVienDAO svDAO = new SinhVienDAO(conn);
        GiangVienDAO gvDAO = new GiangVienDAO(conn);
        LichHocDAO lhDAO = new LichHocDAO(conn);
        MonHocDAO mhDAO = new MonHocDAO(conn);
        DiemDAO diemDAO = new DiemDAO(conn);
        KhoaDAO khoaDAO = new KhoaDAO(conn);

        // View
        svView = new SinhVienView();
        GiangVienView gvView = new GiangVienView();
        LichHocView lhView = new LichHocView();
        MonHocView mhView = new MonHocView();
        diemView = new DiemView();
        KhoaView khoaView = new KhoaView();
        TimKiemView timKiemView = new TimKiemView();
        BaoCaoThongKe baoCaoView = new BaoCaoThongKe(conn); // Thêm view cho báo cáo thống kê

        // Controller
        svController = new SinhVienController(svView, conn);
        gvController = new GiangVienController(gvDAO, gvView);
        lhController = new LichHocController(lhDAO, lhView);
        mhController = new MonHocController(mhView, mhDAO);
        diemController = new DiemController(diemView, diemDAO);
        khoaController = new KhoaController(khoaView, khoaDAO);
        timKiemController = new TimKiemController(timKiemView, svDAO, gvDAO, mhDAO, khoaDAO, diemDAO);

        // Tạo TabbedPane
        tabbedPane = new JTabbedPane();

        // Thêm tabs dựa trên quyền
        if (isAdmin) {
            tabbedPane.addTab("Quản lý Sinh Viên", svView);
            tabbedPane.addTab("Quản lý Giảng Viên", gvView);
            tabbedPane.addTab("Quản lý Lịch Học", lhView);
            tabbedPane.addTab("Quản lý Môn Học", mhView);
            tabbedPane.addTab("Quản lý Điểm", diemView);
            tabbedPane.addTab("Quản lý Khoa", khoaView);
            tabbedPane.addTab("Tìm Kiếm", timKiemView);
            tabbedPane.addTab("Thống Kê", baoCaoView); // Thêm tab thống kê cho admin
        } else {
            tabbedPane.addTab("Thông tin cá nhân", svView);
            tabbedPane.addTab("Xem Điểm", diemView);
            tabbedPane.addTab("Lịch Học", lhView);
            tabbedPane.addTab("Tìm Kiếm", timKiemView);
        }

        add(tabbedPane, BorderLayout.CENTER);

        // Footer: Nút Logout và Export
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("Xuất Excel");
        JButton btnLogout = new JButton("Đăng xuất");

        footerPanel.add(btnExport);
        footerPanel.add(btnLogout);
        add(footerPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện Logout
        btnLogout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView login = new LoginView();
                new LoginController(login);
                login.setVisible(true);
            });
        });

        // Xử lý sự kiện Export Excel
        btnExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setSelectedFile(new java.io.File("BaoCaoThongKe.xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                try {
                    String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).trim();
                    System.out.println("Tab đang chọn: '" + selectedTab + "'");

                    boolean exported = false;

                    if (selectedTab.contains("Sinh Viên")) {
                        SinhVienDAO sinhVienDAO = new SinhVienDAO(conn);
                        List<SinhVien> svList = sinhVienDAO.getAllSinhVien();
                        BaoCaoThongKe.exportSinhVien(svList, filePath);
                        exported = true;
                    } else if (selectedTab.contains("Giảng Viên")) {
                        GiangVienDAO giangVienDAO = new GiangVienDAO(conn);
                        List<GiangVien> gvList = giangVienDAO.getAllGiangVien();
                        BaoCaoThongKe.exportGiangVien(gvList, filePath);
                        exported = true;
                    } else if (selectedTab.contains("Môn Học")) {
                        MonHocDAO monHocDAO = new MonHocDAO(conn);
                        List<MonHoc> mhList = monHocDAO.getAllMonHoc();
                        BaoCaoThongKe.exportMonHoc(mhList, filePath);
                        exported = true;
                    } else if (selectedTab.contains("Thống Kê")) {
                        // Gọi hàm xuất toàn bộ dữ liệu từ BaoCaoThongKe
                        baoCaoView.exportAllDataToExcel(filePath);
                        exported = true;
                    }

                    if (!exported) {
                        System.out.println("Tất cả các tab có sẵn:");
                        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                            System.out.println("Tab " + i + ": '" + tabbedPane.getTitleAt(i) + "'");
                        }
                        JOptionPane.showMessageDialog(this,
                                "Không xác định loại dữ liệu để xuất.\nTab hiện tại: '" + selectedTab + "'");
                        return;
                    }

                    JOptionPane.showMessageDialog(this, "Xuất Excel thành công tại:\n" + filePath);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
                }
            }
        });

        // Load dữ liệu ban đầu
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            svController.loadSinhVien();
            gvController.loadGiangVien();
            lhController.loadLichHoc();
            mhController.loadMonHoc();
            diemController.loadDiem();
            khoaController.loadKhoa();
            timKiemController.prepareSearch();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private void connectDB() {
        String url = "jdbc:mysql://localhost:3306/quanlysv";
        String user = "root";
        String password = "";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối DB thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL!");
            System.exit(1);
        }
    }

    public MainApp(String username, boolean isAdmin, String maSV) {
        this(username, isAdmin);
        this.maSVLoggedIn = maSV;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getMaSVLoggedIn() {
        return maSVLoggedIn;
    }

    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}