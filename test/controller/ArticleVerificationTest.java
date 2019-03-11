package controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleVerificationTest {
    private ArticleVerification test_verification;
    private Article false_article;
    private Article true_article;
    private Article repairable_article;
    private Article not_repairable_article;

    private Article article;
    private String test_source;

    @BeforeAll
    void setup_before_article_verification() {
        // On recupere la liste d'article de la source
        test_source = new String("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles = parser.parse(test_source);

        // Un article pris de la source sans aucune modification
        true_article = new Article();
        true_article.setAuthor(articles.get(0).getAuthor());
        true_article.setTitle(articles.get(0).getTitle());
        true_article.setPublished_date(articles.get(0).getPublished_date());
        true_article.setUpdated_date(articles.get(0).getUpdated_date());
        true_article.setLink(articles.get(0).getLink());
        true_article.setCategory(articles.get(0).getCategory());
        true_article.setDescription(articles.get(0).getDescription());

        // On altere un article de la source
        false_article = new Article();
        false_article.setAuthor(articles.get(0).getAuthor());
        false_article.setTitle("Modified Title");
        false_article.setPublished_date(articles.get(0).getPublished_date());
        false_article.setUpdated_date(articles.get(0).getUpdated_date());
        false_article.setLink(articles.get(0).getLink());
        false_article.setCategory(articles.get(0).getCategory());
        false_article.setDescription(articles.get(0).getDescription());


        // on modifie uniquement le titre, on peut recuperer l'article avec le lien ou la description
        repairable_article = new Article();
        repairable_article.setAuthor(articles.get(0).getAuthor());
        repairable_article.setTitle("Modified Title");
        repairable_article.setPublished_date(articles.get(0).getPublished_date());
        repairable_article.setUpdated_date(articles.get(0).getUpdated_date());
        repairable_article.setLink(articles.get(0).getLink());
        repairable_article.setCategory("nocategory");
        repairable_article.setDescription(articles.get(0).getDescription());





        // on modifie le lien, la description, et le titre -> pas corrigible
        not_repairable_article = new Article();
        not_repairable_article.setAuthor(articles.get(0).getAuthor());
        not_repairable_article.setTitle("Modified Title");
        not_repairable_article.setPublished_date(articles.get(0).getPublished_date());
        not_repairable_article.setUpdated_date(articles.get(0).getUpdated_date());
        not_repairable_article.setLink("link broken");
        not_repairable_article.setCategory("nocategory");
        not_repairable_article.setDescription("This article is broken");





    }

    @Test
    void valid_article_False() {
        test_verification = new ArticleVerification(false_article, test_source);
        assertFalse(test_verification.is_valid());

    }


    @Test
    void valid_article_True() {
        test_verification = new ArticleVerification(true_article, test_source);
        assertTrue(test_verification.is_valid());

    }


    @Test
    void correctable_False() {
        test_verification = new ArticleVerification(not_repairable_article, test_source);
        assertFalse(test_verification.is_correctable());
    }


    @Test
    void correctable_True() {
        test_verification = new ArticleVerification(repairable_article, test_source);
        assertTrue(test_verification.is_correctable());

    }


    @Test
    void corrected_False() {
        test_verification = new ArticleVerification(not_repairable_article, test_source);
        test_verification.correct_article();
        assertFalse(test_verification.is_valid());
    }

    @Test
    void corrected_True() {
        test_verification = new ArticleVerification(repairable_article, test_source);
        test_verification.correct_article();
        assertTrue(test_verification.is_valid());
    }







}
