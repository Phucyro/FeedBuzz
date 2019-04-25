package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleManagerTest {

    private String dbpath;
    private ArticleManager manager;
    private DatabaseArticle article1;
    private DatabaseArticle article2;

    @BeforeAll
    void setup_before_article_manager() {
        this.dbpath = "./test.db";

        this.manager = new ArticleManager(dbpath, "test");
        this.article1 = new DatabaseArticle();
        this.article2 = new DatabaseArticle();

        this.article1.setLink("http://www.test1.com");
        this.article2.setLink("http://www.test2.com");

        this.article1.setTitle("Test1");
        this.article2.setTitle("Test2");
    }

    @AfterEach()
    void clear_database() {
        this.manager.deleteArticle(article1);
        this.manager.deleteArticle(article2);
    }

    @Test
    void add_and_find_article() {
        manager.addArticle(article1);
        DatabaseArticle article_test = manager.findArticle(article1.getLink());
        assertSame(article1.getDescription(), article_test.getDescription());
        //Verifie que l'article ajoute a la base de donnees a la meme description que l'article original
    }

    @Test
    void delete_existing_article() {
        manager.addArticle(article1);
        manager.deleteArticle(article1);
        DatabaseArticle deleted_article = new DatabaseArticle(manager.findArticle(article1.getLink()));
        assertTrue(deleted_article.getDeleted());
        //L'article supprime devrais avoir son attribut deleted a "true" pour signifier que l'article est supprime.
        assertSame(null, deleted_article.getDescription());
        //L'article supprime devrais avoir sa description mise a "null"
    }

    @Test
    void delete_non_existing_article(){
        DatabaseArticle non_existing_article = new DatabaseArticle();
        non_existing_article.setLink("http://www.test3.com");
        non_existing_article.setTitle("Test3");
        assertFalse(manager.deleteArticle(non_existing_article));
        //Le manager doit renvoyer false lorsque l'article n'est pas trouve, alors on assert que la valeur retournee lorsqu'on recherche un article qui n'est pas dans la base de donnees est "false"
    }

    @Test
    void load_articles_count(){
        manager.addArticle(article1);
        manager.addArticle(article2);
        assertSame(manager.loadArticles().size(), 2);
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