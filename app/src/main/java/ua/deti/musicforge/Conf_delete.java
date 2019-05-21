package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class Conf_delete extends DialogFragment {

    FragmentManager fm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you really wish to delete this song?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final String removeThis=getArguments().getString("title");
                        SharedPreferences prefs = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                        String my_songs_json = prefs.getString("my_songs_JOSE_FRIAS","");
                        Gson gson = new Gson();
                        ArrayList<Musica> my_songs = gson.fromJson(my_songs_json, new TypeToken<List<Musica>>(){}.getType());

                        for(Musica m : my_songs){
                            if(m.getTitle().equals(removeThis)){
                                my_songs.remove(m);
                                break;
                            }
                        }

                        my_songs_json = gson.toJson(my_songs);
                        prefs.edit().putString("my_songs_JOSE_FRIAS", my_songs_json).commit();
                        Intent intent = new Intent(getActivity(), Songs.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void setSupport(FragmentManager fm){
        this.fm = fm;
    }
}
