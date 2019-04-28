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
    private String htmlContent;
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
    public void setPublishedDate(Date _publishedDate) { this.publishedDate = _publishedDate; }
    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date _updatedDate) { this.updatedDate = _updatedDate; }
    public String getTitle() { return title; }
    public void setTitle(String _title) { this.title = _title; }
    public String getDescription() { return description; }
    public void setDescription(String _description) { this.description = _description; }
    public String getLink() { return link; }
    public void setLink(String _link) { this.link = _link; }
    public String getAuthor() { return author; }
    public void setAuthor(String _author) { this.author = _author; }
    public String getCategory() { return category; }
    public void setCategory(String _category) { this.category = _category; }
    public int getDaysToSave() { return daysToSave; }
    public void setDaysToSave(int _daysToSave) { this.daysToSave = _daysToSave; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String _localisation) { this.localisation = _localisation; }
    public String getTags() { return tags; }
    public void setTags(String _tags) { this.tags = _tags; }
    public boolean getDeleted() { return deleted; }
    public void setDeleted(boolean _deleted) { this.deleted = _deleted; }
    public String getSourceUrl() {return sourceUrl;}
    public void setSourceUrl(String _url){ sourceUrl = _url;}
    public Date getDownloadDate() { return downloadDate;}
    public void setDownloadDate(Date _now) { downloadDate = _now;}

    public boolean needToBeDeleted() {
        /**
         * Tests if an article is outdated (based on his download date and the days to save the article)
         *
         * @return boolean if an article has to be deleted
         * @see Date
         */
        Date now = new Date();
        Date deletedDate = new Date(getDownloadDate().getTime() + getDaysToSave() * 24 * 60 * 60 * 1000);
        return now.after(deletedDate);
    }
    //public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public String getSource_url() {return sourceUrl;}
    public void setSource_url(String url){sourceUrl = url;}
    public Date getDownload_date() { return downloadDate;}
    public void setDownload_date(Date now) { downloadDate = now;}
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public boolean isDeleted() {
        return deleted;
    }
}