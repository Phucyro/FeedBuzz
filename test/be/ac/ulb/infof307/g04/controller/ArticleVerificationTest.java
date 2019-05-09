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

    private ArticleVerification testVerification;
    private DatabaseArticle falseArticle;
    private DatabaseArticle trueArticle;
    private DatabaseArticle repairableArticle;
    private DatabaseArticle notRepairableArticle;

    private String testSource;

    @BeforeAll
    void setupBeforeArticleVerification() throws IOException, ParserConfigurationException, SAXException, ParseException {
        // On recupere la liste d'article de la source
        testSource = "http://feeds.bbci.co.uk/news/world/rss.xml";
        ParserRss parser = new ParserRss();
        ArrayList<DatabaseArticle> articles = parser.parse(testSource);

        // Un article pris de la source sans aucune modification
        trueArticle = new DatabaseArticle();
        trueArticle.setAuthor(articles.get(0).getAuthor());
        trueArticle.setTitle(articles.get(0).getTitle());
        trueArticle.setPublishedDate(articles.get(0).getPublishedDate());
        trueArticle.setUpdatedDate(articles.get(0).getUpdatedDate());
        trueArticle.setLink(articles.get(0).getLink());
        trueArticle.setCategory(articles.get(0).getCategory());
        trueArticle.setDescription(articles.get(0).getDescription());
        trueArticle.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");
        // On altere un article de la source
        falseArticle = new DatabaseArticle();
        falseArticle.setAuthor(articles.get(0).getAuthor());
        falseArticle.setTitle("Modified Title");
        falseArticle.setPublishedDate(articles.get(0).getPublishedDate());
        falseArticle.setUpdatedDate(articles.get(0).getUpdatedDate());
        falseArticle.setLink(articles.get(0).getLink());
        falseArticle.setCategory(articles.get(0).getCategory());
        falseArticle.setDescription(articles.get(0).getDescription());
        falseArticle.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");

        // on modifie uniquement le titre, on peut recuperer l'article avec le lien ou la description
        repairableArticle = new DatabaseArticle();
        repairableArticle.setAuthor(articles.get(0).getAuthor());
        repairableArticle.setTitle("Modified Title");
        repairableArticle.setPublishedDate(articles.get(0).getPublishedDate());
        repairableArticle.setUpdatedDate(articles.get(0).getUpdatedDate());
        repairableArticle.setLink(articles.get(0).getLink());
        repairableArticle.setCategory("nocategory");
        repairableArticle.setDescription(articles.get(0).getDescription());
        repairableArticle.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");

        // on modifie le lien, la description, et le titre -> pas corrigible
        notRepairableArticle = new DatabaseArticle();
        notRepairableArticle.setAuthor(articles.get(0).getAuthor());
        notRepairableArticle.setTitle("Modified Title");
        notRepairableArticle.setPublishedDate(articles.get(0).getPublishedDate());
        notRepairableArticle.setUpdatedDate(articles.get(0).getUpdatedDate());
        notRepairableArticle.setLink("link broken");
        notRepairableArticle.setCategory("nocategory");
        notRepairableArticle.setDescription("This article is broken");
        notRepairableArticle.setSourceUrl("http://feeds.bbci.co.uk/news/world/rss.xml");
    }

    @Test
    void validArticleFalse() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(falseArticle, testSource);
        assertFalse(testVerification.isValid());

    }


    @Test
    void validArticleTrue() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(trueArticle, testSource);
        assertTrue(testVerification.isValid());

    }


    @Test
    void correctableFalse() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(notRepairableArticle, testSource);
        assertFalse(testVerification.isCorrectable());
    }


    @Test
    void correctableTrue() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(repairableArticle, testSource);
        assertTrue(testVerification.isCorrectable());

    }


    @Test
    void correctedFalse() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(notRepairableArticle, testSource);
        testVerification.correctArticle();
        assertFalse(testVerification.isValid());
    }

    @Test
    void correctedTrue() throws IOException, ParserConfigurationException, SAXException, ParseException {
        testVerification = new ArticleVerification(repairableArticle, testSource);
        testVerification.correctArticle();
        assertTrue(testVerification.isValid());
    }
}
