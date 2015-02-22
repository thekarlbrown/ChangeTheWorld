package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/*
for the filter, opens up a dialog referencing ratio,
will be improved to not select color on negative click
 */

public class RatioDialogue extends DialogFragment {



    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.minimum_ratio_dialogue, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.ratio_dialogue_change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int i=Integer.parseInt(((EditText) view.findViewById(R.id.ratio_dialogue_entry)).getText().toString());
                            if ((i<100)&&(i>0)) {
                                mListener.onRatioDialogPositiveClick(i);
                            }else{
                                mListener.onRatioDialogNegativeClick();
                            }
                        } catch (Exception e) {
                            mListener.onRatioDialogNegativeClick();
                        }
                    }
                }
        );
        builder.setNegativeButton(R.string.ratio_dialogue_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              mListener.onRatioDialogNegativeClick();
            }
        });
        return builder.create();
    }

    public interface NoticeRatioDialogListener {
        public void onRatioDialogPositiveClick(int i);
        public void onRatioDialogNegativeClick();
    }

    NoticeRatioDialogListener mListener;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (NoticeRatioDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
