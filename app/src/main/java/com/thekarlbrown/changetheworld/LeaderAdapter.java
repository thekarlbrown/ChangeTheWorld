package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/*
    adapter for leaderboard
    need to add links on onclick to user profiles
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
        ViewHolderLeader viewHolder;
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
    static class ViewHolderLeader
    {
        TextView leader_text;
        ImageView leader_image;
        TextView leader_rating;
    }
    public static String doubleToString(double d) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        return fmt.format(d);
    }
}