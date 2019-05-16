package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ShareSong extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText caption = new EditText(getActivity());
        final String title=getArguments().getString("title");
        SharedPreferences pref = this.getActivity().getSharedPreferences("app", MODE_PRIVATE);
        String my_songs_json = pref.getString("my_songs_JOSE_FRIAS","banana");
        final Gson gson = new Gson();
        final ArrayList<Musica> my_songs = gson.fromJson(my_songs_json, new TypeToken<List<Musica>>(){}.getType());
        final SharedPreferences prefs = getActivity().getSharedPreferences("app", MODE_PRIVATE);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.share)
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Going back to the main activity, this time sending a new post
                        String lyrics = "ERROR";
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("rpc", 0);
                        for(Musica m:my_songs){
                            if(m.getTitle().equalsIgnoreCase(title)){
                                lyrics = m.getLyrics();
                            }
                        }

                        // fetch the prefs
                        String posted_songs_json = prefs.getString("posted_songs_v2", "");
                        ArrayList<Post> posted_songs = gson.fromJson(posted_songs_json, new TypeToken<List<Post>>() {
                        }.getType());
                        posted_songs.add(new Post(new Musica(title,lyrics), caption.getText().toString()));
                        posted_songs_json = gson.toJson(posted_songs);
                        prefs.edit().putString("posted_songs_v2",posted_songs_json).commit();


                        // unpickle, update and commit both jsons
                        String user_songs_json = prefs.getString("user_songs_v2", "");
                        if(user_songs_json.equalsIgnoreCase("")){
                            ArrayList<Post> user_songs = new ArrayList<>();
                            user_songs.add(new Post(new Musica(title,lyrics), caption.getText().toString()));

                            user_songs_json = gson.toJson(user_songs);

                            prefs.edit().putString("user_songs_v2",user_songs_json).commit();
                        }else{
                            ArrayList<Post> user_songs = gson.fromJson(user_songs_json, new TypeToken<List<Post>>() {
                            }.getType());
                            user_songs.add(new Post(new Musica(title,lyrics), caption.getText().toString()));

                            user_songs_json = gson.toJson(user_songs);

                            prefs.edit().putString("user_songs_v2",user_songs_json).commit();
                        }

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
