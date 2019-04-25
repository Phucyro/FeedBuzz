
package be.ac.ulb.infof307.g04.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseArticleTest {

    private Date test_date;
    private DatabaseArticle test_article;

    @BeforeAll
    void setup_before_article() {
        Date now = new Date();
        test_date = new Date(now.getTime() - 2 * 24 * 60 * 60 * 1000);
        test_article = new DatabaseArticle();
    }

    @Test
    void need_to_be_deleted_false() {
        test_article.setPublished_date(test_date);
        test_article.setDays_to_save(3);
        assertFalse(test_article.need_to_be_deleted());
    }

    @Test
    void need_to_be_deleted_true() {
        test_article.setPublished_date(test_date);
        test_article.setDays_to_save(1);
        assertTrue(test_article.need_to_be_deleted());
    }
}