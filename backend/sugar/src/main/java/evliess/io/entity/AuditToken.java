package evliess.io.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "S_AUDIT")
public class AuditToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;
    @Column(name = "s_name")
    private String user;
    @Column(name = "consumed_at")
    private Long consumedAt;
    @Column(name = "s_type")
    private String type;

    public AuditToken() {
    }

    public AuditToken(String user, String token, String type) {
        this.token = token;
        this.user = user;
        this.type = type;
        this.consumedAt = Instant.now().toEpochMilli();
    }

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

    public Long getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(Long consumedAt) {
        this.consumedAt = consumedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
