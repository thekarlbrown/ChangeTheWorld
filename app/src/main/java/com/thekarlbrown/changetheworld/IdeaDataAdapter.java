package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * this is the primary data adapter extended for ideas that link to ideapage
 */
public class IdeaDataAdapter extends DataAdapter{

    public IdeaDataAdapter(List<IdeaBlock> i, Context context) {
        super(i, context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (IdeaDataAdapterListener) context;
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
                mListener.onIdeaListClick(position);
            }
        });
        return fromsuper;
    }
    public interface IdeaDataAdapterListener {
        public void onIdeaListClick(int number);
    }
    IdeaDataAdapterListener mListener;
}
