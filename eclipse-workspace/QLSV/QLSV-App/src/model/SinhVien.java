package model;

import java.sql.Date;

public class SinhVien {
    private String maSV;
    private String tenSV;
    private Date ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String sdt;
    private String maKhoa;
    private String lopHanhChinh;
    private String khoaHoc;
   

    public SinhVien() {
    }

    public SinhVien(String maSV, String tenSV, Date ngaySinh, String gioiTinh, String diaChi, String sdt, String maKhoa, String lopHanhChinh, String khoaHoc) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.maKhoa = maKhoa;
        this.lopHanhChinh = lopHanhChinh;
        this.khoaHoc = khoaHoc;
       
    }

    // Getter & Setter
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    
    public String getLopHanhChinh() { return lopHanhChinh; }
    public void setLopHanhChinh(String lopHanhChinh) { this.lopHanhChinh = lopHanhChinh; }
    
    
    public String getKhoaHoc() { return khoaHoc; }
    public void setKhoaHoc(String khoaHoc) { this.khoaHoc = khoaHoc; }
}
