package com.videodemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.ui.base.BaseAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.ui.adapter.XFragmentAdapter;
import org.gtq.vhubs.ui.fragment.MoreMoiveFragment;
import org.gtq.vhubs.ui.fragment.TrailerFragment;
import org.gtq.vhubs.utils.DBUtil;
import org.gtq.vhubs.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import support.db.XDB;
import support.utils.SystemUtils;


public class FilmDetailsAct extends BaseAct implements
        View.OnClickListener, SensorEventListener, ScreenObserver.ScreenStateListener {

    ScreenObserver observer;
    private final String mPageName = "FilmDetailsAct";

    // 播放器视图
    private RelativeLayout rtlVideo;
    private FrameLayout fltController;
    private FrameLayout titleController;
    private FrameLayout rechageController;
    private LinearLayout lltLoading;
    private TextView tvNetSpeed;// 网速
    private VideoView videoView;
    private long old_duration = 0;
    private RelativeLayout rltPortrait, rltLandScape;

    // mini播放器相关
    private SeekBar seekBar_time = null;// mini播放器进度条
    private ImageButton btnPlayOrPausePortrait;
    private ImageButton btnFullScreen;
    private ImageButton btnPlay;// 网络状态按钮
    private TextView tvPlayTime;
    private TextView tvAllTime;
    private AlertDialog diaNotice;// 播放失败提示

    // 全屏播放器相关
    private SeekBar full_seekBar_time = null;// 全屏播放器
    private ImageButton btnPlayOrPauseLandscape;
    private ImageButton btnPlayBackward;
    private ImageButton btnPlayForward;
    private ImageButton btnSilence, btnQuitFullScreen;
    private TextView tvFullPlayTime;
    private TextView tvFullAllTime;

    // 标题栏视图
    private RelativeLayout mTitleView = null;
    private ImageButton btnBack;
    private ImageButton btnCollect;
    private ImageButton btnShare;
    private TextView tvwNaviTitle;

    // DRM加密播放器
//    private PlaylistProxy playerProxy;

    // 详情页视图
//    private RelativeLayout ileFilmPager;
    private ViewPager vprRelateFilm;

    // 全屏播放器快进视图
    private View fastforwardView;
    protected PopupWindow fastforwardWindow;

    // 全屏播放器音量视图
    private View mSoundView = null;
    private PopupWindow mSoundWindow = null;
    private VoiceSeekBar skbVolume;

    // 全屏播放器渠道选择
    private PopupWindow selectChannelWindow = null;
    private Button btnFilmNormal;
    private Button btnFilmHigh;
    private Button btnFilmSuper;
    private String tempUrl = "";
    private String playSoonUrl = "";
    private String playHighUrl = "";
    private String playSuperUrl = "";

    // 是否缓冲
    private int CHANGE_URL_TIME = 0;
    private boolean CHANGE_URL = false;

    // 初始化 拖放时间、是否滑动视频
    private static int totalTime;
    private boolean isSeekTo = false;
    private boolean isBrightness = false;
    private int seekTime = 0;
    private boolean isScrollVoice = false;

    // 横竖屏切换时，保存切换前的
    private boolean isPlayingSave = true;
    private boolean isGetVideo = true;// 判断是否获取到视频大小

    // 视频是否能播放
    private boolean playAble = false;

    // 视频播放模式切换(是否使用DRM加密)
    private boolean isUseDRM = false;

    // 媒体音量控制
    private AudioManager mAudioManager;

    /**
     * 当前亮度
     */
    private float mBrightness = -1f;

    // 最大音量，当前音量, 保存音量
    private int maxVolume, currentVolume, saveCurrentVolume;

    // 控制器显示和隐藏时间
    private Timer timer;
    private TimerTask timerTask;

    private int startPlayTime = 0;
    private long historyTime = 0l;
    private String str_min = "";
    private String str_sec = "";
    private int MOVIE_TIME = 0;

    // 传感器判断重力方向
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private int rotateTime = 999;
    private RotationObserver mRotationObserver;
    private boolean isSensor = false;
    private int saveSensor = 1;

    // 保持屏幕常亮
    KeyguardManager mKeyguardManager = null;
    KeyguardManager.KeyguardLock mKeyguardLock = null;

    // 手势
    private GestureDetector mGestureDetector = null;

    // 屏幕
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    // 提示信息
    private String str_prompt_messag;

    private String url = "http://124.14.6.32/v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4?wshc_tag=0&wsts_tag=5753057e&wsid_tag=ec47cce&wsiphost=ipdbm";

    private String id;

    public static void Launch(Activity activity, String moive_id, String url) {
        Intent intent = new Intent(activity, FilmDetailsAct.class);
        intent.putExtra("id", moive_id);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    protected RelativeLayout tab_0;
    protected RelativeLayout tab_1;
    protected ImageView select_iv0;
    protected ImageView select_iv1;
    TextView watch_time;
    ImageView favorites;
    ImageView share;
    TrailerFragment trailer;
    MoreMoiveFragment more;

    protected ArrayList<Fragment> fragmentsList;

    HMoiveItem currentMoive;

    boolean isFavorites = false;

    private void initPager() {
        tab_0 = (RelativeLayout) findViewById(R.id.tab_0);
        tab_1 = (RelativeLayout) findViewById(R.id.tab_1);
        vprRelateFilm = (ViewPager) findViewById(R.id.vprRelateFilm);
        select_iv0 = (ImageView) findViewById(R.id.select_iv0);
        select_iv1 = (ImageView) findViewById(R.id.select_iv1);
        favorites = (ImageView) findViewById(R.id.favorites);

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentMoive != null) {
                    if (isFavorites) {
                        favorites.setImageResource(R.mipmap.favorites);
                        FavoritesMoive favoritesMoive = new FavoritesMoive(currentMoive, 0);
                        XDB.getInstance().delete(favoritesMoive, false);
                    } else {
                        favorites.setImageResource(R.mipmap.shoucang2);
                        DBUtil.addToFavorites(currentMoive);
                    }
                    isFavorites = !isFavorites;
                }
            }
        });
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMoive != null) {
                    SystemUtils.shareMsg(FilmDetailsAct.this, "ddd0", "ddd1", "ddd2", null);
                }
            }
        });
        watch_time = (TextView) findViewById(R.id.watch_time);

        // Fragment容器
        fragmentsList = new ArrayList<Fragment>();
        // 生成每个tab对应的fragment
        trailer = new TrailerFragment();
        more = new MoreMoiveFragment();

        fragmentsList.add(trailer);
        fragmentsList.add(more);
        // 给ViewPager添加适配器
        vprRelateFilm.setAdapter(new XFragmentAdapter(
                getSupportFragmentManager(), fragmentsList));
        // 设置默认的视图为第1个
        vprRelateFilm.setCurrentItem(0);
        // 给Viewpager添加监听事件
        vprRelateFilm.setOnPageChangeListener(initOnPageChangeListener());
        tab_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vprRelateFilm.setCurrentItem(0);
            }
        });
        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vprRelateFilm.setCurrentItem(1);
            }
        });
        select_iv0.getBackground().setAlpha(255);
        select_iv1.getBackground().setAlpha(0);

    }

    protected ViewPager.OnPageChangeListener initOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        select_iv0.getBackground().setAlpha((int) (255 * (1 - positionOffset)));
                        select_iv1.getBackground().setAlpha((int) (255 * positionOffset));
                        break;
                    case 1:
                        select_iv0.getBackground().setAlpha((int) (255 * positionOffset));
                        select_iv1.getBackground().setAlpha((int) (255 * (1 - positionOffset)));

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void loadMoiveMsgForNet() {
        if (TextUtils.isEmpty(id)) {
            return;
        } else {
            Observable.just("").doOnSubscribe(new Action0() {
                @Override
                public void call() {

                }
            }).observeOn(Schedulers.io())
                    .map(new Func1<String, JSONObject>() {
                        @Override
                        public JSONObject call(String s) {


                            try {
                                return new JSONObject(HttpUtils.doPost("movice", "detail", id));
                            } catch (Exception e) {
                                VApplication.toast(getString(R.string.net_fail));
                            }
                            return null;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject bean) {

                            try {
                                if (bean.getInt("code") != 0) {
                                    VApplication.toastJsonError(bean);
                                } else {
                                    JSONArray jsonArray = bean.getJSONObject("data").getJSONArray("imgs");
                                    List<String> urls = new ArrayList<String>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        urls.add(jsonArray.getString(i));
                                    }
                                    trailer.setUrls(urls);
                                    currentMoive = JSON.parseObject(bean.getJSONObject("data").toString(), HMoiveItem.class);
                                    if (!TextUtils.isEmpty(currentMoive.getCategory_id())) {
                                        more.setCategory_id(currentMoive.getCategory_id());
                                    }
                                    if (!TextUtils.isEmpty(currentMoive.getName())) {
                                        tvwNaviTitle.setText(currentMoive.getName());
                                    }
                                    watch_time.setText(currentMoive.getShow_play_time());
                                    DBUtil.addToHistory(currentMoive);
                                    isFavorites = XDB.getInstance().readById(currentMoive.getmId(), FavoritesMoive.class, false) != null;
                                    if (isFavorites) {
                                        favorites.setImageResource(R.mipmap.shoucang2);
                                    } else {
                                        favorites.setImageResource(R.mipmap.favorites);
                                    }
                                }
                            } catch (JSONException e) {
                                VApplication.toast(getString(R.string.net_fail));
                            }


                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        url=getIntent().getStringExtra("url");


        id = getIntent().getStringExtra("id");

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.nor_black));

        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        observer = new ScreenObserver(FilmDetailsAct.this);
        observer.requestScreenStateUpdate(FilmDetailsAct.this);
        setContentView(R.layout.act_film_details);
        instance();
        initPager();
        loadMoiveMsgForNet();
    }

    private void instance() {
        instanceOther();
        initView();
        // initDRMPlayer();
    }

    private void instanceOther() {
        getScreenSize();
        startService();
        initSensor();
    }

    private void initView() {
        initMiniPlayer();
        loadPortraitController();
        initVideoView();
        initFastForward();
        initTitle();
        initVoice();
        playVideo(url, false);
    }

    /**
     * 启动服务监听网速和禁止锁屏回来出现手动解锁
     */
    private void startService() {
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOrientationSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 创建观察类对象
        mRotationObserver = new RotationObserver(new Handler());
    }

    private void initMiniPlayer() {
        rtlVideo = (RelativeLayout) findViewById(R.id.rtlVideo);
        lltLoading = (LinearLayout) findViewById(R.id.lltLoading);
        tvNetSpeed = (TextView) findViewById(R.id.tvNetSpeed);
        fltController = (FrameLayout) findViewById(R.id.fltController);
        titleController = (FrameLayout) findViewById(R.id.titleController);
        rechageController = (FrameLayout) findViewById(R.id.rechangeController);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
    }

    private void playVideo(String playUrl, boolean isDRM) {
        // isDRM = true;
        // playUrl = "http://hls.vodfile.m1905.com/2014/07/F2014070081.m3u8";
        // playUrl = "http://hls.vodfile.m1905.com/ProtectedHls/hls.m3u8";
        // "ms3://ms3.eval.hostedmarlin.com:8443/hms/ms3/rights/?b=ABoAAwAAAyAAEGV4cHJlc3NwbGF5LWRlbW9HQwAQMKYT0XOfRgOjyrU6Wk2uCwBQkCGoEsLP-QWdLeqBYm9Zkbvd_GqJ4oZ8S0HTq7-nDZoiVmtJ2HXJ8WEutbhkCjwMM-5pDhHOgEdXAq6UxqQJj8MjEAu5w-OxtR_lh0WN3dwAAAAU4PsFf6--GAvIZe5owzeRVpsbETI#hls%3A%2F%2Fexpressplay.s3.amazonaws.com%2Fvideo%2FHLSBBB%2Fmaster.m3u8";
        if (!StringUtils.isEmpty(playUrl)) {
            LogUtils.e("***********>" + playUrl);
            videoView.setVideoURI(Uri.parse(playUrl));
            videoView.start();

        } else {
            AppUtils.toastShowMsg(FilmDetailsAct.this, "播放地址无效");
        }

    }

    private void initVideoView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setKeepScreenOn(true);
        videoView.requestFocus();
        videoView.setFocusable(true);
        videoView.setFocusableInTouchMode(true);

        historyTime = startPlayTime / 1;
        if (startPlayTime > 0) {
            int start = startPlayTime / 1;
            int min = start / 60;
            int sec = start % 60;
            str_min = min < 10 ? "0" + min : min + "";
            str_sec = sec < 10 ? "0" + sec : sec + "";
        }
        if (startPlayTime > 0) {
            str_prompt_messag = "即将续播" + ",\n上次播放至" + str_min
                    + ":" + str_sec;
        } else {
            str_prompt_messag = "即将播放";
        }
        tvNetSpeed.setText(str_prompt_messag);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (fltController.getVisibility() == View.VISIBLE
                            && titleController.getVisibility() == View.VISIBLE) {
                        fltController.setVisibility(View.GONE);
                        titleController.setVisibility(View.GONE);
                    } else if (fltController.getVisibility() == View.GONE
                            && titleController.getVisibility() == View.GONE) {
                        fltController.setVisibility(View.VISIBLE);
                        titleController.setVisibility(View.VISIBLE);
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            toggleBtnPlayOrPause(false);
                        } else {
                            toggleBtnPlayOrPause(true);
                        }
                        if (rltLandScape != null) {
                            currentVolume = mAudioManager
                                    .getStreamVolume(AudioManager.STREAM_MUSIC);
                            skbVolume.setProgress(currentVolume);
                        }
                        delayHideController();
                    }
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    boolean result = false;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        videoView.requestFocus();
                        result = mGestureDetector.onTouchEvent(motionEvent);
                    }
                    return result;
                }
                return true;
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (fltController.getVisibility() == View.VISIBLE
                            && titleController.getVisibility() == View.VISIBLE) {
                        fltController.setVisibility(View.GONE);
                        titleController.setVisibility(View.GONE);
                    } else if (fltController.getVisibility() == View.GONE
                            && titleController.getVisibility() == View.GONE) {
                        fltController.setVisibility(View.VISIBLE);
                        titleController.setVisibility(View.VISIBLE);
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            toggleBtnPlayOrPause(false);
                        } else {
                            toggleBtnPlayOrPause(true);
                        }
                        if (rltLandScape != null) {
                            currentVolume = mAudioManager
                                    .getStreamVolume(AudioManager.STREAM_MUSIC);
                            skbVolume.setProgress(currentVolume);
                        }
                        delayHideController();
                    }
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    boolean result = false;
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        // videoView.requestFocus();
                        result = mGestureDetector.onTouchEvent(motionEvent);
                    }
                    return result;
                }
                return true;
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // 添加统计
                playAble = false;
                videoView.stopPlayback();
                handler.removeCallbacks(runnable);
                lltLoading.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                popupDiaNotice();
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                btnPlay.setVisibility(View.GONE);
                totalTime = (int) videoView.getDuration();
                seekBar_time.setMax(totalTime);
                // seekBar_time.setProgress(mActiveCloakMediaPlayer.getCurrentPosition());
                seekBar_time.setProgress((int) videoView.getCurrentPosition());
                int i = (int) videoView.getDuration();
                i /= 1000;
                int minute = i / 60;
                int hour = minute / 60;
                int second = i % 60;
                minute %= 60;
                tvAllTime.setText(String.format("%02d:%02d:%02d", hour, minute,
                        second));
                handler.post(runnable);
                handler.post(run);
                if (observer.isScreenOn((PowerManager) FilmDetailsAct.this
                        .getSystemService(Activity.POWER_SERVICE))) {
                    mediaPlayer.start();
                }
                playAble = true;
                if (observer.isScreenOn((PowerManager) FilmDetailsAct.this
                        .getSystemService(Activity.POWER_SERVICE))) {
                    videoView.start();
                }
                if (CHANGE_URL) {
                    if (CHANGE_URL_TIME != 0) {
                        videoView.seekTo(CHANGE_URL_TIME);
                        if (!observer
                                .isScreenOn((PowerManager) FilmDetailsAct.this
                                        .getSystemService(Activity.POWER_SERVICE)))
                            videoView.pause();
                    }
                    CHANGE_URL = false;
                } else if (startPlayTime > 0) {
                    videoView.seekTo(startPlayTime * 1000);
                    if (!observer.isScreenOn((PowerManager) FilmDetailsAct.this
                            .getSystemService(Activity.POWER_SERVICE)))
                        videoView.pause();

                }

                lltLoading.setVisibility(View.GONE);
                // fltController.setVisibility(View.VISIBLE);
                if (btnPlayOrPausePortrait != null) {
                    btnPlayOrPausePortrait.setClickable(true);
                    btnPlayOrPausePortrait
                            .setImageResource(R.drawable.selector_btn_par_pause);
                }
                if (btnPlayOrPauseLandscape != null) {
                    btnPlayOrPauseLandscape.setClickable(true);
                    btnPlayOrPauseLandscape
                            .setImageResource(R.drawable.selector_btn_lan_pause);
                }
                isSensor = true;
                // 启动重力感应
                mSensorManager.registerListener(FilmDetailsAct.this,
                        mOrientationSensor, SensorManager.SENSOR_DELAY_UI);
                mRotationObserver.startObserver();
                if (videoView != null) {
                    double videoWidth = (double) videoView.getVideoWidth();
                    double videoHeight = (double) videoView.getVideoHeight();
                    if (videoWidth != 0.0 && videoHeight != 0.0 && isGetVideo) {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            setVideoSize(screenHeight, screenWidth);
                        } else {
                            setVideoSize(screenWidth, screenHeight);
                        }
                    }
                }
                mHandler.sendEmptyMessage(4);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (rechageController != null) {
                    rechageController.setVisibility(View.GONE);
                }
                if (lltLoading != null) {
                    lltLoading.setVisibility(View.GONE);
                }

                if (videoView != null) {
                    videoView.seekTo(0);
                    videoView.stopPlayback();
                    handler.removeCallbacks(runnable);
                    playAble = false;
                    isPlayingSave = false;
                }

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rotateTime = 0;
                }

            }
        });

    }

    private void initFastForward() {
        // 时间快进后退
        fastforwardView = View.inflate(FilmDetailsAct.this,
                R.layout.play_time_fastforward, null);
        fastforwardWindow = new PopupWindow(fastforwardView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initTitle() {
        if (mTitleView == null) {
            mTitleView = (RelativeLayout) LayoutInflater.from(
                    FilmDetailsAct.this).inflate(R.layout.navi_top, null);
            mTitleView.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, getResources()
                    .getDimensionPixelSize(R.dimen.player_title_h)));
            btnBack = (ImageButton) mTitleView.findViewById(R.id.btnBack);
            btnCollect = (ImageButton) mTitleView.findViewById(R.id.btnCollect);
            btnShare = (ImageButton) mTitleView.findViewById(R.id.btnShare);
            tvwNaviTitle = (TextView) mTitleView
                    .findViewById(R.id.tvwNaviTitle);
//            tvwNaviTitle.setText(title);
            titleController.removeAllViews();
            titleController.addView(mTitleView);
            btnBack.setOnClickListener(this);
            btnCollect.setOnClickListener(this);
            btnShare.setOnClickListener(this);
            tvwNaviTitle.setOnClickListener(this);
            mTitleView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    if (mHandler.hasMessages(1)) {
                        mHandler.removeMessages(1);
                    }
                    delayHideController();
                }
            });
        }

    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
//            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        isBrightness = true;
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
//        mOperationPercent.setLayoutParams(lp);
    }


    private void initVoice() {
        // 声音
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        mSoundView = View.inflate(this, R.layout.full_box_sound_change, null);
        mSoundWindow = new PopupWindow(mSoundView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mSoundWindow.setFocusable(false);
        mSoundWindow.setOutsideTouchable(true);
        skbVolume = (VoiceSeekBar) mSoundView.findViewById(R.id.skbVolume);
        skbVolume.setMax(maxVolume);
        skbVolume.setProgress(currentVolume);
        skbVolume
                .setOnSeekBarChangeListener(new VoiceSeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(VoiceSeekBar VerticalSeekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(
                            VoiceSeekBar VerticalSeekBar) {
                        // 记录触球前的音量
                        saveCurrentVolume = VerticalSeekBar.getProgress();
                    }

                    @Override
                    public void onProgressChanged(VoiceSeekBar VerticalSeekBar,
                                                  int progress, boolean fromUser) {
                        delayHideController();
                        updateVolume(progress);
                    }
                });

    }

    // 处理各种消息的Handler
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 隐藏控制器
                fltController.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (fastforwardWindow.isShowing()) {
                        fastforwardView.setVisibility(View.INVISIBLE);
                        fastforwardWindow.dismiss();
                    }
                    if (mSoundWindow.isShowing()) {
                        mSoundView.setVisibility(View.INVISIBLE);
                        mSoundWindow.dismiss();
                    }
                }
                titleController.setVisibility(View.GONE);

            } else if (msg.what == 2) { // 暂停
                videoView.pause();
            } else if (msg.what == 3) { // 隐藏购买提示
                if (rechageController != null) {
                    rechageController.setVisibility(View.GONE);
                }
            } else if (msg.what == 4) {

                int i = 0;
                int duration = 0;
                i = (int) videoView.getCurrentPosition();
                duration = (int) videoView.getDuration();
                if (i > duration) {
                    i = duration;
                }
                if (videoView != null) {
                    double videoWidth = (double) videoView.getVideoWidth();
                    double videoHeight = (double) videoView.getVideoHeight();
                    if (videoWidth != 0.0 && videoHeight != 0.0 && isGetVideo) {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            setVideoSize(screenHeight, screenWidth);
                        } else {
                            setVideoSize(screenWidth, screenHeight);
                        }
                        isGetVideo = false;
                    }
                }

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    full_seekBar_time.setProgress(i);
                    i /= 1000;
                    int minute = i / 60;
                    int hour = minute / 60;
                    int second = i % 60;
                    minute %= 60;
                    tvFullPlayTime.setText(String.format("%02d:%02d:%02d",
                            hour, minute, second));

                    totalTime = (int) videoView.getDuration();
                    full_seekBar_time.setMax(totalTime);
                    totalTime /= 1000;
                    int minute1 = totalTime / 60;
                    int hour1 = minute1 / 60;
                    int second1 = totalTime % 60;
                    minute1 %= 60;
                    tvFullAllTime.setText(String.format("%02d:%02d:%02d",
                            hour1, minute1, second1));

                    sendEmptyMessageDelayed(4, 100);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    seekBar_time.setProgress(i);
                    i /= 1000;
                    int minute = i / 60;
                    int hour = minute / 60;
                    int second = i % 60;
                    minute %= 60;
                    tvPlayTime.setText(String.format("%02d:%02d:%02d", hour,
                            minute, second));

                    // totalTime = videoView.getDuration();

                    totalTime = (int) videoView.getDuration();

                    seekBar_time.setMax(totalTime);
                    totalTime /= 1000;
                    int minute1 = totalTime / 60;
                    int hour1 = minute1 / 60;
                    int second1 = totalTime % 60;
                    minute1 %= 60;
                    tvAllTime.setText(String.format("%02d:%02d:%02d", hour1,
                            minute1, second1));
                    sendEmptyMessageDelayed(4, 100);
                }

            } else if (msg.what == 5) {
                // 隐藏快进
                fastforwardView.setVisibility(View.INVISIBLE);
                fastforwardWindow.dismiss();
            } else if (msg.what == 6) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            super.handleMessage(msg);
        }
    };

    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            long duration = videoView.getCurrentPosition();
            if (old_duration == duration && videoView.isPlaying()) {
                str_prompt_messag = "加载中...";
                tvNetSpeed.setText(str_prompt_messag);
                lltLoading.setVisibility(View.VISIBLE);
            } else {
                lltLoading.setVisibility(View.GONE);
            }
            old_duration = duration;

            handler.postDelayed(runnable, 500);
        }
    };

    private Runnable run = new Runnable() {
        public void run() {

            int bufferTime = 0;

            bufferTime = (int) (((float) videoView.getBufferPercentage() / 100.0) * totalTime);

            // int bufferTime = videoView.get
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (videoView.getBufferPercentage() <= 100) {
                    // 设置进度条的次要进度，表示视频的缓冲进度
                    if (full_seekBar_time != null) {
                        full_seekBar_time
                                .setSecondaryProgress(bufferTime * 1000);
                    }

                }

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                if (videoView.getBufferPercentage() <= 100) {
                    // 设置进度条的次要进度，表示视频的缓冲进度
                    seekBar_time.setSecondaryProgress(bufferTime * 1000);
                }

            }
            handler.postDelayed(run, 1000);

        }
    };

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (fltController.getVisibility() == View.VISIBLE
                        && titleController.getVisibility() == View.VISIBLE) {
                    fltController.setVisibility(View.GONE);
                    titleController.setVisibility(View.GONE);
                } else if (fltController.getVisibility() == View.GONE
                        && titleController.getVisibility() == View.GONE) {
                    fltController.setVisibility(View.VISIBLE);
                    titleController.setVisibility(View.VISIBLE);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        toggleBtnPlayOrPause(false);
                    } else {
                        toggleBtnPlayOrPause(true);
                    }
                    delayHideController();
                }
                if (mSoundWindow.isShowing()) {
                    mSoundView.setVisibility(View.INVISIBLE);
                    mSoundWindow.dismiss();
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (fltController.getVisibility() == View.VISIBLE
                        && titleController.getVisibility() == View.VISIBLE) {
                    fltController.setVisibility(View.GONE);
                    titleController.setVisibility(View.GONE);
                } else if (fltController.getVisibility() == View.GONE
                        && titleController.getVisibility() == View.GONE) {
                    fltController.setVisibility(View.VISIBLE);
                    titleController.setVisibility(View.VISIBLE);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        toggleBtnPlayOrPause(false);
                    } else {
                        toggleBtnPlayOrPause(true);
                    }
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                }
                if (mSoundWindow.isShowing()) {
                    mSoundView.setVisibility(View.INVISIBLE);
                    mSoundWindow.dismiss();
                }
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                saveCurrentVolume = skbVolume.getProgress();
                // 初始化 拖放时间、是否滑动视频
                seekTime = full_seekBar_time.getProgress();
                isSeekTo = false;
                isScrollVoice = false;
                isBrightness = false;
                return true;
            }

            // 拖动手势
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                // videoView.setFocusable(true);
                // videoView.setFocusableInTouchMode(true);
                // videoView.requestFocus();
                // videoView.requestFocusFromTouch();
                // 左右位移
                float Xchange = e1.getX() - e2.getX();
                float absXchange = Math.abs(Xchange);
                // 上下位移
                float Ychange = e1.getY() - e2.getY();
                float absYchange = Math.abs(Ychange);
                LogUtils.i("X1===" + e1.getX() + "-----Y1===" + e1.getY());
                LogUtils.i("X===" + absXchange + "-----Y===" + absYchange);
                // 播放时间改变

                if (isSeekTo
                        || (!isScrollVoice && absXchange > 50 && absXchange
                        / absYchange > 1.0)) {

                    // mHandler.removeMessages(1);
                    int currentTime = full_seekBar_time.getProgress();
                    int maxTime = full_seekBar_time.getMax();
                    int newTime = currentTime
                            - (int) ((!isScrollVoice && absXchange > 50 && absXchange
                            / absYchange > 1.0) ? Xchange : Ychange)
                            * 100;
                    newTime = newTime > maxTime ? maxTime : newTime;
                    newTime = newTime < 0 ? 0 : newTime;
                    // ys 记录拖放时间、修改是否可滑动视频
                    seekTime = newTime;
                    isSeekTo = true;
                    // end
                    int i = newTime;
                    i /= 1000;
                    int minute = i / 60;
                    int hour = minute / 60;
                    int second = i % 60;
                    minute %= 60;
                    fastforwardView.setVisibility(View.VISIBLE);
                    // 手势滑动后 快进画面
                    if (distanceX < 1) {
                        ((ImageView) fastforwardView
                                .findViewById(R.id.playfast_image))
                                .setImageDrawable(getResources().getDrawable(
                                        R.mipmap.video_forward__normal));
                    }
                    if (distanceX > 1) {
                        ((ImageView) fastforwardView
                                .findViewById(R.id.playfast_image))
                                .setImageDrawable(getResources().getDrawable(
                                        R.mipmap.video_retrat_normal));
                    }
                    ((TextView) fastforwardView.findViewById(R.id.playtime))
                            .setText(String.format("%02d:%02d:%02d", hour,
                                    minute, second));
                    maxTime /= 1000;
                    int minute1 = maxTime / 60;
                    int hour1 = minute1 / 60;
                    int second1 = maxTime % 60;
                    minute1 %= 60;
                    ((TextView) fastforwardView
                            .findViewById(R.id.video_totaltime)).setText(String
                            .format("%02d:%02d:%02d", hour1, minute1, second1));
                    if (fastforwardWindow.isShowing()) {
                        fastforwardWindow.update();
                    } else {
                        // fastforwardWindow.showAtLocation(videoView,
                        // Gravity.CENTER, 0, 0);
                        fastforwardWindow.showAtLocation(videoView,
                                Gravity.CENTER, 0, 0);
                        fastforwardWindow.update();
                    }
                    delayHideController(3000);
                }
                // myHandler.sendEmptyMessageDelayed(1,
                // 300);
                // 音量调节
                if (isScrollVoice
                        || (!isSeekTo && absYchange > 50 && absYchange
                        / absXchange > 1.0)) {
                    // cancelDelayHide();
                    // 乘以7，设置一个调节音量的灵敏度。
                    int changeVolume = (int) (15.0 / ((float) screenHeight)
                            * distanceY * 7);
                    isScrollVoice = true;
                    // 同时改变音量的进度条
                    // int i = (changeVolume > 0 ? 1 : -1);
                    int i = 0;
                    if (changeVolume > 0) {
                        i = 1;
                    } else if (changeVolume < 0) {
                        i = -1;
                    }
                    int newSound = skbVolume.getProgress() + i;
                    newSound = (newSound > 15 ? 15 : newSound);
                    newSound = (newSound < 0 ? 0 : newSound);
                    mSoundView.setVisibility(View.VISIBLE);
                    mSoundView.findViewById(R.id.videoplay_layout_sound)
                            .setVisibility(View.VISIBLE);
                    updateVolume(newSound);
                    skbVolume.setProgress(newSound);
                    if (mSoundWindow.isShowing()) {
                        mSoundWindow.update();
                    } else {
                        // mSoundWindow.showAtLocation(videoView, Gravity.LEFT
                        // | Gravity.CENTER_VERTICAL, 40, 0);
                        mSoundWindow.showAtLocation(videoView, Gravity.CENTER
                                , 0, 0);
                        mSoundWindow.update();
                    }
                    delayHideController(3000);

                }

                if (isBrightness
                        || (!isScrollVoice && !isSeekTo && absXchange > 50 && absXchange
                        / absYchange > 1.0)) {
                    onBrightnessSlide((e1.getY() - (int) e2.getRawY()) / getWindowManager().getDefaultDisplay().getHeight());
                }

                if (Math.abs(distanceX) < 40)
                    delayHideController();
                return true;
            }

        });
    }

    private void updateVolume(int index) {
        if (mAudioManager != null) {
            // currentVolume =
            // mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume == 0) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
                        0);
                System.out.println("音量改变量：" + index);
            }
            currentVolume = index;
            if (index == 0) {
                btnSilence.setImageDrawable(getResources().getDrawable(
                        R.drawable.selector_btn_jinyin));
            } else {
                btnSilence.setImageDrawable(getResources().getDrawable(
                        R.drawable.selector_btn_volume));
            }
            // btn_sound.setAlpha(findAlphaFromSound());
        }
    }

    // 加载竖屏时的控制视图
    private void loadPortraitController() {
        if (rltPortrait == null) {
            if (btnCollect != null) {
                btnCollect.setVisibility(View.GONE);
            }
            if (btnShare != null) {
                btnShare.setVisibility(View.GONE);
            }
            rltPortrait = (RelativeLayout) LayoutInflater.from(this).inflate(
                    R.layout.box_mini_controller, null);
            RelativeLayout rtlController = (RelativeLayout) rltPortrait
                    .findViewById(R.id.rtlController);
            rtlController.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    if (mHandler.hasMessages(1)) {
                        mHandler.removeMessages(1);
                    }
                    delayHideController();
                }
            });
            rltPortrait
                    .setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            getResources().getDimensionPixelSize(
                                    R.dimen.film_player_h)));
            btnPlayOrPausePortrait = (ImageButton) rltPortrait
                    .findViewById(R.id.btnPlayOrPausePortrait);
            btnFullScreen = (ImageButton) rltPortrait
                    .findViewById(R.id.btnFullScreen);
            seekBar_time = (SeekBar) rltPortrait
                    .findViewById(R.id.videoplay_seekbar_time);
            tvPlayTime = (TextView) rltPortrait.findViewById(R.id.tvPlayTime);
            tvAllTime = (TextView) rltPortrait.findViewById(R.id.tvAllTime);
            btnPlayOrPausePortrait.setOnClickListener(this);
            btnFullScreen.setOnClickListener(this);
            seekBar_time
                    .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            delayHideController();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar,
                                                      int progress, boolean fromUser) {
                            if (fromUser) {
                                // videoView.seekTo(progress);
                                // videoView.setBackgroundColor(getResources()
                                // .getColor(R.color.nor_transparent));

                                videoView.seekTo(progress);

                                // videoView.setBackgroundColor(getResources()
                                // .getColor(R.color.nor_transparent));

                            }
                        }
                    });
        }
        fltController.removeAllViews();
        fltController.addView(rltPortrait);
    }

    // 加载横屏时的控制视图
    private void loadLandScapeController() {
        if (rltLandScape == null) {
            btnCollect.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);
            rltLandScape = (RelativeLayout) LayoutInflater.from(this).inflate(
                    R.layout.box_full_controller, null);
            RelativeLayout rtlController = (RelativeLayout) rltLandScape
                    .findViewById(R.id.rtlController);
            rtlController.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    if (mHandler.hasMessages(1)) {
                        mHandler.removeMessages(1);
                    }
                    delayHideController();
                }
            });
            full_seekBar_time = (SeekBar) rltLandScape
                    .findViewById(R.id.videoplay_full_seekbar_time);
            btnPlayOrPauseLandscape = (ImageButton) rltLandScape
                    .findViewById(R.id.btnPlayOrPauseLandscape);
            btnPlayBackward = (ImageButton) rltLandScape
                    .findViewById(R.id.btnPlayBackward);
            btnPlayBackward.setOnClickListener(this);
            btnPlayForward = (ImageButton) rltLandScape
                    .findViewById(R.id.btnPlayForward);
            btnPlayForward.setOnClickListener(this);
            tvFullPlayTime = (TextView) rltLandScape
                    .findViewById(R.id.tvFullPlayTime);
            tvFullAllTime = (TextView) rltLandScape
                    .findViewById(R.id.tvFullAllTime);
            btnSilence = (ImageButton) rltLandScape
                    .findViewById(R.id.btnSilence);
            btnQuitFullScreen = (ImageButton) rltLandScape
                    .findViewById(R.id.btnQuitFullScreen);
            btnPlayOrPauseLandscape.setOnClickListener(this);
            btnSilence.setOnClickListener(this);
            btnQuitFullScreen.setOnClickListener(this);
            // int i = videoView.getDuration();
            int i;

            // totalTime = videoView.getDuration();

            totalTime = (int) videoView.getDuration();

            full_seekBar_time.setMax(totalTime);
            full_seekBar_time.setProgress((int) videoView.getCurrentPosition());

            i = (int) videoView.getDuration();

            i /= 1000;
            int minute = i / 60;
            int hour = minute / 60;
            int second = i % 60;
            minute %= 60;
            tvFullAllTime.setText(String.format("%02d:%02d:%02d", hour, minute,
                    second));

            // 当前音量为0时
            if (currentVolume != 0) {
                saveCurrentVolume = currentVolume;
                updateVolume(saveCurrentVolume);
                skbVolume.setProgress(saveCurrentVolume);
            } else {
                saveCurrentVolume = currentVolume;
                updateVolume(saveCurrentVolume);
                skbVolume.setProgress(saveCurrentVolume);
            }
            full_seekBar_time
                    .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            delayHideController();
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar,
                                                      int progress, boolean fromUser) {
                            if (fromUser) {

                                // videoView.seekTo(progress);
                                // videoView.setBackgroundColor(getResources()
                                // .getColor(R.color.nor_transparent));

                                videoView.seekTo(progress);

                                // videoView.setBackgroundColor(getResources()
                                // .getColor(R.color.nor_transparent));

                            }
                        }
                    });
        }

        fltController.removeAllViews();
        fltController.addView(rltLandScape);
        initGestureDetector();
    }

    // 改变播放/暂停按钮的状态
    private void toggleBtnPlayOrPause(boolean fullScreen) {

        if (videoView != null && videoView.isPlaying()) {
            if (fullScreen) {
                btnPlayOrPauseLandscape
                        .setImageResource(R.drawable.selector_btn_lan_pause);
            } else {
                btnPlayOrPausePortrait
                        .setImageResource(R.drawable.selector_btn_par_pause);
            }
        } else {
            if (fullScreen) {
                btnPlayOrPauseLandscape
                        .setImageResource(R.drawable.selector_btn_lan_play);
            } else {
                btnPlayOrPausePortrait
                        .setImageResource(R.drawable.selector_btn_par_play);
            }
        }

    }

    // 延时隐藏控制视图
    private void delayHideController() {
        fltController.setVisibility(View.VISIBLE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000);
    }

    private void delayHideController(int time) {
        titleController.setVisibility(View.VISIBLE);
        fltController.setVisibility(View.VISIBLE);

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, time);
    }

    // 根据是否全屏来改变显示状态
    private void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            vprRelateFilm.setVisibility(View.GONE);
            getScreenSize();
            // videoView.setVideoScale(screenWidth, screenHeight);
            rtlVideo.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            loadLandScapeController();
            LogUtils.e("=====================");
            titleController.setVisibility(View.VISIBLE);
            delayHideController(3000);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            vprRelateFilm.setVisibility(View.VISIBLE);
            rtlVideo.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, getResources()
                    .getDimensionPixelSize(R.dimen.vdv_height)));
            loadPortraitController();
        }
        toggleBtnPlayOrPause(fullScreen);
    }

    private void forward() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (mHandler.hasMessages(1)) {
            mHandler.removeMessages(1);
        }
        delayHideController();
        // if (!videoView.canSeekForward()) {
        int currentTime = full_seekBar_time.getProgress();
        int maxTime = full_seekBar_time.getMax();
        int newTime = currentTime + 30 * 1000;
        newTime = newTime > maxTime ? maxTime : newTime;
        newTime = newTime < 0 ? 0 : newTime;
        seekTime = newTime;
        isSeekTo = true;
        // end
        int i = newTime;
        i /= 1000;
        int minute = i / 60;
        int hour = minute / 60;
        int second = i % 60;
        minute %= 60;
        // videoView.seekTo(seekTime);

        videoView.seekTo(seekTime);

        tvFullPlayTime.setText(String.format("%02d:%02d:%02d", hour, minute,
                second));
        // } else {
        // AppUtils.toastShowMsg(FilmDetailsAct.this, "暂不支持快进");
        // }

    }

    private void backward() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (mHandler.hasMessages(1)) {
            mHandler.removeMessages(1);
        }
        delayHideController();
        // if (!videoView.canSeekBackward()) {
        int currentTime = full_seekBar_time.getProgress();
        int maxTime = full_seekBar_time.getMax();
        int newTime = currentTime - 30 * 1000;
        newTime = newTime > maxTime ? maxTime : newTime;
        newTime = newTime < 0 ? 0 : newTime;
        seekTime = newTime;
        isSeekTo = true;
        // end
        int i = newTime;
        i /= 1000;
        int minute = i / 60;
        int hour = minute / 60;
        int second = i % 60;
        minute %= 60;
        // videoView.seekTo(seekTime);

        videoView.seekTo(seekTime);

        tvFullPlayTime.setText(String.format("%02d:%02d:%02d", hour, minute,
                second));
        // } else {
        // AppUtils.toastShowMsg(FilmDetailsAct.this, "暂不支持快退");
        // }
    }

    // 观察屏幕旋转设置变化，类似于注册动态广播监听变化机制
    private class RotationObserver extends ContentObserver {
        ContentResolver mResolver;

        public RotationObserver(Handler handler) {
            super(handler);
            mResolver = getContentResolver();
        }

        // 屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 更新按钮状态
            if (getRotationStatus() == 1) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System
                            .getUriFor(Settings.System.ACCELEROMETER_ROTATION), false,
                    this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }

    // 得到屏幕旋转的状态
    private int getRotationStatus() {
        int status = 0;
        try {
            status = Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            if (videoView != null) {
                isGetVideo = true;
                double videoWidth = (double) videoView.getVideoWidth();
                double videoHeight = (double) videoView.getVideoHeight();
                if (videoWidth != 0.0 && videoHeight != 0.0 && isGetVideo) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setVideoSize(screenHeight, screenWidth);
                    } else {
                        setVideoSize(screenWidth, screenHeight);
                    }
                }
            }

            // mSensorManager.registerListener(this, mOrientationSensor,
            // SensorManager.SENSOR_DELAY_UI);
            // mRotationObserver.startObserver();
            // 由屏幕方向来改变显示状态
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setFullScreen(true);
                btnPlayOrPauseLandscape
                        .setImageResource(R.drawable.selector_btn_lan_pause);
            } else {
                setFullScreen(false);
                btnPlayOrPausePortrait
                        .setImageResource(R.drawable.selector_btn_par_pause);
            }
            checkPlayNet();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void checkPlayNet() {
        if (NetUtils.isConnect(FilmDetailsAct.this)) {
            if ((!NetUtils.isWifi(FilmDetailsAct.this))) {
                // 不是wifi网络
                // wap网络连接，不允许播放
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        FilmDetailsAct.this);
                builder.setCancelable(false);
                builder.setTitle("网络连接提示");
                builder.setMessage("您当前使用的是移动数据网络，效果欠佳并会产生较高流量费，是否播放？");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // videoView.start();

                                videoView.start();

                            }
                        });
                builder.setNegativeButton("否",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                builder.show();
            } else {
                // wifi网络情况
                // videoView.start();

                videoView.start();

            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (btnPlay.getVisibility() == View.INVISIBLE) {
                    lltLoading.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPlay.setImageResource(R.drawable.selector_btn_lan_play_flag);
                }

            } else {
                if (btnPlay.getVisibility() == View.INVISIBLE) {
                    lltLoading.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPlay.setImageResource(R.drawable.selector_btn_par_play_flag);
                }
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mSensorManager.unregisterListener(this);

            if (videoView != null && videoView.isPlaying()) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    btnPlayOrPauseLandscape
                            .setImageResource(R.drawable.selector_btn_lan_play);
                } else {
                    btnPlayOrPausePortrait
                            .setImageResource(R.drawable.selector_btn_par_play);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRotationObserver.stopObserver();
        mHandler.removeMessages(4);
        mHandler.removeMessages(1);
        handler.removeCallbacks(run);
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        // if (mActiveCloakMediaPlayer != null
        // && mActiveCloakMediaPlayer.isOpen()) {
        // mActiveCloakMediaPlayer.close();
        // }
        titleController.setVisibility(View.GONE);
        mKeyguardLock.reenableKeyguard();
        if (observer != null) {
            observer.stopScreenStateUpdate();
        }

        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 当新设置中，屏幕为横屏时
            setFullScreen(true);
            if (videoView != null) {
                double videoWidth = (double) videoView.getVideoWidth();
                double videoHeight = (double) videoView.getVideoHeight();
                if (videoWidth != 0.0 && videoHeight != 0.0) {
                    setVideoSize(screenHeight, screenWidth);
                }
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 当新设置中，屏幕为竖屏时
            if (fastforwardWindow.isShowing()) {
                fastforwardView.setVisibility(View.INVISIBLE);
                fastforwardWindow.dismiss();
            }
            if (mSoundWindow.isShowing()) {
                mSoundView.setVisibility(View.INVISIBLE);
                mSoundWindow.dismiss();
            }
            setFullScreen(false);
            if (videoView != null) {
                double videoWidth = (double) videoView.getVideoWidth();
                double videoHeight = (double) videoView.getVideoHeight();
                if (videoWidth != 0.0 && videoHeight != 0.0) {
                    setVideoSize(screenHeight, screenWidth);
                }
            }
        }
        if (!isPlayingSave) {
            mHandler.sendEmptyMessageDelayed(2, 100);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (saveSensor != getResources().getConfiguration().orientation) {
            return;
        } else {
            float roll = event.values[SensorManager.DATA_X];
            if (Math.abs(roll) < 6) {
                // 手机旋转为纵向；处理之
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (rotateTime == 3) {
                        if (Settings.System.getInt(getContentResolver(),
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                            Log.d(mPageName,
                                    "在传感器作用下变为SCREEN_ORIENTATION_SENSOR");
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            rotateTime = 999;
                        }
                    }
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (rotateTime < 3) {
                        Log.d(mPageName, "rotateTime = " + rotateTime);
                        if (Settings.System.getInt(getContentResolver(),
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                            rotateTime++;
                        }
                    }
                }
            }
            if (Math.abs(roll) > 8.5) {
                // 手机旋转为横向；处理之
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (rotateTime == 3) {
                        if (Settings.System.getInt(getContentResolver(),
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                            Log.d(mPageName,
                                    "在传感器作用下变为SCREEN_ORIENTATION_SENSOR");
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            rotateTime = 999;
                        }
                    }
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (rotateTime < 3) {
                        Log.d(mPageName, "rotateTime = " + rotateTime);
                        if (Settings.System.getInt(getContentResolver(),
                                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                            rotateTime++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCollect:
                if (currentMoive != null) {
                    DBUtil.addToFavorites(currentMoive);
                }
                break;
            case R.id.btnShare:
                SystemUtils.shareMsg(FilmDetailsAct.this, "ddd0", "ddd1", "ddd2", null);
                break;

            case R.id.btnPlayOrPausePortrait:

                if (videoView != null && videoView.isPlaying()) {
                    // videoView.pause();
                    videoView.pause();
                    isPlayingSave = false;
                    btnPlayOrPausePortrait
                            .setImageResource(R.drawable.selector_btn_par_play);
                } else {
                    // videoView.start();
                    // videoView.setBackgroundColor(getResources().getColor(
                    // R.color.nor_transparent));
                    videoView.start();
                    isPlayingSave = true;
                    btnPlayOrPausePortrait
                            .setImageResource(R.drawable.selector_btn_par_pause);
                }

                delayHideController();

                break;
            case R.id.btnPlayOrPauseLandscape:

                if (videoView != null && videoView.isPlaying()) {
                    // videoView.pause();
                    videoView.pause();
                    isPlayingSave = false;
                    btnPlayOrPauseLandscape
                            .setImageResource(R.drawable.selector_btn_lan_play);
                } else {
                    // videoView.start();
                    // videoView.setBackgroundColor(getResources().getColor(
                    // R.color.nor_transparent));
                    videoView.start();
                    isPlayingSave = true;
                    btnPlayOrPauseLandscape
                            .setImageResource(R.drawable.selector_btn_lan_pause);
                }

                delayHideController();

                break;
            case R.id.btnFullScreen:
                if (isSensor) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    rotateTime = 0;
                    saveSensor = Configuration.ORIENTATION_LANDSCAPE;
                }
                break;
            case R.id.btnQuitFullScreen:
                if (isSensor) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rotateTime = 0;
                    saveSensor = Configuration.ORIENTATION_PORTRAIT;
                    titleController.setVisibility(View.VISIBLE);
                    fltController.setVisibility(View.VISIBLE);
                    if (fastforwardWindow.isShowing()) {
                        fastforwardView.setVisibility(View.INVISIBLE);
                        fastforwardWindow.dismiss();
                    }
                    if (mSoundWindow.isShowing()) {
                        mSoundView.setVisibility(View.INVISIBLE);
                        mSoundWindow.dismiss();
                    }
                }
                break;

            case R.id.btnSilence:
                mSoundView.setVisibility(View.VISIBLE);
                mSoundView.findViewById(R.id.videoplay_layout_sound).setVisibility(
                        View.VISIBLE);
                if (mSoundWindow.isShowing()) {
                    mSoundWindow.dismiss();
                } else {
                    // mSoundWindow.showAtLocation(videoView,
                    // Gravity.CENTER_VERTICAL
                    // | Gravity.LEFT, 40, 0);
                    mSoundWindow.showAtLocation(videoView, Gravity.CENTER
                            , 0, 0);
                    mSoundWindow.update();
                }
                delayHideController();
                break;
            case R.id.btnPlayForward:
                forward();
                break;
            case R.id.btnPlayBackward:
                backward();
                break;
            case R.id.btnPlay:
                lltLoading.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.INVISIBLE);
                retryPlay();
                break;

            case R.id.tvwNaviTitle:
            case R.id.btnBack:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    rotateTime = 0;

                    titleController.setVisibility(View.INVISIBLE);
                    fltController.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    if (fastforwardWindow.isShowing()) {
                        fastforwardView.setVisibility(View.INVISIBLE);
                        fastforwardWindow.dismiss();
                    }

                    if (mSoundWindow.isShowing()) {
                        mSoundView.setVisibility(View.INVISIBLE);
                        mSoundWindow.dismiss();
                    }
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setResult(200);
                    finish();
                }
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (rltLandScape != null) {
                    currentVolume = mAudioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);
                    updateVolume(currentVolume);
                    skbVolume.setProgress(currentVolume);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    delayHideController(3000);
                } else {
                    titleController.setVisibility(View.VISIBLE);
                    delayHideController();
                }

                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            rotateTime = 0;

            titleController.setVisibility(View.INVISIBLE);
            fltController.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);

            if (fastforwardWindow.isShowing()) {
                fastforwardView.setVisibility(View.INVISIBLE);
                fastforwardWindow.dismiss();
            }
            if (mSoundWindow.isShowing()) {
                mSoundView.setVisibility(View.INVISIBLE);
                mSoundWindow.dismiss();
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setResult(200);
            finish();
        }
        super.onBackPressed();
    }

    // 声音按键 使手机自身音量进度条与播放器声音进度条同步
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (rltLandScape != null) {
                currentVolume = mAudioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                updateVolume(currentVolume);
                skbVolume.setProgress(currentVolume);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean result = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            result = mGestureDetector.onTouchEvent(event);
            // 触控弹起情况
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // videoView.requestFocus();
                // 可滑动视频，快进、快退操作
                if (isSeekTo) {
                    // videoView.seekTo(seekTime);

                    videoView.seekTo(seekTime);

                    isSeekTo = false;
                    mHandler.sendEmptyMessageDelayed(5, 1000);
                }
                isScrollVoice = false;
                isBrightness = false;
            }
            if (!result) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                result = super.onTouchEvent(event);
            }
        } else {
            if (!result) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                result = super.onTouchEvent(event);
            }
        }

        return result;
    }


    /**
     * 播放失败提示
     */
    private void popupDiaNotice() {
        if (diaNotice == null) {
            createDiaNotice();
        }
        diaNotice.show();
    }

    private void retryPlay() {
        playAble = false;
        videoView.stopPlayback();
        if (NetUtils.isConnect(FilmDetailsAct.this)) {
            lltLoading.setVisibility(View.VISIBLE);
        }
        if (!AppUtils.isDataDisable(url)) {
            btnPlayOrPausePortrait
                    .setImageResource(R.drawable.selector_btn_par_pause);
            isUseDRM = false;
            playVideo(url, isUseDRM);
            // playVideo("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
        }
    }

    /**
     * 提示视图
     */
    private void createDiaNotice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("视频信息获取失败，是否重试");
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // 取消
                        lltLoading.setVisibility(View.GONE);
                        btnPlay.setVisibility(View.VISIBLE);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // 确定
                        lltLoading.setVisibility(View.VISIBLE);
                        btnPlay.setVisibility(View.INVISIBLE);
                        retryPlay();
                        break;
                }
            }
        };
        builder.setPositiveButton("取消", listener);
        builder.setNegativeButton("重试", listener);
        diaNotice = builder.create();
    }

    @Override
    public void onScreenOn() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScreenOff() {
        // TODO Auto-generated method stub
        LogUtils.d("gggggggggggggggggggggggggggggggggggg");
        // if (videoView != null) {
        // videoView.pause();
        // LogUtils.d("hhhhhhhhhhhhhhhhhhhhhhhh");
        // }

        if (videoView != null) {
            videoView.pause();
            LogUtils.d("hhhhhhhhhhhhhhhhhhhhhhhh");
        }

    }

    // 设置视频宽高
    private void setVideoSize(int width, int height) {
        double mVideoWidth;
        double mVideoHeight;
        double videoWidth = (double) videoView.getVideoWidth();
        double videoHeight = (double) videoView.getVideoHeight();
        double videowh = (double) videoWidth / videoHeight;
        double landscapewh = (double) height / width;// 手机横屏 宽高 比
        double partalwh = (double) width
                / (getResources().getDimensionPixelSize(R.dimen.vdv_height));// 手机横屏
        // 宽高
        // 比

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (videowh < landscapewh) {
                mVideoHeight = width;
                mVideoWidth = mVideoHeight * videowh;
            } else {
                mVideoWidth = height;
                mVideoHeight = mVideoWidth / videowh;
            }
            LogUtils.e("$$$$$$$$$$$$$$$$$$$$$$$$");
            videoView.setVideoScale(Integer
                    .parseInt(new java.text.DecimalFormat("0")
                            .format(mVideoWidth)), Integer
                    .parseInt(new java.text.DecimalFormat("0")
                            .format(mVideoHeight)));
        } else {
            if (videowh < partalwh) {
                mVideoHeight = (getResources()
                        .getDimensionPixelSize(R.dimen.vdv_height));
                mVideoWidth = mVideoHeight * videowh;
            } else {
                mVideoWidth = width;
                mVideoHeight = mVideoWidth / videowh;
            }
            LogUtils.e("++++++++++++++++++++++++");

            videoView.setVideoScale(Integer
                    .parseInt(new java.text.DecimalFormat("0")
                            .format(mVideoWidth)), Integer
                    .parseInt(new java.text.DecimalFormat("0")
                            .format(mVideoHeight)));
        }

    }

    @Override
    protected void onViewClick(View view) {

    }
}
