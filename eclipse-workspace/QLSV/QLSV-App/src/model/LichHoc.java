package model;

public class LichHoc {
    private String maLichHoc;
    private String maMH;
    private String maGV;
    private String thoiGian;
    private String phongHoc;

    public LichHoc(String maLichHoc, String maMH, String maGV, String thoiGian, String phongHoc) {
        this.maLichHoc = maLichHoc;
        this.maMH = maMH;
        this.maGV = maGV;
        this.thoiGian = thoiGian;
        this.phongHoc = phongHoc;
    }


    public String getMaLichHoc() { return maLichHoc; }
    public String getMaMH() { return maMH; }
    public String getMaGV() { return maGV; }
    public String getThoiGian() { return thoiGian; }
    public String getPhongHoc() { return phongHoc; }

    public void setMaLichHoc(String maLichHoc) { this.maLichHoc = maLichHoc; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public void setMaGV(String maGV) { this.maGV = maGV; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }
    public void setPhongHoc(String phongHoc) { this.phongHoc = phongHoc; }
}
