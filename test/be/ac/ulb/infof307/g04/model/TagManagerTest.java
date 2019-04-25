package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagManagerTest {

    private String dbpath;
    private TagManager tagManager;
    private DatabaseTag tag;

    @BeforeAll
    void setup_before_tag_manager() {
        this.dbpath = "./test.db";
        tagManager = new TagManager(this.dbpath, "test");
        tag = new DatabaseTag();
    }

    @BeforeEach
    void reset_db(){
        tagManager.deleteAll();
    }

    @Test
    void add_tag() {
        tag.setName("test");
        tagManager.addTag(tag);
        assertEquals(tag.getName(), tagManager.getAll().get(0).getName());
        assertEquals(1, tagManager.getAll().size() );
    }

    @Test
    void delete_tag() {
        tag.setName("test1");
        tagManager.addTag(tag);
        assertEquals(1, tagManager.getAll().size() );
        tag.setName("test2");
        tagManager.addTag(tag);
        assertEquals(2, tagManager.getAll().size() );
        tagManager.deleteTag(tag);
        assertEquals(1, tagManager.getAll().size() );
        tag.setName("test1");
        assertEquals(tag.getName(), tagManager.getAll().get(0).getName());
    }

    @Test
    void modify_tag() {
        tag.setName("test1");
        tagManager.addTag(tag);
        DatabaseTag modify_tag = new DatabaseTag();
        modify_tag.setName("test2");
        tagManager.modifyTag(tag, modify_tag);
        assertEquals(1, tagManager.getAll().size() );
        assertEquals(modify_tag.getName(), tagManager.getAll().get(0).getName());
    }

    @Test
    void get_all() {
        ArrayList<DatabaseTag> tags = new ArrayList<>();
        tags.add(new DatabaseTag());
        tags.get(0).setName("test");
        tagManager.addTag(tags.get(0));
        assertEquals(tags.size(), tagManager.getAll().size());
        for(int i = 0; i < tags.size(); i++){
            assertEquals(tags.get(i).getName(), tagManager.getAll().get(i).getName());
        }
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

    @Test
    void delete_all() {
        tag.setName("test");
        tagManager.addTag(tag);
        tag.setName("test2");
        tagManager.addTag(tag);
        assertEquals(2, tagManager.getAll().size());
        tagManager.deleteAll();
        assertEquals(0, tagManager.getAll().size());
    }

    @AfterAll
    void delete_databse_files() {
        deleteDir(new File(dbpath));
    }


}