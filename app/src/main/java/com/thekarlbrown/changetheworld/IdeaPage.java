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

import java.util.List;

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
    List<IdeaBlock> ib;
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
        idea_title=ib.get(ideaSelected).getTitle();
        idea_category=categoryArray[ib.get(ideaSelected).getCategory()-1][0];
        idea_subcategory=categoryArray[ib.get(ideaSelected).getCategory()-1][ib.get(ideaSelected).getSubcategory()];
        idea_description=ib.get(ideaSelected).getIdea();
        idea_author=ib.get(ideaSelected).getAuthor();
        idea_Tdown=ib.get(ideaSelected).getTdown();
        idea_Tup=ib.get(ideaSelected).getTup();
        idea_number=ib.get(ideaSelected).getNumber();
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
        button=(Button)rv.findViewById(R.id.page_idea_favorite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite){
                    mainActivity.setFavRatD(mainActivity.username,idea_number,0);
                    button=(Button)rv.findViewById(R.id.page_idea_favorite);
                    button.setText(R.string.page_idea_favorite);
                    iv=(ImageView)rv.findViewById(R.id.page_idea_favorite_image);
                    iv.setImageResource(R.drawable.ic_top_bar);
                    favorite=false;
                }else{
                    mainActivity.setFavRatD(mainActivity.username,idea_number,1);
                    iv=(ImageView)rv.findViewById(R.id.page_idea_favorite_image);
                    favorite=true;
                    iv.setImageResource(R.drawable.ic_gold_star);
                    button=(Button)rv.findViewById(R.id.page_idea_favorite);
                    button.setText(R.string.page_idea_unfavorite);
                }
            }
        });
        button=(Button)rv.findViewById(R.id.page_idea_upselect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumbd[0]&&!thumbd[1]) {
                    mainActivity.setFavRatD(mainActivity.username,idea_number,3);
                    idea_Tup++;
                    t=(TextView)rv.findViewById(R.id.page_idea_tup);
                    t.setText(Integer.toString(idea_Tup));
                    thumbd[0]=true;
                }else if(thumbd[1]&&!thumbd[0]){
                    mainActivity.setFavRatD(mainActivity.username,idea_number,3);
                    idea_Tup++;
                    thumbd[0]=true;
                    idea_Tdown--;
                    thumbd[1]=false;
                    t=(TextView)rv.findViewById(R.id.page_idea_tup);
                    t.setText(Integer.toString(idea_Tup));
                    t=(TextView)rv.findViewById(R.id.page_idea_tdown);
                    t.setText(Integer.toString(idea_Tdown));
                }else if(thumbd[0]){
                    mainActivity.setFavRatD(mainActivity.username,idea_number,2);
                    thumbd[0]=false;
                    idea_Tup--;
                    t=(TextView)rv.findViewById(R.id.page_idea_tup);
                    t.setText(Integer.toString(idea_Tup));
                }
            }
        });
        button=(Button)rv.findViewById(R.id.page_idea_downselect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!thumbd[1]&&!thumbd[0]) {
                    mainActivity.setFavRatD(mainActivity.username,idea_number,4);
                    idea_Tdown++;
                    thumbd[1]=true;
                    t=(TextView)rv.findViewById(R.id.page_idea_tdown);
                    t.setText(Integer.toString(idea_Tdown));
                }else if(thumbd[0]&&!thumbd[1]){
                    mainActivity.setFavRatD(mainActivity.username,idea_number,4);
                    idea_Tdown++;
                    thumbd[1]=true;
                    idea_Tup--;
                    thumbd[0]=false;
                    t=(TextView)rv.findViewById(R.id.page_idea_tup);
                    t.setText(Integer.toString(idea_Tup));
                    t=(TextView)rv.findViewById(R.id.page_idea_tdown);
                    t.setText(Integer.toString(idea_Tdown));
                }else if(thumbd[1]){
                    mainActivity.setFavRatD(mainActivity.username,idea_number,2);
                    thumbd[1]=false;
                    idea_Tdown--;
                    t=(TextView)rv.findViewById(R.id.page_idea_tdown);
                    t.setText(Integer.toString(idea_Tdown));
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
                    if(mainActivity.changeFollow(idea_author,0)) {
                        button = (Button) rv.findViewById(R.id.page_idea_follow);
                        button.setText(R.string.page_idea_follow);
                        followed=false;
                    }
                }else{
                    if(mainActivity.changeFollow(idea_author,1)) {
                        button = (Button) rv.findViewById(R.id.page_idea_follow);
                        button.setText(R.string.page_idea_unfollow);
                        followed=true;
                    }
                }
            }
        });
        setVisuals();
        return rv;
    }


    @Override
    public void onDetach() {
        hideSoftKeyboard();
        super.onDetach();
    }
    //Hide that keyboard
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
        iv=(ImageView)rv.findViewById(R.id.page_idea_favorite_image);
        button=(Button)rv.findViewById(R.id.page_idea_favorite);
        if(favorite){
            iv.setImageResource(R.drawable.ic_gold_star);
            button.setText(R.string.page_idea_unfavorite);
        }
        button=(Button)rv.findViewById(R.id.page_idea_follow);
        if(followed){
            button.setText(R.string.page_idea_unfollow);
        }else{
            button.setText(R.string.page_idea_follow);
        }
    }

}
