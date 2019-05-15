package ua.deti.musicforge;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    int rpc = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLinearLayout = findViewById(R.id.linear_layout);

        //TODO: Figure out where should i call this. (I wanna call it whenever ShareSong creates this activity again)
        onAddField("Cool Song", "Aqui está o audio da minha nova música... espero que gostem! Videoclip coming soon! :)");
        onAddField("Cool Song", "Aqui está o audio da minha nova música... espero que gostem! Videoclip coming soon! :)");
        onAddField("Cool Song", "Aqui está o audio da minha nova música... espero que gostem! Videoclip coming soon! :)");

        rpc = getIntent().getIntExtra("rpc", -1);
        String title = getIntent().getStringExtra("title");
        String caption = getIntent().getStringExtra("caption");

        if(rpc == 0){
            Toast.makeText(this, "entrei no if", Toast.LENGTH_LONG).show();
            onAddField(title, caption);
        }

        //TODO: Associate multiple songs to a user and load different lyrics from different songs
        //TODO: Create the profile page, that has the shared posts from a user
        //TODO: Allow song creation
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

        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddField(String title_, String caption_) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View card = inflater.inflate(R.layout.card, null);

        // Add the new card before the add field button.
        parentLinearLayout.addView(card, parentLinearLayout.getChildCount() - 1);

        TextView user = findViewById(R.id.user_name);
        TextView title = findViewById(R.id.song_name);
        TextView caption = findViewById(R.id.caption);


        if(rpc == 0) {
            user.setText("Tiago");
            title.setText(title_);
            caption.setText(caption_);
        }
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
