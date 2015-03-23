package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * tab for searching through ideas
 *
 * figure out how total number of ideas will be calculated
 * is calling (MainActivity)getActivity bad?
 * get the horizontal/vertical change working
 */
public class SearchTab extends Fragment{
    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    List<IdeaBlock> ib;
    MainActivity mainActivity;
    String searchQuery;
    FragmentManager fm;
    FragmentTransaction ft;
    Activity activity;
    BarFilter barFilter=new BarFilter();
    boolean[] selectedf;
    int prefratio;
    int prefminimum;

    public static SearchTab newInstance() {
        SearchTab fragment = new SearchTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchQuery=getArguments().getString("query");
            selectedf=getArguments().getBooleanArray("selectedf");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        rv = inflater.inflate(R.layout.fragment_search_tab, container, false);
        ib = mainActivity.ib;
        dapt = new IdeaDataAdapter(ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.search_list);
        l.setAdapter(dapt);
        t=(TextView)rv.findViewById(R.id.search_query);
        t.setText("Current Search: " + searchQuery);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv, fm,selectedf,getTag(),mainActivity);
        return rv;
    }
    public void filterSelected(int i)
    {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }



    @Override
    public void onAttach(Activity activity) {
        hideSoftKeyboard();
        super.onAttach(activity);
    }
    public void hideSoftKeyboard() {
        try {
            activity=getActivity();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
