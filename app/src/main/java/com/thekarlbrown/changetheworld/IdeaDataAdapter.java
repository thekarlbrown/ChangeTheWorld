package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Extension of the DataAdapter for non-Draft ideas, has a MainActivity listener
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
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
    /**
     * In addition to the super (DataAdapter), sets an onClickListener for each Idea
     * @param position Current position in the ListView
     * @param convertView ViewHolder for optimization purposes
     * @param parent Parent ViewGroup, required for inflation
     * @return Each Idea with an additional onClickListener
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View fromsuper = super.getView(position, convertView, parent);
        fromsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sends you to an Idea Tab for the Idea
                mListener.onIdeaListClick(position);
            }
        });
        return fromsuper;
    }
}
