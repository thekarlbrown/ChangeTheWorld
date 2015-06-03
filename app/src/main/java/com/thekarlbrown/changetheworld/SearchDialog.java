package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * Search Dialogue Class that engages the User search for specific content in the database
 * Has a listener for the MainActivity to call the Search Tab
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */

public class SearchDialog extends DialogFragment {
    SearchDialogListener mListener;
    public interface SearchDialogListener { void onSearchDialogPositiveClick(String r);  }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (SearchDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    /**
     * Set a Custom AlertDialog specific to Ratio Dialogue's that takes valid data into account
     * @param savedInstanceState Holds the tags of the Fragment
     * @return A created Dialogue from the builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.search_dialogue, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.search_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String temp = ((EditText) view.findViewById(R.id.search_dialogue_entry)).getText().toString();
                            if (!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*<>\\\\\\/\\r\\n]+)$", temp)) {

                            } else {
                                mListener.onSearchDialogPositiveClick(temp);
                            }
                        } catch (Exception e) { }
                    }
                }
        );
        builder.setNegativeButton(R.string.search_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }
}
