package hu.db.techshop.model;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Product implements Serializable {

    private int id;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private int categoryId;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String productNumber;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String productName;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String slug;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shortDescription;

    private String description;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private int price;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private double vat;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String image;

    private boolean active;
    private Timestamp createdAt;
    private String categorySlug;
    private List<Comment> comments;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
