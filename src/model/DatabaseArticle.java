package model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.io.Serializable;
import java.util.Date;


@Document(collection = "articles", schemaVersion= "1.0")
public class DatabaseArticle implements Serializable {
    //This field will be used as a primary key, every POJO should have one
    @Id
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

    //private Image image;
    @Secret
    private String localisation;
    @Secret
    private String tags;
    private int days_to_save;
    private boolean deleted;
    private String source_url;
    private Date download_date;


    public DatabaseArticle() { }

    public DatabaseArticle(DatabaseArticle item) {
        this.setPublished_date(item.getPublished_date());
        this.setUpdated_date(item.getUpdated_date());
        this.setTitle(item.getTitle());
        this.setDescription(item.getDescription());
        this.setLink(item.getLink());
        this.setAuthor(item.getAuthor());
        this.setCategory(item.getCategory());
        this.setDays_to_save(item.getDays_to_save());
        this.setLocalisation(item.getLocalisation());
        this.setTags(item.getTags());
        this.setDeleted(item.getDeleted());
        this.setSource_url(item.getSource_url());
        this.setDownload_date(item.getDownload_date());
    }

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
    //public Image getImage() { return image; }
    //public void setImage(Image image) { this.image = image; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public boolean getDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public String getSource_url() {return source_url;}
    public void setSource_url(String url){source_url = url;}
    public Date getDownload_date() { return download_date;}
    public void setDownload_date(Date now) { download_date = now;}
}