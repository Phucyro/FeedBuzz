package model;

import controller.SourceMenu;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class SourceModel implements Serializable {
    private Source[] sources = new Source[8];
    private int number_articles;
    private int lifespan_articles;
    private List<Source> selected_sources = new ArrayList<>();
    private List<String> chosen_numbers = new ArrayList<>();

    public SourceModel() throws IOException {
        File settings = new File("save_settings.txt");
        if (settings.exists()) {
            BufferedReader br = new BufferedReader(new FileReader("save_settings.txt"));
            number_articles = Integer.parseInt(br.readLine());
            lifespan_articles = Integer.parseInt(br.readLine());
            //String str;
            //while ( (str = br.readLine()) != null){

            //}
            String numbers = br.readLine();
            if (numbers != null) {
                for (int i = 0; i < numbers.length(); i++) {
                    chosen_numbers.add(Character.toString((numbers.charAt(i))));
                }
            }
        } else {
            number_articles = 5;
            lifespan_articles = 5;
            //chosen_numbers.add("2");
        }

    }

    public void getArticles() {
        for (int i = 0; i < sources.length; i++) {
            if (selected_sources.get(i).isChecked()) {
                sources[i].download(number_articles, lifespan_articles);
            }
        }
    }

    public void set_articles_persource(int number) {
        number_articles = number;
    }

    public void set_articles_lifespan(int lifespan) {
        lifespan_articles = lifespan;
    }

    public int get_articles_persource() {
        return number_articles;
    }

    public int get_articles_lifespan() {
        return lifespan_articles;
    }

    public void setSources(List<Source> chosen_sources) {
        selected_sources = chosen_sources;
    }

    public List<String> get_chosen_numbers() {
        return chosen_numbers;
    }


    public void applySettings(int number, int lifespan, List<String> numbers)
            throws FileNotFoundException {
        number_articles = number;
        lifespan_articles = lifespan;
        chosen_numbers = numbers;
        PrintWriter writer = new PrintWriter("save_settings.txt");
        writer.println(number_articles);
        writer.println(lifespan_articles);
        System.out.println(numbers);
        for (int i = 0; i < numbers.size(); i++) {
            writer.print(chosen_numbers.get(i));
        }
        writer.close();
        chosen_numbers.clear();
    }
}
