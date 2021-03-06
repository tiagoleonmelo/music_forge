package ua.deti.musicforge;

import java.io.Serializable;

public class Musica implements Serializable {

    private String title, lyrics;

    public Musica(String title, String lyrics){
        this.title=title;
        this.lyrics=lyrics;
    }

    public String getTitle(){
        return this.title;
    }

    public String getLyrics(){
        return this.lyrics;
    }

    public void setTitle(String t){
        this.title = t;
    }

    public void setLyrics(String l){
        this.lyrics = l;
    }
}
