package com.thekarlbrown.changetheworld;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * MainActivity for the Application, which primarily uses Fragments as Tabs to display data
 * Serves as backbone of application, utilizes Google Location API, and uses ASynctask to poll my Web API
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */

public class MainActivity extends Activity implements IdeaDataAdapter.IdeaDataAdapterListener, ProfileTab.ProfileTabListener, IdeaPage.IdeaPageListener, DraftDataAdapter.DraftDataAdapterListener, SearchDialog.SearchDialogListener, RatioDialogue.NoticeRatioDialogListener, ThumbDialogue.NoticeThumbDialogListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String[][] categories = {{"Inventions", "Electronic", "Medical", "Environmental", "Tools", "Machines", "Industrial", "Chemical", "Agricultural", "Instruments", "Build It Yourself", "Other"},
            {"Innovations", "Recipes", "College Hacks", "Thrifty Living", "Manufacturing Techniques", "Going Green", "Home Decor", "Pet Hacks", "Parenting Hacks",
                    "Costume/Cosplay Inspiration", "Common Mistake/Solutions", "Fixing Everyday Issues", "Other"},
            {"Abstract/Concepts/Processes", "Algorithms", "Computer Programs/Mobile Apps", "Economic/Governmental/Social Systems", "Philosophical", "Real World Services",
                    "Internet Services", "Cultural", "Events/Conventions/Meetups", "Party Concepts", "Processes", "Dance Moves", "Other"},
            {"The Suggestion Box", "Government", "Public School", "College", "Economics", "Rules,Regulations, and Laws", "Life Tips", "Other"},
            {"Written/Visual Compositions", "TV Shows", "Movie Plots", "Book Ideas", "Theatre", "Fine Art", "Fashion", "Music", "Other"},
            {"NSFW", "Sexual Maneuvers", "Combat Techniques", "Stealth", "Immoral", "Got It For \"Free\"", "Politically Incorrect", "Other"},
            {"Joke Ideas", "Bad", "Funny", "Inherently Flawed", "Sarcastic", "Absolutely Ridiculous", "What-Ifs", "Other"},
            {"Other", "Doesn't Fit Anywhere", "Suggestions for App", "New Category's/Subcategory", "Bugs Discovered", "Messages to the Creator", "Other"}
    };
    String[] categorytitles;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences sharedPref;
    public SharedPreferences getPref() { return sharedPref;    }
    SplitToolbar st;
    Bundle b;
    List<IdeaBlock> ib, drafts, ibrecent;
    LeaderBlock leaderBlock;
    SearchDialog searchDialog;
    double minratio;
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
    boolean[] bar_filter_status = {false, false, true};
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
    //MergeSort implementation
    MergeSortTopRated mergeSortTopRated;
    //ProgressDialog for various loading activities
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a dialog for the initial google APIs
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setTitle("Loading ChangeTheWorld");
        loadingDialog.show();

        mergeSortTopRated = new MergeSortTopRated();
        setContentView(R.layout.activity_main);
        st = (SplitToolbar) findViewById(R.id.toolbar_top);
        // Choose how we display Toolbar by Calculating DIP then checking to see if the display is a minimum of 450 dip
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float dpWidth = outMetrics.widthPixels / getResources().getDisplayMetrics().density;
        boolean wideDisplay = (dpWidth>450);
        st.inflateMenu(R.menu.menu_navigation,wideDisplay);

        // Set preferences
        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        ib = new ArrayList<>();
        ibrecent=new ArrayList<>();
        //Test Data for the current LeaderBlock
        leaderBlock = new LeaderBlock(new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"}, new String[]{"putin", "obama", "farage", "assad", "kadyrov"},
                new double[]{59.523, 42.70, 9.11, 3.041, 99.99}, new int[]{999, 70, 911, 101, 1337}, new double[]{59.523, 42.69, 9.11, 3.041, 99.99}, new double[]{59.523, 42.70, 9.11, 3.041, 99.99});
        //Create category titles
        createTitles();

        //default tracking data for if we are not using google tracking
        /*
        state="VA";
        country="US";
        latitude=38.957657;
        longitude=-77.175932;
        //*/
        ///*

        //initiate google tracking
        buildGoogleApiClient();
        getLocationDataFromGoogle();
        //*/
        //Set the initial screen
        if (fm == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            ft.add(R.id.current_tab, new InitialScreen(), "initial");
            ft.commit();
        }
    }

    /**
     * Here is my utilization of Google Location API's
     * Get the last location known location whenever the connection occurs successfully
     */
    public void obtainLocation(){
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        latitude=mLastLocation.getLatitude();
        longitude=mLastLocation.getLongitude();
        getMyLocationAddress();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {    }
    @Override
    public void onConnected(Bundle bundle) {
        obtainLocation();
        mGoogleApiClient.disconnect();
        loadingDialog.dismiss();
        //This is a temporary bandaid
        if(st.getVisibility()==View.VISIBLE){  openTrending();    }
    }
    @Override
    public void onConnectionSuspended(int i) {    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    /**
     * Sets the State and Country attributes based on Geolocation data obtained
     */
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
        setStatesMap();
        if(!mGoogleApiClient.isConnected()){  mGoogleApiClient.connect();   }
    }

    /**
     * State to Abbreviation hashmap for my
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

    /**
     *  Fill in the Category titles automatically (used for Category Tab)
     */
    public void createTitles() {
        int length = categories.length;
        categorytitles = new String[length];
        for (int x = 0; x < length; x++) {
            categorytitles[x] = categories[x][0];
        }
    }

    /**
     * Dynamically creates category String[] for Add Tab
     * @return String[] with each possible Category
     */
    public String[] createAddTitles() {
        int temp = categorytitles.length;
        String[] addTitles = new String[temp + 1];
        addTitles[0] = "Select a Category";
        System.arraycopy(categorytitles, 0, addTitles, 1, temp);
        return addTitles;
    }

    /**
     * Set a ratio filter based on earlier input for display
     * @param tofilter - List<IdeaBlock> to be filtered
     */
    public void ratioFilter(List<IdeaBlock> tofilter){
        for(int position=0;position<tofilter.size();position++){
            if(((double)tofilter.get(position).getTup()/(double)tofilter.get(position).getTdown())<minratio){
                tofilter.remove(position);
                position--;
            }
        }
    }

    /**
     * Set a thumb filter based on earlier input for display
     * @param tofilter - List<IdeaBlock> to be filtered
     */
    public void thumbFilter(List<IdeaBlock> tofilter){
        for(int position=0;position<tofilter.size();position++){
            if((tofilter.get(position).getTup()+tofilter.get(position).getTdown())<minthumbs){
                tofilter.remove(position);
                position--;
            }
        }
    }

    /**
     * Sets the onClickListener's for each of the top bar icon's
     */
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
     * Clear all Fragments from the BackStack
     * @param fragmentManager Current FragmentManager to pop Fragments from
     */
    public static void clearFragmentsFromBackStack (FragmentManager fragmentManager) {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) { fragmentManager.popBackStack();  }
    };
    /**
     * Start Fragment for Trending Tab (icon at top)
     */
    public void openTrending() {
        getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byAreaJSON.php?lat=" + latitude + "&long=" + longitude + "&state=" + state + "&country=" + country + "&username=" + username + "&case=2");
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        b = new Bundle();
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status = new boolean[]{false, false, true};
        b.putInt("selecteda", 2);
        trendingTab = new TrendingTab();
        trendingTab.setArguments(b);
        clearFragmentsFromBackStack(fm);
        ft.replace(R.id.current_tab, trendingTab, "trending");
        ft.commit();
    }

    /**
     * Start Fragment for Category Tab (icon at top)
     */
    public void openCategory() {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        b = new Bundle();
        categoryTab = new CategoryTab();
        b.putStringArray("titles", categorytitles);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status = new boolean[]{false, false,true};
        b.putInt("selectedt", 4);
        categoryTab.setArguments(b);
        ft.addToBackStack("fromCategory");
        ft.replace(R.id.current_tab, categoryTab, "category");
        ft.commit();
    }

    /**
     * Start Dialog for Search Tab (icon at top)
     */
    public void openSearch() {
        searchDialog = new SearchDialog();
        searchDialog.show(getFragmentManager(), "search");
    }

    /**
     * Start Fragment for Add Tab (icon at top)
     */
    public void openAdd() {
        if (fm.findFragmentByTag("add") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            addTab = new AddTab();
            b.putStringArray("category", createAddTitles());
            b.putBoolean("draftscoming", false);
            addTab.setArguments(b);
            ft.addToBackStack("fromAdd");
            ft.replace(R.id.current_tab, addTab, "add");
            ft.commit();
        }
    }

    /**
     * Start Fragment for Leaderboard Tab (icon at top)
     */
    public void openTop() {
        if (fm.findFragmentByTag("top") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            b = new Bundle();
            leaderboardTab = new LeaderboardTab();
            b.putInt("selecteda", 0);
            b.putInt("selectedt", 4);
            leaderboardTab.setArguments(b);
            ft.addToBackStack("fromTop");
            ft.replace(R.id.current_tab, leaderboardTab, "top");
            ft.commit();
        }
    }

    /**
     * Start Fragment for Leaderboard Tab (icon at top)
     */
    public void openProfile() {
        if (fm.findFragmentByTag("profile") == null) {
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            profileTab=new ProfileTab();
            b=new Bundle();
            b.putString("username", username);
            profileTab.setArguments(b);
            ft.addToBackStack("fromProfile");
            ft.replace(R.id.current_tab, profileTab, "profile");
            ft.commit();
        }
    }

    /**
     * Listener to start Fragment for Draft Idea Tab
     * @param position Position of Idea in drafts IdeaBlock
     */
    @Override
    public void onDraftListClick(int position) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        b = new Bundle();
        AddTab addT = new AddTab();
        b.putStringArray("category", createAddTitles());
        b.putBoolean("draftscoming", true);
        b.putString("drafttitle", drafts.get(position).getTitle());
        b.putString("draftdescription", drafts.get(position).getIdea());
        b.putInt("draftcategory", drafts.get(position).getCategory());
        b.putInt("draftsubcategory", drafts.get(position).getSubcategory());
        addT.setArguments(b);
        ft.addToBackStack("fromDraftList");
        ft.replace(R.id.current_tab, addT, "add");
        ft.commit();
    }

    /**
     * Listener to start Fragment for Idea Tab
     * @param position Position of Idea in IdeaBlock
     */
    @Override
    public void onIdeaListClick(int position) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        IdeaPage ideaPage = new IdeaPage();
        b = new Bundle();
        b.putInt("position", position);
        //Bundle values assigned here after querying server with getFavRatD
        getFavRatD(username, ib.get(position).getNumber());
        ideaPage.setArguments(b);
        ft.addToBackStack("fromIdeaList");
        ft.replace(R.id.current_tab, ideaPage, "ideapage");
        ft.commit();
    }

    /**
     * Listener to start Fragment for ByUser Tab
     * @param username Username whose ideas will be displayed in a Tab
     */
    @Override
    public void toUserIdeaPage(String username) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        userPage = new ByUserPage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status = new boolean[]{false, false};
        userPage.setArguments(b);
        ft.addToBackStack("fromUserIdeaList");
        ft.replace(R.id.current_tab, userPage, "byuser");
        ft.commit();
    }

    /**
     * Listener to start Fragment for Followers Ideas Tab
     * @param username Username whose followers ideas will be displayed in a Tab
     */
    @Override
    public void toFriendsIdeaPage(String username) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        friendsPage = new ByFriendsPage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status = new boolean[]{false, false};
        friendsPage.setArguments(b);
        ft.addToBackStack("fromFollowerIdeaList");
        ft.replace(R.id.current_tab, friendsPage, "byfriends");
        ft.commit();
    }

    /**
     * Listener to start Fragment for Favorited Ideas Tab
     * @param username Username whose favorited ideas will be displayed
     */
    @Override
    public void toFavoriteIdeaPage(String username) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        favoritePage = new ByFavoritePage();
        b.putString("username", username);
        b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
        bar_filter_status = new boolean[]{false, false};
        favoritePage.setArguments(b);
        ft.addToBackStack("fromFavoriteIdeaList");
        ft.replace(R.id.current_tab, favoritePage, "byfavorite");
        ft.commit();
    }

    /**
     * Listener method to start a Search Tab based on String results from Search Dialog
     * @param r String Query to be sent to PHP Web API
     */
    @Override
    public void onSearchDialogPositiveClick(String r) {
        if (r != null) {
            getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_bySearchJSON.php?search=" + r);
            fm = getFragmentManager();
            ft = fm.beginTransaction();
            searchTab = new SearchTab();
            b = new Bundle();
            b.putString("query", r);
            b.putBooleanArray("selectedf", new boolean[]{false, false, true, false});
            bar_filter_status = new boolean[]{false, false,true};
            searchTab.setArguments(b);
            clearFragmentsFromBackStack(fm);
            ft.replace(R.id.current_tab, searchTab, "search");
            ft.commit();
        } else {
            searchDialog.show(getFragmentManager(), "SearchDialog");
        }
    }

    /**
     * Listener method to change IdeaBlock based on results of Ratio Dialogue positive click
     * @param i Minimum ratio set
     * @param tag Tag that references the Fragment the Dialogue occurred in
     */
    @Override
    public void onRatioDialogPositiveClick(double i, String tag) {
        minratio = i;
        ib.clear();
        ib.addAll(ibrecent);
        ratioFilter(ib);
        if(bar_filter_status[1]){
            thumbFilter(ib);
        }
        if(!bar_filter_status[2]){
            mergeSortTopRated.sort(ib);
        }
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

    /**
     * Listener method to change IdeaBlock based on results of Thumb Dialogue positive click
     * @param i Minimum thumb count set
     * @param tag Tag that references the Fragment the Dialogue occurred in
     */
    @Override
    public void onThumbDialogPositiveClick(int i, String tag) {
        minthumbs = i;
        bar_filter_status[1] = true;
        ib.clear();
        ib.addAll(ibrecent);
        if(bar_filter_status[0]){
            ratioFilter(ib);
        }
        thumbFilter(ib);
        if(!bar_filter_status[2]){
            mergeSortTopRated.sort(ib);
        }
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

    /**
     * Revert the changes made by a bar filter selection upon de-selection
     * @param tag Tag of the fragment Bar resides in
     * @param which_filter True reverts Minimum Ratio, False reverts Minimum Thumbs
     */
    public void revertBarFilter(String tag, boolean which_filter) {
        //Changes applied based on Minimum Ratio now De-Selected
        if (which_filter) {
            minratio = 0;
            bar_filter_status[0] = false;
            ib.clear();
            ib.addAll(ibrecent);
            if(bar_filter_status[1]){
                thumbFilter(ib);
            }
            if(!bar_filter_status[2]){
                mergeSortTopRated.sort(ib);
            }
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
        }//Changes applied based on Minimum Thumbs now De-Selected
        else {
            minthumbs = 0;
            bar_filter_status[1] = false;
            ib.clear();
            ib.addAll(ibrecent);
            if(bar_filter_status[0]){
                ratioFilter(ib);
            }
            if(!bar_filter_status[2]){
                mergeSortTopRated.sort(ib);
            }
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

    /**
     * Changes IdeaBlock based on a new selection of Most Recent vs Top Rated
     * @param which True for Most Recent, False for Top Rated
     * @param tag Tag of the Fragment bar resides in
     */
    public void switchBarFilter(boolean which, String tag) {
        bar_filter_status[2]=which;
        //Changing IdeaBlock based on Most Recent selected
        if (which) {
            ib.clear();
            ib.addAll(ibrecent);
            if(bar_filter_status[0]){
                ratioFilter(ib);
            }
            if(bar_filter_status[1]){
                thumbFilter(ib);
            }
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
        }//Changing IdeaBlock based on Top Rated selected
        else {
            ib.clear();
            ib.addAll(ibrecent);
            if(bar_filter_status[0]){
                ratioFilter(ib);
            }
            if(bar_filter_status[1]){
                thumbFilter(ib);
            }
            mergeSortTopRated.sort(ib);
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

    /**
     * Gets JSON data from the Database and put into IdeaBlock via PHP Web API
     * @param url - URL query for the Web API
     */
    public void getJSONtoIdeaBlock(String url) {
        ibrecent.clear();
        ib.clear();
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
                ib.add(new IdeaBlock(jsonObject.getString("title"), jsonObject.getString("descrip"), jsonObject.getString("author"),
                        Integer.parseInt(jsonObject.getString("thumbsup")), Integer.parseInt(jsonObject.getString("thumbsdown")), Integer.parseInt(jsonObject.getString("ididea")),
                        Integer.parseInt(jsonObject.getString("cat")), Integer.parseInt(jsonObject.getString("sub"))));
            }
        } catch (JSONException e) {
            Log.println(0, "Error", e.getMessage());
        }
        if (ib.size() == 0) {
            ibrecent.add(new IdeaBlock(":[", "There does not seem to be any ideas here yet! Too bad. Either you have internet connectivitivity issues, or you are in an empty category", "thekarlbrown",
                    0, 999, 1, 8, 2));
            ib.add(new IdeaBlock(":[", "There does not seem to be any ideas here yet! Too bad. Either you have internet connectivitivity issues, or you are in an empty category", "thekarlbrown",
                    0, 999, 1, 8, 2));
        }else{
            ibrecent.addAll(ib);
        }
    }

    /**
     * Pull JSON Data from the Database to a Tab based on Tag and Case
     * @param area Case corresponding to the PHP Web API
     * @param tag Tag of the Fragment the Bar is in
     */
    public void pullAreaBar(int area, String tag) {
        //Pull JSON data for each specific Area case. Switch implemented for future leaderboard
        switch (tag) {
            case "trending":
                getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byAreaJSON.php?lat=" + latitude + "&long=" + longitude + "&state=" + state + "&country=" + country + "&username=" + username + "&case=" + area);
                trendingTab.dapt = new IdeaDataAdapter(ib,this);
                trendingTab.l.setAdapter(trendingTab.dapt);
                break;
            case "leaderboard":
                //To be implemented in the future
                break;
            default:
                //This should never be reached
                break;
        }
    }

    /**
     * Pull JSON Data in a Tab based on Time
     * @param time Case corresponding to the PHP Web API
     * @param tag Tag of the Fragment the Bar is in
     */
    public void pullTimeBar(int time, String tag) {
        //Pull JSON data for each specific Time case. Switch implemented for future leaderboard
        switch (tag) {
            case "category":
                getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byCatSubTimeJSON.php?cat=" + (categoryTab.firstCategorySelected + 1) + "&sub=" + categoryTab.subcategorySelected + "&case=" + time);
                categoryTab.dapt = new IdeaDataAdapter(ib,this);
                categoryTab.l.setAdapter(categoryTab.dapt);
                break;
            case "leaderboard":
                //To be implemented in the future
                break;
            default:
                //This should never be reached
                break;
        }
    }

    /**
     * Login Query to Web API
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
     * Attempt account creation via the PHP Web API
     * @param username Username to be created
     * @param password Password to be used
     * @param email  Email to be used
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
     * Adding ideas to the database via the PHP Web API
     * @param title Idea title
     * @param description Idea description
     * @param cat Category starting at 1
     * @param sub Subcategory starting at 1
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

    /**
     * Change if the current user is following a given user or not
     * @param follower User to be potentially following
     * @param choice 0 to remove, 1 to add as a follower
     * @return Boolean indicating if change was a success
     */
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
     * @param username Username of current user
     * @param ideaid IdeaID of selected idea
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
     * This Method updates the database favorite/rated idea status via PHP Web API
     * @param username Username of current user
     * @param ideaid IdeaID of selected idea
     * @param selection Case that will be used with the PHP Web Api
     */
    public void setFavRatD(String username, int ideaid, int selection) {
        try {
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
    }
}
