    package com.thekarlbrown.changetheworld;

    import android.app.Activity;
    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.app.Fragment;
    import android.os.Environment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import java.io.File;
    import java.util.regex.Pattern;

    /**
     * This tab is where ideas are added
     can questionmarks and return characters be allowed in the description?
     stylize input
     */
    public class AddTab extends Fragment {
        LocalIdeas localIdeas=new LocalIdeas();
        View rv;
        TextView t;
        EditText et;
        ListView l;
        SharedPreferences pref;
        DataAdapter dapt;
        IdeaBlock ib;
        ImageView iv;
        MainActivity mainActivity;
        String add_title;
        String add_description;
        int add_category=0;
        int add_subcategory=0;
        Spinner spin1,spin2;
        String[] category,subcategory;
        String[][]categoryArray;
        ArrayAdapter<String>spinadapter1,spinadapter2;
        Context curcontext;
        Button button;
        Activity activity;
        boolean draftscoming=false;


        public static AddTab newInstance() {
            AddTab fragment = new AddTab();
            Bundle args = new Bundle();

            fragment.setArguments(args);
            return fragment;
        }

        public AddTab() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                category=getArguments().getStringArray("category");
                draftscoming=getArguments().getBoolean("draftscoming");
                if(draftscoming)
                {
                    add_title=getArguments().getString("drafttitle");
                    add_description=getArguments().getString("draftdescription");
                    add_category=getArguments().getInt("draftcategory")+1;
                    add_subcategory=getArguments().getInt("draftsubcategory")+1;
                }
            }

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mainActivity = (MainActivity) getActivity();
            rv = inflater.inflate(R.layout.fragment_add_tab, container, false);
            curcontext=rv.getContext();
            pref = mainActivity.getPref();
            categoryArray=mainActivity.categories;
            iv = (ImageView) rv.findViewById(R.id.add_ok_title);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv=(ImageView)rv.findViewById(R.id.add_ok_title);
                    et = (EditText) rv.findViewById(R.id.add_edit_title);
                    try {
                        add_title = et.getText().toString();
                        if(!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/\\r\\n]+)$", add_title)||(add_title==null))
                        {
                            et.setText(null);
                            iv.setImageResource(R.drawable.ic_red_x);
                            add_title=null;
                        }else
                        {
                            iv.setImageResource(R.drawable.ic_green_check);
                        }
                    } catch (Exception e) {
                        add_title = null;
                        iv.setImageResource(R.drawable.ic_red_x);
                        et.setText(null);
                    }
                    try{
                        if(add_title.length()>35)
                        {
                            iv.setImageResource(R.drawable.ic_red_x);
                        }
                    }catch (NullPointerException e)
                    {
                        iv.setImageResource(R.drawable.ic_red_x);
                        hideSoftKeyboard();
                    }

                    hideSoftKeyboard();
                }
            });
            iv = (ImageView) rv.findViewById(R.id.add_ok_description);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv = (ImageView) rv.findViewById(R.id.add_ok_description);
                    et = (EditText) rv.findViewById(R.id.add_edit_description);
                    try {
                        add_description = et.getText().toString();
                        if(!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/]+)$", add_description)||(add_description==null))
                        {
                            et.setText(null);
                            iv.setImageResource(R.drawable.ic_red_x);
                            add_description=null;
                        }else
                        {
                            iv.setImageResource(R.drawable.ic_green_check);
                        }
                    } catch (Exception e) {
                        add_description = null;
                        iv.setImageResource(R.drawable.ic_red_x);
                    }
                    try{
                        if((add_description.length()>700))
                        {
                            iv.setImageResource(R.drawable.ic_red_x);
                        }
                    }catch(NullPointerException e)
                    {
                        iv.setImageResource(R.drawable.ic_red_x);
                        hideSoftKeyboard();
                    }

                    hideSoftKeyboard();
                }
            });
            spin1=(Spinner)rv.findViewById(R.id.add_spin_category);
            spinadapter1=new ArrayAdapter<>(curcontext,R.layout.simple_spinner_dropdown_item,category);
            spin1.setAdapter(spinadapter1);
            spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(draftscoming) {
                        spin2.setSelection(add_subcategory);
                        draftscoming=false;
                    }else {
                        if (position != 0) {
                            add_category = position;
                            add_subcategory = 0;
                            int q = categoryArray[position-1].length;
                            subcategory = new String[q];
                            System.arraycopy(categoryArray[position - 1], 0, subcategory, 0, q);

                            subcategory[0] = "Select a Subcategory";
                            iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                            iv.setImageResource(R.drawable.ic_red_x);
                            spin2 = (Spinner) rv.findViewById(R.id.add_spin_subcategory);
                            spinadapter2 = new ArrayAdapter<>(curcontext, R.layout.simple_spinner_dropdown_item, subcategory);
                            spin2.setAdapter(spinadapter2);
                            spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        add_subcategory = position;
                                        iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                                        iv.setImageResource(R.drawable.ic_green_check);
                                    } else {
                                        add_subcategory = 0;
                                        iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                                        iv.setImageResource(R.drawable.ic_red_x);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else if (add_subcategory != 0) {
                            add_subcategory = 0;
                            subcategory[0] = "Select a Subcategory";
                            iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                            iv.setImageResource(R.drawable.ic_red_x);
                            spin2 = (Spinner) rv.findViewById(R.id.add_spin_subcategory);
                            spin2.setSelection(0);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            button=(Button)rv.findViewById(R.id.add_draft);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t=(TextView)rv.findViewById(R.id.add_submission_status);
                    hideSoftKeyboard();
                    if(isDone())
                    {
                        //fire up prompt switch comments once prompt is done
                        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                            t.setText(R.string.add_external_unavail);
                        }else {
                            if (localIdeas.saveDraft(new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS),"drafts_" + pref.getString(getString(R.string.preference_username), "fuckedUp")),add_title,add_description,pref.getString(getString(R.string.preference_username),"No author"),0,0,11,add_category-1,add_subcategory-1)) {

                                t.setText(R.string.add_draft_success);
                            } else {
                                t.setText(R.string.add_draft_fail);
                            }
                        }
                    }else{
                        t.setText(R.string.add_fields_bad);
                    }
                }
            });
            button=(Button)rv.findViewById(R.id.add_submit);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t=(TextView)rv.findViewById(R.id.add_submission_status);
                    hideSoftKeyboard();
                    if(isDone())
                    {
                        //fire up prompt switch comments once prompt is done

                        mainActivity.ib.add(add_title,add_description,pref.getString(getString(R.string.preference_username),"No author"),0,0,11,add_category-1,add_subcategory-1);
                        t.setText(R.string.add_submit_success);
                    }else{
                        t.setText(R.string.add_fields_bad);
                    }
                }
            });
            if(draftscoming) {
                et = (EditText) rv.findViewById(R.id.add_edit_title);
                et.setText(add_title);
                et = (EditText) rv.findViewById(R.id.add_edit_description);
                et.setText(add_description);
                spin1.setSelection(add_category);
                int q = categoryArray[0].length;
                subcategory = new String[q];
                System.arraycopy(categoryArray[add_category], 0, subcategory, 0, q);
                subcategory[0] = "Select a Subcategory";
                spin2 = (Spinner) rv.findViewById(R.id.add_spin_subcategory);
                spinadapter2 = new ArrayAdapter<>(curcontext, R.layout.simple_spinner_dropdown_item, subcategory);
                spin2.setAdapter(spinadapter2);

                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            add_subcategory = position;
                            iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                            iv.setImageResource(R.drawable.ic_green_check);
                        } else {
                            add_subcategory = 0;
                            iv = (ImageView) rv.findViewById(R.id.add_ok_spinner);
                            iv.setImageResource(R.drawable.ic_red_x);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spin2.setSelection(add_subcategory);
            }
            return rv;
        }

        private boolean isDone() {
            rv=getView();
            iv = (ImageView) rv.findViewById(R.id.add_ok_title);
            et = (EditText) rv.findViewById(R.id.add_edit_title);
            boolean wegood=true;
            try {
                add_title = et.getText().toString();
                if (!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/\\r\\n]+)$", add_title) ||(add_title==null)) {
                    iv.setImageResource(R.drawable.ic_red_x);
                    add_title = null;
                    wegood= false;
                } else {
                    iv.setImageResource(R.drawable.ic_green_check);
                }
            } catch (Exception e) {
                add_title = null;
                iv.setImageResource(R.drawable.ic_red_x);
                wegood= false;
            }
            try{
                if((add_title.length()>35))
                {
                    iv.setImageResource(R.drawable.ic_red_x);
                    wegood= false;
                }
            }catch (NullPointerException e)
            {
                iv.setImageResource(R.drawable.ic_red_x);
                wegood= false;
            }

            iv = (ImageView) rv.findViewById(R.id.add_ok_description);
            et = (EditText) rv.findViewById(R.id.add_edit_description);
            try {
                add_description = et.getText().toString();
                if (!Pattern.matches("(?i)^([^\"\\[:\\]\\|=\\+\\*\\?<>\\\\\\/]+)$", add_description) || (add_description==null)) {
                    iv.setImageResource(R.drawable.ic_red_x);
                    add_description = null;
                    wegood= false;
                } else {
                    iv.setImageResource(R.drawable.ic_green_check);
                }
            } catch (Exception e) {
                add_description = null;
                iv.setImageResource(R.drawable.ic_red_x);
               wegood=false;
            }
            try {
                if (add_description.length() > 700) {
                    iv.setImageResource(R.drawable.ic_red_x);
                    wegood = false;
                }
            }catch (NullPointerException e)
            {
                iv.setImageResource(R.drawable.ic_red_x);
                wegood = false;
            }
            if((add_subcategory==0)||(add_category==0)){
                wegood= false;
            }
            return wegood;
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
