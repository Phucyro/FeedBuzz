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
    void setupBeforeArticleManager() {
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
    void clearDatabase() {
        this.manager.deleteArticle(article1);
        this.manager.deleteArticle(article2);
    }

    @Test
    void addAndFindArticle() {
        manager.addArticle(article1);
        DatabaseArticle articleTest = manager.findArticle(article1.getLink());
        assertSame(article1.getDescription(), articleTest.getDescription());
        //Verifie que l'article ajoute a la base de donnees a la meme description que l'article original
    }

    @Test
    void deleteExistingArticle() {
        manager.addArticle(article1);
        manager.deleteArticle(article1);
        DatabaseArticle deletedArticle = new DatabaseArticle(manager.findArticle(article1.getLink()));
        assertTrue(deletedArticle.getDeleted());
        //L'article supprime devrais avoir son attribut deleted a "true" pour signifier que l'article est supprime.
        assertSame(null, deletedArticle.getDescription());
        //L'article supprime devrais avoir sa description mise a "null"
    }

    @Test
    void deleteNonExistingArticle(){
        DatabaseArticle nonExistingArticle = new DatabaseArticle();
        nonExistingArticle.setLink("http://www.test3.com");
        nonExistingArticle.setTitle("Test3");
        assertFalse(manager.deleteArticle(nonExistingArticle));
        //Le manager doit renvoyer false lorsque l'article n'est pas trouve, alors on assert que la valeur retournee lorsqu'on recherche un article qui n'est pas dans la base de donnees est "false"
    }

    @Test
    void loadArticlesCount(){
        manager.addArticle(article1);
        manager.addArticle(article2);
        assertSame(manager.loadArticles().size(), 2);
        //Verifie qu'il y a bien deux articles dans la base de donnees apres l'ajout de deux articles
    }

    /**
     * @param file directory that need to be deleted
     * @see <a href="https://www.baeldung.com/java-delete-directory">source</a>
     */
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
    void deleteDatabaseFiles() {
        deleteDir(new File(dbpath));
    }
}