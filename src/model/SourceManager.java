package model;

import io.jsondb.JsonDBTemplate;

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;

    public SourceManager(String database_path) {
        String baseScanPackage = "model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            create_collection();
        }
    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseSource.class);
    }

    public void download(int number,int lifespan){
        /*ParserRss source = new ParserRss();
        //ArticleManager articleManager = new ArticleManager("./test.db", "test");
        ArrayList<Article> articles = source.parse(this.getUrl());
        Article article_temp;
        for (int i = 0; i < number; i++) {
            article_temp = articles.get(i);
            //articleManager.add_article(article_temp);
        }*/

    }

}
