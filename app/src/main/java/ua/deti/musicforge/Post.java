package ua.deti.musicforge;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Post {

    private Musica m;
    private String caption;
    private String poster;
    private ArrayList<Comment> comments;
    private int likes;
    private String musicLyrics, musicTitle;

    public Post(){

    }

    public Post(Musica m, String c){
        this.m=m;
        this.caption=c;
    }

    public Post(Musica m, String c, String p){
        this.m=m;
        this.caption=c;
        this.poster=p;
    }

    public Post(String title, String lyrics, String caption){
        this.m = new Musica(title, lyrics);
        this.caption = caption;
    }

    public Post(String title, String lyrics, String caption, String poster){
        this.m = new Musica(title, lyrics);
        this.caption = caption;
        this.poster=poster;
        this.likes = 0;
        this.comments = new ArrayList<>();
    }

    public String getPoster(){return poster;}

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

    public void like(){
        this.likes++;
    }

    public void unlike(){
        this.likes--;
    }

    public void addComment(Comment s){
        if(this.comments==null){
            this.comments= new ArrayList<>();
        }
        this.comments.add(s);
    }

    public ArrayList<Comment> getComments(){
        if(this.comments==null){
            return new ArrayList<>();
        }
        return this.comments;
    }

    public int getLikes(){
        return likes;
    }

    public void setComments(ArrayList<Comment> e){
        this.comments = e;
    }

    public void setPoster(String poster){
        this.poster = poster;
    }

    public void setMusicLyrics(String lyric){
        this.musicLyrics=lyric;
    }

    public void setMusicTitle(String title){
        this.musicTitle=title;
    }
}

