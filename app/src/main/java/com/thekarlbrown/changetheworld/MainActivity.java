package com.thekarlbrown.changetheworld;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
/*
       where everything goes through
       will be implementing asynctask for mysql handling
       potentially will be creating separate activity for algorithms applied to data containers, but may just use java class
 */

public class MainActivity extends Activity implements IdeaDataAdapter.IdeaDataAdapterListener, ProfileTab.ProfileTabListener, IdeaPage.IdeaPageListener, DraftDataAdapter.DraftDataAdapterListener, SearchDialog.NoticeDialogListener, RatioDialogue.NoticeRatioDialogListener, ThumbDialogue.NoticeThumbDialogListener {

    String[][]categories={{"LolTitle","Lol1","Lol2","Lol3","Lol4","Lol5","Lol6"},
            {"WutTitle","Wut1","Wut2","Wut3","Wut4","Wut5","Wut6"},{"BroTitle","Bro1","Bro2","Bro3","Bro4","Bro5","Bro6"}};
    String[] categorytitles;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences sharedPref;
    SplitToolbar st;
    Bundle b;
    OpeningScreen os;
    IdeaBlock ib,bytemp,drafts, byfavorite;
    LeaderBlock leaderBlock;
    String searchQuery;
    SearchDialog searchDialog;
    boolean[] filter_default={false,false,true,false};
    boolean[] filter_current;
    int minratio;
    int minthumbs;
    public SharedPreferences getPref()
    {
        return sharedPref;
    }

   public void openTrending()
    {
        if( fm.findFragmentByTag("trending") == null )
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            b=new Bundle();
            b.putBooleanArray("selectedf",filter_default);
            b.putInt("selecteda",0);
            TrendingTab trendingTab=new TrendingTab();
            trendingTab.setArguments(b);
            ft.replace(R.id.current_tab,trendingTab,"trending");

            ft.commit();
        }
    }
    public void openCategory()
    {
        if( fm.findFragmentByTag("category") == null )
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            b=new Bundle();
            CategoryTab cat=new CategoryTab();
            b.putStringArray("titles",categorytitles);
            b.putBooleanArray("selectedf",filter_default);
            b.putInt("selectedt",4);
            cat.setArguments(b);
            ft.replace(R.id.current_tab,cat,"category");
            ft.commit();
        }
    }
    public void openSearch()
    {
        searchDialog=new SearchDialog();
        searchDialog.show(getFragmentManager(),"search");
    }
    public void openAdd()
    {
        if( fm.findFragmentByTag("add") == null )
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            b=new Bundle();
            AddTab addT=new AddTab();
            b.putStringArray("category",createAddTitles());
            b.putBoolean("draftscoming",false);
            addT.setArguments(b);
            ft.replace(R.id.current_tab,addT,"add");
            ft.commit();
        }
    }
    public void openTop()
    {
        if( fm.findFragmentByTag("top") == null )
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            b=new Bundle();
            LeaderboardTab leaderboardTab=new LeaderboardTab();
            b.putInt("selecteda",0);
            b.putInt("selectedt",4);
            leaderboardTab.setArguments(b);
            ft.replace(R.id.current_tab,leaderboardTab,"top");
            ft.commit();
        }
    }
    public void openProfile()
    {
        if( fm.findFragmentByTag("profile") == null )
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            ft.replace(R.id.current_tab,new ProfileTab(),"profile");
            ft.commit();
        }
    }
    //how I do the dialog
    @Override
    public void onDialogPositiveClick(String r) {
        if(r!=null) {
            searchQuery=r;
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            SearchTab st =new SearchTab();
            b=new Bundle();
            b.putString("query",searchQuery);
            b.putBooleanArray("selectedf",filter_default);
            st.setArguments(b);
            ft.replace(R.id.current_tab,st,"search");
            ft.commit();
        }else{
            searchDialog.show(getFragmentManager(),"SearchDialog");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        st = (SplitToolbar) findViewById(R.id.toolbar_top);

        st.inflateMenu(R.menu.menu_navigation);
        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //test blocks of data due to lack of mysql implementation
        //ideablock
        ib=new IdeaBlock("Placeholder","Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck","Karl Brown",999,1,70,2,3);
        ib.add("heres the plan","add ideas like crazy","wutang clan",100,0,63,0,1);
        ib.add(new String[]{"I have a suggestion","However please remember"},new String[]{"Try to break the app and tell me what messed up","There is test data available. Please also have the latest release, and check what I know is broken, will be changed, and will be implemented later"},new String[]{"Richard Stallman","George Soros"},new int[]{2,3},new int[]{2,3},new int[]{2,3},new int[]{1,1},new int[]{3,4});
        ib.add(ib.titles,ib.ideas,ib.authors,ib.tups,ib.tdowns,ib.numbers,ib.categorys,ib.subcategorys);
        ib.add(ib.titles,ib.ideas,ib.authors,ib.tups,ib.tdowns,ib.numbers,ib.categorys,ib.subcategorys);
        //leaderblock
        leaderBlock=new LeaderBlock(new String[]{"putin","obama","farage","assad","kadyrov"},new String[]{"putin","obama","farage","assad","kadyrov"},new String[]{"putin","obama","farage","assad","kadyrov"},new String[]{"putin","obama","farage","assad","kadyrov"},
                                    new double[]{59.523,42.70,9.11,3.041,99.99},new int[]{999,70,911,101,1337},new double[]{59.523,42.69,9.11,3.041,99.99},new double[]{59.523,42.70,9.11,3.041,99.99});


        //this deletes your user every time. comment it out to save username and be more persistent
        sharedPref.edit().remove(getString(R.string.preference_setup)).apply();

        //create category titles
        createTitles();
        st.setOnMenuItemClickListener(new SplitToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab_trending:
                        openTrending();
                        return true;
                    case R.id.tab_category:
                        openCategory();
                        return true;
                    case R.id.tab_search:
                        openSearch();
                        return true;
                    case R.id.tab_add:
                        openAdd();
                        return true;
                    case R.id.tab_top:
                        openTop();
                        return true;
                    case R.id.tab_profile:
                        openProfile();
                        return true;
                    default:
                        return false;
                }
            }
        });

        if(fm==null)
        {
            fm=getFragmentManager();
            ft=fm.beginTransaction();
            if(sharedPref.getBoolean(getString(R.string.preference_setup), false)) {
                os = new OpeningScreen();
                ft.add(R.id.current_tab, os, "opening");
            }else{
                InitialScreen init = new InitialScreen();
                b=new Bundle();
                b.putStringArray("categorytitles",createAddTitles());
                init.setArguments(b);
                ft.add(R.id.current_tab,init,"initial");
            }
            ft.commit();
        }

    }
    public void createTitles()
    {
        int length=categories.length;
        categorytitles=new String[length];
        for(int x=0;x<length;x++)
        {
            categorytitles[x]=categories[x][0];
        }
    }
    public String[] createAddTitles()
    {
        int temp=categorytitles.length;
        String[] addTitles=new String[temp+1];
        addTitles[0]= "Select a Category";
        System.arraycopy(categorytitles,0,addTitles,1,temp);
        return addTitles;
    }

    @Override
    public void onRatioDialogPositiveClick(int i) {
        minratio=i;
        fm=getFragmentManager();
        fm.popBackStack();

    }
    @Override
    public void onRatioDialogNegativeClick() {
          minratio=0;
    }
    @Override
    public void onThumbDialogPositiveClick(int i) {
        minratio=i;
        fm=getFragmentManager();
        fm.popBackStack();
    }
    @Override
    public void onThumbDialogNegativeClick() {
        minratio=0;
    }
    @Override
    public void onDraftListClick(int position) {
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        b=new Bundle();
        AddTab addT=new AddTab();
        b.putStringArray("category",createAddTitles());
        b.putBoolean("draftscoming",true);
        b.putString("drafttitle",drafts.getTitle(position));
        b.putString("draftdescription",drafts.getIdea(position));
        b.putInt("draftcategory",drafts.getCategory(position));
        b.putInt("draftsubcategory",drafts.getSubcategory(position));
        addT.setArguments(b);
        ft.replace(R.id.current_tab,addT,"add");
        ft.commit();
    }
    @Override
    public void onIdeaListClick(int position) {
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        b=new Bundle();
        IdeaPage ideaPage=new IdeaPage();
        b.putInt("position",position);
        ideaPage.setArguments(b);
        ft.replace(R.id.current_tab,ideaPage,"ideapage");
        ft.commit();
    }

    @Override
    public void toUserIdeaPage(String username) {
        //call the necessary requirements for byuser here
        bytemp=ib;
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ByUserPage userPage=new ByUserPage();
        b.putString("username",username);
        b.putBooleanArray("selectedf",filter_default);
        userPage.setArguments(b);
        ft.replace(R.id.current_tab,userPage,"byuser");
        ft.commit();
    }
    @Override
    public void toFavoriteIdeaPage(String username)
    {
        //call the necessary requirements for favoriteideas here
        bytemp=ib;
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ByFriendsPage friendsPage=new ByFriendsPage();
        b.putString("username",username);
        b.putBooleanArray("selectedf",filter_default);
        friendsPage.setArguments(b);
        ft.replace(R.id.current_tab,friendsPage,"byfriends");
        ft.commit();
    }
    @Override
    public void toFriendsIdeaPage(String username)
    {
        //call the necessary requirements for favoriteideas here
        bytemp=ib;
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ByFavoritePage favoritePage=new ByFavoritePage();
        b.putString("username",username);
        b.putBooleanArray("selectedf",filter_default);
        favoritePage.setArguments(b);
        ft.replace(R.id.current_tab,favoritePage,"byfavorite");
        ft.commit();
    }
}
