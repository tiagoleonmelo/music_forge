package ua.deti.musicforge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        rpc = getIntent().getIntExtra("rpc",-1);

        if(pref.contains("posted_songs_v6")) {
            posted_songs_json = pref.getString("posted_songs_v6", "");
            ArrayList<Post> posted_songs = gson.fromJson(posted_songs_json, new TypeToken<List<Post>>() {
            }.getType());

            if(posted_songs != null) {
                for( int j = posted_songs.size()-1; j >= 0; j--){
                    Musica temp = posted_songs.get(j).getM();
                    onAddField(temp.getTitle(), posted_songs.get(j).getCaption(), temp.getLyrics(), i, posted_songs.get(j).getPoster());
                    i++;
                }

                posted_songs_json = gson.toJson(posted_songs);
                pref.edit().putString("posted_songs_v6",posted_songs_json).commit();
            }

        }else{
            ArrayList<Post> posted_songs = new ArrayList<>();
            posted_songs.add(new Post(new Musica("Song", "This should be audio"), "This is a caption", "Roberto"));
            posted_songs.add(new Post(new Musica("Edgy", "These are some edgy lyrics\nThis is where they belong"), "Sabado a noite..", "Cláudia"));
            posted_songs.add(new Post(new Musica("Don't Wanna Miss a Thing", "DON'T WANNA CLOSE MY EYES"), "Segunda a noite..", "Aerosmith"));
            posted_songs.add(new Post(new Musica("Live and Let Die", "Right now"), "Quarta a noite..", "Paul McCartney"));
            posted_songs.add(new Post(new Musica("HUMBLE ", "Sit down"), "Pretty good", "Kendrick Lamar"));
            posted_songs.add(new Post(new Musica("Sandstorm", "Source needed"), "Good vibes only :P", "Darude"));

            for( int j = posted_songs.size()-1; j >= 0; j--){
                Musica temp = posted_songs.get(j).getM();
                onAddField(temp.getTitle(), posted_songs.get(j).getCaption(), temp.getLyrics(), i, posted_songs.get(j).getPoster());
                i++;
            }

            posted_songs_json = gson.toJson(posted_songs);
            pref.edit().putString("posted_songs_v6",posted_songs_json).commit();
        }

        // TODO: Add AUDIO (mp3), SCRIPT (pdf) and VIDEOCLIP (mp4) to the Songs page

        // TODO: Bugfixes: Each post isnt saving its comments; infinite likes bug

        /*ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a0e5")));*/

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

            case R.id.settings:
                Intent search = new Intent(MainActivity.this, Search.class);
                startActivity(search);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddField(final String title_, final String caption_, String lyrics_, int n, final String user_) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card, null);
        card.setId(n);

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount());

        final TextView user = card.findViewById(R.id.user_name);
        TextView title = card.findViewById(R.id.song_name);
        TextView caption = card.findViewById(R.id.caption);
        TextView lyric = card.findViewById(R.id.lyrics);

        final Button like = card.findViewById(R.id.button_like);
        final ArrayList<Integer> flag_overkill = new ArrayList<>();

        final SharedPreferences pref = getSharedPreferences("app",MODE_PRIVATE);
        final String post_json = pref.getString("posted_songs_v6", "");
        final Gson gson = new Gson();
        final ArrayList<Post> posts = gson.fromJson(post_json, new TypeToken<List<Post>>() {
        }.getType());

        for(Post p : posts){
            if(p.getPoster().equalsIgnoreCase(user_) && p.getCaption().equalsIgnoreCase(caption_)){
                like.setText("Like ("+p.getLikes()+")");
                break;
            }
        }

        // card on click
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent post = new Intent(MainActivity.this, PostActivity.class);
                post.putExtra("poster", user_);
                post.putExtra("caption", caption_);
                startActivity(post);
            }
        });


        like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!flag_overkill.contains(0)) {
                    String s = "You liked the song " + title_ + "!";
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                    flag_overkill.add(0);
                    for(Post p : posts){
                        if(p.getPoster().equalsIgnoreCase(user_) && p.getCaption().equalsIgnoreCase(caption_)){
                            p.like();
                            like.setText("Liked ("+p.getLikes()+")");
                            break;
                        }
                    }

                }else{
                    String s = "You unliked the song " + title_ + "!";
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                    flag_overkill.remove(0);
                    for(Post p : posts){
                        if(p.getPoster().equalsIgnoreCase(user_) && p.getCaption().equalsIgnoreCase(caption_)){
                            p.unlike();
                            like.setText("Like ("+p.getLikes()+")");
                            break;
                        }
                    }
                }

                String post_json_ = gson.toJson(posts);
                pref.edit().putString("posted_songs_v6", post_json_).commit();
            }
        });

        final Button com = card.findViewById(R.id.button_comment);
        com.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent post = new Intent(MainActivity.this, PostActivity.class);
                post.putExtra("poster", user_);
                post.putExtra("caption", caption_);
                startActivity(post);
            }
        });

        final Button donate = card.findViewById(R.id.button_donate);
        donate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                 Toast.makeText(getBaseContext(), "You have insufficient funds. ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
            }
        });

        title.setText(title_);
        caption.setText(caption_);
        lyric.setText(lyrics_);
        user.setText(user_);

        user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(MainActivity.this, Profile.class);
                if(user.getText().toString().equalsIgnoreCase("Beethoven")){
                    startActivity(profile);
                }else{
                    profile.putExtra("user_name",user.getText().toString() );
                    startActivity(profile);
                }
            }
        });



    }

}
