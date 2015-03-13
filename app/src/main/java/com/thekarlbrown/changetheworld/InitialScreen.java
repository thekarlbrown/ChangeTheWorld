package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    MainActivity mainActivity;
    View rv;
    TextView t;
    ListView l;
    Button but;
    SharedPreferences pref;
    SharedPreferences.Editor epref;
    EditText editText;
    FragmentTransaction ft;
    FragmentManager fm;
    boolean createbars=false;
    String username,email,emailcheck;
    /*public void loginToAccount()
    {
        mainActivity.searchTabClick();
        epref=pref.edit();
        epref.putString(getString(R.string.preference_username), username);
        epref.apply();
        mainActivity.loginToAccount(username,password);
    }
    public void toChange(View v)
    {
        mainActivity.searchTabClick();
        epref=pref.edit();
        epref.putString(getString(R.string.preference_username), username);
        epref.apply();
        mainActivity.createAccount(username,password,email);
    }
    */
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
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rv = inflater.inflate(R.layout.fragment_initial_screen, container, false);
        mainActivity=((MainActivity)getActivity());
        pref = mainActivity.getPref();
        username=pref.getString(getString(R.string.preference_username), "defaultUSERcheck");
        if(!username.equals("defaultUSERcheck")){
            ((EditText) rv.findViewById(R.id.login_username)).setText(username);
        }
        t = (TextView) rv.findViewById(R.id.welcometext);
        t.setText(R.string.login_welcome);
        but = (Button)rv.findViewById(R.id.login_attempt_login);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) rv.findViewById(R.id.login_username)).getText().toString();
                if (createbars) {
                    createbars = false;
                    rv.findViewById(R.id.login_password_confirm).setVisibility(View.INVISIBLE);
                    rv.findViewById(R.id.login_email).setVisibility(View.INVISIBLE);
                    rv.findViewById(R.id.login_email_confirm).setVisibility(View.INVISIBLE);
                }
                if (!(Pattern.matches("^[A-Za-z0-9]+", username) && (username.length() < 26))) {
                    editText=((EditText) rv.findViewById(R.id.login_username));
                    editText.setHint(R.string.login_username_problem);
                    editText.setText(null);
                } else {
                    System.out.println("We made it test");
                    /*if(mainActivity.verifyLogin(username,((EditText)rv.findViewById(R.id.login_password)).getText())){
                        mainActivity.searchTabClick();
                        epref=pref.edit();
                        epref.putString(getString(R.string.preference_username), username);
                        epref.apply();
                        mainActivity.authenticated(username);
                    }else{
                        ((TextView) rv.findViewById(R.id.welcometext)).setText(R.string.login_error_login);
                        ((EditText) rv.findViewById(R.id.login_username)).setText(R.string.login_username_prompt);
                        ((EditText) rv.findViewById(R.id.login_password)).setText(R.string.login_password_prompt);
                    }
                    */
                }
            }
        });
        but = (Button)rv.findViewById(R.id.login_create_account);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!createbars) {
                    createbars = true;
                    rv.findViewById(R.id.login_password_confirm).setVisibility(View.VISIBLE);
                    rv.findViewById(R.id.login_email).setVisibility(View.VISIBLE);
                    rv.findViewById(R.id.login_email_confirm).setVisibility(View.VISIBLE);
                } else {
                    username = ((EditText) rv.findViewById(R.id.login_username)).getText().toString();
                    email=((EditText) rv.findViewById(R.id.login_email)).getText().toString();
                    emailcheck=((EditText) rv.findViewById(R.id.login_email_confirm)).getText().toString();
                    if (!(Pattern.matches("^[A-Za-z0-9]+", username) && (username.length() < 26))) {
                        editText=(EditText) rv.findViewById(R.id.login_username);
                        editText.setHint(R.string.login_username_problem);
                        editText.setText(null);
                    } else if (!(((EditText) rv.findViewById(R.id.login_password)).getText().toString().equals(((EditText) rv.findViewById(R.id.login_password_confirm)).getText().toString()))) {
                        editText=(EditText) rv.findViewById(R.id.login_password);
                        editText.setHint(R.string.login_password_problem);
                        editText.setText(null);
                        editText=(EditText) rv.findViewById(R.id.login_password_confirm);
                        editText.setHint(R.string.login_password_problem);
                        editText.setText(null);
                    }else if(!isEmailValid(email)||!isEmailValid(emailcheck)){
                        editText=(EditText) rv.findViewById(R.id.login_email);
                        editText.setHint(R.string.login_email_problem_valid);
                        editText.setText(null);
                        editText=(EditText) rv.findViewById(R.id.login_email_confirm);
                        editText.setHint(R.string.login_email_problem_valid);
                        editText.setText(null);
                    }else if (!email.equals(emailcheck)) {
                        editText=(EditText) rv.findViewById(R.id.login_email);
                        editText.setHint(R.string.login_email_problem_matching);
                        editText.setText(null);
                        editText=(EditText) rv.findViewById(R.id.login_email_confirm);
                        editText.setHint(R.string.login_email_problem_matching);
                        editText.setText(null);
                    }else{
                        System.out.println("We made it test");
                   /* if(mainActivity.verifyCreate(username,((EditText)rv.findViewById(R.id.login_password)).getText(),email)){
                        mainActivity.searchTabClick();
                        epref=pref.edit();
                        epref.putString(getString(R.string.preference_username), username);
                        epref.apply();
                        mainActivity.authenticated(username);
                    }else{
                        ((TextView) rv.findViewById(R.id.welcometext)).setText(R.string.login_error_login);
                        ((EditText) rv.findViewById(R.id.login_username)).setText(R.string.login_username_prompt);
                        ((EditText) rv.findViewById(R.id.login_password)).setText(R.string.login_password_prompt);
                        ((EditText) rv.findViewById(R.id.login_password_confirm)).setText(R.string.login_password_prompt);
                        ((EditText) rv.findViewById(R.id.login_email)).setText(R.string.login_email_prompt);
                        ((EditText) rv.findViewById(R.id.login_email_confirm)).setText(R.string.login_email_prompt);
                    }
                    */

                    }
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

    /**
     * Check if email is valid
     * @param email - email to check
     * @return True if valid, false if invalid
     */
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    /**
     * Hide the keyboard
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
