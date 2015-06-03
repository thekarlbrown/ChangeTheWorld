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
 * Search Tab that displays results of String Queries to the MySQL Database
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
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

    public static SearchTab newInstance() {
        SearchTab fragment = new SearchTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

    /**
     * Method used with MainActivity to change data based on user selection
     * @param i Item on the Filter Bar that is selected
     */
    public void filterSelected(int i) {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }

    /**
     * Hide the keyboard when we leave the View
     */
    @Override
    public void onDetach() {
        hideSoftKeyboard();
        super.onDetach();
    }

    /**
     * Oft-repeated method (should have extended a class) to hide the keyboard when it has been in use
     */
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
