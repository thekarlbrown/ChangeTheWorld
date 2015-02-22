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

/*
dialog for the search tab

 */


public class SearchDialog extends DialogFragment {
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
                            if (!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/\\r\\n]+)$", temp)) {

                            } else {
                                mListener.onDialogPositiveClick(temp);
                            }
                        } catch (Exception e) {

                        }
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

        public interface NoticeDialogListener {
            public void onDialogPositiveClick(String r);
        }

    NoticeDialogListener mListener;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
