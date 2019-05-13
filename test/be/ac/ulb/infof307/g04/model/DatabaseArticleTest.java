
package be.ac.ulb.infof307.g04.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseArticleTest {

    private Date testDate;
    private DatabaseArticle testArticle;

    @BeforeAll
    void setupBeforeArticle() {
        Date now = new Date();
        testDate = new Date(now.getTime() - 2 * 24 * 60 * 60 * 1000);
        testArticle = new DatabaseArticle();
        testArticle.setDownloadDate(testDate);
    }

    @Test
    void needToBeDeletedFalse() {
        testArticle.setDaysToSave(3);
        assertFalse(testArticle.needToBeDeleted());
    }

    @Test
    void needToBeDeletedTrue() {
        testArticle.setDaysToSave(1);
        assertTrue(testArticle.needToBeDeleted());
    }
}