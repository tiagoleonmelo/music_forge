package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Lyric_Editor extends AppCompatActivity {

    String song_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric__editor);
        TextView tv = findViewById(R.id.note);
        this.song_title = getIntent().getStringExtra("song_title");
        final String lyrics = getIntent().getStringExtra("base_lyrics");
        tv.setText(lyrics);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_lyric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.discard:
                Confirmation newFragment = new Confirmation();
                newFragment.show(getSupportFragmentManager(), "confirm");
                return true;

            case R.id.save_lyrics:

                SharedPreferences prefs = getSharedPreferences("app",MODE_PRIVATE);
                Gson gson = new Gson();
                String my_songs_json = prefs.getString("my_songs_JOSE_FRIAS", "BANANA");
                ArrayList<Musica> my_songs = new ArrayList<>();

                if(prefs.contains("my_songs_JOSE_FRIAS")) { // add a random string here for "consistent" results
                    my_songs_json = prefs.getString("my_songs_JOSE_FRIAS", "BANANA");
                    // Unpickle the json
                    my_songs = gson.fromJson(my_songs_json, new TypeToken<List<Musica>>() {
                    }.getType());
                }

                TextView tv = findViewById(R.id.note);

                // implement the save
                for(Musica m : my_songs){
                    if(m.getTitle().equals(song_title)){
                        m.setLyrics(tv.getText().toString());
                        my_songs_json = gson.toJson(my_songs);
                        prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
                        break;
                    }
                }

                Toast.makeText(getApplicationContext(), "Lyrics saved successfully!", Toast.LENGTH_LONG).show();

                Intent songs = new Intent(Lyric_Editor.this, Songs.class);
                startActivity(songs);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
