package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseArticle;

import java.util.Date;

public class Article extends DatabaseArticle {

    public Article(){ super(); }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author) {
        super();
        setPublished_date(_published_date);
        setUpdated_date(_updated_date);
        setTitle(_title);
        setDescription(_description);
        setLink(_link);
        setAuthor(_author);
    }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author,String _image_url, String _localisation, String _tags){
        super();
        setPublished_date(_published_date);
        setUpdated_date(_updated_date);
        setTitle (_title);
        setDescription(_description);
        setLink(_link);
        setAuthor(_author);

        if (_image_url != null) {
            //Image image = new Image(_image_url);
            //setImage(image);
        }
        if (_localisation != null) {
            setLocalisation(_localisation);
        }
        if (_tags != null){
            setTags(_tags);
        }
    }

    public Article(DatabaseArticle item) {
        super(item);
    }


    public String toString(){
        String res;
        res = "Title: "+ getTitle();
        res = res.concat("\nAuthor: "+ getAuthor());
        res = res.concat("\nDescription: "+ getDescription());
        res = res.concat("\nCategory: "+ getCategory());
        res = res.concat("\nLink: "+ getLink());
        res = res.concat("\nPublished: "+ getPublished_date());
        res = res.concat("\nUpdated: "+ getUpdated_date());
        return res;
    }

    public boolean need_to_be_deleted() {
        Date now = new Date();
        Date delete_date = new Date(getDownload_date().getTime() + getDays_to_save() * 24 * 60 * 60 * 1000);
        return now.after(delete_date);
    }
}

