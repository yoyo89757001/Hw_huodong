package megvii.testfacepass.beans;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

@Entity
public class DaKaBean {

    @Id
    private Long id;
    @Uid(5847109750357316730L)
    private String sid;
    private String name;
    private String department;
    private long time;
    private String b64;


    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
