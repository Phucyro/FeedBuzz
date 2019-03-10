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
    private Article article;
    private String test_source;

    @BeforeAll
    void setup_before_article_verification() {

        test_source = new String("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles = parser.parse(test_source);


        true_article = new Article();
        true_article.setAuthor(articles.get(0).getAuthor());
        true_article.setTitle(articles.get(0).getTitle());
        true_article.setPublished_date(articles.get(0).getPublished_date());
        true_article.setUpdated_date(articles.get(0).getUpdated_date());
        true_article.setLink(articles.get(0).getLink());
        true_article.setCategory(articles.get(0).getCategory());
        true_article.setDescription(articles.get(0).getDescription());


        false_article = new Article();
        false_article.setAuthor(articles.get(0).getAuthor());
        false_article.setTitle("Modified Title");
        false_article.setPublished_date(articles.get(0).getPublished_date());
        false_article.setUpdated_date(articles.get(0).getUpdated_date());
        false_article.setLink(articles.get(0).getLink());
        false_article.setCategory(articles.get(0).getCategory());
        false_article.setDescription(articles.get(0).getDescription());





    }

    @Test
    void valid_article_False() {
        System.out.println("false");
        test_verification = new ArticleVerification(false_article, "http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        assertFalse(test_verification.is_valid());

    }


    @Test
    void valid_article_True() {
        test_verification = new ArticleVerification(true_article, "http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        assertTrue(test_verification.is_valid());

    }


}
