package com.thekarlbrown.changetheworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;


/**
 * page for viewing friends (currently views ideas by friends)
 * will implement a friend data adapter later
 */

public class ByFriendsPage extends Fragment {

    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    MainActivity mainActivity;
    FragmentManager fm;
    FragmentTransaction ft;
    String username;
    BarFilter barFilter=new BarFilter();
    boolean[]selectedf;
     int prefratio;
    int prefminimum;

    public static ByFriendsPage newInstance() {
        ByFriendsPage fragment = new ByFriendsPage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ByFriendsPage() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username=getArguments().getString("username");
            selectedf=getArguments().getBooleanArray("selectedf");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        rv = inflater.inflate(R.layout.fragment_by_friends_page, container, false);
        mainActivity.ib.clear();
        mainActivity.getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/friends_getIdeaListJSON.php?username=" + username);
        dapt = new IdeaDataAdapter(mainActivity.ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.by_friends_list);
        l.setAdapter(dapt);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv,fm,selectedf,getTag(),mainActivity);
        return rv;
    }
    public void filterSelected(int i)
    {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }

}
