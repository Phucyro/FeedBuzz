package controller;

import java.util.Date;

public class Article {
    private Date published_date;
    private Date updated_date;
    private String title;
    private String description;
    private String link;
    private String author;
    private String category;


    Article(){
    }

    public Date get_published_date() {
        return published_date;
    }

    void set_published_date(Date published_date) {
        this.published_date = published_date;
    }

    public Date get_updated_date() {
        return updated_date;
    }

    void set_updated_date(Date updated_date) {
        this.updated_date = updated_date;
    }

    public String get_title() {
        return title;
    }

    void set_title(String title) {
        this.title = title;
    }

    public String get_description() {
        return description;
    }

    void set_description(String description) {
        this.description = description;
    }

    public String get_link() {
        return link;
    }

    void set_link(String link) {
        this.link = link;
    }

    public String get_author() {
        return author;
    }

    void set_author(String author) {
        this.author = author;
    }

    public String get_category() {
        return category;
    }

    void set_category(String category) {
        this.category = category;
    }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author){
        published_date = _published_date;
        updated_date = _updated_date;
        title = _title;
        description = _description;
        link = _link;
        author = _author;
    }

    public String toString(){
        String res;
        res = "Title: "+ title;
        res = res.concat("\nAuthor: "+ author);
        res = res.concat("\nDescription: "+ description);
        res = res.concat("\nCategory: "+ category);
        res = res.concat("\nLink: "+ link);
        res = res.concat("\nPublished: "+ published_date);
        res = res.concat("\nUpdated: "+ updated_date);
        return res;
    }
}
