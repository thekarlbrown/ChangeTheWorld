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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;


/**
profile page

 will later be adapted using bundles to allow references to ANY profile
 will later have profiles backed up online, not in downloads
 */
public class ProfileTab extends Fragment {

    View rv;
    TextView t, temp;
    ListView l;
    SharedPreferences pref;
    DataAdapter dapt;
    IdeaBlock ib;
    MainActivity mainActivity;
    Context curcontext;
    Button button;
    int userid;
    String username;
    SharedPreferences.Editor epref;
    LocalIdeas localIdeas = new LocalIdeas();
    FragmentTransaction ft;
    FragmentManager fm;

    public static ProfileTab newInstance() {
        ProfileTab fragment = new ProfileTab();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ProfileTab() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //int i=savedInstanceState.getStringArray("category").length;
            // category=new String[i];
        }
    }
    ProfileTabListener mListener;
    public interface ProfileTabListener{
        public void toUserIdeaPage(String username);
        public void toFavoriteIdeaPage(String username);
        public void toFriendsIdeaPage(String username);
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
            throw new ClassCastException(curcontext.toString()
                    + " must implement NoticeDialogListener");
        }

        pref = mainActivity.getPref();
        username = pref.getString(getString(R.string.preference_username), "yagoofed");
        userid = pref.getInt(getString(R.string.preference_userid), -1);
        t = (TextView) rv.findViewById(R.id.profile_welcome);
        t.setText("Welcome " + username);
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
        button = (Button) rv.findViewById(R.id.profile_import);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t = (TextView) rv.findViewById(R.id.profile_prof_status);
                try {
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        if (loadSharedPreferencesFromFile(new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS), pref.getString(getString(R.string.preference_username), "yagoofed")))) {
                            t.setText(R.string.profile_import_success);
                        } else {
                            t.setText(R.string.profile_error_missing);
                        }
                    } else {
                        t.setText(R.string.profile_error_access);
                    }
                } catch (Exception e) {
                    t.setText(R.string.profile_error_unknown);
                }
            }
        });
        button = (Button) rv.findViewById(R.id.profile_export);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t = (TextView) rv.findViewById(R.id.profile_prof_status);
                try {
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        if (saveSharedPreferencesToFile(new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS), pref.getString(getString(R.string.preference_username), "yagoofed")))) {
                            t.setText(R.string.profile_export_success);
                        } else {
                            t.setText(R.string.profile_error_unknown);
                        }
                    } else {
                        t.setText(R.string.profile_error_access);
                    }
                } catch (Exception e) {
                    t.setText(R.string.profile_error_unknown);
                }
            }
        });
        return rv;
    }
    //import/export preferences
    private boolean saveSharedPreferencesToFile(File dst) {
        boolean res = false;
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(dst));
            output.writeObject(pref.getAll());
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                res = false;
            }
        }
        return res;
    }
    @SuppressWarnings({"unchecked"})
    private boolean loadSharedPreferencesFromFile(File src) {
        boolean res = false;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            epref = pref.edit();
            epref.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Map.Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();

                if (v instanceof Boolean)
                    epref.putBoolean(key, ((Boolean) v));
                else if (v instanceof Integer)
                    epref.putInt(key, ((Integer) v));
                else if (v instanceof String)
                    epref.putString(key, ((String) v));
                else if (v instanceof Set)
                    epref.putStringSet(key, ((Set) v));
            }
            epref.apply();
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                res = false;
            }
        }
        return res;
    }
}
