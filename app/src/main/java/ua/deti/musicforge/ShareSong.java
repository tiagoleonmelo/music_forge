package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ShareSong extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText caption = new EditText(getActivity());
        final String title=getArguments().getString("title");
        SharedPreferences pref = this.getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        String my_songs_json = pref.getString("my_songs_JOSE_FRIAS","banana");
        Gson gson = new Gson();
        final ArrayList<Musica> my_songs = gson.fromJson(my_songs_json, new TypeToken<List<Musica>>(){}.getType());


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.share)
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Going back to the main activity, this time sending a new post

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("rpc", 0);
                        for(Musica m:my_songs){
                            if(m.getTitle().equalsIgnoreCase(title)){
                                intent.putExtra("lyrics", m.getLyrics());
                            }
                        }
                        intent.putExtra("title", title);
                        intent.putExtra("caption", caption.getText().toString());

                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing happens, really
                    }
                })
                .setView(caption)
                .setMessage("Enter a caption");

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
