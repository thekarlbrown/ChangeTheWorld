package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;
/*
clean the search dialog better
 */

public class ThumbDialogue extends DialogFragment {



    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.minimum_thumbs_dialogue, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.thumb_dialogue_change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //String temp = ((EditText) view.findViewById(R.id.search_dialogue_entry)).getText().toString();
                            int i=Integer.parseInt(((EditText) view.findViewById(R.id.thumbs_dialogue_entry)).getText().toString());
                            // if (!Pattern.matches("[0-9]+", temp)||(i>99)||(i<1)) {
                            //ratio = -1;

                            if ((i<100)&&(i>0)) {
                                mListener.onThumbDialogPositiveClick(i);
                            }else{
                                mListener.onThumbDialogNegativeClick();
                            }
                        } catch (Exception e) {
                            mListener.onThumbDialogNegativeClick();
                        }

                    }
                }
        );
        /*
        builder.setNeutralButton(R.string.search_neutral, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String temp = ((EditText) view.findViewById(R.id.search_dialogue_entry)).getText().toString();
                    if (!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/\\r\\n]+)$", temp)) {
                        searchtext = null;
                    } else {
                        mListener.onDialogNeutralClick(temp);
                    }
                } catch (Exception e) {
                    searchtext = null;
                }
            }
        });
*/
        builder.setNegativeButton(R.string.thumb_dialogue_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mListener.onDialogNegativeClick();
            }
        });

        return builder.create();
    }

    public interface NoticeThumbDialogListener {
        public void onThumbDialogPositiveClick(int i);
        //public void onDialogNeutralClick(String r);
        public void onThumbDialogNegativeClick();
    }

    NoticeThumbDialogListener mListener;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeThumbDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
