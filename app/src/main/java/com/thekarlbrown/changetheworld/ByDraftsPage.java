package com.thekarlbrown.changetheworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;

/**
 * page for viewing saved drafts
 */
public class ByDraftsPage extends Fragment {

    View rv;
    TextView t, temp;
    ListView l;
    SharedPreferences pref;
    DataAdapter dapt;
    IdeaBlock ib;
    MainActivity mainActivity;
    FragmentManager fm;
    FragmentTransaction ft;
    LocalIdeas localIdeas=new LocalIdeas();



    public static ByDraftsPage newInstance() {
        ByDraftsPage fragment = new ByDraftsPage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ByDraftsPage() {

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
        rv = inflater.inflate(R.layout.fragment_by_drafts_page, container, false);
        pref=mainActivity.getPref();
        ib=localIdeas.loadIdeaBlock(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "drafts_" + pref.getString(getString(R.string.preference_username), "fuckedUp")));
        dapt = new DraftDataAdapter(ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.by_drafts_list);
        l.setAdapter(dapt);
        t=(TextView)rv.findViewById(R.id.by_drafts_username);
        t.setText(pref.getString(getString(R.string.preference_username),"yagoofed"));
        return rv;
    }


}
