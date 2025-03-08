package evliess.io.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "S_AUDITS")
public class AuditToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;
    @Column(name = "s_name")
    private String user;
    @Column(name = "consumed_at")
    private Date consumedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(Date consumedAt) {
        this.consumedAt = consumedAt;
    }
}
