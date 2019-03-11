package model;

import controller.Article;
import controller.ParserRss;

import java.util.ArrayList;

public class Source {
    private String source_name ;
    private String url;
    private boolean checked = true;

    public Source(String name, String _url) {
        source_name = name;
        url = _url;
    }

    public boolean  isChecked() { return checked; }

    public void download(int number,int lifespan){
        source_name = getName();
        ParserRss source = new ParserRss();
        ArticleManager articleManager = new ArticleManager("./test.db", "test");
        ArrayList<Article> articles = source.parse(url);
        Article article_temp;
        for (int i = 0; i < number; i++) {
            article_temp = articles.get(i);
            articleManager.add_article(article_temp);
        }

    }

    public String getName() {
        return source_name;
    }
    public void setName(String name) {
        source_name = name;
    }
}
