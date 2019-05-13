package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArticleLabelizerTest {
    @Test
    void labeLizeArticleFoodPositive() {
        String textTest = "Eating meat is part of the daily life of billions of people all over the world. Every day thousands of animals are killed for the production of meat food for people. However, studies have shown that meat is not essential for our existence and gives us nothing more than the other foods on the market.";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertEquals("Food", res);
    }

    @Test
    void labeLizeArticleTestSportPositive() {
        String textTest = "Organized basketball involves two teams of five players each. The players score points by throwing a large round ball into a raised goal called a basket. One basket is at each end of the playing area, or court.Players may move the ball toward the basket only by bouncing it on the floor or passing it to another team member. Each team also tries to prevent the other team from scoring. The team that scores the most points is the winner.";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertEquals("Sports", res);
    }

    @Test
    void labeLizeArticleTestAmericaNegative() {
        String textTest = "Movies are a favorite pastime throughout America. There are many different types of movies, and people prefer different ones. Whether it is drama, comedy, or suspense, it seems like they are all equally preferred. The three best movies of the year were the comedy, Meet The Parents, the drama, Ghost, and the suspenseful, Final Destination.";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertNotEquals("America", res);
    }

    @Test
    void labeLizeArticleTestMoviePositive() {
        String textTest = "Movies are a favorite pastime throughout America. There are many different types of movies, and people prefer different ones. Whether it is drama, comedy, or suspense, it seems like they are all equally preferred. The three best movies of the year were the comedy, Meet The Parents, the drama, Ghost, and the suspenseful, Final Destination.";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertEquals("Cinema/Series", res);
    }

    @Test
    void labeLizeArticleTestPlantNegative() {
        String textTest = "Art has been a part of our life for as long as humanity has existed. For thousands of years people have been creating, looking at, criticizing, and enjoying art. I would like to address three questions: what is art, what is its purpose, and why has it survived for this long.";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertNotEquals("Plant", res);
    }

    @Test
    void labeLizeArticleTestEmpty() {
        String textTest = "";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertEquals("Default", res);
    }

    @Test
    void labeLizeArticleTestAlmostEmpty() {
        String textTest = "a";
        String res = ArticleLabelizer.labelizeArticle(textTest);
        assertEquals("Default", res);
    }


}