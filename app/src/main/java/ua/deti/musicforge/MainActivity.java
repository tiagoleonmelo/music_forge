package ua.deti.musicforge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    int rpc = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLinearLayout = findViewById(R.id.linear_layout);
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        String posted_songs_json;
        Gson gson = new Gson();
        int i = 0;

        if(pref.contains("posted_songs")) {
            posted_songs_json = pref.getString("posted_songs", "");
            ArrayList<Musica> posted_songs = gson.fromJson(posted_songs_json, new TypeToken<List<Musica>>() {
            }.getType());

            if(posted_songs != null) {
                for( int j = posted_songs.size()-1; j >= 0; j--){
                    Musica temp = posted_songs.get(j);
                    onAddField(temp.getTitle(), "Sabado a noite..", temp.getLyrics(), i);
                    i++;
                }

                posted_songs_json = gson.toJson(posted_songs);
                pref.edit().putString("posted_songs",posted_songs_json).commit();
            }

        }else{
            ArrayList<Musica> posted_songs = new ArrayList<>();
            posted_songs.add(new Musica("Song", "This should be audio"));
            posted_songs.add(new Musica("Edgy", "These are some edgy lyrics\nThis is where they belong"));
            posted_songs.add(new Musica("First of many", "DON'T WANNA CLOSE MY EYES"));
            posted_songs.add(new Musica(" of many", "DON'T WANNA CLOSE MY EYES"));
            posted_songs.add(new Musica("First ", "DON'T WANNA CLOSE MY EYES"));
            posted_songs.add(new Musica("y", "DON'T WANNA CLOSE MY EYES"));

            for( int j = posted_songs.size()-1; j >= 0; j--){
                Musica temp = posted_songs.get(j);
                onAddField(temp.getTitle(), "Sabado a noite..", temp.getLyrics(), i);
                i++;
            }

            posted_songs_json = gson.toJson(posted_songs);
            pref.edit().putString("posted_songs",posted_songs_json).commit();
        }

        //TODO: Figure out where should i call this. (I wanna call it whenever ShareSong creates this activity again)
        /*onAddField("Cool Song", "Aqui está o audio da minha nova música... espero que gostem! Videoclip coming soon! :)","This should be audio");
        onAddField("Edgy", "Sabado a noite..","These are some edgy lyrics\n This is where they belong");
        onAddField("First of many", "Bom dia! Aqui está a letra de a minha primeira música nesta plataforma! Sigam se gostarem ;)","DON'T WANNA CLOSE MY EYES");*/

        // Criar a classe post que associa uma musica a uma caption



        /*rpc = getIntent().getIntExtra("rpc", -1);
        String title = getIntent().getStringExtra("title");
        String caption = getIntent().getStringExtra("caption");
        String lyrics = getIntent().getStringExtra("lyrics");


        if(rpc == 0){
            Toast.makeText(this, "Song Shared successfully!", Toast.LENGTH_LONG).show();
            posted_songs.add(new Musica(title, lyrics));
        }*/

        //TODO: Associate multiple songs to a user and load different lyrics from different songs
        //TODO: Create the profile page, that has the shared posts from a user

        //TODO V2 : Associate each song w a caption by creating class Post
        // TODO CREATE AN ARRAYLIST CONTAINING ONLY THE SONGS THE USER SHARED
        // TODO profile page


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.my_songs:
                Intent intent = new Intent(MainActivity.this, Songs.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent profile = new Intent(MainActivity.this, Profile.class);
                startActivity(profile);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddField(String title_, String caption_, String lyrics_, int n) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card, null);
        card.setId(n);
        int stuff = card.getId();   // we are always getting the same id.
        Toast.makeText(this, Integer.toString(stuff), Toast.LENGTH_LONG).show();

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount());

        TextView user = card.findViewById(R.id.user_name);
        TextView title = card.findViewById(R.id.song_name);
        TextView caption = card.findViewById(R.id.caption);
        TextView lyric = card.findViewById(R.id.lyrics);

        //if(rpc == 0) {
        user.setText("Tiago");
        title.setText(title_);
        caption.setText(caption_);
        lyric.setText(lyrics_);
        //}
        // try to pass as arguments the caption and the components to share
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            //do the things u wanted
            onAddField();
        }
    }*/
}
