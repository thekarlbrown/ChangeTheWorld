package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * Class that serves to handle the Bar for different time periods.
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class BarTime extends Fragment {
    TextView[] complete;
    boolean[]times={false,false,false,false,false};

    /**
     * Setting the onClickListeners for each item in the bar
     * @param in The View of the current fragment
     * @param selected The currently selected item
     * @param tag The Tag of the current fragment
     * @param mainActivity The base activity
     */
    public void barTimeClick(View in, int selected, final String tag, final MainActivity mainActivity) {
        complete=new TextView[]{(TextView) in.findViewById(R.id.time_day),
                (TextView) in.findViewById(R.id.time_week),
                (TextView) in.findViewById(R.id.time_month),
                (TextView) in.findViewById(R.id.time_year),
                (TextView) in.findViewById(R.id.time_ever),
        };
        for(int i=0;i<complete.length;i++) {
            final int x=i;
            complete[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!times[x]) {
                        times[x]=true;
                        complete[x].setBackgroundColor(0xff00AC00);
                        for(int y=0;y<times.length;y++) {
                            if(y!=x) {
                                if(times[y]) {
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
    /**
     * Set a item in the bar as selected
     * @param i Which of the items in the bar to select
     */
    public void setSelected (int i)
    {
        times[i]=true;
        complete[i].setBackgroundColor(0xff00AC00);
    }

}
