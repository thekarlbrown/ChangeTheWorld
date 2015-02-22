package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

/**
 * class for the filter bar
 */
public class BarFilter extends Fragment {
    FragmentManager fm;
    FragmentTransaction ft;
    boolean[]filter={false,false,false,false};
    public void barFilterClick(View in, FragmentManager fragmentManager,boolean[]selected) {
        final TextView[] complete={ (TextView) in.findViewById(R.id.filter_ratio),
        (TextView) in.findViewById(R.id.filter_thumbs),
         (TextView) in.findViewById(R.id.filter_recent),
       (TextView) in.findViewById(R.id.filter_toprated)};
        fm = fragmentManager;
        complete[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[0]) {
                    filter[0] = true;
                    complete[0].setBackgroundColor(0xffff0000);
                    //set ratio here with popup

                    ft = fm.beginTransaction();
                    ft.add(new RatioDialogue(), "ratiodialogue");
                    ft.commit();
                    //ft.addToBackStack(null);


                } else {
                    filter[0] = false;
                    complete[0].setBackgroundColor(0xffffffff);
                }
            }
        });
        complete[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[1]) {
                    filter[1] = true;
                    complete[1].setBackgroundColor(0xffff0000);
                    //set thumbs here with popup
                    ft = fm.beginTransaction();
                    ft.add(new ThumbDialogue(), "thumbdialogue");
                    //ft.addToBackStack(null);
                    ft.commit();


                } else {
                    filter[1] = false;
                    complete[1].setBackgroundColor(0xffffffff);
                }
            }
        });
        complete[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[2]) {
                    filter[2] = true;
                    filter[3] = false;
                    //alter the ib here
                    complete[2].setBackgroundColor(0xffff0000);
                    complete[3].setBackgroundColor(0xffffffff);
                }
            }
        });
        complete[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filter[3]) {
                    filter[2] = false;
                    filter[3] = true;
                    //alter the ib here
                    complete[3].setBackgroundColor(0xffff0000);
                    complete[2].setBackgroundColor(0xffffffff);
                }
            }
        });
        for (int y = 0; y < selected.length; y++)
        {
            if(selected[y])
            {
                complete[y].setBackgroundColor(0xffff0000);
                filter[y]=true;
            }
        }
    }
}
