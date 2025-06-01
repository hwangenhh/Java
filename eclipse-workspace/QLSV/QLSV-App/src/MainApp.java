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
        this.maSVLoggedIn = maSVLoggedIn;

        setTitle("Hệ thống quản lý sinh viên  - " + username + (isAdmin ? " (Admin)" : ""));
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
        SinhVienView svView = new SinhVienView();
        GiangVienView gvView = new GiangVienView();
        LichHocView lhView = new LichHocView();
        MonHocView mhView = new MonHocView();
        DiemView diemView = new DiemView();
        KhoaView khoaView = new KhoaView();
        TimKiemView timKiemView = new TimKiemView();

        // Controller
        svController = new SinhVienController(svView, conn);
        gvController = new GiangVienController(gvDAO, gvView);
        lhController = new LichHocController(lhDAO, lhView);
        mhController = new MonHocController(mhView, mhDAO);
        diemController = new DiemController(diemView, diemDAO);
        khoaController = new KhoaController(khoaView, khoaDAO);
        timKiemController = new TimKiemController(timKiemView, svDAO, gvDAO, mhDAO, khoaDAO, diemDAO);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý Sinh Viên", svView);
        tabbedPane.addTab("Quản lý Giảng Viên", gvView);
        tabbedPane.addTab("Quản lý Lịch Học", lhView);
        tabbedPane.addTab("Quản lý Môn Học", mhView);
        tabbedPane.addTab("Quản lý Điểm", diemView);
        tabbedPane.addTab("Quản lý Khoa", khoaView);
        tabbedPane.addTab("Tìm Kiếm", timKiemView);
        

     // Tabs

        if (isAdmin) {
            // Admin có quyền xem tất cả
            tabbedPane.addTab(" Quản lý Sinh Viên", svView);
            tabbedPane.addTab("Quản lý Giảng Viên", gvView);
            tabbedPane.addTab("Quản lý Lịch Học", lhView);
            tabbedPane.addTab("Quản lý Môn Học", mhView);
            tabbedPane.addTab("Quản lý Điểm", diemView);
            tabbedPane.addTab("Quản lý Khoa", khoaView);
            tabbedPane.addTab("Tìm Kiếm", timKiemView);
        } else {
            // Sinh viên chỉ được xem thông tin cá nhân, điểm và lịch học
            tabbedPane.addTab("Quản lý Sinh Viên", svView);
            tabbedPane.addTab("Quản lý Lịch Học", lhView);
            tabbedPane.addTab("Quản lý Điểm", diemView);
        }
        

        add(tabbedPane, BorderLayout.CENTER);
        

        // Footer: Nút Logout và Export
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("Xuất Excel");
        JButton btnLogout = new JButton("Đăng xuất");

        footerPanel.add(btnExport);
        footerPanel.add(btnLogout);
        add(footerPanel, BorderLayout.SOUTH);

        btnLogout.addActionListener(e -> {
            dispose(); // Đóng MainApp
            SwingUtilities.invokeLater(() -> {
                LoginView login = new LoginView();
                new LoginController(login);
                login.setVisible(true);
            });
        });

        btnExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setSelectedFile(new java.io.File("BaoCaoThongKe.xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                try {
                    // Lấy dữ liệu và xuất theo loại được chọn
                    String selectedTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                    System.out.println("Tab đang chọn: " + selectedTab);


                    switch (selectedTab) {
                        case "Quản lý Sinh Viên" -> {
                            SinhVienDAO sinhVienDAO = new SinhVienDAO(conn);
                            List<SinhVien> svList = sinhVienDAO.getAllSinhVien();
                            BaoCaoThongKe.exportSinhVien(svList, filePath);
                        }
                        case "Quản lý Giảng Viên" -> {
                            GiangVienDAO giangVienDAO = new GiangVienDAO(conn);
                            List<GiangVien> gvList = giangVienDAO.getAllGiangVien();
                            BaoCaoThongKe.exportGiangVien(gvList, filePath);
                        }
                        case "Quản lý Khoa" -> {
                            KhoaDAO KhoaDAO = new KhoaDAO(conn);
                            List<Khoa> khoaList = KhoaDAO.getAllKhoa();
                            BaoCaoThongKe.exportKhoa(khoaList, filePath);
                        }
                        case "Quản lý Môn học" -> {
                            MonHocDAO monHocDAO = new MonHocDAO(conn);
                            List<MonHoc> mhList = monHocDAO.getAllMonHoc();
                            BaoCaoThongKe.exportMonHoc(mhList, filePath);
                        }
                        default -> {
                            JOptionPane.showMessageDialog(this, "Không xác định loại dữ liệu để xuất.");
                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(this, "Xuất Excel thành công tại:\n" + filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
                }
            }
        });
        if (!isAdmin) {
            btnExport.setVisible(false); // Ẩn nút export nếu là sinh viên
        }


        // Load dữ liệu ban đầu
        svController.loadSinhVien();
        gvController.loadGiangVien();
        lhController.loadLichHoc();
        mhController.loadMonHoc();
        diemController.loadDiem();
        khoaController.loadKhoa();
        timKiemController.prepareSearch();
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
        }
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}