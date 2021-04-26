package hu.db.techshop.model;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

public class Content {

    private int id;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String title;
    private String content;
    private Timestamp createdAt;

    public Content() {
    }

    public Content(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Content(int id, String title, String content, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
