package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DatabaseArticle;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class Article, where articles are created
 * @version 2.0
 */

public class Article extends DatabaseArticle {

    public Article(){ super(); }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author) {
        /**
         * Constructor of an Article
         * @param _published_date
         *                      Publication date of the Article
         * @param _updated_date
         *                      Date where the article has been updated
         * @param _title
         *                      Title of the Article
         * @param _description
         *                      Description of the article
         * @param _link
         *                      Web link to get to the Article's page
         * @param _author
         *                      Author of the article
         *
         */
        super();
        setPublished_date(_published_date);
        setUpdated_date(_updated_date);
        setTitle(_title);
        setDescription(_description);
        setLink(_link);
        setAuthor(_author);
    }

    public Article(Date _published_date, Date _updated_date, String _title, String _description, String _link, String _author,String _image_url, String _tags){
        /**
         * Constructor of an Article with more parameters
         * @param _image_url
         *                      Image of the Article
         * @param _tags
         *                      Tags of the sources of the Article
         *
         */
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
        if (_tags != null){
            setTags(_tags);
        }
    }

    public Article(DatabaseArticle item) {
        /**
         * Constructor of the Article based on the Database
         */
        super(item);
    }


    public String toString(){
        /**
         * Returns a textual representation of an article
         *
         * @return A String representation of the article
         * @see String
         */
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
        /**
         * Tests if an article is outdated (based on his donwload date and the days to save the article)
         *
         * @return boolean if an article has to be deleted
         * @see Date
         */
        Date now = new Date();
        Date delete_date = new Date(getDownload_date().getTime() + getDays_to_save() * 24 * 60 * 60 * 1000);
        return now.after(delete_date);
    }
}

