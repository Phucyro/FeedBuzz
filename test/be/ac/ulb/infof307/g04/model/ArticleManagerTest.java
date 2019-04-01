package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.model.ArticleManager;
import be.ac.ulb.infof307.g04.model.DatabaseArticle;
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
    void add_and_find_article() {
        manager.add_article(article1);
        DatabaseArticle article_test = manager.findArticle(article1.getLink());
        assertSame(article1.getDescription(), article_test.getDescription());
        //Verifie que l'article ajoute a la base de donnees a la meme description que l'article original
    }

    @Test
    void delete_existing_article() {
        manager.add_article(article1);
        manager.delete_article(article1);
        Article deleted_article = new Article(manager.findArticle(article1.getLink()));
        assertTrue(deleted_article.getDeleted());
        //L'article supprime devrais avoir son attribut deleted a "true" pour signifier que l'article est supprime.
        assertSame(null, deleted_article.getDescription());
        //L'article supprime devrais avoir sa description mise a "null"
    }

    @Test
    void delete_non_existing_article(){
        Article non_existing_article = new Article();
        non_existing_article.setLink("http://www.test3.com");
        non_existing_article.setTitle("Test3");
        assertFalse(manager.delete_article(non_existing_article));
        //Le manager doit renvoyer false lorsque l'article n'est pas trouve, alors on assert que la valeur retournee lorsqu'on recherche un article qui n'est pas dans la base de donnees est "false"
    }

    @Test
    void load_articles_count(){
        manager.add_article(article1);
        manager.add_article(article2);
        assertSame(manager.load_articles().size(), 2);
        //Verifie qu'il y a bien deux articles dans la base de donnees apres l'ajout de deux articles
    }

    void deleteDir(File file) {
        //Supprime le dossier de la base de donnees de test apres que les tests soient termines
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
    void delete_database_files() {
        deleteDir(new File(dbpath));
    }
}