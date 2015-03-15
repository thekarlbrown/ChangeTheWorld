package com.thekarlbrown.changetheworld;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * page for individual ideas
 *
deal with thumbs up/down and favorites (sending when we leave, reflecting  choice when we enter, plus obv comments later
 */
public class IdeaPage extends Fragment {

    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    IdeaBlock ib;
    ImageView iv;
    MainActivity mainActivity;
    String idea_title;
    String idea_description;
    String idea_author;
    String idea_category;
    String idea_subcategory;
    int ideaSelected;
    int idea_Tup;
    int idea_Tdown;
    int idea_number;
    String[][]categoryArray;
    Context curcontext;
    Button button;
    Activity activity;
    boolean[]thumbd={false,false};
    boolean favorite;
    boolean followed;

    public static IdeaPage newInstance() {
        IdeaPage fragment = new IdeaPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public IdeaPage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //int i=savedInstanceState.getStringArray("category").length;
            // category=new String[i];
            ideaSelected=getArguments().getInt("position");
            favorite=getArguments().getBoolean("favorite");
            followed=getArguments().getBoolean("followed");
            thumbd=getArguments().getBooleanArray("thumbd");
        }


    }
    IdeaPageListener mListener;
    public interface IdeaPageListener{
        public void toUserIdeaPage(String username);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        rv = inflater.inflate(R.layout.fragment_idea_page, container, false);
        curcontext = rv.getContext();
        try {
            mListener = (IdeaPageListener) curcontext;
        } catch (ClassCastException e) {
            throw new ClassCastException(curcontext.toString()
                    + " must implement NoticeDialogListener");
        }
        categoryArray = mainActivity.categories;
        ib=mainActivity.ib;
        idea_title=ib.getTitle(ideaSelected);
        idea_category=categoryArray[ib.getCategory(ideaSelected)-1][0];
        idea_subcategory=categoryArray[ib.getCategory(ideaSelected)-1][ib.getSubcategory(ideaSelected)];
        idea_description=ib.getIdea(ideaSelected);
        idea_author=ib.getAuthor(ideaSelected);
        idea_Tdown=ib.getTdown(ideaSelected);
        idea_Tup=ib.getTup(ideaSelected);
        idea_number=ib.getNumber(ideaSelected);
        t=(TextView)rv.findViewById(R.id.page_idea_title);
        t.setText("~ " + idea_title + " ~");
        t=(TextView)rv.findViewById(R.id.page_idea_author);
        t.setText("Author: " +  idea_author);
        t=(TextView)rv.findViewById(R.id.page_idea_category);
        t.setText(idea_category);
        t=(TextView)rv.findViewById(R.id.page_idea_subcategory);
        t.setText(idea_subcategory);
        t=(TextView)rv.findViewById(R.id.page_idea_description);
        t.setText(idea_description);
        t.setMovementMethod(new ScrollingMovementMethod());
        t=(TextView)rv.findViewById(R.id.page_idea_tup);
        t.setText(Integer.toString(idea_Tup));
        t=(TextView)rv.findViewById(R.id.page_idea_tdown);
        t.setText(Integer.toString(idea_Tdown));
        iv=(ImageView)rv.findViewById(R.id.page_idea_favorite);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite){
                    iv=(ImageView)rv.findViewById(R.id.page_idea_favorite);
                    favorite=false;
                    //push to mysql, refresh
                    iv.setImageResource(R.drawable.ic_top_bar);
                }else{
                    iv=(ImageView)rv.findViewById(R.id.page_idea_favorite);
                    favorite=true;
                    //push to mysql, refresh
                    iv.setImageResource(R.drawable.ic_gold_star);
                }
            }
        });
        iv=(ImageView)rv.findViewById(R.id.page_idea_upselect);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumbd[0]) {
                    //potentially alter thumbd values, depends on how we refresh
                    //push to mysql thumbs down, refresh
                }
            }
        });
        iv=(ImageView)rv.findViewById(R.id.page_idea_downselect);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumbd[1]) {
                    //potentially alter thumbd values, depends on how we refresh
                    //push to mysql thumbs down, refresh
                }
            }
        });
        button=(Button)rv.findViewById(R.id.page_idea_other);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toUserIdeaPage(idea_author);
            }
        });
        button=(Button)rv.findViewById(R.id.page_idea_follow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followed){
                    followed=false;
                    //push to mysql to unfollow
                    button.setText(R.string.page_idea_follow);
                }else{
                    followed=true;
                    //push to mysql to follow
                    button.setText(R.string.page_idea_unfollow);
                }
            }
        });
        setVisuals();
        return rv;
    }


    @Override
    public void onDetach() {
        hideSoftKeyboard();
        if(thumbd[0]){
            //send a thumbs up to idea and do required activity
        }else if(thumbd[1]){
            //send a thumbs up to idea and do required activity
        }
        /*
        if(favorite){
           //deal with favoriting
        }*/
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

    //how I set if items are liked/favorited in terms of visuals
    private void setVisuals(){
        iv=(ImageView)rv.findViewById(R.id.page_idea_favorite);
        if(favorite){
            iv.setImageResource(R.drawable.ic_gold_star);
        }else{
            iv.setImageResource(R.drawable.ic_top_bar);
        }
        button=(Button)rv.findViewById(R.id.page_idea_follow);
        if(followed){
            button.setText(R.string.page_idea_unfollow);
        }else{
            button.setText(R.string.page_idea_follow);
        }
    }

}
