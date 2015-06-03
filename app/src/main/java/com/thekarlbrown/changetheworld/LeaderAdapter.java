package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * This is the LeaderAdapter for the Leaderboard to be implemented, implementing a ViewHolder for performance
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class LeaderAdapter extends BaseAdapter{
    Context context;
    LeaderBlock current;
    LayoutInflater inflater;
    TextView t;
    int curposition;
    int curleader;

    public LeaderAdapter(LeaderBlock l, Context context, int selection) {
        current=l;
        curleader=selection;
        this.context=context;
    }

    public int getCount() { return current.size(); }
    public Object getItem(int arg0) {  return current.atPosition(arg0);}
    public long getItemId(int position) { return curposition; }

    //ViewHolder to optimize the speed of the ListView
    static class ViewHolderLeader {
        TextView leader_text;
        ImageView leader_image;
        TextView leader_rating;
    }

    /**
     * Over-rides BaseAdapter to create a ListView for the Leaderboard with all necessary characteristics
     * @param position Current position in the ListView
     * @param convertView ViewHolder for optimization purposes
     * @param parent Parent ViewGroup, required for inflation
     * @return each  specific Leaderboard View item for the displayed ListView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        curposition=position;
        ViewHolderLeader viewHolder;
        //Create the ViewHolder if necessary
        if(convertView==null)
        {
            inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.leader_container, parent, false);
            viewHolder=new ViewHolderLeader();
            viewHolder.leader_image=(ImageView)convertView.findViewById(R.id.container_leader_image);
            viewHolder.leader_rating=(TextView)convertView.findViewById(R.id.container_leader_rating);
            viewHolder.leader_text=(TextView)convertView.findViewById(R.id.container_leader_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderLeader)convertView.getTag();
        }
        //Based on the current selection of the Leaderboard tab, change how the View is displayed
        switch(curleader)
        {
            case 0:
                viewHolder.leader_text.setText((curposition+1) + ".    " + current.getAccurName(position) + "    :");
                viewHolder.leader_image.setImageResource(R.drawable.ic_gold_thumb);
                viewHolder.leader_rating.setText("    " + doubleToString(current.getAccurPer(position)) + "% accuracy when rating!");
                return  convertView;
            case 1:
                viewHolder.leader_text.setText((curposition+1) + ".    " + current.getAddedName(position)+ "    :");
                viewHolder.leader_image.setImageResource(R.drawable.ic_gold_star);
                viewHolder.leader_rating.setText("    " + current.getAddedNum(position) + " quality ideas added!");
                return  convertView;
            case 2:
                viewHolder.leader_text.setText((curposition+1) + ".    " + current.getQualityName(position) + "    :");
                viewHolder.leader_image.setImageResource(R.drawable.ic_gold_bulb);
                viewHolder.leader_rating.setText("    " +  doubleToString(current.getQualityPer(position)) + "% rated submission!");
                return  convertView;
            case 3:
                viewHolder.leader_text.setText((curposition+1) + ".    " + current.getCommentName(position) + "    :");
                viewHolder.leader_image.setImageResource(R.drawable.ic_gold_speech);
                viewHolder.leader_rating.setText("    " +  doubleToString(current.getCommentPer(position)) + "% rated improvement!");
                return  convertView;
            default:
                viewHolder.leader_text.setText("Ya goofed");
                viewHolder.leader_image.setImageResource(R.drawable.ic_red_x);
                viewHolder.leader_rating.setText("Dunderpate");
                return  convertView;
        }
    }

    /**
     * Converts a double to string including decimal places
     * @param d Double to convert
     * @return String that is the double in two decimal places
     */
    public static String doubleToString(double d) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        return fmt.format(d);
    }
}