package org.gtq.vhubs.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.Log;
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
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HistoryMoive;
import org.gtq.vhubs.utils.Function_Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import support.db.XDB;
import support.ui.activity.VBaseActivity;
import support.ui.frt.BaseFrtFactory;
import support.utils.SystemUtils;

public class MainActivity extends VBaseActivity implements AdapterView.OnItemClickListener {

    private ListView mLvLeftMenu;
    DrawerLayout drawer;
    List<Fragment> fragments;
    Toolbar toolbar;

    int select = 0;

    String[] tags = new String[]{"home"
            , "favorites", "history", "setting"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.nav_home);
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

        fragments = new ArrayList<>();
        Fragment home = BaseFrtFactory.createForActivityView(
                HomeActivity.class.getName(), "main");
        Fragment favorites = BaseFrtFactory.createForActivityView(
                FavoritesActivity.class.getName(), "main");
        Fragment history = BaseFrtFactory.createForActivityView(
                HistoryActivity.class.getName(), "main");
        Fragment setting = BaseFrtFactory.createForActivityView(
                SettingActivity.class.getName(), "main");
//        Fragment setting = BaseFrtFactory.createForActivityView(
//                SettingActivity_.class.getName(), "main");
//        Fragment chat = BaseFrtFactory.createForActivityView(
//                ChatActivity_.class.getName(), "main");
        fragments.add(home);
        fragments.add(favorites);
        fragments.add(history);
        fragments.add(setting);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_main, fragments.get(0)).commit();
        stateCheck(savedInstanceState);
        Function_Utility.checkUpdate(this);
    }

    @Override
    protected void setmContentView() {
        setContentView(R.layout.activity_main);
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
        menu.findItem(R.id.action_find).setVisible(true);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.action_clear).setVisible(false);
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
        } else if (id == R.id.action_share) {
            SystemUtils.shareMsg(this,"ddd0", "ddd1", "ddd2", null);
        } else if (id == R.id.action_clear) {
            if (select == 1) {
                //删除喜爱
                createYesNoDialog(this, getString(android.R.string.yes), getString(android.R.string.cancel),
                        getString(R.string.clear_favorites_msg), 0, getString(R.string.clear), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==-1){
                                    XDB.getInstance().deleteAll(FavoritesMoive.class,false);
                                    getSupportFragmentManager().beginTransaction().show(fragments.get(0)).hide
                                            (fragments.get(1)).hide(fragments.get(2)).hide(fragments.get(3)).commit();

                                    getSupportFragmentManager().beginTransaction().show(fragments.get(1)).hide
                                            (fragments.get(0)).hide(fragments.get(2)).hide(fragments.get(3)).commit();
                                }
                            }
                        }).show();
            } else if (select == 2) {
                //删除历史
                createYesNoDialog(this, getString(android.R.string.yes), getString(android.R.string.cancel),
                        getString(R.string.clear_history_msg), 0, getString(R.string.clear), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==-1){
                                    XDB.getInstance().deleteAll(HistoryMoive.class,false);
                                    getSupportFragmentManager().beginTransaction().show(fragments.get(0)).hide
                                            (fragments.get(1)).hide(fragments.get(2)).hide(fragments.get(3)).commit();

                                    getSupportFragmentManager().beginTransaction().show(fragments.get(2)).hide
                                            (fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(3)).commit();
                                }

                            }
                        }).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 状态检测 用于内存不足的时候保证fragment不会重叠
     *
     * @param savedInstanceState
     */
    private void stateCheck(Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
            fts.add(R.id.content_main, fragments.get(0), tags[0]);
            fts.commit();

//            fts.add(R.id.content_main, fragments.get(1),tags[1]);
//            fts.add(R.id.content_main, fragments.get(2),tags[2]);
//            fts.add(R.id.content_main, fragments.get(3),tags[3]);
//            fts.add(R.id.content_main, fragments.get(4),tags[4]);
//            fts.add(R.id.content_main, fragments.get(5),tags[5]);
//            getSupportFragmentManager().beginTransaction().show(fragments.get(0)).hide
//                    (fragments.get(1)).hide(fragments.get(2)).hide(fragments.get(3)).hide(fragments.get(4)).hide
//                    (fragments.get(5)).commit();

        } else {
            Fragment af = (Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[0]);
            Fragment pf = (Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[1]);
            Fragment rf = (Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[2]);
            Fragment inf = (Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[3]);
            getSupportFragmentManager().beginTransaction().show(af).hide(pf).hide(rf)
                    .hide(inf).commit();
        }
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
            select = i - 1;

        }

        switch (i) {
            case 1:
                getSupportFragmentManager().beginTransaction().show(fragments.get(0)).hide
                        (fragments.get(1)).hide(fragments.get(2)).hide(fragments.get(3)).commit();
                toolbar.setTitle(R.string.nav_home);
                toolbar.getMenu().findItem(R.id.action_find).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_share).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_clear).setVisible(false);
                break;
            case 2:
                if (getSupportFragmentManager().findFragmentByTag(tags[1]) != null) {
                    getSupportFragmentManager().beginTransaction().show(fragments.get(1)).hide
                            (fragments.get(0)).hide(fragments.get(2)).hide(fragments.get(3)).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.content_main, fragments.get(1), tags[1]).show(fragments.get(1)).hide
                            (fragments.get(0)).hide(fragments.get(2)).hide(fragments.get(3)).commit();
                }
                toolbar.setTitle(R.string.nav_favorites);
                toolbar.getMenu().findItem(R.id.action_find).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_clear).setVisible(true);
                break;
            case 3:
                if (getSupportFragmentManager().findFragmentByTag(tags[2]) != null) {
                    getSupportFragmentManager().beginTransaction().show(fragments.get(2)).hide
                            (fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(3)).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.content_main, fragments.get(2), tags[2]).show
                            (fragments.get(2)).hide
                            (fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(3)).commit();
                }
                toolbar.setTitle(R.string.nav_history);
                toolbar.getMenu().findItem(R.id.action_find).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_clear).setVisible(true);
                break;
            case 4:
                if (getSupportFragmentManager().findFragmentByTag(tags[3]) != null) {
                    getSupportFragmentManager().beginTransaction().show(fragments.get(3)).hide
                            (fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(2)).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.content_main, fragments.get(3), tags[3]).show
                            (fragments.get(3)).hide
                            (fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(2)).commit();
                }
                toolbar.setTitle(R.string.nav_settings);
                toolbar.getMenu().findItem(R.id.action_find).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_clear).setVisible(false);
                break;

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
            }

            return convertView;
        }


    }
}
