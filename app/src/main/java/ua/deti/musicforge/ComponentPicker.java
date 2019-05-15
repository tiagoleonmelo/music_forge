package ua.deti.musicforge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

public class ComponentPicker extends DialogFragment {

    FragmentManager fm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> selectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String title=getArguments().getString("title");


        String[] array = {"Lyrics"};    // ideally, we'd iterate over every item and check if their value is different than null. but for now, assuming we only want to share
        // lyrics is enough
        builder.setTitle(R.string.pick_comps)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedItems.add(which);
                                    AlertDialog dialog1 = (AlertDialog) getDialog();
                                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                } else if (selectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedItems.remove(Integer.valueOf(which));
                                    AlertDialog dialog1 = (AlertDialog) getDialog();
                                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                }

                            }

                        })
                // Set the action buttons
                .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        DialogFragment newFragment = new ShareSong();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", title);
                        //set Arguments
                        newFragment.setArguments(bundle);
                        newFragment.show(fm, "share_two");

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        // disable positive button by default
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public void setSupport(FragmentManager fm){
        this.fm = fm;
    }

}
