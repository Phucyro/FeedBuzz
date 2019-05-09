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
    private final DatabaseArticle articleToVerify;
    private DatabaseArticle articleFromSource;
    private final String source;

    /**
     * @param _article article to verify
     * @param _source_url source of the article
     */
    public ArticleVerification(DatabaseArticle _article, String _source_url){
        /*
          Constructor of the DatabaseArticle verification

          @param _article
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
    private ArrayList<DatabaseArticle> getArticlesFromSource() throws IOException, ParserConfigurationException, SAXException, ParseException {
        ParserRss parser = new ParserRss();
        ArrayList<DatabaseArticle> articles;
        articles = parser.parse(source);
        return articles;
    }


    /**
     * @param _object object to hash
     * @return hashcode of the object
     */
    public static int hashCode(Object _object) {
        /*
        Method used to generate a hash
         */
        return _object != null ? _object.hashCode() : 0;
    }

    /**
     * Test if an article is equal to another
     * @return boolean
     */
    public boolean isEqual(){


        DatabaseArticle a1 = articleToVerify;
        DatabaseArticle a2 = articleFromSource;

        // condition de la forme, soit les deux champs sont nuls, soit aucun des deux n'est nul et les hash correspondent
        return (((a1.getDescription() == null && a2.getDescription() == null) || (a1.getDescription() != null && a2.getDescription() != null)) && hashCode(a1.getDescription()) == hashCode(a2.getDescription())) &&
                (((a1.getLink() == null && a2.getLink() == null) || (a1.getLink() != null && a2.getLink() != null)) && hashCode(a1.getLink()) == hashCode(a2.getLink())) &&
                (((a1.getPublishedDate() == null && a2.getPublishedDate() == null) || (a1.getPublishedDate() != null && a2.getPublishedDate() != null)) && hashCode(a1.getPublishedDate()) == hashCode(a2.getPublishedDate())) &&
                (((a1.getUpdatedDate() == null && a2.getUpdatedDate() == null) || (a1.getUpdatedDate() != null && a2.getUpdatedDate() != null)) && hashCode(a1.getUpdatedDate()) == hashCode(a2.getUpdatedDate()));
    }

    /**
     * Check if an article exists and wasn't modified from the source
     *
     * @return boolean
     */
    public boolean isValid() throws IOException, ParserConfigurationException, SAXException, ParseException {
        ArrayList<DatabaseArticle> articles = getArticlesFromSource();
        for (DatabaseArticle article : articles) {
            if (((articleToVerify.getTitle() == null && article.getTitle() == null) || (articleToVerify.getTitle() != null && article.getTitle() != null)) && articleToVerify.getTitle().equals(article.getTitle()) &&
                    ((articleToVerify.getAuthor() == null && article.getAuthor() == null) || (articleToVerify.getAuthor() != null && article.getAuthor() != null)) && hashCode(articleToVerify.getAuthor()) == hashCode(article.getAuthor())) {
                articleFromSource = article;
                return isEqual();
            }
        }
        return false;
    }

    /**
     * check if an article can be corrected
     */
    public boolean isCorrectable() throws IOException, ParserConfigurationException, SAXException, ParseException {

        ArrayList<DatabaseArticle> articles = getArticlesFromSource();

        for (DatabaseArticle article : articles) {
            if (((articleToVerify.getLink() != null && article.getLink() != null) && (hashCode(articleToVerify.getLink()) == hashCode(article.getLink()))) ||
                    ((articleToVerify.getTitle() != null && article.getTitle() != null) && (hashCode(articleToVerify.getTitle()) == hashCode(article.getTitle())) &&
                            (articleToVerify.getAuthor() != null && article.getAuthor() != null) && (hashCode(articleToVerify.getAuthor()) == hashCode(article.getAuthor()))) ||
                    ((articleToVerify.getDescription() != null && article.getDescription() != null) && (hashCode(articleToVerify.getDescription()) == hashCode(article.getDescription())))) {
                articleFromSource = article;
                return true;
            }
        }
        return false;
    }

    /**
     * correct an article if it is possible
     */
    public void correctArticle() throws IOException, ParserConfigurationException, SAXException, ParseException {
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

    /**
     * Get the article that has to be verified
     * @return article to verify
     */
    public DatabaseArticle getArticle(){
        return articleToVerify;
    }


}
