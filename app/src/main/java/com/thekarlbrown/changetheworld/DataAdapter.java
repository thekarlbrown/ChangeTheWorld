package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
this is the base data adapter for ideas, implements a viewholder for performance
 */
public class DataAdapter extends BaseAdapter{
    Context context;
    IdeaBlock current;
    LayoutInflater inflater;
    View row;
    TextView t;
    int curposition;
    //for later when I am doing screen scaling
    int adjustable = 115;


    public DataAdapter(IdeaBlock i, Context context) {
        current=i;
        this.context=context;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        //return current.Length(); should be fixed when container is properly dealt with
        return current.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return current.atPosition(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return curposition;
    }
    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        curposition=position;
        ViewHolderData viewHolder;
        if(convertView==null)
        {
            inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.idea_container, parent, false);
            viewHolder=new ViewHolderData();
            viewHolder.ideatext=(TextView)convertView.findViewById(R.id.container_idea_text);
            viewHolder.upcount=(TextView) convertView.findViewById(R.id.container_up_count);
            viewHolder.downcount=(TextView) convertView.findViewById(R.id.container_down_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderData) convertView.getTag();
        }
        if( current.getIdea(position).length()>adjustable) {
            viewHolder.ideatext.setText("Title: " + current.getTitle(position) + "\n" + current.getIdea(position).substring(0, adjustable) + ".....");
        }else{
            viewHolder.ideatext.setText("Title: " + current.getTitle(position) + "\n" + current.getIdea(position));
        }
        viewHolder.upcount.setText(Integer.toString(current.getTup(position)));
        viewHolder.downcount.setText(Integer.toString(current.getTdown(position)));
        return convertView;
    }
    static class ViewHolderData
    {
        TextView ideatext;
        TextView upcount;
        TextView downcount;
    }



}