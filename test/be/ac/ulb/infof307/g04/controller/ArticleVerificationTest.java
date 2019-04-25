package be.ac.ulb.infof307.g04.controller;


import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleVerificationTest {

    private ArticleVerification test_verification;
    private DatabaseArticle false_article;
    private DatabaseArticle true_article;
    private DatabaseArticle repairable_article;
    private DatabaseArticle not_repairable_article;

    private String test_source;

    @BeforeAll
    void setup_before_article_verification() throws IOException, ParserConfigurationException, SAXException, ParseException {
        // On recupere la liste d'article de la source
        test_source = new String("http://feeds.bbci.co.uk/news/world/rss.xml");
        ParserRss parser = new ParserRss();
        ArrayList<DatabaseArticle> articles = parser.parse(test_source);

        // Un article pris de la source sans aucune modification
        true_article = new DatabaseArticle();
        true_article.setAuthor(articles.get(0).getAuthor());
        true_article.setTitle(articles.get(0).getTitle());
        true_article.setPublishedDate(articles.get(0).getPublishedDate());
        true_article.setUpdatedDate(articles.get(0).getUpdatedDate());
        true_article.setLink(articles.get(0).getLink());
        true_article.setCategory(articles.get(0).getCategory());
        true_article.setDescription(articles.get(0).getDescription());
        true_article.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");
        // On altere un article de la source
        false_article = new DatabaseArticle();
        false_article.setAuthor(articles.get(0).getAuthor());
        false_article.setTitle("Modified Title");
        false_article.setPublishedDate(articles.get(0).getPublishedDate());
        false_article.setUpdatedDate(articles.get(0).getUpdatedDate());
        false_article.setLink(articles.get(0).getLink());
        false_article.setCategory(articles.get(0).getCategory());
        false_article.setDescription(articles.get(0).getDescription());
        false_article.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");

        // on modifie uniquement le titre, on peut recuperer l'article avec le lien ou la description
        repairable_article = new DatabaseArticle();
        repairable_article.setAuthor(articles.get(0).getAuthor());
        repairable_article.setTitle("Modified Title");
        repairable_article.setPublishedDate(articles.get(0).getPublishedDate());
        repairable_article.setUpdatedDate(articles.get(0).getUpdatedDate());
        repairable_article.setLink(articles.get(0).getLink());
        repairable_article.setCategory("nocategory");
        repairable_article.setDescription(articles.get(0).getDescription());
        repairable_article.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");

        // on modifie le lien, la description, et le titre -> pas corrigible
        not_repairable_article = new DatabaseArticle();
        not_repairable_article.setAuthor(articles.get(0).getAuthor());
        not_repairable_article.setTitle("Modified Title");
        not_repairable_article.setPublishedDate(articles.get(0).getPublishedDate());
        not_repairable_article.setUpdatedDate(articles.get(0).getUpdatedDate());
        not_repairable_article.setLink("link broken");
        not_repairable_article.setCategory("nocategory");
        not_repairable_article.setDescription("This article is broken");
        not_repairable_article.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");
    }

    @Test
    void valid_article_False() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(false_article, test_source);
        assertFalse(test_verification.isValid());

    }


    @Test
    void valid_article_True() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(true_article, test_source);
        assertTrue(test_verification.isValid());

    }


    @Test
    void correctable_False() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(not_repairable_article, test_source);
        assertFalse(test_verification.isCorrectable());
    }


    @Test
    void correctable_True() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(repairable_article, test_source);
        assertTrue(test_verification.isCorrectable());

    }


    @Test
    void corrected_False() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(not_repairable_article, test_source);
        test_verification.correctArticle();
        assertFalse(test_verification.isValid());
    }

    @Test
    void corrected_True() throws IOException, ParserConfigurationException, SAXException, ParseException {
        test_verification = new ArticleVerification(repairable_article, test_source);
        test_verification.correctArticle();
        assertTrue(test_verification.isValid());
    }







}
