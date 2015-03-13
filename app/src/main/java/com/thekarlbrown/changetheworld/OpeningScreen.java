/*
NOTE: Class is currently deprecated (along with accompanying XML) and if not re-incorporated will be removed

package com.thekarlbrown.changetheworld;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;



//screen the user is presented with if there is no initial creation

public class OpeningScreen extends Fragment {

    View v;
    TextView t;
    ListView l;
    SharedPreferences pref;
    DataAdapter dapt;
    IdeaBlock ib;
    MainActivity mainActivity;

    public static OpeningScreen newInstance() {
        OpeningScreen fragment = new OpeningScreen();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    public OpeningScreen()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        v = inflater.inflate(R.layout.fragment_opening_screen, container, false);
        t = (TextView) v.findViewById(R.id.welcometext);
        pref = mainActivity.sharedPref;
        int ideas = 500; //mysql/backend/database required
        t.setText("Welcome " + pref.getString(getString(R.string.preference_username),"YouGoofed")
                + "!\nSince you have been here, there have been " + (ideas - pref.getInt(getString(R.string.preference_ideas_seen),-1))
                + " new ideas.\nHere are some suggestions for you to check out:");
        ib =(mainActivity.ib);
        dapt=new IdeaDataAdapter(ib,mainActivity);
        l=(ListView)v.findViewById(R.id.opening_list);
        l.setAdapter(dapt);
        return v;
    }
}
*/