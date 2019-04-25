package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.io.Serializable;
import java.util.Date;

/**
 * Class DatabaseArtcle where all the articles are stored
 * @see DatabaseArticle
 */
@Document(collection = "articles", schemaVersion= "1.0")
public class DatabaseArticle implements Serializable {
    //This field will be used as a primary key, every POJO should have one
    @Id
    private String link;
    private Date publishedDate;
    private Date updatedDate;
    @Secret
    private String title;
    @Secret
    private String description;
    @Secret
    private String author;
    @Secret
    private String category;

    @Secret
    private String localisation;
    @Secret
    private String tags;
    private int daysToSave;
    private boolean deleted;
    private String sourceUrl;
    private Date downloadDate;


    public DatabaseArticle() { }

    public DatabaseArticle(DatabaseArticle _item) {
        this.setPublishedDate(_item.getPublishedDate());
        this.setUpdatedDate(_item.getUpdatedDate());
        this.setTitle(_item.getTitle());
        this.setDescription(_item.getDescription());
        this.setLink(_item.getLink());
        this.setAuthor(_item.getAuthor());
        this.setCategory(_item.getCategory());
        this.setDaysToSave(_item.getDaysToSave());
        this.setLocalisation(_item.getLocalisation());
        this.setTags(_item.getTags());
        this.setDeleted(_item.getDeleted());
        this.setSourceUrl(_item.getSourceUrl());
        this.setDownloadDate(_item.getDownloadDate());
    }

    /*
    All the methods to get/set infos from an article
     */

    public Date getPublishedDate() { return publishedDate; }
    public void setPublishedDate(Date publishedDate) { this.publishedDate = publishedDate; }
    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }
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
    public int getDaysToSave() { return daysToSave; }
    public void setDaysToSave(int daysToSave) { this.daysToSave = daysToSave; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public boolean getDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public String getSourceUrl() {return sourceUrl;}
    public void setSourceUrl(String url){
        sourceUrl = url;}
    public Date getDownloadDate() { return downloadDate;}
    public void setDownloadDate(Date now) { downloadDate = now;}

    public boolean needToBeDeleted() {
        /**
         * Tests if an article is outdated (based on his download date and the days to save the article)
         *
         * @return boolean if an article has to be deleted
         * @see Date
         */
        Date now = new Date();
        Date delete_date = new Date(getDownloadDate().getTime() + getDaysToSave() * 24 * 60 * 60 * 1000);
        return now.after(delete_date);
    }
}