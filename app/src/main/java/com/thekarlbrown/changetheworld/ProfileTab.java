package com.thekarlbrown.changetheworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.List;


/**
 * Profile Tab to allow one to access profile functions
 * TODO: Will utilize ones own face instead of mine
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class ProfileTab extends Fragment {

    View rv;
    TextView t;
    ListView l;
    SharedPreferences pref;
    DataAdapter dapt;
    List<IdeaBlock> ib;
    MainActivity mainActivity;
    Context curcontext;
    Button button;
    SharedPreferences.Editor editor;
    String username;
    LocalIdeas localIdeas = new LocalIdeas();
    FragmentTransaction ft;
    FragmentManager fm;

    public static ProfileTab newInstance() {
        ProfileTab fragment = new ProfileTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username=getArguments().getString("username");
        }
    }

    ProfileTabListener mListener;
    public interface ProfileTabListener{
       void toUserIdeaPage(String username);
       void toFavoriteIdeaPage(String username);
       void toFriendsIdeaPage(String username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        rv = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        curcontext=rv.getContext();
        try {
            mListener = (ProfileTabListener) curcontext;
        } catch (ClassCastException e) {
            throw new ClassCastException(curcontext.toString() + " must implement NoticeDialogListener");
        }
        pref = mainActivity.getPref();
        t = (TextView) rv.findViewById(R.id.profile_welcome);
        t.setText("     Welcome " + username);
        //Set each of the buttons that reference specific Tabs via MainActivity
        button = (Button) rv.findViewById(R.id.profile_your_ideas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toUserIdeaPage(username);
            }
        });
        button = (Button) rv.findViewById(R.id.profile_favorite_ideas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toFavoriteIdeaPage(username);
            }
        });
        button = (Button) rv.findViewById(R.id.profile_friends);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toFriendsIdeaPage(username);
            }
        });

        //Set the Draft and Profile Buttons
        button = (Button) rv.findViewById(R.id.profile_drafts);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (localIdeas.loadIdeaBlock(new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "drafts_" + pref.getString(getString(R.string.preference_username), "fuckedUp"))) == null) {
                    t = (TextView) rv.findViewById(R.id.profile_prof_status);
                    t.setText(R.string.profile_no_drafts);
                } else {
                    mainActivity.drafts = localIdeas.loadIdeaBlock(new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), "drafts_" + pref.getString(getString(R.string.preference_username), "fuckedUp")));
                    fm = getFragmentManager();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.current_tab, new ByDraftsPage(), "bydrafts");
                    ft.commit();
                }
            }
        });
        button = (Button) rv.findViewById(R.id.profile_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor=pref.edit();
                editor.remove(getString(R.string.preference_password));
                editor.remove(getString(R.string.preference_username));
                editor.apply();
                mainActivity.st.setVisibility(View.GONE);
                mainActivity.username=null;
                fm=getFragmentManager();
                ft=fm.beginTransaction();
                ft.replace(R.id.current_tab,new InitialScreen(),"initialscreen");
                ft.commit();
            }
        });
        return rv;
    }
}
