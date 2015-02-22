package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.regex.Pattern;


/**
 * screen when you first enter the app
 */
public class InitialScreen extends Fragment {
Activity activity;
    String[] categorytitles;
    View rv;
    TextView t;
    ListView l;
    Button but;
    SharedPreferences pref;
    SharedPreferences.Editor epref;
    FragmentTransaction ft;
    FragmentManager fm;
    Bundle b;
    int totalideas  = 500; //mysql/backend/database required
    String welcomemsg="Welcome to the app that enables the sharing, evaluation, " + "\nand tweaking of new ideas and innovation!" +"\nNavigate with the bars at the top! Sort what you see with" +"\noptions at the bottom! All you have to do to start is set your username."+"\nSo who are YOU, my friend?";
    String welcomemsgerror="Welcome to the app that enables the sharing, evaluation, " + "\nand tweaking of new ideas and innovation!" +"\nNavigate with the bars at the top! Sort what you see with" +"\noptions at the bottom! All you have to do to start is set your username."+"\nSo who are YOU, my friend?\nSorry: Your username must be letters at numbers, 25 characters max...\nOr this bugged";
    public void toLearn(View v)
    {
        pref = ((MainActivity)getActivity()).getPref();
        epref=pref.edit();
        epref.putString(getString(R.string.preference_username), ((EditText) v.getRootView().findViewById(R.id.initial_username)).getText().toString());
        epref.putBoolean(getString(R.string.preference_setup), true);
        epref.putInt(getString(R.string.preference_ideas_seen),totalideas);
        epref.apply();
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.current_tab,new OpeningScreen(),"opening");
        ft.commit();
    }
    public void toChange(View v)
    {
        pref = ((MainActivity)getActivity()).getPref();
        epref=pref.edit();
        epref.putString(getString(R.string.preference_username), ((EditText) v.getRootView().findViewById(R.id.initial_username)).getText().toString());
        epref.putBoolean(getString(R.string.preference_setup),true);
        epref.putInt(getString(R.string.preference_ideas_seen),totalideas);
        epref.putInt(getString(R.string.preference_userid),9189);
        epref.apply();
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        AddTab addT=new AddTab();
        b=new Bundle();
        b.putStringArray("category",categorytitles);
        addT.setArguments(b);
        ft.replace(R.id.current_tab,addT,"add");
        ft.commit();
    }
    public static InitialScreen newInstance() {
        InitialScreen fragment = new InitialScreen();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    public InitialScreen()
    {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categorytitles=getArguments().getStringArray("categorytitles");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rv = inflater.inflate(R.layout.fragment_initial_screen, container, false);
        t = (TextView) rv.findViewById(R.id.welcometext);
        t.setText(welcomemsg);
        but = (Button)rv.findViewById(R.id.initial_noidea);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=((EditText) rv.findViewById(R.id.initial_username)).getText().toString();
                if(Pattern.matches("^[A-Za-z0-9]+",temp)&&(temp.length()<26))
                {
                    hideSoftKeyboard();
                    toLearn(v);
                }else
                {
                    ((EditText) rv.findViewById(R.id.initial_username)).setText(null);
                    t = (TextView) rv.findViewById(R.id.welcometext);
                    t.setText(welcomemsgerror);
                }
            }
        });
        but = (Button)rv.findViewById(R.id.initial_haveidea);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=((EditText) rv.findViewById(R.id.initial_username)).getText().toString();
                if(Pattern.matches("^[A-Za-z0-9]+",temp)&&(temp.length()<26))
                {
                    hideSoftKeyboard();
                    toChange(v);
                }else
                {
                    ((EditText) rv.findViewById(R.id.initial_username)).setText(null);
                    t = (TextView) rv.findViewById(R.id.welcometext);
                    t.setText(welcomemsgerror);
                }
            }
        });
        return rv;
    }
    @Override
    public void onDetach() {
        hideSoftKeyboard();
        super.onDetach();

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
