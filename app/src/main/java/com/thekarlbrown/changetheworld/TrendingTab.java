package com.thekarlbrown.changetheworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
/**
 * tab for all the trending ideas
 *
 * is calling (MainActivity)getActivity bad?
 * get the horizontal/vertical change working
 */
public class TrendingTab extends Fragment {

    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    IdeaBlock ib;
    MainActivity mainActivity;
    FragmentManager fm;
    FragmentTransaction ft;
    BarFilter barFilter=new BarFilter();
    BarArea barArea=new BarArea();
    int prefratio;
    int prefminimum;
    boolean[]selectedf;
    int selecteda;

    public static TrendingTab newInstance() {
        TrendingTab fragment = new TrendingTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TrendingTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedf=getArguments().getBooleanArray("selectedf");
            selecteda=getArguments().getInt("selecteda");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        rv = inflater.inflate(R.layout.fragment_trending_tab, container, false);
        ib = mainActivity.ib;
        dapt = new IdeaDataAdapter(ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.trending_list);
        l.setAdapter(dapt);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv, fm,selectedf);
        barArea.barAreaClick(rv,selecteda);
        return rv;
    }
 }
