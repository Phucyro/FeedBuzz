package model;

import controller.SourceMenu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceModel {
    private Source[] sources = new Source[10];
    private int number_articles = 5;
    private int lifespan_articles = 5;
    private List<Source> selected_sources = new ArrayList<Source>();

    public void getArticles() {
        for (int i = 0; i < 10; i++) {
            if (sources[i].isChecked()) {
                sources[i].download(number_articles, lifespan_articles);
            }
        }
    }
    public void setArticles_persource(int number)  { number_articles = number;}
    public void setArticles_lifespan(int lifespan) { lifespan_articles = lifespan;}
    public int getArticles_persource()              { return number_articles;}
    public int getArticles_lifespan()              { return lifespan_articles;}


    public void applySettings(List<Source> chosen_sources, int number, int lifespan){
        number_articles = number;
        lifespan_articles = lifespan;
        selected_sources = chosen_sources;
    }
}
