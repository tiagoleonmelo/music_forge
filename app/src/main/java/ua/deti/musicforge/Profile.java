package ua.deti.musicforge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        parentLinearLayout = findViewById(R.id.linear_layout);
        SharedPreferences pref = getSharedPreferences("app", MODE_PRIVATE);
        String user_songs_json;
        Gson gson = new Gson();

        String userName;
        userName = getIntent().getStringExtra("user_name");
        if(userName == null || userName.equalsIgnoreCase("")){
            userName = getString(R.string.user1);
        }

        TextView name = findViewById(R.id.tvName);
        name.setText(userName);

        if (pref.contains("posted_songs_v6")) {
            user_songs_json = pref.getString("posted_songs_v6", "");
            ArrayList<Post> user_songs = gson.fromJson(user_songs_json, new TypeToken<List<Post>>() {
            }.getType());

            if (user_songs != null) {
                for (int j = user_songs.size() - 1; j >= 0; j--) {
                    if(user_songs.get(j).getPoster().equalsIgnoreCase(userName)) {
                        Musica temp = user_songs.get(j).getM();
                        onAddField(temp.getTitle(), user_songs.get(j).getCaption(), temp.getLyrics(), i, user_songs.get(j).getPoster());
                        i++;
                    }
                }

                user_songs_json = gson.toJson(user_songs);
                pref.edit().putString("posted_songs_v6", user_songs_json).commit();
            }

        }

        ArrayList<User> users;

        String users_json;

        if(pref.contains("users_v2")) {
            users_json = pref.getString("users_v2", "");
            users = gson.fromJson(users_json, new TypeToken<List<User>>() {
            }.getType());
        }else{
            users = new ArrayList<>();
            users.add(new User("Beethoven", "Music is my passion. I love classical!"));
            users.add(new User("Darude", "I made more than one song."));
            users.add(new User("Kendrick Lamar", "DAMN. OUT NOW."));
            users.add(new User("Paul McCartney", "I enjoy rock mostly :)"));
            users.add(new User("Aerosmith", "Now on tour 8)"));
            users.add(new User("Cláudia", "Gosto muito de musica Pop e Hip Hop"));
            users.add(new User("Roberto", "Techno e Hardstyle."));

        }

        for(User u : users){
            if(u.getName().equalsIgnoreCase(userName)){
                TextView bio = findViewById(R.id.tvDescription);
                bio.setText(u.getBio());
            }
        }

        users_json = gson.toJson(users);
        pref.edit().putString("users_v2", users_json).commit();

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
                Intent intent = new Intent(Profile.this, Songs.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent profile = new Intent(Profile.this, Profile.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent search = new Intent(Profile.this, Search.class);
                startActivity(search);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddField(String title_, final String caption_, String lyrics_, int n, final String user_) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.card, null);
        card.setId(n);

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount());

        TextView user = card.findViewById(R.id.user_name);
        TextView title = card.findViewById(R.id.song_name);
        TextView caption = card.findViewById(R.id.caption);
        TextView lyric = card.findViewById(R.id.lyrics);

        user.setText(user_);
            title.setText(title_);
            caption.setText(caption_);
            lyric.setText(lyrics_);

        final Button like = card.findViewById(R.id.button_like);
        final ArrayList<Integer> flag_overkill = new ArrayList<>();
        final String title_again = title_;

        final SharedPreferences pref = getSharedPreferences("app",MODE_PRIVATE);
        String post_json = pref.getString("posted_songs_v6", "");
        final Gson gson = new Gson();
        final ArrayList<Post> posts = gson.fromJson(post_json, new TypeToken<List<Post>>() {
        }.getType());

        for(Post p : posts){
            if(p.getPoster().equalsIgnoreCase(user_) && p.getCaption().equalsIgnoreCase(caption_)){
                like.setText("Liked ("+p.getLikes()+")");
                break;
            }
        }

        like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!flag_overkill.contains(0)) {
                    String s = "You liked the song " + title_again + "!";
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
                    String s = "You unliked the song " + title_again + "!";
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
                Intent post = new Intent(Profile.this, PostActivity.class);
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

    }
}
