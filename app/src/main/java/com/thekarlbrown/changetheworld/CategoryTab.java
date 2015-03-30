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
 * This tab allows filtering of ideas by category
 * is calling (MainActivity)getActivity bad?
 * get the horizontal/vertical change working

 */
public class CategoryTab extends Fragment {

    View rv;
    TextView t;
    ListView l;
    DataAdapter dapt;
    MainActivity mainActivity;
    //drawer expander
    DrawerLayout mDrawerLayout;
    String[][]categoryarray;
    String[]categoryarraytitles;
    ListView mDrawerList;
    int prefratio;
    int prefminimum;
    int categoryfirst;
    int categorysecond;
    FragmentManager fm;
    FragmentTransaction ft;
    BarFilter barFilter=new BarFilter();
    BarTime barTime=new BarTime();
    boolean[]selectedf;
    int selectedt;
    public static CategoryTab newInstance() {
        CategoryTab fragment = new CategoryTab();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    public CategoryTab() {
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

        mDrawerLayout=(DrawerLayout)rv.findViewById(R.id.drawer_category);
        mDrawerList=(ListView)rv.findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,R.layout.category_drawer_container,categoryarraytitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.openDrawer(mDrawerList);

        fm=getFragmentManager();
        barFilter.barFilterClick(rv,fm,selectedf,getTag(),mainActivity);
        barTime.barTimeClick(rv,selectedt,getTag(),mainActivity);
        return rv;
    }
    public void filterSelected(int i)
    {
        selectedf[i]=true;
        barFilter.setSelected(i);
        dapt.notifyDataSetChanged();
    }

    // The click listener for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            categoryfirst=(position);
            mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity, R.layout.category_drawer_container, Arrays.copyOfRange(categoryarray[position], 1, categoryarray[position].length)));
            mDrawerList.setOnItemClickListener(new CategoryItemClickListener());
        }
    }
    private class CategoryItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            t=(TextView)getView().findViewById(R.id.category_query);
            t.setText("Current Section: " + categoryarray[categoryfirst][0] + " -> " + categoryarray[categoryfirst][categorysecond]);
        }
        private void selectItem(int position)
        {
            categorysecond=(position+1);
            mainActivity.getJSONtoIdeaBlock("http://www.thekarlbrown.com/ctwapp/ideas_byCatSubTimeJSON.php?cat=" + (categoryfirst+1) + "&sub=" + categorysecond +"&case=4");
            dapt = new IdeaDataAdapter(mainActivity.ib, mainActivity);
            l = (ListView) rv.findViewById(R.id.category_list);
            l.setAdapter(dapt);
            //mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerList.setAdapter(new ArrayAdapter<>(mainActivity,R.layout.category_drawer_container,categoryarraytitles));
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        }
    }
}
