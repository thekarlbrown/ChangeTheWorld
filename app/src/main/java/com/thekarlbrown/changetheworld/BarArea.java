package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * class for the area selection bar
 */
public class BarArea extends Fragment {
    //how we do by area
    TextView[] complete;
    boolean[]areas={false,false,false,false,false,false};
    public void barAreaClick(View in, int selected,final String tag, final MainActivity mainActivity) {
        complete=new TextView[]{(TextView) in.findViewById(R.id.bot_area_local),
                (TextView) in.findViewById(R.id.bot_area_hun),
                (TextView) in.findViewById(R.id.bot_area_state),
                (TextView) in.findViewById(R.id.bot_area_country),
                (TextView) in.findViewById(R.id.bot_area_global),
                (TextView) in.findViewById(R.id.bot_area_friends)};
        for(int i=0;i<complete.length;i++)
        {
            final int x=i;
            complete[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!areas[x])
                    {
                        areas[x]=true;
                        complete[x].setBackgroundColor(0xffff0000);
                        for(int y=0;y<areas.length;y++)
                        {
                            if(y!=x)
                            {
                                if(areas[y])
                                {
                                    areas[y]=false;
                                    complete[y].setBackgroundColor(0xffffffff);
                                    y=areas.length;
                                }
                            }
                        }
                        mainActivity.pullAreaBar(x,tag);

                    }
                }
            });
        }
        setSelected(selected);
    }
    public void setSelected(int i)
    {
        complete[i].setBackgroundColor(0xffff0000);
        areas[i]=true;
    }
}
