package com.thekarlbrown.changetheworld;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Extension of the DataAdapter for Drafts, has MainActivity listener
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class DraftDataAdapter extends DataAdapter{
    DraftDataAdapterListener mListener;

    public interface DraftDataAdapterListener {
        void onDraftListClick(int number);
    }

    public DraftDataAdapter(List<IdeaBlock> i, Context context) {
        super(i, context);
        try{
            mListener = (DraftDataAdapterListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    /**
     * In addition to the super (DataAdapter), sets an onClickListener for each Draft
     * @param position Current position in the ListView
     * @param convertView ViewHolder for optimization purposes
     * @param parent Parent ViewGroup, required for inflation
     * @return Each Draft with an additional onClickListener
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       View fromsuper = super.getView(position,convertView, parent);
        fromsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sends you to an Idea Tab for the Draft
                mListener.onDraftListClick(position);
            }
        });
        return fromsuper;
    }
}
