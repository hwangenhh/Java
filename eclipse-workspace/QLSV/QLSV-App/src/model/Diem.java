package model;

public class Diem {
    private String maSV;
    private String tenSV;
    private String maMH;
    private Float diemSo;

    public Diem(String maSV, String tenSV, String maMH, Float diemSo) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.maMH = maMH;
        this.diemSo = diemSo;
    }

    public String getMaSV() { return maSV; }
    public String getTenSV() { return tenSV; }
    public String getMaMH() { return maMH; }
    public Float getDiemSo() { return diemSo; }

    public void setMaSV(String maSV) { this.maSV = maSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public void setDiemSo(Float diemSo) { this.diemSo = diemSo; }
}
