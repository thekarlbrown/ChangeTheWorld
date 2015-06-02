package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * This is the base DataAdapter for ideas, implementing a ViewHolder for Performance
 * By Karl Brown ( thekarlbrown )
 */
public class DataAdapter extends BaseAdapter{
    Context context;
    List<IdeaBlock> current;
    LayoutInflater inflater;
    TextView t;
    int curposition;
    int adjustable = 150; //To set a limit to the code displayed per line

    public DataAdapter(List<IdeaBlock> i, Context context) {
        current=i;
        this.context=context;
    }
    public int getCount() {   return current.size();  }
    public Object getItem(int arg0) { return current.get(arg0); }
    public long getItemId(int position) { return curposition; }

    //ViewHolder to optimize the speed of the ListView
    static class ViewHolderData {
        TextView ideatitle;
        TextView ideatext;
        TextView upcount;
        TextView downcount;
    }

    @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        curposition=position;
        ViewHolderData viewHolder;
        //Create the ViewHolder if necessary
        if(convertView==null) {
            inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.idea_container, parent, false);
            viewHolder=new ViewHolderData();
            viewHolder.ideatitle=(TextView)convertView.findViewById(R.id.container_idea_title);
            viewHolder.ideatext=(TextView)convertView.findViewById(R.id.container_idea_text);
            viewHolder.upcount=(TextView) convertView.findViewById(R.id.container_up_count);
            viewHolder.downcount=(TextView) convertView.findViewById(R.id.container_down_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderData) convertView.getTag();
        }
        //Set tags for each Idea
        viewHolder.ideatitle.setText( current.get(position).getTitle());
        if( current.get(position).getIdea().length()>adjustable) {
            viewHolder.ideatext.setText(current.get(position).getIdea().substring(0, adjustable) + ".....");
        }else{
            viewHolder.ideatext.setText(current.get(position).getIdea());
        }
        viewHolder.upcount.setText(Integer.toString(current.get(position).getTup()));
        viewHolder.downcount.setText(Integer.toString(current.get(position).getTdown()));
        return convertView;
    }
}