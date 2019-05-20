package ua.deti.musicforge;

public class Comment {

    private String u;
    private String comment;

    public Comment(String u, String c){
        this.u=u;
        this.comment=c;
    }

    public String getUser(){
        return this.u;
    }

    public String getComment(){
        return this.comment;
    }
}
