package controller;

import model.DatabaseArticle;

import java.util.Date;

public class Article extends DatabaseArticle {

    public Article(){ super(); }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author){
        setPublished_date(_published_date);
        setUpdated_date(_updated_date);
        setTitle (_title);
        setDescription(_description);
        setLink(_link);
        setAuthor(_author);
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

    boolean need_to_be_deleted(){
        Date now = new Date();
        Date delete_date = new Date(getPublished_date().getTime() + getDays_to_save() * 24 * 60 * 60 * 1000);
        return now.after(delete_date);
    }
}
