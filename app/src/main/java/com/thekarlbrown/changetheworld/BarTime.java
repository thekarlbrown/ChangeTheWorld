package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * class for the time selection bar
 */
public class BarTime extends Fragment {
    TextView[] complete;
    //how we do by time
    boolean[]times={false,false,false,false,false};
    public void barTimeClick(View in, int selected, final String tag, final MainActivity mainActivity) {
        complete=new TextView[]{(TextView) in.findViewById(R.id.time_day),
                (TextView) in.findViewById(R.id.time_week),
                (TextView) in.findViewById(R.id.time_month),
                (TextView) in.findViewById(R.id.time_year),
                (TextView) in.findViewById(R.id.time_ever),
        };
        for(int i=0;i<complete.length;i++)
        {
            final int x=i;
            complete[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!times[x])
                    {
                        times[x]=true;
                        complete[x].setBackgroundColor(0xffff0000);
                        for(int y=0;y<times.length;y++)
                        {
                            if(y!=x)
                            {
                                if(times[y])
                                {
                                    times[y]=false;
                                    complete[y].setBackgroundColor(0xffffffff);
                                    y=times.length;
                                }
                            }
                        }
                        mainActivity.pullTimeBar(x,tag);
                    }
                }
            });
        }
        setSelected(selected);
    }
    public void setSelected (int i)
    {
        times[i]=true;
        complete[i].setBackgroundColor(0xffff0000);
    }

}
