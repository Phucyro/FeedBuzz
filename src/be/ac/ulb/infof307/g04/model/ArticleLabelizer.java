package be.ac.ulb.infof307.g04.model;


import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Class ArticleLabeliseur, used to associate a label to an article to use a recommandation system
 * @see DatabaseArticle
 */

public class ArticleLabelizer {

    public DatabaseArticle article;
    public ArrayList<String> bag_of_word;


    public int[] histogram_article;   // histogramme d'occurence de chaque mot du bag of word
    public int[][] histogram_topics;  // liste des histogramme de chaque topic

    /**
     * Constructor that set the wordlists
     */
    public ArticleLabelizer() {


        JSONObject wordlist = new JSONObject("./article_db/wordlists.json");
        System.out.println(wordlist.getJSONArray("health"));


    }

    /**
     * Constructor with only the path to the database
     * @param _database_path path to the database
     */
    public ArticleLabelizer(String _database_path) {
      

    }



}