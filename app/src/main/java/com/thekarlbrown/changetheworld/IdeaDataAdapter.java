package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Extension of the DataAdapter for non-Draft ideas, has a MainActivity listener
 */
public class IdeaDataAdapter extends DataAdapter{
    IdeaDataAdapterListener mListener;

    public interface IdeaDataAdapterListener {
        void onIdeaListClick(int number);
    }

    public IdeaDataAdapter(List<IdeaBlock> i, Context context) {
        super(i, context);
        try {
            mListener = (IdeaDataAdapterListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View fromsuper = super.getView(position, convertView, parent);
        fromsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onIdeaListClick(position);
            }
        });
        return fromsuper;
    }
}
