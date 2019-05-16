package ua.deti.musicforge;

public class Post {

    private Musica m;
    private String caption;

    public Post(Musica m, String c){
        this.m=m;
        this.caption=c;
    }

    public Post(String title, String lyrics, String caption){
        this.m = new Musica(title, lyrics);
        this.caption = caption;
    }

    public Musica getM() {
        return m;
    }

    public void setM(Musica m) {
        this.m = m;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
