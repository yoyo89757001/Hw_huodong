package megvii.testfacepass.beans;

public class UserInfo {
    private String id;
    private String name;
    private String department;
    private String b64;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }
}
