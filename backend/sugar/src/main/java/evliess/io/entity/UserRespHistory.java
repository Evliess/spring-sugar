package evliess.io.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "S_USERS_RESP_HISTORY")
public class UserRespHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s_name")
    private String username;

    @Column(name = "s_message")
    private String message;

    @Column(name = "consumed_at")
    private Long consumedAt;

    public UserRespHistory() {}

    public UserRespHistory(String username, String message) {
        this.username = username;
        this.message = message;
        this.consumedAt = Instant.now().toEpochMilli();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(Long consumedAt) {
        this.consumedAt = consumedAt;
    }
}
