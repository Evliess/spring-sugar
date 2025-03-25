package evliess.io.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "S_DICT")
public class NameDict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "s_name")
    private String name;
    @Column(name = "s_meaning")
    private String meaning;
    @Column(name = "s_type")
    private String type;
    @Column(name = "s_other")
    private String other;

    public NameDict() {
    }
    public NameDict(String name, String meaning, String type, String other) {
        this.name = name;
        this.meaning = meaning;
        this.type = type;
        this.other = "";
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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
