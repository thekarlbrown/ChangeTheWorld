package com.thekarlbrown.changetheworld;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * this is for drafts and ties back to the mainactivity
 */
public class DraftDataAdapter extends DataAdapter{

    public DraftDataAdapter(IdeaBlock i, Context context) {
            super(i, context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DraftDataAdapterListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       View fromsuper = super.getView(position,convertView, parent);
        fromsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDraftListClick(position);
            }
        });
        return fromsuper;
    }
    public interface DraftDataAdapterListener {
        public void onDraftListClick(int number);
        //public void onDialogNeutralClick(String r);
        // public void onDialogNegativeClick();
    }

    DraftDataAdapterListener mListener;
}
