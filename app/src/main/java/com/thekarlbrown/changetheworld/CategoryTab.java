package com.thekarlbrown.changetheworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Arrays;


/**
 * Category Tab that allows filtering of ideas by category
 * By Karl Brown ( thekarlbrown ) 2nd June 2015
 */
public class CategoryTab extends Fragment {

    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    MainActivity mainActivity;
    int firstCategorySelected;
    int subcategorySelected;
    FragmentManager fm;
    FragmentTransaction ft;
    BarFilter barFilter=new BarFilter();
    BarTime barTime=new BarTime();
    boolean[]selectedf;
    int selectedt;

    //To expand Drawers
    DrawerLayout mDrawerLayout;
    String[][]categoryarray;
    String[]categoryarraytitles;
    ListView mDrawerList;

    public static CategoryTab newInstance() {
        CategoryTab fragment = new CategoryTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryarraytitles=getArguments().getStringArray("titles");
            selectedf=getArguments().getBooleanArray("selectedf");
            selectedt=getArguments().getInt("selectedt");
        }
        mainActivity=(MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryarray=mainActivity.categories;
        rv = inflater.inflate(R.layout.fragment_category_tab, container, false);
        fm=getFragmentManager();
        barFilter.barFilterClick(rv,fm,selectedf,getTag(),mainActivity);
        barTime.barTimeClick(rv,selectedt,getTag(),mainActivity);
        //Create the drawer
        mDrawerLayout=(DrawerLayout)rv.findViewById(R.id.drawer_category);
        mDrawerList=(ListView)rv.findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,R.layout.category_drawer_container,categoryarraytitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.openDrawer(mDrawerList);
        return rv;
    }

    /**
     * Actions performed when item on Bar Filter is pressed
     * @param i Item selected (starting at 0)
     */
    public void filterSelected(int i) {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }

    //Implement onItemClickListeners for the Drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            firstCategorySelected=(position);
            mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity, R.layout.category_drawer_container, Arrays.copyOfRange(categoryarray[position], 1, categoryarray[position].length)));
            mDrawerList.setOnItemClickListener(new CategoryItemClickListener());
        }
    }
    //Implement onItemClickListeners for the Drawers Contents dynamically based on user selection
    private class CategoryItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            t=(TextView)getView().findViewById(R.id.category_query);
            t.setText("Current Section: " + categoryarray[firstCategorySelected][0] + " -> " + categoryarray[firstCategorySelected][subcategorySelected]);
        }

        /**
         * Do a JSON query for category/subcategorySelected based on selection for Idea Block, close Drawers and set new Listeners
         * @param position Position on the list, 1 less than the actual category/subcategorySelected
         */
        private void selectItem(int position) {
            subcategorySelected=(position+1);
            mainActivity.getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byCatSubTimeJSON.php?cat=" + (firstCategorySelected+1) + "&sub=" + subcategorySelected +"&case=4");
            dapt = new IdeaDataAdapter(mainActivity.ib, mainActivity);
            l = (ListView) rv.findViewById(R.id.category_list);
            l.setAdapter(dapt);
            mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,R.layout.category_drawer_container,categoryarraytitles));
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }
    }
}
