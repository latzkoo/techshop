package hu.db.techshop.model;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

public class Category {

    private int id;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String categoryName;
    private String slug;
    private boolean active;
    private Timestamp createdAt;
    private int productCount;

    public Category() {
    }

    public Category(String categoryName, String slug, boolean active) {
        this.categoryName = categoryName;
        this.slug = slug;
        this.active = active;
    }

    public Category(int id, String categoryName, String slug, boolean active, Timestamp createdAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.slug = slug;
        this.active = active;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
