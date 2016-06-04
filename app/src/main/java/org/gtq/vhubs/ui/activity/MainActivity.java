package org.gtq.vhubs.ui.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.gtq.vhubs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import support.ui.frt.BaseFrtFactory;
import support.utils.SystemUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    View status_bar;
    private ListView mLvLeftMenu;
    DrawerLayout drawer;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
//
//        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        mLvLeftMenu = (ListView) findViewById(R.id.nav_lv);

        LayoutInflater inflater = LayoutInflater.from(this);
        mLvLeftMenu.addHeaderView(inflater.inflate(R.layout.nav_header_main, mLvLeftMenu, false));
        mLvLeftMenu.setAdapter(new MenuItemAdapter(this));
        mLvLeftMenu.setOnItemClickListener(this);
        status_bar = findViewById(R.id.status_bar);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) status_bar.getLayoutParams();
        lp.height = SystemUtils.getStatusBarHeight(this);
        status_bar.setLayoutParams(lp);

        fragments = new ArrayList<>();
        Fragment home = BaseFrtFactory.createForActivityView(
                HomeActivity.class.getName(), "main");
//        Fragment agreement = BaseFrtFactory.createForActivityView(
//                AgreementActivity_.class.getName(), "main");
//        Fragment burse = BaseFrtFactory.createForActivityView(
//                BurseActivity_.class.getName(), "main");
//        Fragment record = BaseFrtFactory.createForActivityView(
//                RecordActivity_.class.getName(), "main");
//        Fragment setting = BaseFrtFactory.createForActivityView(
//                SettingActivity_.class.getName(), "main");
//        Fragment chat = BaseFrtFactory.createForActivityView(
//                ChatActivity_.class.getName(), "main");
        fragments.add(home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, fragments.get(0)).commit();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            SearchActivity.Launch(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        for (int p = 0; p < mLvLeftMenu.getChildCount(); p++) {
            View child = mLvLeftMenu.getChildAt(p);
            if (child != null) {
                child.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        }
        if (i - 1 >= 0) {
            view.setBackgroundColor(getResources().getColor(R.color.nav_item_p));
            drawer.closeDrawer(GravityCompat.START);
        }

    }


    private static class LvMenuItem {
        public LvMenuItem(int icon, String name) {
            this.icon = icon;
            this.name = name;

            if (icon == NO_ICON && TextUtils.isEmpty(name)) {
                type = TYPE_SEPARATOR;
            } else if (icon == NO_ICON) {
                type = TYPE_NO_ICON;
            } else {
                type = TYPE_NORMAL;
            }

            if (type != TYPE_SEPARATOR && TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("you need set a name for a non-SEPARATOR item");
            }


        }

        public LvMenuItem(String name) {
            this(NO_ICON, name);
        }

        public LvMenuItem() {
            this(null);
        }

        private static final int NO_ICON = 0;
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_NO_ICON = 1;
        public static final int TYPE_SEPARATOR = 2;

        int type;
        String name;
        int icon;

    }

    private static class MenuItemAdapter extends BaseAdapter {
        private final int mIconSize;
        private LayoutInflater mInflater;
        private Context mContext;
        private List<LvMenuItem> mItems;

        public MenuItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mIconSize = mContext.getResources().getDimensionPixelSize(R.dimen.nav_icon_w);
            mItems = new ArrayList<LvMenuItem>(
                    Arrays.asList(
                            new LvMenuItem(R.mipmap.home, mContext.getResources().getString(R.string.nav_home)),
                            new LvMenuItem(R.mipmap.favorites, mContext.getResources().getString(R.string.nav_favorites)),
                            new LvMenuItem(R.mipmap.history, mContext.getResources().getString(R.string.nav_history)),
                            new LvMenuItem(R.mipmap.settings, mContext.getResources().getString(R.string.nav_settings))
                    ));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }


        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LvMenuItem item = mItems.get(position);
            switch (item.type) {
                case LvMenuItem.TYPE_NORMAL:
                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.nav_item, parent,
                                false);
                    }
                    TextView itemView = (TextView) convertView.findViewById(R.id.nav_tv);
                    itemView.setText(item.name);
                    ImageView iv = (ImageView) convertView.findViewById(R.id.nav_iv);
                    iv.setImageResource(item.icon);
                    break;
//                case LvMenuItem.TYPE_NO_ICON:
//                    if (convertView == null)
//                    {
//                        convertView = mInflater.inflate(R.layout.design_drawer_item_subheader,
//                                parent, false);
//                    }
//                    TextView subHeader = (TextView) convertView;
//                    subHeader.setText(item.name);
//                    break;
//                case LvMenuItem.TYPE_SEPARATOR:
//                    if (convertView == null)
//                    {
//                        convertView = mInflater.inflate(R.layout.design_drawer_item_separator,
//                                parent, false);
//                    }
//                    break;
            }

            return convertView;
        }


    }
}
