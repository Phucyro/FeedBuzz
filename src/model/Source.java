package model;

public class Source {
    private String source_name ;
    private boolean checked = true;

    public Source(String name) {
        source_name = name;
    }

    public boolean  isChecked() { return checked; }

    public void download(int number,int lifespan){

    }

    public String getName() {
        return source_name;
    }
    public void setName(String name) {
        source_name = name;
    }
}
