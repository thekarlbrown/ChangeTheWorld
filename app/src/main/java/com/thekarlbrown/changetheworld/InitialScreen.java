package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.regex.Pattern;


/**
 * Login Tab when app loads. Will be bypassed if saved username/password checks out
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rv = inflater.inflate(R.layout.fragment_initial_screen, container, false);
        mainActivity=((MainActivity)getActivity());
        pref = mainActivity.getPref();
        username=pref.getString(getString(R.string.preference_username), "defaultUSERcheck$3");
        //If login is verified, initialize the Trending Tab
        if(mainActivity.verifyLogon(username,pref.getString(getString(R.string.preference_password),"defaultPASScheck$3"))){
            mainActivity.searchTabClick();
            mainActivity.openTrending();
        }// Otherwise clear the login data
        else {
            epref=pref.edit();
            epref.remove(getString(R.string.preference_password));
            epref.remove(getString(R.string.preference_username));
            epref.apply();
        }

        //Set The Keyboard to Hide when touching non-text Areas
        t = (TextView) rv.findViewById(R.id.welcometext);
        t.setText(R.string.login_welcome);
        t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
        ImageView imageView=(ImageView)rv.findViewById(R.id.login_mainlogo);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
        LinearLayout linearLayout=(LinearLayout)rv.findViewById(R.id.login_background);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        //Set an onClickListener for logging in that will verify each of the fields as legitimate and exit with error messages if not
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
                    hideSoftKeyboard();
                    but=(Button)rv.findViewById(R.id.login_attempt_login);
                    but.setClickable(false);
                    but=(Button)rv.findViewById(R.id.login_create_account);
                    but.setClickable(false);
                    //If we have a legitimate logon, then save the logon credentials and initiate the Trending Tab
                    if(mainActivity.verifyLogon(username,((EditText)rv.findViewById(R.id.login_password)).getText().toString())){
                        mainActivity.searchTabClick();
                        epref=pref.edit();
                        epref.putString(getString(R.string.preference_username), username);
                        epref.putString(getString(R.string.preference_password),((EditText)rv.findViewById(R.id.login_password)).getText().toString());
                        epref.apply();
                        mainActivity.openTrending();
                    }else{
                        ((TextView) rv.findViewById(R.id.welcometext)).setText(R.string.login_error_login);
                        editText=(EditText) rv.findViewById(R.id.login_username);
                        but=(Button)rv.findViewById(R.id.login_attempt_login);
                        but.setClickable(true);
                        but=(Button)rv.findViewById(R.id.login_create_account);
                        but.setClickable(true);
                    }

                }
            }
        });

        //Set an onClickListener for creating an account that will verify each of the fields as legitimate and exit with error messages if not
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
                        but=(Button)rv.findViewById(R.id.login_attempt_login);
                        but.setClickable(false);
                        but=(Button)rv.findViewById(R.id.login_create_account);
                        but.setClickable(false);
                        //If we have a legitimate account creation, then save the logon credentials and initiate the Trending Tab
                        if(mainActivity.verifyCreate(username,((EditText)rv.findViewById(R.id.login_password)).getText().toString(),email)){
                            hideSoftKeyboard();
                            mainActivity.searchTabClick();
                            epref=pref.edit();
                            epref.putString(getString(R.string.preference_username), username);
                            epref.putString(getString(R.string.preference_password),((EditText)rv.findViewById(R.id.login_password)).getText().toString());
                            epref.apply();
                            mainActivity.openTrending();
                        }else{
                            hideSoftKeyboard();
                            ((TextView) rv.findViewById(R.id.welcometext)).setText(R.string.login_error_create);
                            but=(Button)rv.findViewById(R.id.login_attempt_login);
                            but.setClickable(true);
                            but=(Button)rv.findViewById(R.id.login_create_account);
                            but.setClickable(true);

                        }
                    }
                }
            }

        });
        return rv;
    }

    /**
     * Check if email is valid
     * @param email - Email to check
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
