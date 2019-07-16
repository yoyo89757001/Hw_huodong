package megvii.testfacepass.beans;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MSRBean {
    @Id
    private Long id;
    private String time;
    private String b64;
    private long time2;

    public long getTime2() {
        return time2;
    }

    public void setTime2(long time2) {
        this.time2 = time2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }
}
