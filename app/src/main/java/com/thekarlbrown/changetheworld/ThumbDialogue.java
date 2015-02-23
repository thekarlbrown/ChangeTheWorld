package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * dialog for setting thumbs
 *
 */
public class ThumbDialogue extends DialogFragment {
    String tag;
    public static ThumbDialogue newInstance() {
        ThumbDialogue fragment = new ThumbDialogue();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag=getArguments().getString("tag");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.minimum_thumbs_dialogue, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.thumb_dialogue_change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int i=Integer.parseInt(((EditText) view.findViewById(R.id.thumbs_dialogue_entry)).getText().toString());
                            if ((i<100)&&(i>0)) {
                                mListener.onThumbDialogPositiveClick(i,tag);
                            }else{
                                mListener.onThumbDialogNegativeClick();
                            }
                        } catch (Exception e) {
                            mListener.onThumbDialogNegativeClick();
                        }

                    }
                }
        );

        builder.setNegativeButton(R.string.thumb_dialogue_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onThumbDialogNegativeClick();
            }
        });

        return builder.create();
    }

    public interface NoticeThumbDialogListener {
        public void onThumbDialogPositiveClick(int i,String tag);
        public void onThumbDialogNegativeClick();
    }

    NoticeThumbDialogListener mListener;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (NoticeThumbDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
