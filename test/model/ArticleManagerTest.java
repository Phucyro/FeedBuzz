package model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.model.ArticleManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleManagerTest {

    private String dbpath;
    private ArticleManager manager;
    private Article article1;
    private Article article2;

    @BeforeAll
    void setup_before_article_manager() {
        this.dbpath = "./test.db";

        this.manager = new ArticleManager(dbpath, "test");
        this.article1 = new Article();
        this.article2 = new Article();

        this.article1.setLink("http://www.test1.com");
        this.article2.setLink("http://www.test2.com");

        this.article1.setTitle("Test1");
        this.article2.setTitle("Test2");
    }

    @AfterEach()
    void clear_database() {
        this.manager.delete_article(article1);
        this.manager.delete_article(article2);
    }

    @Test
    void add_article_true() {
        assertTrue(manager.add_article(article1));
    }

    @Test
    void add_article_false() {
        manager.add_article(article1);
        assertFalse(manager.add_article(article1));
    }

    @Test
    void delete_article_true() {
        manager.add_article(article1);
        assertTrue(manager.delete_article(article1));
    }

    @Test
    void delete_article_false() {
        assertFalse(manager.delete_article(article1));
    }

    @Test
    void find_article_null() {
        assertNull(manager.findArticle(article1.getLink()));
    }

    @Test
    void find_article_ok() {
        manager.add_article(article1);
        manager.add_article(article2);
        assertEquals(manager.findArticle(article2.getLink()).getTitle(),article2.getTitle());
    }

    /*@Test
    void load_all_articles_content(){
        manager.add_article(article1);
        manager.add_article(article2);
        assertTrue(manager.load_all_articles().contains(article1) &&
                manager.load_all_articles().contains(article2));
    }*/

    @Test
    void load_articles_count(){
        manager.add_article(article1);
        manager.add_article(article2);
        assertTrue(manager.load_articles().size() == 2);
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    @AfterAll
    void delete_databse_files() {
        deleteDir(new File(dbpath));
    }
}