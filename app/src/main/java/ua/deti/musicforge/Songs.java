package ua.deti.musicforge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    Gson gson = new Gson();
    String my_songs_json = gson.toJson(my_songs);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        final SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        final ArrayList<String> titles = new ArrayList<>();

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

        // Spinner setup
        Spinner dropdown = findViewById(R.id.spinner1);

        boolean contains = false;

        /*for(Musica m : this.my_songs){
            titles.add(m.getTitle());
        }*/

        for(int i = my_songs.size()-1; i >= 0; i--){
            titles.add(my_songs.get(i).getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, titles);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);



        // Share button
        Button share = findViewById(R.id.button_share);
        updateButton(share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tv = findViewById(R.id.song_title);
                TextView tv1 = findViewById(R.id.lyric_editor);

                // implement the save
                for(Musica m : my_songs){
                    if(m.getTitle().equals(tv.getText().toString())){
                        m.setLyrics(tv1.getText().toString());
                        my_songs_json = gson.toJson(my_songs);
                        prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
                        break;
                    }
                }

                ComponentPicker newFragment = new ComponentPicker();
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

                Intent intent = new Intent(Songs.this, Songs.class); // f5
                startActivity(intent);

            }
        });

        // Add file buttons
        Button add_file1 = findViewById(R.id.add_file);
        Button add_file2 = findViewById(R.id.add_file2);
        Button add_file3 = findViewById(R.id.add_file3);

        add_file1.setEnabled(false);
        add_file2.setEnabled(false);
        add_file3.setEnabled(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Intent intent = new Intent(Songs.this, Songs.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent profile = new Intent(Songs.this, Profile.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent search = new Intent(Songs.this, Search.class);
                startActivity(search);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        TextView tv = findViewById(R.id.song_title);
        TextView tv1 = findViewById(R.id.lyric_editor);
        final SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);

        // implement the save
        for(Musica m : my_songs){
            if(m.getTitle().equals(tv.getText().toString())){
                m.setLyrics(tv1.getText().toString());
                my_songs_json = gson.toJson(my_songs);
                prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
                break;
            }
        }

        if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Create new song...")) {

            // Add dialog here
            Create_Song newF = new Create_Song();
            newF.show(getSupportFragmentManager(), "new_song");

        } else {

            // updating activity
            tv.setText(parent.getItemAtPosition(pos).toString());
            for(Musica m : my_songs){
                if(m.getTitle().equals(parent.getItemAtPosition(pos).toString())){
                    tv1.setText(m.getLyrics());
                    break;
                }
            }
        }

        Button share = findViewById(R.id.button_share);
        Button del = findViewById(R.id.button_delete);
        Button re = findViewById(R.id.button_rename);

        updateButton(share);
        updateButton(del);
        updateButton(re);

    }

    public void updateButton(Button share){
        TextView tv = findViewById(R.id.song_title);

        if(tv.getText().toString().equals("") || tv.getText().length() == 0){
            share.setEnabled(false);
        }else{
            share.setEnabled(true);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
