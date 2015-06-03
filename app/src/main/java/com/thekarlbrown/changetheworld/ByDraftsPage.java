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
import java.util.List;

/**
 * This is the Tab that allows you to view your saved drafts.
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class ByDraftsPage extends Fragment {

    View rv;
    TextView t;
    ListView l;
    SharedPreferences pref;
    DataAdapter dapt;
    List<IdeaBlock> ib;
    MainActivity mainActivity;
    FragmentManager fm;
    FragmentTransaction ft;
    LocalIdeas localIdeas=new LocalIdeas();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity=(MainActivity)getActivity();
        rv = inflater.inflate(R.layout.fragment_by_drafts_page, container, false);
        pref=mainActivity.getPref();
        //Set the ideablock in the main activity to the contents of the locally saved drafts
        ib=localIdeas.loadIdeaBlock(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "drafts_" + pref.getString(getString(R.string.preference_username), "fuckedUp")));
        dapt = new DraftDataAdapter(ib,mainActivity);
        l = (ListView) rv.findViewById(R.id.by_drafts_list);
        l.setAdapter(dapt);
        return rv;
    }
}
