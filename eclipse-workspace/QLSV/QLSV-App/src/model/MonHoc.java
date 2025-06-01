package model;

public class MonHoc {
    private String maMon;
    private String tenMon;
    private String soTinChi;
    private String maKhoa;
   

    public MonHoc(String maMon, String tenMon, String soTinChi, String maKhoa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        this.maKhoa = maKhoa;
    }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public String getSoTinChi() { return soTinChi; }
    public void setSoTinChi(String soTinChi) { this.soTinChi = soTinChi; }
    
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    
    
}
