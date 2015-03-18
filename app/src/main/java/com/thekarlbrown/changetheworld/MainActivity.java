package com.thekarlbrown.changetheworld;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/*
       core of application
       currently implementing asynctask for PHP backend
       potentially will be creating separate activity for algorithms applied to data containers, but may just use asynctask
 */

public class MainActivity extends Activity implements IdeaDataAdapter.IdeaDataAdapterListener, ProfileTab.ProfileTabListener, IdeaPage.IdeaPageListener, DraftDataAdapter.DraftDataAdapterListener, SearchDialog.SearchDialogListener, RatioDialogue.NoticeRatioDialogListener, ThumbDialogue.NoticeThumbDialogListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String[][] categories = {{"Inventions", "Electronic", "Medical", "Environmental", "Tools", "Machines", "Industrial", "Chemical", "Agricultural", "Instruments", "Build It Yourself", "Other"},
            {"Innovations", "Recipes", "College Hacks", "Thrifty Living", "Manufacturing Techniques", "Going Green", "Home Decor", "Pet Hacks", "Parenting Hacks",
                    "Costume/Cosplay Inspiration", "Common Mistake/Solutions", "Fixing Everyday Issues", "Other"},
            {"Abstract/Concepts/Processes", "Algorithms", "Computer Programs/Mobile Apps", "Economic/Governmental/Social Systems", "Philosophical", "Real World Services",
                    "Internet Services", "Cultural", "Events/Conventions/Meetups", "Party Concepts", "Processes", "Dance Moves", "Other"},
            {"The Suggestion Box", "Government", "Public School", "College", "Economics", "Rules,Regulations, and Laws", "Life Tips", "Other"},
            {"Written/Visual Compositions", "TV Shows", "Movie Plots", "Book Ideas", "Theatre", "Fine Art", "Fashion", "Other"},
            {"NSFW", "Sexual Maneuvers", "Combat Techniques", "Stealth", "Immoral", "Got It For \"Free\"", "Politically Incorrect", "Other"},
            {"Joke Ideas", "Bad", "Funny", "Inherently Flawed", "Sarcastic", "Absolutely Ridiculous", "What-Ifs", "Other"},
            {"Other", "Doesn't Fit Anywhere", "Suggestions for App", "New Category's/Subcategory", "Bugs Discovered", "Messages to the Creator", "Other"}
    };
    String[] categorytitles;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences sharedPref;
    SplitToolbar st;
    Bundle b;
    IdeaBlock ib, drafts, ibtop, ibratio, ibthumbs;
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
    ProfileTab profileTab;
    boolean[] bar_filter_status = {false, false};
    String username;
    //for asynctask
    ASyncParser aSyncParser;
    JSONObject jsonObject;
    JSONArray jsonArray;
    //For location data
    GoogleApiClient mGoogleApiClient;
    String state, country;
    double latitude, longitude;
    Geocoder geocoder;
    Location mLastLocation;
    Map<String,String> states;
    public SharedPreferences getPref() {
        return sharedPref;
    }

    public void openTrending() {

            ib = new IdeaBlock();
            getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byAreaJSON.php?lat="+latitude+"&long="+longitude+"&state=" + state + "&country=" + country + "&username=" + username + "&case=3");
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status = new boolean[]{false, false};
            b.putInt("selecteda", 0);
            trendingTab = new TrendingTab();
            trendingTab.setArguments(b);
            ft.replace(R.id.current_tab, trendingTab, "trending");

            ft.commit();

    }

    public void openCategory() {
        if (fm.findFragmentByTag("category") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            categoryTab = new CategoryTab();
            b.putStringArray("titles", categorytitles);
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status = new boolean[]{false, false};
            b.putInt("selectedt", 4);
            categoryTab.setArguments(b);
            ft.replace(R.id.current_tab, categoryTab, "category");
            ft.commit();
        } else {
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
            profileTab=new ProfileTab();
            b=new Bundle();
            b.putString("username",username);
            profileTab.setArguments(b);
            ft.replace(R.id.current_tab, profileTab , "profile");
            ft.commit();
        }
    }

    //how I do the dialog
    @Override
    public void onSearchDialogPositiveClick(String r) {
        if (r != null) {
            //searchQuery = r;
            ib = new IdeaBlock();
            getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_bySearchJSON.php?search=" + r);
            //should I process filters?
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            searchTab = new SearchTab();
            b = new Bundle();
            b.putString("query", r);
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status = new boolean[]{false, false};
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

        ib = new IdeaBlock();
        //leaderblock
        leaderBlock = new LeaderBlock(new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"},
                new double[]{59.523, 42.70, 9.11, 3.041, 99.99}, new int[]{999, 70, 911, 101, 1337}, new double[]{59.523, 42.69, 9.11, 3.041, 99.99}, new double[]{59.523, 42.70, 9.11, 3.041, 99.99});

        //create category titles
        createTitles();

        //initiate google tracking
        buildGoogleApiClient();
        setStatesMap();
        getLocationDataFromGoogle();


        if (fm == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
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

    public void switchBarFilter(boolean which, String tag) {
        if (which) {
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
        } else {
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

    public void pullAreaBar(int area, String tag) {
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

    public void pullTimeBar(int time, String tag) {
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
        IdeaPage ideaPage = new IdeaPage();
        b = new Bundle();
        b.putInt("position", position);
        //bundle values assigned here after querying server
        getFavRatD(username,ib.getNumber(position));
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
        bar_filter_status = new boolean[]{false, false};
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
        bar_filter_status = new boolean[]{false, false};
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
        bar_filter_status = new boolean[]{false, false};
        favoritePage.setArguments(b);
        ft.replace(R.id.current_tab, favoritePage, "byfavorite");
        ft.commit();
    }

    public void searchTabClick() {
        st.setVisibility(View.VISIBLE);
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
     *
     * @param url - URL to connect to
     */
    public void getJSONtoIdeaBlock(String url) {
        aSyncParser = new ASyncParser(url);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                ib.add(jsonObject.getString("title"), jsonObject.getString("descrip"), jsonObject.getString("author"),
                        Integer.parseInt(jsonObject.getString("thumbsup")), Integer.parseInt(jsonObject.getString("thumbsdown")), Integer.parseInt(jsonObject.getString("ididea")),
                        Integer.parseInt(jsonObject.getString("cat")), Integer.parseInt(jsonObject.getString("sub")));
            }
        } catch (JSONException e) {
            Log.println(0, "Error", e.getMessage());
        }
        if (ib.size() == 0) {
            ib.add(":[", "There does not seem to be any ideas here yet! Too bad. Either you have internet connectivitivity issues, or you are in an empty category", "thekarlbrown",
                    0, 999, 1, 8, 2);
        }
    }

    /**
     * Login Query to JSON
     *
     * @param username - Username to attempt login with
     * @param password - Password to attempt login with
     * @return - Boolean representing success of query
     */
    public boolean verifyLogon(String username, String password) {
        try {
            username = URLEncoder.encode(username, "utf-8");
            password = URLEncoder.encode(password, "utf-8");
        }catch (UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.toString());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/user_verifyPHash.php?username=" + username + "&password=" + password);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try {
            int result = Integer.parseInt(jsonArray.getJSONObject(0).getString("verified"));
            if (result == 0) {
                return false;
            } else {
                // do whatever must be done upon successful password verification here if necessary for security
                this.username = username;
                return true;
            }
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
    }

    /**
     * Attempt account creation
     *
     * @param username - Username to be created
     * @param password - Password to be used
     * @param email    - Email to be user
     * @return Boolean based on success of account addition to database
     */
    public boolean verifyCreate(String username, String password, String email) {
        try {
            username = URLEncoder.encode(username, "utf-8");
            password = URLEncoder.encode(password, "utf-8");
            email=URLEncoder.encode(email,"utf-8");
        }catch (UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.toString());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/user_addPHash.php?username=" + username + "&password=" + password + "&email=" + email);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try {
            int result = Integer.parseInt(jsonArray.getJSONObject(0).getString("created"));
            if (result == 0) {
                return false;
            } else {
                // do whatever must be done upon successful account creation here if necessary for security
                this.username = username;
                return true;
            }
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
    }

    /**
     * Adding ideas
     * @param title - Idea title
     * @param description - Idea description
     * @param cat - Category starting at 1
     * @param sub -Subcategory starting at 1
     * @return If idea was successfully queried or not
     */
    public boolean verifyAdd(String title, String description,int cat,int sub) {
        try{
            title= URLEncoder.encode(title,"utf-8");
            description=URLEncoder.encode(description,"utf-8");
        }catch(UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.getMessage());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/idea_addJSON.php?title="+title+ "&username=" + username + "&descrip=" + description + "&cat=" + cat +"&sub="+sub+"&lat="+latitude+"&long="+longitude+"&state="+state+"&country="+country);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try {
            int result = Integer.parseInt(jsonArray.getJSONObject(0).getString("added"));
            if (result == 0) {
                return false;
            } else {
                // do whatever must be done upon successful idea creation here
                return true;
            }
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
    }

    public boolean changeFollow(String follower,int choice) {
        try{
            username= URLEncoder.encode(username,"utf-8");
            follower=URLEncoder.encode(follower,"utf-8");
        }catch(UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.getMessage());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/follower_JSON.php?&username=" + username + "&follower=" + follower + "&case=" + choice);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
        try {
            int result = Integer.parseInt(jsonArray.getJSONObject(0).getString("succeeded"));
            if (result == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            return false;
        }
    }

    /**
     * Get the current favorited, followed, and rated status of the given idea for the user
     * @param username - Username of current user
     * @param ideaid - IdeaID of selected idea
     */
    public void getFavRatD(String username, int ideaid) {
        try{
            username= URLEncoder.encode(username,"utf-8");
        }catch(UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.getMessage());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/favratd_byUserJSON.php?ideaid=" + ideaid + "&username=" + username);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            b.putBoolean("favorite", false);
            b.putBooleanArray("thumbd", new boolean[]{false, false});
            b.putBoolean("followed", false);
        }
        try {
            String result = jsonArray.getJSONObject(0).getString("fav");
            if (result.equals("null")||result.equals("0") ) {
                b.putBoolean("favorite", false);
            } else {
                b.putBoolean("favorite", true);
            }
            result = jsonArray.getJSONObject(0).getString("rated");
            if (result.equals("null") ) {
                b.putBooleanArray("thumbd", new boolean[]{false, false});
            } else if (result.equals("0")){
                b.putBooleanArray("thumbd", new boolean[]{false, true});
            }else{
                b.putBooleanArray("thumbd", new boolean[]{true, false});
            }
            result = jsonArray.getJSONObject(1).getString("followed");
            if (result.equals("0")){
                b.putBoolean("followed", false);
            }else{
                b.putBoolean("followed", true);
            }
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
            b.putBoolean("favorite", false);
            b.putBooleanArray("thumbd", new boolean[]{false, false});
            b.putBoolean("followed", false);
        }
    }

    /**
     * This tool is used to set the selection in the ideapage and update the database accordingly
     * @param username - Username of current user
     * @param ideaid - IdeaID of selected idea
     * @param selection - Selector based on what the user clicked
     */
    public void setFavRatD(String username, int ideaid, int selection) {
        try{
            username= URLEncoder.encode(username,"utf-8");
        }catch(UnsupportedEncodingException e){
            Log.println(0,"Error: ",e.getMessage());
        }
        aSyncParser = new ASyncParser("http://www.thekarlbrown.com/ctwapp/favratdJSON.php?username="+username+"&ideaid="+ideaid+"&case="+selection);
        try {
            aSyncParser.execute();
            jsonArray = aSyncParser.get();
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
        }
        try {
            //TODO: if favorite/follow/rate fails, do I re-attempt rating, revert changes on the user page and alert the user, or alternative
            String result = jsonArray.getJSONObject(0).getString("added");
        } catch (Exception e) {
            Log.println(0, "Error", e.getMessage());
        }
    }


    //Location tracking :3
    /**
     * Get the last location
     */
    public void obtainLocation(){
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latitude=mLastLocation.getLatitude();
        longitude=mLastLocation.getLongitude();
        getMyLocationAddress();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onConnected(Bundle bundle) {
        obtainLocation();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public void getMyLocationAddress() {
        geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
               state = states.get(addresses.get(0).getAdminArea());
               country=addresses.get(0).getCountryCode();
            }else{
                state = "VA";
                country = "US";
            }
        } catch (IOException e) {
            Log.println(0, "Error", e.getMessage());
            state = "VA";
            country = "US";
        }
    }

    /**
     * Get latitude, longitude, state, and country
     */
    public void getLocationDataFromGoogle(){
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    /**\
     * State to abbreviation hashmap
     */
    public void setStatesMap(){
        states = new HashMap<String, String>();
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");
    }
}
