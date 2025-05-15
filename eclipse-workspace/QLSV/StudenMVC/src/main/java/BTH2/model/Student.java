package BTH2.model;

public class Student {
    private String id;
    private String name;
    private String phone;
    private String address;

    // ✅ Constructor đúng cần thêm
    public Student(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // ✅ Các getter và setter (nếu chưa có)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
