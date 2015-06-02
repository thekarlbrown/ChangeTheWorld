package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * Class that serves to handle the Bar for different area selection.
 * By Karl Brown ( thekarlbrown )
 */
public class BarArea extends Fragment {
    TextView[] complete;
    boolean[]areas={false,false,false,false,false,false};

    /**
     * Setting the onClickListener to each item in the bar.
     * @param in Current view passed in
     * @param selected Item already selected
     * @param tag Tag of the current fragment
     * @param mainActivity The base activity
     */
    public void barAreaClick(View in, int selected,final String tag, final MainActivity mainActivity) {
        complete=new TextView[]{(TextView) in.findViewById(R.id.bot_area_local),
                (TextView) in.findViewById(R.id.bot_area_hun),
                (TextView) in.findViewById(R.id.bot_area_state),
                (TextView) in.findViewById(R.id.bot_area_country),
                (TextView) in.findViewById(R.id.bot_area_global),
                (TextView) in.findViewById(R.id.bot_area_friends)};
        for(int i=0;i<complete.length;i++) {
            final int x=i;
            complete[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!areas[x]) {
                        areas[x]=true;
                        complete[x].setBackgroundColor(0xff00AC00);
                        for(int y=0;y<areas.length;y++) {
                            if(y!=x) {
                                if(areas[y]) {
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

    /**
     * Set a item in the bar as selected
     * @param i Which of the items in the bar to select
     */
    public void setSelected(int i){
        complete[i].setBackgroundColor(0xff00AC00);
        areas[i]=true;
    }
}
