package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

/**
 * tab for top users
 *
 stylize input
 */
public class LeaderboardTab extends Fragment {

    View rv;
    TextView t;
    ListView l;
    LeaderAdapter ladpt;
    LeaderBlock leaderBlock;
    MainActivity mainActivity;
    BarTime barTime=new BarTime();
    BarArea barArea=new BarArea();
    Context curcontext;
    int selecteda;
    int selectedt;
    boolean[]leaderselect={false,false,false,false};

    public static LeaderboardTab newInstance() {
        LeaderboardTab fragment = new LeaderboardTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public LeaderboardTab() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selecteda=getArguments().getInt("selecteda");
            selectedt=getArguments().getInt("selectedt");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        rv = inflater.inflate(R.layout.fragment_leaderboard_tab, container, false);
        curcontext=rv.getContext();
        leaderBlock=mainActivity.leaderBlock;
        barArea.barAreaClick(rv,selecteda);
        barRankClick(rv);
        barTime.barTimeClick(rv,selectedt);
        ladpt=new LeaderAdapter(leaderBlock,mainActivity,-1);
        l=(ListView)rv.findViewById(R.id.leaderboard_list);
        l.setAdapter(ladpt);
        return rv;
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void barRankClick(View in) {
        final TextView[] complete={(TextView) in.findViewById(R.id.leaderboard_accurate),
                (TextView) in.findViewById(R.id.leaderboard_added),
                (TextView) in.findViewById(R.id.leaderboard_quality),
                (TextView) in.findViewById(R.id.leaderboard_comments),
        };
       final View fin=in;
        for(int i=0;i<complete.length;i++)
        {
            final int x=i;
            complete[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!leaderselect[x])
                    {
                        leaderselect[x]=true;
                        complete[x].setBackgroundColor(0xffff0000);
                        for(int y=0;y<leaderselect.length;y++)
                        {
                            if(y!=x)
                            {
                                if(leaderselect[y])
                                {
                                    leaderselect[y]=false;
                                    complete[y].setBackgroundColor(0xffffffff);
                                }
                            }
                        }
                        ladpt=new LeaderAdapter(leaderBlock,mainActivity,x);
                        l=(ListView)fin.findViewById(R.id.leaderboard_list);
                        l.setAdapter(ladpt);
                        ladpt.notifyDataSetChanged();
                        t=(TextView)getView().findViewById(R.id.leaderboard_selection);
                        t.setText(complete[x].getText());

                    }
                }
            });
        }
    }
    @Override
    public void onDetach() {
        hideSoftKeyboard();
        super.onDetach();
    }
}



