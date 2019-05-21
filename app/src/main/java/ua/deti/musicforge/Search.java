package ua.deti.musicforge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        parentLinearLayout = findViewById(R.id.linear_layout);

        Button search = findViewById(R.id.search_button);
        final EditText et = findViewById(R.id.search_txt);
        search.setOnClickListener(new View.OnClickListener() {
            int i = 1;

            @Override
            public void onClick(View v) {

                // Deleting previous results
                for( int j = 0; j <= i; j++){
                    View card = findViewById(j);
                    if(card != null) {
                        ((ViewManager) card.getParent()).removeView(card);
                    }
                }



                // Getting an array list of the registered users
                SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
                Gson gson = new Gson();
                String users_json = prefs.getString("users_v2","");
                final ArrayList<User> users = gson.fromJson(users_json, new TypeToken<List<User>>() {
                }.getType());

                if(users != null) {
                    for(User u: users){
                        if(u.getName().toLowerCase().contains(et.getText().toString().toLowerCase())){
                            onAddResult(u.getName(), i, 0, "", "");
                            i++;
                        }
                    }
                }

                boolean flag = true;
                String posts_json = prefs.getString("posted_songs_v6","");
                final ArrayList<Post> posts = gson.fromJson(posts_json, new TypeToken<List<Post>>() {
                }.getType());

                if(posts != null) {
                    for(Post u: posts){
                        if(u.getM().getTitle().toLowerCase().contains(et.getText().toString().toLowerCase())){
                            if(i != 1 && flag){
                                // print horizontal line
                                View view = new View(getBaseContext());
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
                                lp.setMargins(0,50,0,0);
                                view.setLayoutParams(lp);
                                view.setBackgroundColor(Color.parseColor("#B3B3B3"));

                                parentLinearLayout.addView(view);
                                flag = false;
                            }
                            onAddResult(u.getM().getTitle(), i, 1, u.getPoster(), u.getCaption());
                            i++;
                        }
                    }
                }

            }
        });
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
                Intent intent = new Intent(Search.this, Songs.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent profile = new Intent(Search.this, Profile.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent search = new Intent(Search.this, Search.class);
                startActivity(search);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddResult(String username, int n, int selector, final String poster, final String caption) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.search_result, null);
        card.setId(n);

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount());

        final TextView user = card.findViewById(R.id.userName);
        final ImageView imag = card.findViewById(R.id.avatar);

        user.setText(username);

        if(selector==0) {
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(Search.this, Profile.class);
                    if (user.getText().toString().equalsIgnoreCase("Beethoven")) {
                        startActivity(profile);
                    } else {
                        profile.putExtra("user_name", user.getText().toString());
                        startActivity(profile);
                    }
                }
            });

            imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(Search.this, Profile.class);
                    if (user.getText().toString().equalsIgnoreCase("Beethoven")) {
                        startActivity(profile);
                    } else {
                        profile.putExtra("user_name", user.getText().toString());
                        startActivity(profile);
                    }
                }
            });
        }else{

            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent post = new Intent(Search.this, PostActivity.class);
                    post.putExtra("poster", poster);
                    post.putExtra("caption", caption);
                    startActivity(post);
                }
            });

            imag.setImageResource(R.drawable.music_note);

            imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent post = new Intent(Search.this, PostActivity.class);
                    post.putExtra("poster", poster);
                    post.putExtra("caption", caption);
                    startActivity(post);
                }
            });
        }

    }
}
