package com.thekarlbrown.changetheworld;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This is the tab that allows you to view your user favorites
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class ByFavoritePage extends Fragment {

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

    public static ByFavoritePage newInstance() {
        ByFavoritePage fragment = new ByFavoritePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        rv = inflater.inflate(R.layout.fragment_by_favorite_page, container, false);
        mainActivity.getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/favorites_getListIdeasJSON.php?username=" + username);
        dapt = new IdeaDataAdapter(mainActivity.ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.by_favorite_list);
        l.setAdapter(dapt);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv,fm,selectedf,getTag(),mainActivity);
        return rv;
    }

    /**
     * Actions performed when item on Bar Filter is pressed
     * @param i Item selected (starting at 0)
     */
    public void filterSelected(int i) {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }

}
