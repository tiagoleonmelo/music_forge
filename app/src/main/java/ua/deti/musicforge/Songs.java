package ua.deti.musicforge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Songs extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<Musica> my_songs = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();
    Gson gson = new Gson();
    String my_songs_json = gson.toJson(my_songs);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

        if(prefs.contains("my_songs_JOSE_FRIAS")){ // add a random string here for "consistent" results
            this.my_songs_json = prefs.getString("my_songs_JOSE_FRIAS", "BANANA");
            // Unpickle the json
            this.my_songs = gson.fromJson(this.my_songs_json, new TypeToken<List<Musica>>(){}.getType());
        }else{
            prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
        }

        String new_title = getIntent().getStringExtra("title");
        if(new_title != null){
            my_songs.add(new Musica(new_title, ""));
            my_songs_json = gson.toJson(my_songs);
            prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
        }

        /*song_titles = getIntent().getStringArrayListExtra("titles");
        song_lyrics = getIntent().getStringArrayListExtra("lyrics");

        if(song_titles == null){
            song_titles = new ArrayList<>();
        }

        if(song_lyrics == null){
            song_lyrics = new ArrayList<>();
        }*/

        // Spinner setup
        Spinner dropdown = findViewById(R.id.spinner1);

        boolean contains = false;

        for(Musica m : this.my_songs){
            titles.add(m.getTitle());
        }

        for(Musica m : this.my_songs){
            if(m.getTitle().equalsIgnoreCase("Create new song...")) {
                contains = true;
                break;
            }
        }

        if(my_songs.size() == 0 || !contains) {
            my_songs.add(new Musica("Create new song...",""));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, titles);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);



        // Share button
        Button share = findViewById(R.id.button_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentPicker newFragment = new ComponentPicker();
                TextView tv = findViewById(R.id.song_title);
                Bundle bundle = new Bundle();

                // Sending the song title to the new fragment
                bundle.putString("title", tv.getText().toString());
                //set Arguments
                newFragment.setArguments(bundle);
                newFragment.setSupport(getSupportFragmentManager());
                newFragment.show(getSupportFragmentManager(), "share_one");

            }
        });


        // Delete button
        Button delete = findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tv = findViewById(R.id.song_title);
                String removeThis = tv.getText().toString();

                for(Musica m : my_songs){
                    if(m.getTitle().equals(removeThis)){
                        my_songs.remove(m);
                        break;
                    }
                }

                titles.remove(removeThis);

                my_songs_json = gson.toJson(my_songs);
                prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub, menu);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        TextView tv = findViewById(R.id.song_title);

        if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Create new song...")) {

            // Add dialog here
            Create_Song newF = new Create_Song();
            newF.show(getSupportFragmentManager(), "new_song");

        } else {
            tv.setText(parent.getItemAtPosition(pos).toString());
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
