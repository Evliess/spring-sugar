package evliess.io.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "S_USERS_CHAT_HISTORY")
public class SugarUserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s_name")
    private String username;

    @Column(name = "s_message")
    private String message;

    @Column(name = "s_history")
    private String history;

    @Column(name = "consumed_at")
    private Long consumedAt;

    public SugarUserHistory(){}

    public SugarUserHistory(String username, String message, String history) {
        this.username = username;
        this.message = message;
        this.history = history;
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

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Long getConsumedAt() {
        return consumedAt;
    }

    public void setConsumedAt(Long consumedAt) {
        this.consumedAt = consumedAt;
    }
}
