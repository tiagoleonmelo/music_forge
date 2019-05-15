package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

public class ShareSong extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText caption = new EditText(getActivity());
        final String title=getArguments().getString("title");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.share)
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Going back to the main activity, this time sending a new post

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("rpc", 0);
                        //intent.putExtra("lyric", lyrics.getText().toString());
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
