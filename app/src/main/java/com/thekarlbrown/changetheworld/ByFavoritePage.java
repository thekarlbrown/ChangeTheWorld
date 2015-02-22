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
 * page for viewing user favorites
 */
public class ByFavoritePage extends Fragment {

    View rv;
    TextView t, temp;
    ListView l;
    DataAdapter dapt;
    IdeaBlock ib;
    MainActivity mainActivity;
    FragmentManager fm;
    FragmentTransaction ft;
    String username;
    BarFilter barFilter=new BarFilter();
    boolean[]selectedf;
    int prefratio;
    int prefminimum;

    public static ByFavoritePage newInstance() {
        ByFavoritePage fragment = new ByFavoritePage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ByFavoritePage() {

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
        ib = mainActivity.bytemp;
        dapt = new IdeaDataAdapter(ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.by_favorite_list);
        l.setAdapter(dapt);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv,fm,selectedf);
        t=(TextView)rv.findViewById(R.id.by_favorite_username);
        t.setText(username + "'s favorite ideas: ");
        return rv;
    }


}
