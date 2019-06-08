package vn.sefvi.ps09105_lamvanthong_lab4;

public class DanhBa {
    private int ma;
    private  String ten;
    private String phone;
    private String chitiet;

    public DanhBa(int ma, String ten, String phone, String chitiet) {
        this.ma = ma;
        this.ten = ten;
        this.phone = phone;
        this.chitiet = chitiet;
    }

    public String getChitiet() {
        return chitiet;
    }

    public void setChitiet(String chitiet) {
        this.chitiet = chitiet;
    }

    public DanhBa() {
    }

    public DanhBa(int ma, String ten, String phone) {
        this.ma = ma;
        this.ten = ten;
        this.phone = phone;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
