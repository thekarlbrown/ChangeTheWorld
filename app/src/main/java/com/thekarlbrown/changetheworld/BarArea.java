package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * class for the area selection bar
 */
public class BarArea extends Fragment {
    //how we do by area
    boolean[]areas={false,false,false,false,false,false};
    public void barAreaClick(View in, int selected) {
        final TextView[] complete={(TextView) in.findViewById(R.id.bot_area_local),
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
                                }
                            }
                        }
                    }
                }
            });
        }
        complete[selected].setBackgroundColor(0xffff0000);
        areas[selected]=true;
    }
}
