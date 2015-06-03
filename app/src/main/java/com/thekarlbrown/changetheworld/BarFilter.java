package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Class that serves to handle the Bar for different filter selections.
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class BarFilter extends Fragment {
    FragmentManager fm;
    FragmentTransaction ft;
    Bundle bundle;
    boolean[]filter={false,false,false,false};
    TextView[] complete;

    /**
     * Setting the onClickListener's in the bar. This method is complex as it can launch Dialog Fragments from its own fragment
     * @param in Current view
     * @param fragmentManager Fragment manager to be passed in
     * @param selected Item already selected
     * @param tag Tag of current fragment
     * @param mainActivity The base activity
     */
    public void barFilterClick(View in, FragmentManager fragmentManager,boolean[]selected,final String tag,final MainActivity mainActivity) {
        fm = fragmentManager;
        complete= new TextView[]{(TextView) in.findViewById(R.id.filter_ratio),
                (TextView) in.findViewById(R.id.filter_thumbs),
                (TextView) in.findViewById(R.id.filter_recent),
                (TextView) in.findViewById(R.id.filter_toprated)};
        complete[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[0]) {
                    //set Ratio here with popup
                    ft = fm.beginTransaction();
                    RatioDialogue ratioDialogue=new RatioDialogue();
                    bundle=new Bundle();
                    bundle.putString("tag",tag);
                    ratioDialogue.setArguments(bundle);
                    ft.add(ratioDialogue, "ratiodialogue");
                    ft.commit();
                } else {
                    filter[0] = false;
                    complete[0].setBackgroundColor(0xffffffff);
                    mainActivity.revertBarFilter(tag,true);
                }
            }
        });
        complete[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[1]) {
                    //set Thumb ratio here with popup
                    ft = fm.beginTransaction();
                    ThumbDialogue thumbDialogue=new ThumbDialogue();
                    bundle=new Bundle();
                    bundle.putString("tag",tag);
                    thumbDialogue.setArguments(bundle);
                    ft.add(thumbDialogue, "thumbdialogue");
                    ft.commit();

                } else {
                    filter[1] = false;
                    complete[1].setBackgroundColor(0xffffffff);
                    mainActivity.revertBarFilter(tag,false);
                }
            }
        });
        complete[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[2]) {
                    filter[2] = true;
                    filter[3] = false;
                    complete[2].setBackgroundColor(0xff00AC00);
                    complete[3].setBackgroundColor(0xffffffff);
                    mainActivity.switchBarFilter(true,tag);
                }
            }
        });
        complete[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[3]) {
                    filter[2] = false;
                    filter[3] = true;
                    complete[3].setBackgroundColor(0xff00AC00);
                    complete[2].setBackgroundColor(0xffffffff);
                    mainActivity.switchBarFilter(false,tag);
                }
            }
        });
        for (int y = 0; y < selected.length; y++) {
            if (selected[y]) {
                setSelected(y);
            }
        }
    }
    /**
     * Set a item in the bar as selected
     * @param i Which of the items in the bar to select
     */
    public void setSelected(int i) {
        complete[i].setBackgroundColor(0xff00AC00);
        filter[i]=true;
    }
}
