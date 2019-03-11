package model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.util.Date;


@Document(collection = "articles", schemaVersion= "1.0")
public class DatabaseArticle {
    //This field will be used as a primary key, every POJO should have one
    @Id @Secret
    private String link;
    private Date published_date;
    private Date updated_date;
    @Secret
    private String title;
    @Secret
    private String description;
    @Secret
    private String author;
    @Secret
    private String category;
    private int days_to_save;


    public DatabaseArticle() { }

    public Date getPublished_date() { return published_date; }
    public void setPublished_date(Date published_date) { this.published_date = published_date; }
    public Date getUpdated_date() { return updated_date; }
    public void setUpdated_date(Date updated_date) { this.updated_date = updated_date; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getDays_to_save() { return days_to_save; }
    public void setDays_to_save(int days_to_save) { this.days_to_save = days_to_save; }
}