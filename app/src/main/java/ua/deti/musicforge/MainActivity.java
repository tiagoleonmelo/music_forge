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

        if(pref.contains("posted_songs_v2")) {
            posted_songs_json = pref.getString("posted_songs_v2", "");
            ArrayList<Post> posted_songs = gson.fromJson(posted_songs_json, new TypeToken<List<Post>>() {
            }.getType());

            if(posted_songs != null) {
                for( int j = posted_songs.size()-1; j >= 0; j--){
                    Musica temp = posted_songs.get(j).getM();
                    onAddField(temp.getTitle(), posted_songs.get(j).getCaption(), temp.getLyrics(), i);
                    i++;
                }

                posted_songs_json = gson.toJson(posted_songs);
                pref.edit().putString("posted_songs_v2",posted_songs_json).commit();
            }

        }else{
            ArrayList<Post> posted_songs = new ArrayList<>();
            posted_songs.add(new Post(new Musica("Song", "This should be audio"), "This is a caption"));
            posted_songs.add(new Post(new Musica("Edgy", "These are some edgy lyrics\nThis is where they belong"), "Sabado a noite.."));
            posted_songs.add(new Post(new Musica("First of many", "DON'T WANNA CLOSE MY EYES"), "Segunda a noite.."));
            posted_songs.add(new Post(new Musica(" of many", "DON'T WANNA CLOSE MY EYES"), "Quarta a noite.."));
            posted_songs.add(new Post(new Musica("First ", "DON'T WANNA CLOSE MY EYES"), "Pretty good"));
            posted_songs.add(new Post(new Musica("Darude Sandstorm", "Source needed"), "Good vibes only :P"));

            for( int j = posted_songs.size()-1; j >= 0; j--){
                Musica temp = posted_songs.get(j).getM();
                onAddField(temp.getTitle(), posted_songs.get(j).getCaption(), temp.getLyrics(), i);
                i++;
            }

            posted_songs_json = gson.toJson(posted_songs);
            pref.edit().putString("posted_songs_v2",posted_songs_json).commit();
        }

        //TODO: Associate multiple songs to a user and load different lyrics from different songs
        //TODO: Create the profile page, that has the shared posts from a user
        // TODO CREATE AN ARRAYLIST CONTAINING ONLY THE SONGS THE USER SHARED
        // TODO profile page
        // TODO Add AUDIO (mp3), SCRIPT (pdf) and VIDEOCLIP (mp4) to the Songs page (or at least the option to add those files)


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

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount());

        TextView user = card.findViewById(R.id.user_name);
        TextView title = card.findViewById(R.id.song_name);
        TextView caption = card.findViewById(R.id.caption);
        TextView lyric = card.findViewById(R.id.lyrics);

        //if(rpc == 0) {
        user.setText("Tiago"); // TODO Add a random name picker here for better feed credibility (don't forget to add user_icons)
        title.setText(title_);
        caption.setText(caption_);
        lyric.setText(lyrics_);
        //}

    }

}
