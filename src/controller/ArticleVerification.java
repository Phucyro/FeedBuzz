package controller;
import java.util.ArrayList;

/*
    Prend en attribut un article et une url et fournit une méthode pour verifier la validité de l'article
    Appeler les methodes dans cet ordre: pour verifier si un article est valide et à jour -> is_valid()
    S'il n'est pas valide, appeler is_correctable, pour verifier s'il est corrigible, si ce n'est pas le cas, il faut se debarrasser de l'article
    Si il est corrigible, on peut appeler correct_article, pour mettre à jour l'article
 */
public class ArticleVerification {
    private Article article_to_verify;
    private Article article_from_source;
    private String source;

    public ArticleVerification(Article article, String source_url){
        article_to_verify = article;
        source = source_url;
    }

    public ArrayList<Article> get_articles_from_source(){
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles;
        articles = parser.parse(source);
        return articles;
    }



    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }




    public boolean is_equal(){
        Article a1 = article_to_verify;
        Article a2 = article_from_source;

        // condition de la forme, soit les deux champs sont nuls, soit aucun des deux n'est nul et les hash correspondent
        if((((a1.getDescription() == null && a2.getDescription() == null ) || (a1.getDescription() != null && a2.getDescription() != null )) && hashCode(a1.getDescription()) == hashCode(a2.getDescription())) &&
                /*(((a1.getCategory() == null && a2.getCategory() == null ) || (a1.getCategory() != null && a2.getCategory() != null )) && hashCode(a1.getCategory()) == hashCode(a2.getCategory())) &&*/
                    (((a1.getLink() == null && a2.getLink() == null ) || (a1.getLink() != null && a2.getLink() != null )) && hashCode(a1.getLink()) == hashCode(a2.getLink())) &&
                        (((a1.getPublished_date() == null && a2.getPublished_date() == null ) || (a1.getPublished_date() != null && a2.getPublished_date() != null )) && hashCode(a1.getPublished_date()) == hashCode(a2.getPublished_date())) &&
                            (((a1.getUpdated_date() == null && a2.getUpdated_date() == null ) || (a1.getUpdated_date() != null && a2.getUpdated_date() != null )) && hashCode(a1.getUpdated_date()) == hashCode(a2.getUpdated_date()))){
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
        // verifie si l'article existe et est non modifié à partir de la source
        ArrayList<Article> articles = new ArrayList<>();
        articles = get_articles_from_source();
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



    public boolean is_correctable(){
        /* Champs Unique (clé) ->  description , (title+ author), link -> si le champ de l'article broken correspond avec un champ d'un article d'une source on peut mettre à jour un article
        Champs non unique -> dates, categories, author, title -> à partir de ces champs on ne peut pas reconstruire un article (plusieurs articles peuvent avoir le meme titre ou le meme auteur, mais probablement pas le meme titre et le meme auteur)
        exemple, les description des articles-> un des deux champs est null -> false  ,  les deux champs sont null -> false  ,  les deux champs sont non null et correspondent -> true
        false = pas de possibilité de corriger l'article avec le champs courant
        true = possibilité de corriger l'article à partir de la source grace a une correspondance des champs*/
        ArrayList<Article> articles = new ArrayList<>();
        articles = get_articles_from_source();

        for(int i=0; i< articles.size(); i++){
            if(((article_to_verify.getLink() != null && articles.get(i).getLink() != null   )   &&   (   hashCode(article_to_verify.getLink()) == hashCode(articles.get(i).getLink()))) ||
                    ((article_to_verify.getTitle() != null && articles.get(i).getTitle() != null ) && (  hashCode(article_to_verify.getTitle()) == hashCode(articles.get(i).getTitle())) &&
                    (article_to_verify.getAuthor() != null && articles.get(i).getAuthor() != null   ) && (  hashCode(article_to_verify.getAuthor()) == hashCode(articles.get(i).getAuthor()))) ||
                        ((article_to_verify.getDescription() != null && articles.get(i).getDescription() != null ) && (  hashCode(article_to_verify.getDescription()) == hashCode(articles.get(i).getDescription())))){
                article_from_source = articles.get(i);
                return true;
            }
        }
        return false;
    }




    public void correct_article(){
        if(is_correctable()){
            article_to_verify.setTitle(article_from_source.getTitle());
            article_to_verify.setAuthor(article_from_source.getAuthor());
            article_to_verify.setCategory(article_from_source.getCategory());
            article_to_verify.setLink(article_from_source.getLink());
            article_to_verify.setDescription(article_from_source.getDescription());
            article_to_verify.setUpdated_date(article_from_source.getUpdated_date());
            article_to_verify.setPublished_date(article_from_source.getPublished_date());
        }
    }



    public Article get_article(){
        return article_to_verify;
    }


}
