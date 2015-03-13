package com.thekarlbrown.changetheworld;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/*
       core of application
       currently implementing asynctask for PHP backend
       potentially will be creating separate activity for algorithms applied to data containers, but may just use asynctask
 */

public class MainActivity extends Activity implements IdeaDataAdapter.IdeaDataAdapterListener, ProfileTab.ProfileTabListener, IdeaPage.IdeaPageListener, DraftDataAdapter.DraftDataAdapterListener, SearchDialog.SearchDialogListener, RatioDialogue.NoticeRatioDialogListener, ThumbDialogue.NoticeThumbDialogListener {

    String[][] categories = {{"Inventions","Electronic","Medical","Environmental","Tools","Machines","Industrial","Chemical","Agricultural","Instruments","Build It Yourself","Other"},
            {"Innovations","Recipes","College Hacks","Thrifty Living","Manufacturing Techniques","Going Green","Home Decor","Pet Hacks","Parenting Hacks",
                    "Costume/Cosplay Inspiration","Common Mistake/Solutions","Fixing Everyday Issues","Other"},
            {"Abstract/Concepts/Processes","Algorithms","Computer Programs/Mobile Apps","Economic/Governmental/Social Systems","Philosophical","Real World Services",
                    "Internet Services","Cultural","Events/Conventions/Meetups","Party Concepts","Processes","Dance Moves","Other"},
            {"The Suggestion Box","Government","Public School","College","Economics","Rules,Regulations, and Laws","Life Tips","Other"},
            {"Written/Visual Compositions","TV Shows","Movie Plots","Book Ideas","Theatre","Fine Art","Fashion","Other"},
            {"NSFW","Sexual Maneuvers","Combat Techniques","Stealth","Immoral","Got It For \"Free\"","Politically Incorrect","Other"},
            {"Joke Ideas","Bad","Funny","Inherently Flawed","Sarcastic","Absolutely Ridiculous","What-Ifs","Other"},
            {"Other","Doesn't Fit Anywhere","Suggestions for App","New Category's/Subcategory","Bugs Discovered","Messages to the Creator","Other"}
    };
    String[] categorytitles;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences sharedPref;
    SplitToolbar st;
    Bundle b;
    IdeaBlock ib, drafts, ibtop,ibratio,ibthumbs;
    LeaderBlock leaderBlock;
    SearchDialog searchDialog;
    int minratio;
    int minthumbs;
    TrendingTab trendingTab;
    CategoryTab categoryTab;
    AddTab addTab;
    LeaderboardTab leaderboardTab;
    SearchTab searchTab;
    ByUserPage userPage;
    ByFriendsPage friendsPage;
    ByFavoritePage favoritePage;
    boolean[] bar_filter_status = {false, false};

    //for asynctask
    ASyncParser aSyncParser;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String username, state,country;
    //float lat, long;

    public SharedPreferences getPref() {
        return sharedPref;
    }

    public void openTrending() {
        if (fm.findFragmentByTag("trending") == null) {
            ib=new IdeaBlock();
            getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byAreaJSON.php?lat=37&long=-76&state=" + state + "&country=" + country + "&username= " + sharedPref.getString(getString(R.string.preference_username),"YouGoofed") +"&case=3");
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status=new boolean[]{false,false};
            b.putInt("selecteda", 0);
            trendingTab = new TrendingTab();
            trendingTab.setArguments(b);
            ft.replace(R.id.current_tab, trendingTab, "trending");

            ft.commit();
        }
    }

    public void openCategory() {
        if (fm.findFragmentByTag("category") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            categoryTab = new CategoryTab();
            b.putStringArray("titles", categorytitles);
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status=new boolean[]{false,false};
            b.putInt("selectedt", 4);
            categoryTab.setArguments(b);
            ft.replace(R.id.current_tab, categoryTab, "category");
            ft.commit();
        }else{
            categoryTab.mDrawerLayout.openDrawer(categoryTab.mDrawerList);
        }
    }

    public void openSearch() {
        searchDialog = new SearchDialog();
        searchDialog.show(getFragmentManager(), "search");
    }

    public void openAdd() {
        if (fm.findFragmentByTag("add") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            addTab = new AddTab();
            b.putStringArray("category", createAddTitles());
            b.putBoolean("draftscoming", false);
            addTab.setArguments(b);
            ft.replace(R.id.current_tab, addTab, "add");
            ft.commit();
        }
    }

    public void openTop() {
        if (fm.findFragmentByTag("top") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            leaderboardTab = new LeaderboardTab();
            b.putInt("selecteda", 0);
            b.putInt("selectedt", 4);
            leaderboardTab.setArguments(b);
            ft.replace(R.id.current_tab, leaderboardTab, "top");
            ft.commit();
        }
    }

    public void openProfile() {
        if (fm.findFragmentByTag("profile") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.current_tab, new ProfileTab(), "profile");
            ft.commit();
        }
    }

    //how I do the dialog
    @Override
    public void onSearchDialogPositiveClick(String r) {
        if (r != null) {
            //searchQuery = r;
            ib=new IdeaBlock();
            getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_bySearchJSON.php?search=" + r);
            //should I process filters?
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            searchTab = new SearchTab();
            b = new Bundle();
            b.putString("query", r);
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status=new boolean[]{false,false};
            searchTab.setArguments(b);
            ft.replace(R.id.current_tab, searchTab, "search");
            ft.commit();
        } else {
            searchDialog.show(getFragmentManager(), "SearchDialog");
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
        /*ideablock
        ib = new IdeaBlock("Placeholder", "Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck Heres a lllama theres a llama and another little llama fuzzy llama funny llama llamma llamma duck", "Karl Brown", 999, 1, 70, 2, 11);
        ib.add("heres the plan", "add yo ideas like crazy till da brain feels lazy", "k breezy", 100, 0, 63, 4, 4);
        ib.add(new String[]{"I have a suggestion", "However please remember"}, new String[]{"Try to break the app and tell me what messed up", "There is test data available. Please also have the latest release, and check what I know is broken, will be changed, and will be implemented later"}, new String[]{"Richard Stallman", "George Soros"}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{7, 3}, new int[]{3, 4});
        ib.add(ib.titles, ib.ideas, ib.authors, ib.tups, ib.tdowns, ib.numbers, ib.categorys, ib.subcategorys);
        ib.add(ib.titles, ib.ideas, ib.authors, ib.tups, ib.tdowns, ib.numbers, ib.categorys, ib.subcategorys);
        */
        ib=new IdeaBlock();
        state="va";
        country="us";
        //leaderblock
        leaderBlock = new LeaderBlock(new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"},
                new double[]{59.523, 42.70, 9.11, 3.041, 99.99}, new int[]{999, 70, 911, 101, 1337}, new double[]{59.523, 42.69, 9.11, 3.041, 99.99}, new double[]{59.523, 42.70, 9.11, 3.041, 99.99});


        //this deletes your user every time. comment it out to save username and be more persistent
        sharedPref.edit().remove(getString(R.string.preference_username)).apply();

        //create category titles
        createTitles();


        if (fm == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            /*
            if (sharedPref.getBoolean(getString(R.string.preference_setup), false)) {
                os = new OpeningScreen();
                ft.add(R.id.current_tab, os, "opening");
                searchTabClick();
            } else {
                InitialScreen init = new InitialScreen();
                init.setArguments(b);
                */
                ft.add(R.id.current_tab, new InitialScreen(), "initial");
            //}
            ft.commit();
        }

    }

    public void createTitles() {
        int length = categories.length;
        categorytitles = new String[length];
        for (int x = 0; x < length; x++) {
            categorytitles[x] = categories[x][0];
        }
    }

    public String[] createAddTitles() {
        int temp = categorytitles.length;
        String[] addTitles = new String[temp + 1];
        addTitles[0] = "Select a Category";
        System.arraycopy(categorytitles, 0, addTitles, 1, temp);
        return addTitles;
    }

    @Override
    public void onRatioDialogPositiveClick(int i, String tag) {
        minratio = i;
        //do sorting algo here
        bar_filter_status[0] = true;
        switch (tag) {
            case "byfriends":
                friendsPage.filterSelected(0);
                break;
            case "byfavorite":
                favoritePage.filterSelected(0);
                break;
            case "byuser":
                userPage.filterSelected(0);
                break;
            case "trending":
                trendingTab.filterSelected(0);
                break;
            case "category":
                categoryTab.filterSelected(0);
                break;
            case "search":
                searchTab.filterSelected(0);
                break;
            default:
                tag = "leave b4 admin bans u";
                break;
        }
        fm = getFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onRatioDialogNegativeClick() {

    }

    public void revertBarFilter(String tag, boolean which_filter) {
        if (which_filter) {
            minratio = 0;
            bar_filter_status[0] = false;
            //insert algorithm to revert data based on what is easiest
            switch (tag) {
                case "byfriends":
                    friendsPage.dapt.notifyDataSetChanged();
                    break;
                case "byfavorite":
                    favoritePage.dapt.notifyDataSetChanged();
                    break;
                case "byuser":
                    userPage.dapt.notifyDataSetChanged();
                    break;
                case "trending":
                    trendingTab.dapt.notifyDataSetChanged();
                    break;
                case "category":
                    categoryTab.dapt.notifyDataSetChanged();
                    break;
                case "search":
                    searchTab.dapt.notifyDataSetChanged();
                    break;
                default:
                    tag = "leave b4 admin bans u";
                    break;
            }
        } else {
            minthumbs = 0;
            bar_filter_status[1] = false;
            //insert algorithm to revert data based on what is easiest
            switch (tag) {
                case "byfriends":
                    friendsPage.dapt.notifyDataSetChanged();
                    break;
                case "byfavorite":
                    favoritePage.dapt.notifyDataSetChanged();
                    break;
                case "byuser":
                    userPage.dapt.notifyDataSetChanged();
                    break;
                case "trending":
                    trendingTab.dapt.notifyDataSetChanged();
                    break;
                case "category":
                    categoryTab.dapt.notifyDataSetChanged();
                    break;
                case "search":
                    searchTab.dapt.notifyDataSetChanged();
                    break;
                default:
                    tag = "leave b4 admin bans u";
                    break;
            }
        }
    }
    public void switchBarFilter(boolean which, String tag)
    {
        if(which)
        {
            //insert algorithm to sort by recent
            switch (tag) {
                case "byfriends":
                    friendsPage.dapt.notifyDataSetChanged();
                    break;
                case "byfavorite":
                    favoritePage.dapt.notifyDataSetChanged();
                    break;
                case "byuser":
                    userPage.dapt.notifyDataSetChanged();
                    break;
                case "trending":
                    trendingTab.dapt.notifyDataSetChanged();
                    break;
                case "category":
                    categoryTab.dapt.notifyDataSetChanged();
                    break;
                case "search":
                    searchTab.dapt.notifyDataSetChanged();
                    break;
                default:
                    tag = "leave b4 admin bans u";
                    break;
            }
        }else{
            //insert algorithm to sort by top rated
            switch (tag) {
                case "byfriends":
                    friendsPage.dapt.notifyDataSetChanged();
                    break;
                case "byfavorite":
                    favoritePage.dapt.notifyDataSetChanged();
                    break;
                case "byuser":
                    userPage.dapt.notifyDataSetChanged();
                    break;
                case "trending":
                    trendingTab.dapt.notifyDataSetChanged();
                    break;
                case "category":
                    categoryTab.dapt.notifyDataSetChanged();
                    break;
                case "search":
                    searchTab.dapt.notifyDataSetChanged();
                    break;
                default:
                    tag = "leave b4 admin bans u";
                    break;
            }
        }
    }
    public void pullAreaBar(int area,String tag)
    {
        //set area of choice to be pulled here
        //method with int area
        switch (tag) {
            case "byfriends":
                friendsPage.dapt.notifyDataSetChanged();
                break;
            case "byfavorite":
                favoritePage.dapt.notifyDataSetChanged();
                break;
            case "byuser":
                userPage.dapt.notifyDataSetChanged();
                break;
            case "trending":
                trendingTab.dapt.notifyDataSetChanged();
                break;
            case "category":
                categoryTab.dapt.notifyDataSetChanged();
                break;
            case "search":
                searchTab.dapt.notifyDataSetChanged();
                break;
            default:
                tag = "leave b4 admin bans u";
                break;
        }
    }
    public void pullTimeBar(int time,String tag)
    {
        //set time of choice to be pulled here
        //method with int time
        switch (tag) {
            case "byfriends":
                friendsPage.dapt.notifyDataSetChanged();
                break;
            case "byfavorite":
                favoritePage.dapt.notifyDataSetChanged();
                break;
            case "byuser":
                userPage.dapt.notifyDataSetChanged();
                break;
            case "trending":
                trendingTab.dapt.notifyDataSetChanged();
                break;
            case "category":
                categoryTab.dapt.notifyDataSetChanged();
                break;
            case "search":
                searchTab.dapt.notifyDataSetChanged();
                break;
            default:
                tag = "leave b4 admin bans u";
                break;
        }
    }
    @Override
    public void onThumbDialogPositiveClick(int i, String tag) {
        minthumbs = i;
        bar_filter_status[1] = true;
        //do sorting algo here
        switch (tag) {
            case "byfriends":
                friendsPage.filterSelected(1);
                break;
            case "byfavorite":
                favoritePage.filterSelected(1);
                break;
            case "byuser":
                userPage.filterSelected(1);
                break;
            case "trending":
                trendingTab.filterSelected(1);
                break;
            case "category":
                categoryTab.filterSelected(1);
                break;
            case "search":
                searchTab.filterSelected(1);
                break;
            default:
                tag = "leave b4 admin bans u";
                break;
        }
        fm = getFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onThumbDialogNegativeClick() {
    }

    @Override
    public void onDraftListClick(int position) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        b = new Bundle();
        AddTab addT = new AddTab();
        b.putStringArray("category", createAddTitles());
        b.putBoolean("draftscoming", true);
        b.putString("drafttitle", drafts.getTitle(position));
        b.putString("draftdescription", drafts.getIdea(position));
        b.putInt("draftcategory", drafts.getCategory(position));
        b.putInt("draftsubcategory", drafts.getSubcategory(position));
        addT.setArguments(b);
        ft.replace(R.id.current_tab, addT, "add");
        ft.commit();
    }

    @Override
    public void onIdeaListClick(int position) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        b = new Bundle();
        IdeaPage ideaPage = new IdeaPage();
        b.putInt("position", position);
        //here we will have a asynctask that will obtain their current value based on position
        //this will require us to know the idea id's. IdeaBlock may have an additional category added
        //sample data for now
        b.putBooleanArray("thumbd",new boolean[]{false,false});
        b.putBoolean("favorite",true);
        b.putBoolean("followed",true);
        ideaPage.setArguments(b);
        ft.replace(R.id.current_tab, ideaPage, "ideapage");
        ft.commit();
    }

    @Override
    public void toUserIdeaPage(String username) {
        //call the necessary requirements for byuser here
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        userPage = new ByUserPage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status=new boolean[]{false,false};
        userPage.setArguments(b);
        ft.replace(R.id.current_tab, userPage, "byuser");
        ft.commit();
    }

    @Override
    public void toFriendsIdeaPage(String username) {
        //call the necessary requirements for favoriteideas here
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        friendsPage = new ByFriendsPage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status=new boolean[]{false,false};
        friendsPage.setArguments(b);
        ft.replace(R.id.current_tab, friendsPage, "byfriends");
        ft.commit();
    }

    @Override
    public void toFavoriteIdeaPage(String username) {
        //call the necessary requirements for favoriteideas here
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        favoritePage = new ByFavoritePage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status=new boolean[]{false,false};
        favoritePage.setArguments(b);
        ft.replace(R.id.current_tab, favoritePage, "byfavorite");
        ft.commit();
    }
    public void searchTabClick()
    {
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
    }

    /**
     * Gets JSON data from URL and put into IdeaBlock
     * @param url - URL to connect to
     */
    public void getJSONtoIdeaBlock(String url){
        aSyncParser=new ASyncParser(url);
        try{
            aSyncParser.execute();
            jsonArray=aSyncParser.get();
        }catch(Exception e){
            Log.println(0, "Error", e.getMessage());
        }
        try{
            for(int i=0;i<jsonArray.length();i++){
                jsonObject=jsonArray.getJSONObject(i);
                ib.add(jsonObject.getString("title"), jsonObject.getString("descrip"), jsonObject.getString("author"),
                        Integer.parseInt(jsonObject.getString("thumbsup")), Integer.parseInt(jsonObject.getString("thumbsdown")), Integer.parseInt(jsonObject.getString("ididea")),
                        Integer.parseInt(jsonObject.getString("cat")), Integer.parseInt(jsonObject.getString("sub")));
            }
        }catch (JSONException e){
            Log.println(0,"Error",e.getMessage());
        }
        if (ib.size()==0){
            ib.add(":[","There does not seem to be any ideas here yet! Too bad. Either you have internet connectivitivity issues, or you are in an empty category","thekarlbrown",
                    0,999,1,8,2);
        }
    }

    /**
     * Login Query to JSON
     * @param username - Username to attempt login with
     * @param password - Password to attempt login with
     * @return - Boolean representing success of query
     */
    public boolean verifyLogon(String username,String password){
        aSyncParser=new ASyncParser("http://www.thekarlbrown.com/ctwapp/user_verifyPHash.php?username="+username+"&password="+password);
        try{
            aSyncParser.execute();
            jsonArray=aSyncParser.get();
        }catch(Exception e){
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try{
            int result=Integer.parseInt(jsonArray.getJSONObject(0).getString("verified"));
            if(result==0){
                return false;
            }else{
                // do whatever must be done upon successful password verification here if necessary for security
                this.username=username;
                return true;
            }
        }catch (Exception e){
            Log.println(0,"Error",e.getMessage());
            return false;
        }
    }

    public boolean verifyCreate(String username,String password,String email){
        aSyncParser=new ASyncParser("http://www.thekarlbrown.com/ctwapp/user_addPHash.php?username="+username+"&password="+password+"&email="+email);
        try{
            aSyncParser.execute();
            jsonArray=aSyncParser.get();
        }catch(Exception e){
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try{
            int result=Integer.parseInt(jsonArray.getJSONObject(0).getString("created"));
            if(result==0){
                return false;
            }else{
                // do whatever must be done upon successful account creation here if necessary for security
                this.username=username;
                return true;
            }
        }catch (Exception e){
            Log.println(0,"Error",e.getMessage());
            return false;
        }
    }
}
