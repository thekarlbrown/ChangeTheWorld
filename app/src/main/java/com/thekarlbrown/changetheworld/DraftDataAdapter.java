package com.thekarlbrown.changetheworld;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Extension of the DataAdapter for Drafts, has MainActivity listener
 * By Karl Brown ( thekarlbrown )
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
}
