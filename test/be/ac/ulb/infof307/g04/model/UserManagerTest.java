package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagerTest {

    private String dbpath;
    private String username_test;
    private String password_test;
    private UserManager user_manager;
    private DatabaseUser user;

    @BeforeAll
    void setup_before_user_manager() {
        this.dbpath = "./test.db";

        this.username_test = "usertest";
        this.password_test = "password";
        this.user_manager = new UserManager(dbpath, "test");
        this.user = new DatabaseUser(username_test,password_test);
    }

    @AfterEach()
    void clear_database() {
        this.user_manager.delete_user(user.getUsername());
    }

    @Test
    void add_and_find_user() {
        user_manager.add_user(user);
        assertNotNull(user_manager.findUser_by_username(user.getUsername()));
    }

    @Test
    void delete_existing_user() {
        user_manager.add_user(user);
        user_manager.delete_user(user.getUsername());
        assertFalse(user_manager.existUsername(user.getUsername()));
    }


    @Test
    void delete_not_existing_user() {
        user_manager.delete_user(user.getUsername());
        assertFalse(user_manager.existUsername(user.getUsername()));
    }


    @Test
    void user_infos_matching() {
        user_manager.add_user(user);
        assertNotNull(user_manager.findUser(username_test,password_test));
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