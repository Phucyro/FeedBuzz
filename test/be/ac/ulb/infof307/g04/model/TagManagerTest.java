package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagManagerTest {

    private final String TAG_1 = "tag1";
    private final String TAG_2 = "tag2";
    private String dbpath;
    private TagManager tagManager;
    private DatabaseTag tag;
    private float score = 100;

    @BeforeAll
    void setupBeforeTagManager() {
        this.dbpath = "./test.db";
        tagManager = new TagManager(this.dbpath, "test");
        tag = new DatabaseTag();
    }

    @BeforeEach
    void resetDb(){
        tagManager.deleteAll();
    }

    @Test
    void addTag() {
        tag.setName(TAG_1);
        tagManager.addTag(tag);
        assertEquals(tag.getName(), tagManager.getAll().get(0).getName());
        assertEquals(1, tagManager.getAll().size() );
    }

    @Test
    void deleteTag() {
        tag.setName(TAG_1);
        tagManager.addTag(tag);
        assertEquals(1, tagManager.getAll().size() );
        tag.setName(TAG_2);
        tagManager.addTag(tag);
        assertEquals(2, tagManager.getAll().size() );
        tagManager.deleteTag(tag);
        assertEquals(1, tagManager.getAll().size() );
        tag.setName(TAG_1);
        assertEquals(tag.getName(), tagManager.getAll().get(0).getName());
    }

    @Test
    void modifyTag() {
        tag.setName(TAG_1);
        tagManager.addTag(tag);
        DatabaseTag modify_tag = new DatabaseTag();
        modify_tag.setName(TAG_2);
        tagManager.modifyTag(tag, modify_tag);
        assertEquals(1, tagManager.getAll().size() );
        assertEquals(modify_tag.getName(), tagManager.getAll().get(0).getName());
    }

    @Test
    void getAll() {
        ArrayList<DatabaseTag> tags = new ArrayList<>();
        tags.add(new DatabaseTag());
        tags.get(0).setName(TAG_1);
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
    void deleteAll() {
        tag.setName(TAG_1);
        tagManager.addTag(tag);
        tag.setName(TAG_2);
        tagManager.addTag(tag);
        assertEquals(2, tagManager.getAll().size());
        tagManager.deleteAll();
        assertEquals(0, tagManager.getAll().size());
    }

    @AfterAll
    void deleteDatabaseFiles() {
        deleteDir(new File(dbpath));
    }


    @Test
    void actualizeScore() {
        tag.setScore(score);
        DatabaseTag tag2 = new DatabaseTag();
        float score2 = 36;
        tag2.setScore(score2);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.actualizeScore(0);
        assertEquals(score,tag.getScore());
        assertEquals(score2,tag2.getScore());
        tagManager.actualizeScore(2);
        assertEquals(98,tag.getScore());
        assertEquals(35.28,tag2.getScore());
    }

    @Test
    void getBest() {
        tag.setName(TAG_1);
        DatabaseTag tag2 = new DatabaseTag();
        tag2.setName(TAG_2);
        tag.setScore(39);
        tag2.setScore(40);
        tagManager.addTag(tag);
        tagManager.addTag(tag2);
        assertEquals(tag2.getName(),tagManager.getBest());
    }

    @Test
    void removeDislike() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.removeDislike(TAG_1);
        assertEquals(score+1,tag.getScore());
    }

    @Test
    void addDislike() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.addDislike(TAG_1);
        assertEquals(score-1,tag.getScore());
    }

    @Test
    void removeLike() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.removeLike(TAG_1);
        assertEquals(score-1,tag.getScore());
    }

    @Test
    void addLike() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.addLike(TAG_1);
        assertEquals(score+1,tag.getScore());
    }

    @Test
    void addTime() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.addTime(TAG_1,2);
        assertEquals(score +2,tag.getScore());
    }

    @Test
    void addView() {
        tag.setName(TAG_1);
        tag.setScore(score);
        tagManager.addTag(tag);
        assertEquals(score,tag.getScore());
        tagManager.addView(TAG_1);
        assertEquals(score+1,tag.getScore());
    }

}