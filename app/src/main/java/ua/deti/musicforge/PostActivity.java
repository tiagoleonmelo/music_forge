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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        int i = 0;
        parentLinearLayout = findViewById(R.id.linear_layout);
        String user = getIntent().getStringExtra("poster");
        String caption = getIntent().getStringExtra("caption");
        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        String posts_json = prefs.getString("posted_songs_v7", "");
        final Gson gson = new Gson();
        final ArrayList<Post> posts = gson.fromJson(posts_json, new TypeToken<List<Post>>() {
        }.getType());
        final Post post = new Post();

        for(Post p : posts){
            if(p.getCaption().equalsIgnoreCase(caption) && p.getPoster().equalsIgnoreCase(user)){
                final TextView userName = findViewById(R.id.user_name);
                final TextView caption_ = findViewById(R.id.caption);
                final TextView title = findViewById(R.id.song_name);
                TextView lyrics = findViewById(R.id.lyrics);

                userName.setText(user);
                caption_.setText(caption);
                title.setText(p.getM().getTitle());
                lyrics.setText(p.getM().getLyrics());

                post.setPoster(user);
                post.setCaption(caption);
                post.setMusicTitle(p.getM().getTitle());
                post.setMusicLyrics(p.getM().getLyrics());

                if(p.getComments() == null){
                    p.setComments(new ArrayList<Comment>());
                }

                for(Comment c : p.getComments()){
                    onAddComment(c.getUser(), c.getComment(), i);
                    i++;
                }

                final ArrayList<Integer> flag_overkill = new ArrayList<>();
                final SharedPreferences pref = getSharedPreferences("app",MODE_PRIVATE);

                final ImageButton like_btn = findViewById(R.id.button_like);
                final TextView like = findViewById(R.id.like_txt);
                like.setText(""+p.getLikes());
                like_btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(!flag_overkill.contains(0)) {
                            String s = "You liked the song " + title.getText().toString() + "!";
                            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                            flag_overkill.add(0);
                            for(Post p : posts){
                                if(p.getPoster().equalsIgnoreCase(userName.getText().toString()) && p.getCaption().equalsIgnoreCase(caption_.getText().toString())){
                                    p.like();
                                    like.setText(""+p.getLikes());
                                    like_btn.setImageResource(R.drawable.hearts_filled);
                                    break;
                                }
                            }

                        }else{
                            String s = "You unliked the song " + title.getText().toString() + "!";
                            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                            flag_overkill.remove(0);
                            for(Post p : posts){
                                if(p.getPoster().equalsIgnoreCase(userName.getText().toString()) && p.getCaption().equalsIgnoreCase(caption_.getText().toString())){
                                    p.unlike();
                                    like.setText(""+p.getLikes());
                                    like_btn.setImageResource(R.drawable.hearts);
                                    break;
                                }
                            }
                        }

                        String post_json_ = gson.toJson(posts);
                        pref.edit().putString("posted_songs_v7", post_json_).commit();
                    }
                });

                final ImageButton donate = findViewById(R.id.button_donate);
                donate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getBaseContext(), "You have insufficient funds. ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            }
        }

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            int j = 0;
            @Override
            public void onClick(View v) {
                EditText com = findViewById(R.id.comment_editor);
                post.getComments().add(new Comment("Beethoven", com.getText().toString()));
                post.addComment(new Comment("Beethoven", com.getText().toString()));
                onAddComment("Beethoven", com.getText().toString(), j);
                j++;
                com.setText("");
            }
        });


    }

    private void onAddComment(String user, String comment_, int n) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View comment = inflater.inflate(R.layout.comment, null);
        comment.setId(n);

        // Add the new card before the add field button.
        parentLinearLayout.addView(comment, parentLinearLayout.getChildCount());

        TextView user_name = comment.findViewById(R.id.user);
        TextView comm = comment.findViewById(R.id.comment);

        user_name.setText(user);
        comm.setText(comment_);


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
                Intent intent = new Intent(PostActivity.this, Songs.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent profile = new Intent(PostActivity.this, Profile.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent search = new Intent(PostActivity.this, Search.class);
                startActivity(search);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
