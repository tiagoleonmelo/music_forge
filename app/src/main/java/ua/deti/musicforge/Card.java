package ua.deti.musicforge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Card extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        Toast.makeText(this.getBaseContext(), "my text",Toast.LENGTH_LONG).show();


        // Like button
        Button like = findViewById(R.id.button_like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "my text",Toast.LENGTH_LONG).show();
            }
        });
    }

}
