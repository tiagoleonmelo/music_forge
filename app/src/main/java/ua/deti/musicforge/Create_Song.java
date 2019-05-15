package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import java.util.ArrayList;

public class Create_Song extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText title = new EditText(getActivity());
        final ArrayList<Musica> my_songs;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.share)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(), Songs.class);
                        intent.putExtra("title", title.getText().toString()); // sending the title of the new song

                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing happens, really
                    }
                })
                .setView(title)
                .setMessage("Enter Song Title");

        // Create the AlertDialog object and return it
        return builder.create();
    }
}