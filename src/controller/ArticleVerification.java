package controller;
import java.util.ArrayList;

/*
    Prend en attribut un article et une url et fournit une méthode pour verifier la validité de l'article
 */
public class ArticleVerification {
    private Article article_to_verify;
    private Article article_from_source;
    private String source;

    public ArticleVerification(Article article, String source_url){
        article_to_verify = article;
        source = source_url;
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }
    public boolean is_equal(){
        Article a1 = article_to_verify;
        Article a2 = article_from_source;
        if(((a1.getDescription() == null && a2.getDescription() == null ) || (a1.getDescription() != null && a2.getDescription() != null )) && hashCode(a1.getDescription()) == hashCode(a2.getDescription()) &&
                ((a1.getCategory() == null && a2.getCategory() == null ) || (a1.getCategory() != null && a2.getCategory() != null )) && hashCode(a1.getCategory()) == hashCode(a2.getCategory()) &&
                    ((a1.getLink() == null && a2.getLink() == null ) || (a1.getLink() != null && a2.getLink() != null )) && hashCode(a1.getLink()) == hashCode(a2.getLink()) &&
                        ((a1.getPublished_date() == null && a2.getPublished_date() == null ) || (a1.getPublished_date() != null && a2.getPublished_date() != null )) && hashCode(a1.getPublished_date()) == hashCode(a2.getPublished_date()) &&
                            ((a1.getUpdated_date() == null && a2.getUpdated_date() == null ) || (a1.getUpdated_date() != null && a2.getUpdated_date() != null )) && hashCode(a1.getUpdated_date()) == hashCode(a2.getUpdated_date())){
            return true;
        }
        else{
            return false;
        }

    }

    public void set_article(Article article){
        article_to_verify = article;
    }

    public boolean is_valid(){
        System.out.println(source);
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles = new ArrayList<>();
        articles = parser.parse(source);
        boolean found = false;
        for(int i=0; i< articles.size(); i++){
            if(((article_to_verify.getTitle() == null && articles.get(i).getTitle() == null ) || (article_to_verify.getTitle() != null && articles.get(i).getTitle() != null )) && article_to_verify.getTitle().equals(articles.get(i).getTitle()) &&
                    ((article_to_verify.getAuthor() == null && articles.get(i).getAuthor() == null ) || (article_to_verify.getAuthor() != null && articles.get(i).getAuthor() != null )) && hashCode(article_to_verify.getAuthor()) == hashCode(articles.get(i).getAuthor())){
                article_from_source = articles.get(i);
                return is_equal();
            }
        }
        return false;
    }
}
