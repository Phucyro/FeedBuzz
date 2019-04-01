package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.controller.ParserRss;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserRssTest {
    @Test
    void articles_len_test() {
        try {
            assertEquals(15, new ParserRss().parse("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml").size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void bad_url_test() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new ParserRss().parse("http:/"));
    }
}