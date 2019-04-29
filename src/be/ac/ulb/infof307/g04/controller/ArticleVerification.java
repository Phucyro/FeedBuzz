package be.ac.ulb.infof307.g04.controller;
import org.xml.sax.SAXException;

import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Class ArticleVerification, where articles are analyzed on different to see their integrity or their equilaty
 *
 * @version 2.0
 *
 */

public class ArticleVerification {
    private DatabaseArticle articleToVerify;
    private DatabaseArticle articleFromSource;
    private String source;

    public ArticleVerification(DatabaseArticle _article, String _source_url){
        /**
         * Constructor of the DatabaseArticle verification
         *
         * @param _article
         *              get the article that needs to be verified
         * @param _source_url
         *              get the url of the article
         */
        articleToVerify = _article;
        source = _source_url;
    }

    /**
     * Get all the articles from a source
     * @return An ArrayList of articles
     */
    public ArrayList<DatabaseArticle> getArticlesFromSource() throws IOException, ParserConfigurationException, SAXException, ParseException {
        ParserRss parser = new ParserRss();
        ArrayList<DatabaseArticle> articles;
        articles = parser.parse(source);
        return articles;
    }


    public static int hashCode(Object _object) {
        /*
        Method used to generate a hash
         */
        return _object != null ? _object.hashCode() : 0;
    }

    public boolean isEqual(){
        /**
         * Test if an article is equal to another
         * @return boolean
         * @throws
         */

        DatabaseArticle a1 = articleToVerify;
        DatabaseArticle a2 = articleFromSource;

        // condition de la forme, soit les deux champs sont nuls, soit aucun des deux n'est nul et les hash correspondent
        if((((a1.getDescription() == null && a2.getDescription() == null ) || (a1.getDescription() != null && a2.getDescription() != null )) && hashCode(a1.getDescription()) == hashCode(a2.getDescription())) &&
                    (((a1.getLink() == null && a2.getLink() == null ) || (a1.getLink() != null && a2.getLink() != null )) && hashCode(a1.getLink()) == hashCode(a2.getLink())) &&
                        (((a1.getPublishedDate() == null && a2.getPublishedDate() == null ) || (a1.getPublishedDate() != null && a2.getPublishedDate() != null )) && hashCode(a1.getPublishedDate()) == hashCode(a2.getPublishedDate())) &&
                            (((a1.getUpdatedDate() == null && a2.getUpdatedDate() == null ) || (a1.getUpdatedDate() != null && a2.getUpdatedDate() != null )) && hashCode(a1.getUpdatedDate()) == hashCode(a2.getUpdatedDate()))){
            return true;
        }
        else{
            return false;
        }
    }



    public boolean isValid() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
         * Check if an article exists and wasn't modified from the source
         *
         * @return boolean
         * @see is_equal()
         */
        ArrayList<DatabaseArticle> articles = new ArrayList<>();
        articles = getArticlesFromSource();
        boolean found = false;
        for(int i=0; i< articles.size(); i++){
            if(((articleToVerify.getTitle() == null && articles.get(i).getTitle() == null ) || (articleToVerify.getTitle() != null && articles.get(i).getTitle() != null )) && articleToVerify.getTitle().equals(articles.get(i).getTitle()) &&
                    ((articleToVerify.getAuthor() == null && articles.get(i).getAuthor() == null ) || (articleToVerify.getAuthor() != null && articles.get(i).getAuthor() != null )) && hashCode(articleToVerify.getAuthor()) == hashCode(articles.get(i).getAuthor())){
                articleFromSource = articles.get(i);
                return isEqual();
            }
        }
        return false;
    }


    public boolean isCorrectable() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
         * check if an article can be corrected
         * @see databaseArticle
         */
        ArrayList<DatabaseArticle> articles = getArticlesFromSource();

        for(int i=0; i< articles.size(); i++){
            if(((articleToVerify.getLink() != null && articles.get(i).getLink() != null   )   &&   (   hashCode(articleToVerify.getLink()) == hashCode(articles.get(i).getLink()))) ||
                    ((articleToVerify.getTitle() != null && articles.get(i).getTitle() != null ) && (  hashCode(articleToVerify.getTitle()) == hashCode(articles.get(i).getTitle())) &&
                    (articleToVerify.getAuthor() != null && articles.get(i).getAuthor() != null   ) && (  hashCode(articleToVerify.getAuthor()) == hashCode(articles.get(i).getAuthor()))) ||
                        ((articleToVerify.getDescription() != null && articles.get(i).getDescription() != null ) && (  hashCode(articleToVerify.getDescription()) == hashCode(articles.get(i).getDescription())))){
                articleFromSource = articles.get(i);
                return true;
            }
        }
        return false;
    }


    public void correctArticle() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /*
         * correct an article if it is possible
         */
        if(isCorrectable()){
            articleToVerify.setTitle(articleFromSource.getTitle());
            articleToVerify.setAuthor(articleFromSource.getAuthor());
            articleToVerify.setCategory(articleFromSource.getCategory());
            articleToVerify.setLink(articleFromSource.getLink());
            articleToVerify.setDescription(articleFromSource.getDescription());
            articleToVerify.setUpdatedDate(articleFromSource.getUpdatedDate());
            articleToVerify.setPublishedDate(articleFromSource.getPublishedDate());
        }
    }



    public DatabaseArticle getArticle(){
        /*
        Get the article that has to be verified
         */
        return articleToVerify;
    }


}
