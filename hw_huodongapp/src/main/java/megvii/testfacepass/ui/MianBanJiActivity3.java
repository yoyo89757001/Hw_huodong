package megvii.testfacepass.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.badoo.mobile.util.WeakHandler;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.sdsmdg.tastytoast.TastyToast;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.CharsetUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.query.LazyList;
import io.objectbox.query.QueryBuilder;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;
import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BenDiJiLuBean;
import megvii.testfacepass.beans.DaKaBean;
import megvii.testfacepass.beans.DaKaBean_;
import megvii.testfacepass.beans.GuanHuai;
import megvii.testfacepass.beans.HistoryBean;
import megvii.testfacepass.beans.MSRBean;
import megvii.testfacepass.beans.MSRBean_;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.beans.Subject_;
import megvii.testfacepass.beans.TQBean;
import megvii.testfacepass.beans.TodayBean;
import megvii.testfacepass.beans.XGBean;
import megvii.testfacepass.beans.XinXiAll;
import megvii.testfacepass.beans.XinXiIdBean;
import megvii.testfacepass.beans.YanZhiBean;
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;
import megvii.testfacepass.dialog.MiMaDialog4;
import megvii.testfacepass.tts.control.InitConfig;
import megvii.testfacepass.tts.control.MySyntherizer;
import megvii.testfacepass.tts.control.NonBlockSyntherizer;
import megvii.testfacepass.tts.listener.UiMessageListener;
import megvii.testfacepass.tts.util.OfflineResource;
import megvii.testfacepass.tuisong_jg.TSXXChuLi;
import megvii.testfacepass.utils.BitmapUtil;
import megvii.testfacepass.utils.DateUtils;
import megvii.testfacepass.utils.FacePassUtil;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GsonUtil;
import megvii.testfacepass.utils.MacUtils;
import megvii.testfacepass.utils.NV21ToBitmap;
import megvii.testfacepass.utils.SettingVar;
import megvii.testfacepass.utils.ZXingUtils;
import megvii.testfacepass.view.Dddddd;
import megvii.testfacepass.view.GlideCircleTransform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class MianBanJiActivity3 extends Activity implements CameraManager.CameraListener {


    @BindView(R.id.tv1)
    LinearLayout tv1;
    @BindView(R.id.xingbie)
    TextView xingbie;
    @BindView(R.id.tv2)
    LinearLayout tv2;
    @BindView(R.id.fuzhi)
    TextView fuzhi;
    @BindView(R.id.tv3)
    LinearLayout tv3;
    @BindView(R.id.guanzhu)
    TextView guanzhu;
    @BindView(R.id.tv4)
    LinearLayout tv4;
    @BindView(R.id.biaoqing)
    TextView biaoqing;
    @BindView(R.id.tv5)
    LinearLayout tv5;
    @BindView(R.id.erweima)
    ImageView erweima;
    @BindView(R.id.meilizhi)
    TextView meilizhi;
    @BindView(R.id.baifenbi)
    TextView baifenbi;
    @BindView(R.id.dh3)
    ImageView dh3;
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.dh2)
    LinearLayout dh2;
    @BindView(R.id.dh1)
    LinearLayout dh1;
    @BindView(R.id.nianling)
    TextView nianling;
    @BindView(R.id.daojishi)
    Button daojishi;

    private Box<BenDiJiLuBean> benDiJiLuBeanBox = null;
    private Box<Subject> subjectBox = null;
    protected Handler mainHandler;
    private String appId = "11644783";
    private String appKey = "knGksRFLoFZ2fsjZaMC8OoC7";
    private String secretKey = "IXn1yrFezEo55LMkzHBGuTs1zOkXr9P4";
    private TtsMode ttsMode = TtsMode.MIX;
    private String offlineVoice = OfflineResource.VOICE_FEMALE;
    private MySyntherizer synthesizer;
    private RelativeLayout rl1, rl4;
    private RequestOptions myOptions = new RequestOptions()
            .fitCenter()
            // .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.erroy_bg)
            .transform(new GlideCircleTransform(MyApplication.myApplication, 0, Color.parseColor("#ffffffff")));
    // .transform(new GlideRoundTransform(MainActivity.this,10));
    private static boolean isHuoQu = false;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .writeTimeout(25000, TimeUnit.MILLISECONDS)
            .connectTimeout(25000, TimeUnit.MILLISECONDS)
            .readTimeout(25000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
            //        .retryOnConnectionFailure(true)
            .build();
    private final Timer timer = new Timer();
    private TimerTask task;

    private LinkedBlockingQueue<Subject> linkedBlockingQueue;
    /* 人脸识别Group */
    private static final String group_name = "facepasstestx";
    /* 程序所需权限 ：相机 文件存储 网络访问 */
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private static final String PERMISSION_ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
    private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE,
            PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE, PERMISSION_ACCESS_WIFI_STATE, PERMISSION_READ_PHONE_STATE};

    private MediaPlayer mMediaPlayer;
    //  private WindowManager wm;
    /* SDK 实例对象 */
    public FacePassHandler mFacePassHandler;
    /* 相机实例 */
    private CameraManager manager;
    /* 显示人脸位置角度信息 */
    // private XiuGaiGaoKuanDialog dialog = null;
    /* 相机预览界面 */
    private CameraPreview cameraView;
    //  private boolean isAnXia = true;
    private ImageView logo;
    /* 在预览界面圈出人脸 */
    //   private FaceView faceView;
    /* 相机是否使用前置摄像头 */
    private static boolean cameraFacingFront = true;
    private int cameraRotation;
    private static final int cameraWidth = 1280;
    private static final int cameraHeight = 720;
    private int heightPixels;
    private int widthPixels;
    int screenState = 0;// 0 横 1 竖
    private int count = 0;
    ArrayBlockingQueue<FacePassDetectionResult> mDetectResultQueue;
    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;
    /*recognize thread*/
    RecognizeThread mRecognizeThread;
    FeedFrameThread mFeedFrameThread;
    TanChuangThread tanChuangThread;
    TimeThread timeThread;
    private ConcurrentHashMap<Long, Integer> concurrentHashMap = new ConcurrentHashMap<Long, Integer>();
    private int dw, dh;
    private Box<BaoCunBean> baoCunBeanDao = null;
    private Box<DaKaBean> daKaBeanBox = null;
    private Box<TodayBean> todayBeanBox = null;
    private BaoCunBean baoCunBean = null;
    private TimeChangeReceiver timeChangeReceiver;
    private WeakHandler mHandler;
    private TSXXChuLi tsxxChuLi = null;
    private static boolean isSC = true;
    //private RelativeLayout linearLayout;
    private static boolean isDH = false;
    private boolean liet = false;
    private int w, h;
    private Subject subjectli;
    private Box<GuanHuai> guanHuaiBox = null;
    private Box<XinXiAll> xinXiAllBox = null;
    private Box<XinXiIdBean> xinXiIdBeanBox = null;
    public static boolean isL = true;
    private NV21ToBitmap nv21ToBitmap;
    private TodayBean todayBean = null;
    private Box<MSRBean> msrBeanBox = null;
    private String URL_SS = null;
    private String imei = "";
    private View viewRemove = null;
    private RelativeLayout dialogBg;
    private LinearLayout relativeLayout1;
    private LinearLayout relativeLayout2;
    private LinearLayout relativeLayout3;
    private View addView = null;
    public static boolean isTC = true;
    private CountDownTimer countDownTimer=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetectResultQueue = new ArrayBlockingQueue<>(5);
        mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);
        benDiJiLuBeanBox = MyApplication.myApplication.getBenDiJiLuBeanBox();
        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
        guanHuaiBox = MyApplication.myApplication.getGuanHuaiBox();
        xinXiAllBox = MyApplication.myApplication.getXinXiAllBox();
        xinXiIdBeanBox = MyApplication.myApplication.getXinXiIdBeanBox();
        todayBeanBox = MyApplication.myApplication.getTodayBeanBox();
        todayBean = todayBeanBox.get(123456L);
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

        };
        baoCunBean = baoCunBeanDao.get(123456L);
        subjectBox = MyApplication.myApplication.getSubjectBox();
        daKaBeanBox = MyApplication.myApplication.getDaKaBeanBox();
        msrBeanBox = MyApplication.myApplication.getMSRBeanBox();
        isL = true;
        //isERM=true;
        //每分钟的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        timeChangeReceiver = new TimeChangeReceiver();
        registerReceiver(timeChangeReceiver, intentFilter);
        linkedBlockingQueue = new LinkedBlockingQueue<>();

        EventBus.getDefault().register(this);//订阅

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;
        tsxxChuLi = new TSXXChuLi();


        //intent = new Intent(this, ProximityService.class);
        nv21ToBitmap = new NV21ToBitmap(MianBanJiActivity3.this);

        /* 初始化界面 */
        initView();
        if (baoCunBean.getXiaBanTime() != null) {
            logo.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getXiaBanTime()));
        }


        cameraView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //只需要获取一次高度，获取后移除监听器
                w = cameraView.getMeasuredWidth();
                h = cameraView.getMeasuredHeight();

                cameraView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }

        });

        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {
            //初始化
            start();
        }

        if (baoCunBean != null)
            initialTts();

        if (baoCunBean != null) {
            FacePassUtil util = new FacePassUtil();
            util.init(MianBanJiActivity3.this, getApplicationContext(), cameraRotation, baoCunBean);
        } else {
            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "获取本地设置失败,请进入设置界面设置基本信息", TastyToast.LENGTH_LONG, TastyToast.INFO);
            tastyToast.setGravity(Gravity.CENTER, 0, 0);
            tastyToast.show();
        }

        /* 初始化网络请求库 */
        //   requestQueue = Volley.newRequestQueue(getApplicationContext());

        mFeedFrameThread = new FeedFrameThread();
        mFeedFrameThread.start();

        mRecognizeThread = new RecognizeThread();
        mRecognizeThread.start();


        tanChuangThread = new TanChuangThread();
        tanChuangThread.start();

        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 111: {
                        final Subject bean = (Subject) msg.obj;
                        subjectli = (Subject) msg.obj;

                        if (relativeLayout1.getChildCount() > 0) {
                            addView(relativeLayout1, relativeLayout2, relativeLayout3);
                        }

                        final View view1 = View.inflate(MianBanJiActivity3.this, R.layout.dialog, null);
                        RelativeLayout llll = view1.findViewById(R.id.llllll);
                        TextView name = view1.findViewById(R.id.name);
                        Dddddd touxiang = view1.findViewById(R.id.touxiang);
                        TextView bumen = view1.findViewById(R.id.bumen);

                        final Bitmap fileBitmap = nv21ToBitmap.nv21ToBitmap(bean.getTxBytes(), bean.getW(), bean.getH());
                        touxiang.setmMaskBitmap(fileBitmap);

                        name.setText(bean.getName() + "");
                        bumen.setText("欢迎参加\n华为云城市峰会2019");


                        view1.setY(dh);
                        if (relativeLayout1.getChildCount() == 0) {
                            relativeLayout1.addView(view1, 0);
                            Log.d("addView", "view1:addView");
                        }

                        //竖屏
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) touxiang.getLayoutParams();
                        layoutParams.width = (int) (dw * 0.289f);
                        layoutParams.height = (int) (dw * 0.289f);
                        layoutParams.topMargin = (int) (dw * 0.05f);
                        touxiang.setLayoutParams(layoutParams);
                        touxiang.invalidate();


                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) llll.getLayoutParams();
                        layoutParams1.height = (int) (dh * 0.4f);
                        layoutParams1.width = (int) (dw * 0.5f);
//                            layoutParams1.width = (int) (dw * 0.25f);
                        llll.setLayoutParams(layoutParams1);
                        llll.invalidate();


                        //启动定时器或重置定时器
                        if (task != null) {
                            task.cancel();
                            //timer.cancel();
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = 222;
                                    mHandler.sendMessage(message);
                                }
                            };
                            timer.schedule(task, 5000);
                        } else {
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    Message message = new Message();
                                    message.what = 222;
                                    mHandler.sendMessage(message);
                                }
                            };
                            timer.schedule(task, 5000);
                        }

//                        入场动画(从右往左)
                        ValueAnimator anim = ValueAnimator.ofInt(dh, (int) (dh * 0.02f));
                        anim.setDuration(400);
                        anim.setRepeatMode(ValueAnimator.RESTART);
                        Interpolator interpolator = new DecelerateInterpolator(2f);
                        anim.setInterpolator(interpolator);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int currentValue = (Integer) animation.getAnimatedValue();
                                view1.setY(currentValue);
                                view1.requestLayout();
                            }
                        });
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                //tvSon.setText(singInSubjectBox.count() + "");
                                if (dialogBg.getVisibility() == View.GONE)
                                    dialogBg.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                        anim.start();


                        break;
                    }

                    case 222: {


                        if (!isDH) {
                            if (relativeLayout3.getChildCount() > 0) {
                                viewRemove = relativeLayout3.getChildAt(relativeLayout3.getChildCount() - 1);
                            } else {
                                if (relativeLayout2.getChildCount() > 0) {
                                    viewRemove = relativeLayout2.getChildAt(relativeLayout2.getChildCount() - 1);
                                } else {
                                    if (relativeLayout1.getChildCount() > 0) {
                                        viewRemove = relativeLayout1.getChildAt(relativeLayout1.getChildCount() - 1);
                                    }
                                }
                            }
                            if (viewRemove != null) {
                                List<Animator> animators = new ArrayList<>();//设置一个装动画的集合
                                final ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(viewRemove, "scaleX", 0.78f, 0f);
                                alphaAnim.setDuration(200);//设置持续时间
                                ObjectAnimator alphaAnim2 = ObjectAnimator.ofFloat(viewRemove, "scaleY", 0.75f, 0f);
                                alphaAnim2.setDuration(200);//设置持续时间
                                ObjectAnimator alpha = ObjectAnimator.ofFloat(viewRemove, "alpha", 1.0f, 0.0f);
                                alpha.setDuration(200);//设置持续时间
                                alphaAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        //底部列表的

                                    }
                                });
                                alphaAnim2.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        // isDH = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        // isDH = false;
//                                            linearLayout.removeView(viewRemove);
                                        if (relativeLayout3.getChildCount() > 0) {
                                            viewRemove = relativeLayout3.getChildAt(relativeLayout3.getChildCount() - 1);
                                            if (viewRemove != null)
                                                relativeLayout3.removeView(viewRemove);
                                        } else {
                                            if (relativeLayout2.getChildCount() > 0) {
                                                viewRemove = relativeLayout2.getChildAt(relativeLayout2.getChildCount() - 1);
                                                if (viewRemove != null)
                                                    relativeLayout2.removeView(viewRemove);
                                            } else {
                                                if (relativeLayout1.getChildCount() > 0) {
                                                    viewRemove = relativeLayout1.getChildAt(relativeLayout1.getChildCount() - 1);
                                                    if (viewRemove != null)
                                                        relativeLayout1.removeView(viewRemove);
                                                }
                                            }
                                        }
                                        //         isShow = true;
                                        if (relativeLayout1.getChildCount() > 0 || relativeLayout2.getChildCount() > 0 || relativeLayout2.getChildCount() > 0) {
                                            //启动定时器或重置定时器
                                            if (task != null) {
                                                task.cancel();
                                                //timer.cancel();
                                                task = new TimerTask() {
                                                    @Override
                                                    public void run() {

                                                        Message message = new Message();
                                                        message.what = 222;

                                                        mHandler.sendMessage(message);

                                                    }
                                                };
                                                timer.schedule(task, 5000);
                                            } else {
                                                task = new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Message message = new Message();
                                                        message.what = 222;

                                                        mHandler.sendMessage(message);
                                                    }
                                                };
                                                timer.schedule(task, 5000);
                                            }
                                        }

                                        boolean isChild = relativeLayout1.getChildCount() == 0 && relativeLayout2.getChildCount() == 0 && relativeLayout3.getChildCount() == 0;
                                        if (isChild && dialogBg.getVisibility() == View.VISIBLE) {
                                            dialogBg.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }

                                });
                                animators.add(alphaAnim);
                                animators.add(alphaAnim2);
                                animators.add(alpha);
                                AnimatorSet btnSexAnimatorSet = new AnimatorSet();//动画集
                                btnSexAnimatorSet.playTogether(animators);//设置一起播放
                                btnSexAnimatorSet.start();//开始播放
                            }

                        }

                        break;


                    }
                    case 120:{

                        rl1.setVisibility(View.VISIBLE);
                        rl4.setVisibility(View.GONE);
                        isTC=true;

                        break;
                    }

                }
                return false;
            }
        });

        isSC = true;

    }


    @Override
    protected void onResume() {
        URL_SS = baoCunBean.getGonggao();
        imei = MacUtils.getIMEI(MyApplication.myApplication);

        /* 打开相机 */
        if (hasPermission()) {
            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);
        }

        guanPing();
        super.onResume();
    }


    /* 相机回调函数 */
    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {
        /* 如果SDK实例还未创建，则跳过 */
        if (mFacePassHandler == null) {
            return;
        }
        /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
        FacePassImage image;
        try {
            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
        } catch (FacePassException e) {
            e.printStackTrace();
            return;
        }
        mFeedFrameQueue.offer(image);
    }


    private class FeedFrameThread extends Thread {
        boolean isIterrupt;

        @Override
        public void run() {
            while (!isIterrupt) {

                try {
                    final FacePassImage image = mFeedFrameQueue.take();
                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
                    FacePassDetectionResult detectionResult = null;
                    detectionResult = mFacePassHandler.feedFrame(image);
                    if (detectionResult == null || detectionResult.faceList.length == 0) {
                        //没人脸的时候
                        //mianBanJiView.tishi(false);
                        //   faceView.postInvalidate();


                    } else {


//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                faceView.invalidate();
//                                if (isShow)
//                                tishi.setVisibility(View.VISIBLE);
//
//                            }
//                        });

                        //拿陌生人图片
                        //   showFacePassFace(detectionResult.faceList);
                        //   Log.d("FeedFrameThread", "detectionResult.images.length:" + image.width+"  "+image.height);

                    }

                    /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                    if (detectionResult != null && detectionResult.message.length != 0) {
                        mDetectResultQueue.offer(detectionResult);
                        // Log.d(DEBUG_TAG, "1 mDetectResultQueue.size = " + mDetectResultQueue.size());
                    }


                } catch (InterruptedException | FacePassException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isIterrupt = true;
            super.interrupt();
        }
    }


    public class RecognizeThread extends Thread {

        boolean isInterrupt;

        @Override
        public void run() {
            while (!isInterrupt) {
                try {
                    if (isL) {
                        // Log.d("RecognizeThread", "识别线程");
                        FacePassDetectionResult detectionResult = mDetectResultQueue.take();
                        FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult.message);
                        if (recognizeResult != null && recognizeResult.length > 0) {
                            for (final FacePassRecognitionResult result : recognizeResult) {
                                //String faceToken = new String(result.faceToken);
                                if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
                                    //识别的
//                                //  getFaceImageByFaceToken(result.trackId, faceToken);
//
//                                        try {
//                                            final Subject subject = subjectBox.query().equal(Subject_.teZhengMa, new String(result.faceToken)).build().findUnique();
//                                            if (subject != null) {
//                                                linkedBlockingQueue.offer(subject);
//                                               // mianBanJiView.setBitmap(FileUtil.toRoundBitmap(mFacePassHandler.getFaceImage(result.faceToken)),subject.getName());
//                                               // mianBanJiView.setType(1);
//                                                long time = System.currentTimeMillis();
//                                                //保存刷脸记录
//                                                DaKaBean daKaBean=new DaKaBean();
//                                                daKaBean.setSid(subject.getSid());
//                                                daKaBean.setName(subject.getName());
//                                                daKaBean.setDepartment(subject.getDepartmentName()+"");
//                                                daKaBean.setTime(time);//DateUtils.time
//                                                daKaBeanBox.put(daKaBean);
//
//                                                for (int i=0;i<detectionResult.faceList.length;i++){
//                                                    FacePassImage images= detectionResult.images[i];
//                                                    if (images.trackId == result.trackId){
//
//                                                        final Bitmap fileBitmap = nv21ToBitmap.nv21ToBitmap(images.image,images.width,images.height);
//                                                       String paths= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"ruitongzipmbj";
//                                                       boolean tt= nv21ToBitmap.saveBitmap(fileBitmap,paths,time+".png");
//                                                        if (tt){
//                                                            subject.setZpPath(paths+File.separator+time+".png");
//                                                            Log.d("RecognizeThread", "subjectBox.put(subject):" + subjectBox.put(subject));
//                                                        }
//                                                       // link_shangchuanjilu22(subject, BitmapUtil.bitmaptoString(fileBitmap));
//                                                        break;
//                                                    }
//
//                                                }
//                                             //   mFacePassHandler.reset();
//                                                link_shangchuanjilu(subject);
//
//
//                                            } else {
//                                                EventBus.getDefault().post("没有查询到人员信息");
//                                            }
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            EventBus.getDefault().post(e.getMessage()+"");
//                                        }
//

                                } else {
                                    //未识别的
                                    if (concurrentHashMap.size() > 15) {
                                        concurrentHashMap.clear();
                                    }
                                    if (concurrentHashMap.get(result.trackId) == null) {
                                        //找不到新增
                                        concurrentHashMap.put(result.trackId, 1);
                                    } else {
                                        //找到了 把value 加1
                                        concurrentHashMap.put(result.trackId, (concurrentHashMap.get(result.trackId)) + 1);
                                    }
                                    //判断次数超过3次
                                    if (concurrentHashMap.get(result.trackId) == 2) {
                                        // concurrentHashMap.clear();
                                        for (FacePassImage images : detectionResult.images) {
                                            if (images.trackId == result.trackId) {

                                                Subject subject1 = new Subject();
                                                subject1.setTxBytes(images.image);
                                                subject1.setW(images.width);
                                                subject1.setH(images.height);
                                                subject1.setId(System.currentTimeMillis());
                                                subject1.setName("嘉宾您好");
                                                subject1.setTeZhengMa(null);
                                                subject1.setPeopleType("员工");
                                                subject1.setDepartmentName("没有进入权限,请与前台联系!");

                                                if (isTC){
                                                    linkedBlockingQueue.offer(subject1);
                                                }

                                                if (isHuoQu && !isTC) {
                                                    isHuoQu = false;
                                                    Bitmap fileBitmap = nv21ToBitmap.nv21ToBitmap(images.image, images.width, images.height);
                                                    getOkHttpClient2(fileBitmap);

                                                }

                                                break;
                                            }

                                        }
                                    }


                                    Log.d("RecognizeThread", "未识别的" + result.trackId);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void interrupt() {
            isInterrupt = true;
            super.interrupt();
        }

    }

    private class TimeThread extends Thread {

        boolean isRing=false;

        @Override
        public void run() {
            while (!isRing) {
                count++;
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        guanzhu.setText(count + "''");
                    }
                });
            }
        }

        @Override
        public void interrupt() {
            isRing = true;
            // Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }

    }

    private class TanChuangThread extends Thread {
        boolean isRing;

        @Override
        public void run() {
            while (!isRing) {
                try {
                    //有动画 ，延迟到一秒一次
                    Subject subject = linkedBlockingQueue.take();
                    if (subject.getPeopleType() != null) {
                        switch (subject.getPeopleType()) {
                            case "员工": {

                                Message message2 = Message.obtain();
                                message2.what = 111;
                                message2.obj = subject;
                                mHandler.sendMessage(message2);
                                break;
                            }
                            case "普通访客": {
                                //普通访客
                                subject.setDepartmentName("访客");
                                Message message2 = Message.obtain();
                                message2.what = 111;
                                message2.obj = subject;
                                mHandler.sendMessage(message2);

                                break;
                            }
                            case "白名单":
                                //vip
                                subject.setDepartmentName("VIP访客");
                                Message message2 = Message.obtain();
                                message2.what = 111;
                                message2.obj = subject;
                                mHandler.sendMessage(message2);

                                break;
                            case "黑名单":

                                break;
                            default:
                                EventBus.getDefault().post("没有对应身份类型,无法弹窗");

                        }
                    }
                    SystemClock.sleep(600);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isRing = true;
            // Log.d("RecognizeThread", "中断了弹窗线程");
            super.interrupt();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        shipingView.pause();
    }


    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /* 请求程序所需权限 */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Permission, PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED)
                    granted = false;
            }
            if (!granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA)
                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_WRITE_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_INTERNET)
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_WIFI_STATE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_PHONE_STATE)) {
                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
                    }
            } else {

                start();
            }
        }
    }


    private void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
        if (windowRotation == 0) {
            cameraRotation = FacePassImageRotation.DEG90;
        } else if (windowRotation == 90) {
            cameraRotation = FacePassImageRotation.DEG0;
        } else if (windowRotation == 270) {
            cameraRotation = FacePassImageRotation.DEG180;
        } else {
            cameraRotation = FacePassImageRotation.DEG270;
        }
//        Log.i(DEBUG_TAG, "cameraRation: " + cameraRotation);
        cameraFacingFront = true;
        SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
        SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
        SettingVar.isCross = preferences.getBoolean("isCross", SettingVar.isCross);
        SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
        SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
        SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);
        if (SettingVar.isSettingAvailable) {
            cameraRotation = SettingVar.faceRotation;
            cameraFacingFront = SettingVar.cameraFacingFront;
        }
        //  Log.i("orientation", String.valueOf(windowRotation));
        final int mCurrentOrientation = getResources().getConfiguration().orientation;

        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }
        setContentView(R.layout.activity_mianbanji3);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);

        ImageView shezhi = findViewById(R.id.shezhi);
        rl1 = findViewById(R.id.rl1);
        rl4 = findViewById(R.id.rl4);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;

        /* 初始化界面 */
        //  faceView = findViewById(R.id.fcview);
//        faceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MiMaDialog3 miMaDialog=new MiMaDialog3(MianBanJiActivity3.this,baoCunBean.getMima2());
//                WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//                params.width=dw;
//                params.height=dh+60;
//                miMaDialog.getWindow().setGravity(Gravity.CENTER);
//                miMaDialog.getWindow().setAttributes(params);
//                miMaDialog.show();
//
//            }
//        });
        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
        /* 注册相机回调函数 */
        manager.setListener(this);
        cameraView.setHight(dh);
        dialogBg = findViewById(R.id.dialog_bg);
        relativeLayout1 = findViewById(R.id.relative1);
        relativeLayout2 = findViewById(R.id.relative2);
        relativeLayout3 = findViewById(R.id.relative3);

        shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MiMaDialog4 miMaDialog = new MiMaDialog4(MianBanJiActivity3.this, baoCunBean.getMima());
                WindowManager.LayoutParams params = miMaDialog.getWindow().getAttributes();
                params.width = dw;
                params.height = dh;
//              miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                miMaDialog.getWindow().setAttributes(params);
                miMaDialog.show();
            }
        });

    }


    public void bck1(View view) {
        startActivity(new Intent(MianBanJiActivity3.this,YiChengXinXiActivity.class));

    }

    public void bck2(View view) {
        startActivity(new Intent(MianBanJiActivity3.this,HuiChangPingMianActivity.class));

    }

    public void fanhui(View view) {

        if (task != null){
            task.cancel();
            task=null;
        }
        if (timeThread!=null){
            timeThread.isRing=true;
            timeThread.interrupt();
            timeThread=null;
        }
        if (countDownTimer!=null)
        countDownTimer.cancel();
        rl1.setVisibility(View.VISIBLE);
        rl4.setVisibility(View.GONE);
        isTC=true;


    }


    public void bck3(View view) {
        if (mFacePassHandler!=null) {
            isTC=false;
            if (relativeLayout1.getChildCount() > 0) {
                relativeLayout1.removeAllViews();
            }
            if (relativeLayout2.getChildCount() > 0) {
                relativeLayout1.removeAllViews();
            }
            if (relativeLayout3.getChildCount() > 0) {
                relativeLayout1.removeAllViews();
            }
            if (task != null){
                task.cancel();
                task=null;
            }
            dialogBg.setVisibility(View.GONE);

            touxiang.setVisibility(View.GONE);
            touxiang.setImageBitmap(null);
            daojishi.setVisibility(View.GONE);
            dh3.setVisibility(View.GONE);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);
            tv5.setVisibility(View.GONE);
            dh2.setVisibility(View.GONE);
            dh1.setVisibility(View.GONE);
            concurrentHashMap.clear();
            rl1.setVisibility(View.GONE);
            rl4.setVisibility(View.VISIBLE);
            isHuoQu = true;
            mFacePassHandler.reset();
        }
    }

    @Override
    protected void onStop() {

        SettingVar.isButtonInvisible = false;
        // mToastBlockQueue.clear();
        mDetectResultQueue.clear();
        mFeedFrameQueue.clear();
        if (manager != null) {
            manager.release();
        }
        //  marqueeView.stopFlipping();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        //faceView.clear();
        // faceView.invalidate();
        //  if (shipingView!=null)
        // shipingView.start();
        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        //marqueeView.startFlipping();
    }


    @Override
    protected void onDestroy() {


        if (mFeedFrameQueue != null) {
            mFeedFrameQueue.clear();
        }
        if (mFeedFrameThread != null) {
            mFeedFrameThread.isIterrupt = true;
            mFeedFrameThread.interrupt();
        }

        if (tanChuangThread != null) {
            tanChuangThread.isRing = true;
            tanChuangThread.interrupt();
        }
        if (timeThread != null) {
            timeThread.isRing = true;
            timeThread.interrupt();
        }

        if (mRecognizeThread != null) {
            mRecognizeThread.isInterrupt = true;
            mRecognizeThread.interrupt();
        }

        //   mianBanJiView.desoure();
        //时钟
        //clockView.crean();
        unregisterReceiver(timeChangeReceiver);
        EventBus.getDefault().unregister(this);//解除订阅

        if (manager != null) {
            manager.release();
        }

//        if (mFacePassHandler != null) {
//            mFacePassHandler.release();
//        }

        if (synthesizer != null)
            synthesizer.release();

        timer.cancel();
        if (task != null)
            task.cancel();

        super.onDestroy();
    }
//3753

    private void showFacePassFace(final FacePassFace[] detectResult) {
//         runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                faceView.clear();
//                for (FacePassFace face : detectResult) {
//                   // float pitch=face.pose.pitch;
//                    //float roll=face.pose.roll;
//                  //  float yaw=face.pose.yaw;
////                    if (pitch>20 || pitch<-20 || roll>20 || roll<-20 || yaw>20 || yaw<-20){
////                        //提示
////                       // mianBanJiView.tishi(true);
////                    }else {
////                       // mianBanJiView.tishi(false);
////                    }
//
//                    boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
////                    StringBuilder faceIdString = new StringBuilder();
////                    faceIdString.append("ID = ").append(face.trackId);
////                  //  SpannableString faceViewString = new SpannableString(faceIdString);
////                  //  faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    StringBuilder faceRollString = new StringBuilder();
////                    faceRollString.append("旋转: ").append((int) face.pose.roll).append("°");
////                    StringBuilder facePitchString = new StringBuilder();
////                    facePitchString.append("上下: ").append((int) face.pose.pitch).append("°");
////                    StringBuilder faceYawString = new StringBuilder();
////                    faceYawString.append("左右: ").append((int) face.pose.yaw).append("°");
////                    StringBuilder faceBlurString = new StringBuilder();
////                    faceBlurString.append("模糊: ").append(String.format("%.2f", face.blur));
////                    StringBuilder faceAgeString = new StringBuilder();
////                    faceAgeString.append("年龄: ").append(face.age);
////                    StringBuilder faceGenderString = new StringBuilder();
////
////                    switch (face.gender) {
////                        case 0:
////                            faceGenderString.append("性别: 男");
////                            break;
////                        case 1:
////                            faceGenderString.append("性别: 女");
////                            break;
////                        default:
////                            faceGenderString.append("性别: ?");
////                    }
//
//                    Matrix mat = new Matrix();
//
//                    int cameraHeight = manager.getCameraheight();
//                    int cameraWidth = manager.getCameraWidth();
//
//                    float left = 0;
//                    float top = 0;
//                    float right = 0;
//                    float bottom = 0;
//                    switch (cameraRotation) {
//                        case 0:
//                            left = face.rect.left;
//                            top = face.rect.top;
//                            right = face.rect.right;
//                            bottom = face.rect.bottom;
//                            mat.setScale(mirror ? -1 : 1, 1);
//                            mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
//                            mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
//                            break;
//                        case 90:
//                            mat.setScale(mirror ? -1 : 1, 1);
//                            mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
//                            mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
//                            left = face.rect.top;
//                            top = cameraWidth - face.rect.right;
//                            right = face.rect.bottom;
//                            bottom = cameraWidth - face.rect.left;
//
//                            //北京面板机特有方向
////                            left =cameraHeight-face.rect.bottom;
////                            top = face.rect.left;
////                            right =cameraHeight-face.rect.top;
////                            bottom =face.rect.right;
//
//                            break;
//                        case 180:
//                            mat.setScale(1, mirror ? -1 : 1);
//                            mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
//                            mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
//                            left = face.rect.right;
//                            top = face.rect.bottom;
//                            right = face.rect.left;
//                            bottom = face.rect.top;
//                            break;
//                        case 270:
//                            mat.setScale(mirror ? -1 : 1, 1);
//                            mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
//                            mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
//                            left = cameraHeight - face.rect.bottom;
//                            top = face.rect.left;
//                            right = cameraHeight - face.rect.top;
//                            bottom = face.rect.right;
//                    }
//
//                    RectF drect = new RectF();
//                    RectF srect = new RectF(left, top, right, bottom);
//                    mat.mapRect(drect, srect);
//                    faceView.addRect(drect);
//                   // faceView.addId(faceIdString.toString());
//                   // faceView.addRoll(faceRollString.toString());
//                   // faceView.addPitch(facePitchString.toString());
//                   // faceView.addYaw(faceYawString.toString());
//                   // faceView.addBlur(faceBlurString.toString());
//                   // faceView.addAge(faceAgeString.toString());
//                   // faceView.addGenders(faceGenderString.toString());
//                }
//                faceView.invalidate();
//            }
//        });

    }


    private static final int REQUEST_CODE_CHOOSE_PICK = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //从相册选取照片后读取地址
            case REQUEST_CODE_CHOOSE_PICK:
                if (resultCode == RESULT_OK) {
                    String path = "";
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    if (cursor != null) {
                        cursor.moveToFirst();
                        path = cursor.getString(cursor.getColumnIndex(pojo[0]));
                    }
                    if (!TextUtils.isEmpty(path) && "file".equalsIgnoreCase(uri.getScheme())) {
                        path = uri.getPath();
                    }
                    if (TextUtils.isEmpty(path)) {
                        try {
                            path = FileUtil.getPath(getApplicationContext(), uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (TextUtils.isEmpty(path)) {
                        toast("图片选取失败！");
                        return;
                    }
//                    if (!TextUtils.isEmpty(path) && mFaceOperationDialog != null && mFaceOperationDialog.isShowing()) {
//                        EditText imagePathEdt = (EditText) mFaceOperationDialog.findViewById(R.id.et_face_image_path);
//                        imagePathEdt.setText(path);
//                    }
                }
                break;
        }
    }


    private void toast(String msg) {
        Toast.makeText(MianBanJiActivity3.this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 根据facetoken下载图片缓存
     */
    private static class FaceImageCache implements ImageLoader.ImageCache {

        private static final int CACHE_SIZE = 6 * 1024 * 1024;

        LruCache<String, Bitmap> mCache;

        public FaceImageCache() {
            mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {

                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }

    private class FacePassRequest extends Request<String> {

        HttpEntity entity;

        FacePassDetectionResult mFacePassDetectionResult;
        private Response.Listener<String> mListener;

        public FacePassRequest(String url, FacePassDetectionResult detectionResult, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            mFacePassDetectionResult = detectionResult;
            mListener = listener;
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String parsed;
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }

        @Override
        public String getBodyContentType() {
            return entity.getContentType().getValue();
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//        beginRecogIdArrayList.clear();

            for (FacePassImage passImage : mFacePassDetectionResult.images) {
                /* 将人脸图转成jpg格式图片用来上传 */
                YuvImage img = new YuvImage(passImage.image, ImageFormat.NV21, passImage.width, passImage.height, null);
                Rect rect = new Rect(0, 0, passImage.width, passImage.height);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                img.compressToJpeg(rect, 95, os);
                byte[] tmp = os.toByteArray();
                ByteArrayBody bab = new ByteArrayBody(tmp, String.valueOf(passImage.trackId) + ".jpg");
//            beginRecogIdArrayList.add(passImage.trackId);
                entityBuilder.addPart("image_" + String.valueOf(passImage.trackId), bab);
            }
            StringBody sbody = null;
            try {
                sbody = new StringBody(MianBanJiActivity3.group_name, ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            entityBuilder.addPart("group_name", sbody);
            StringBody data = null;
            try {
                data = new StringBody(new String(mFacePassDetectionResult.message), ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            entityBuilder.addPart("face_data", data);
            entity = entityBuilder.build();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                entity.writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream");
            }
            byte[] result = bos.toByteArray();
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                startActivity(new Intent(MianBanJiActivity3.this, SheZhiActivity2.class));
                finish();
            }

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("MianBanJiActivity3", "按下");

        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        // 设置初始化参数
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler); // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程

    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        params.put(SpeechSynthesizer.PARAM_SPEAKER, baoCunBean.getBoyingren() + ""); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_VOLUME, "8"); // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, baoCunBean.getYusu() + "");// 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, baoCunBean.getYudiao() + "");// 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);         // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());

        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            // toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(String event) {
        if (event.equals("mFacePassHandler")) {
            mFacePassHandler = MyApplication.myApplication.getFacePassHandler();

            return;
        }


        if (event.equals("guanbimain")) {
            finish();
            return;
        }
        Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, event, TastyToast.LENGTH_LONG, TastyToast.INFO);
        tastyToast.setGravity(Gravity.CENTER, 0, 0);
        tastyToast.show();

    }


    class TimeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case Intent.ACTION_TIME_TICK:
                    //mianBanJiView.setTime(DateUtils.time(System.currentTimeMillis()+""));
                    // String riqi11 = DateUtils.getWeek(System.currentTimeMillis()) + "   " + DateUtils.timesTwo(System.currentTimeMillis() + "");
                    //  riqi.setTypeface(tf);
//                    List<Subject> subjectList=subjectBox.query().contains(Subject_.birthday,DateUtils.nyr(System.currentTimeMillis()+"")).and()
//                            .equal(Subject_.peopleType,"普通访客")
//                            .or().equal(Subject_.peopleType,"白名单").build().find();
//
                    String times = DateUtils.timeMinute(System.currentTimeMillis() + "");


                    //3个月删掉打卡记录跟陌生人记录
                    if (times.split(":")[0].equals("15") && times.split(":")[1].equals("01")) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Date dNow = new Date();   //当前时间
                                Calendar calendar = Calendar.getInstance(); //得到日历
                                calendar.setTime(dNow);//把当前时间赋给日历
                                calendar.add(Calendar.MONTH, -3);  //设置为前3月
                                calendar.getTime();   //得到前3月的时间
                                Log.d("MianBanJiActivity3", "dBefore.getTime():" + calendar.getTime().getTime());
                                Log.d("MianBanJiActivity3", DateUtils.time(calendar.getTime().getTime() + ""));
                                QueryBuilder<DaKaBean> builderDaKaBean = daKaBeanBox.query();
                                builderDaKaBean.less(DaKaBean_.time, calendar.getTime().getTime());
                                LazyList<DaKaBean> daKaBeanList = builderDaKaBean.build().findLazy();
                                for (DaKaBean daKaBean : daKaBeanList) {
                                    daKaBeanBox.remove(daKaBean);
                                }

                                QueryBuilder<MSRBean> builderMsr = msrBeanBox.query();
                                builderMsr.less(MSRBean_.time2, calendar.getTime().getTime());
                                LazyList<MSRBean> msrBeans = builderMsr.build().findLazy();
                                for (MSRBean msrBean : msrBeans) {
                                    msrBeanBox.remove(msrBean);
                                }

                            }
                        }).start();

                    }


                    if (times.split(":")[0].equals("06") && times.split(":")[1].equals("30")) {
                        Log.d("TimeChangeReceiver", "同步");
                        final List<BenDiJiLuBean> benDiJiLuBeans = benDiJiLuBeanBox.getAll();
                        final int size = benDiJiLuBeans.size();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < size; i++) {
                                    while (isSC) {
                                        isSC = false;
                                        link_shangchuanjilu2(benDiJiLuBeans.get(i));
                                    }

                                }

                            }
                        }).start();

                    }

                    if (times.split(":")[0].equals("22") && times.split(":")[1].equals("30")) {
                        //删除今天的访客
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<Subject> subjectList = subjectBox.query().contains(Subject_.birthday, DateUtils.nyr(System.currentTimeMillis() + "")).and()
                                        .equal(Subject_.peopleType, "普通访客")
                                        .or().equal(Subject_.peopleType, "白名单").build().find();
                                for (Subject s : subjectList) {
                                    Log.d("TimeChangeReceiver", s.toString());
                                    try {
                                        mFacePassHandler.deleteFace(s.getTeZhengMa().getBytes());
                                        subjectBox.remove(s);
                                    } catch (FacePassException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }).start();

                    }
                    if (times.split(":")[0].equals("10") && times.split(":")[1].equals("01")) {
                        //如果用户在22：30 前关了机 就需要第二天早上删除昨天的访客
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<Subject> subjectList = subjectBox.query().contains(Subject_.birthday, DateUtils.nyr((System.currentTimeMillis() - 86400000) + "")).and()
                                        .equal(Subject_.peopleType, "普通访客")
                                        .or().equal(Subject_.peopleType, "白名单").build().find();
                                for (Subject s : subjectList) {
                                    Log.d("TimeChangeReceiver", s.toString());
                                    try {
                                        mFacePassHandler.deleteFace(s.getTeZhengMa().getBytes());
                                        subjectBox.remove(s);
                                    } catch (FacePassException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }).start();

                    }


                    AssetManager mgr = getAssets();
                    //  Univers LT 57 Condensed
                    //   Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
                    //    Typeface tf2 = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
                    //    Typeface tf3 = Typeface.createFromAsset(mgr, "fonts/kai.ttf");
                    //   String riqi2 = DateUtils.timesTwo(System.currentTimeMillis() + "") + "   " + DateUtils.getWeek(System.currentTimeMillis());
                    //  riqi.setTypeface(tf);
                    //  riqi.setText(riqi2);
                    ///   xiaoshi.setTypeface(tf);
                    //   xiaoshi.setText(DateUtils.timeMinute(System.currentTimeMillis() + ""));

                    Date date = new Date();
                    date.setTime(System.currentTimeMillis());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int t = calendar.get(Calendar.HOUR_OF_DAY);

                    //每过一分钟 触发
                    if (baoCunBean != null && baoCunBean.getDangqianShiJian() != null && !baoCunBean.getDangqianShiJian().equals(DateUtils.timesTwo(System.currentTimeMillis() + "")) && t >= 6) {

                        //一天请求一次
                        try {
                            if (baoCunBean.getDangqianChengShi2() == null) {

                                return;
                            }
                            Log.d("TimeChangeReceiver", baoCunBean.getDangqianChengShi());
                            okHttpClient = new OkHttpClient();
                            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                                    .get()
                                    .url("http://apis.juhe.cn/simpleWeather/query?city=" +
                                            baoCunBean.getDangqianChengShi() + "&key=8ee00f3d480b636d67d6a8966f07ffb7");
//                                    .url("http://v.juhe.cn/weather/index?format=1&cityname=" +
//                                            baoCunBean.getDangqianChengShi() + "&key=356bf690a50036a5cfc37d54dc6e8319");

                            // step 3：创建 Call 对象
                            Call call = okHttpClient.newCall(requestBuilder.build());
                            //step 4: 开始异步请求
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("AllConnects", "请求失败" + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    Log.d("AllConnects", "请求成功" + call.request().toString());
                                    //获得返回体
                                    try {

//                                        ResponseBody body = response.body();
//                                        String ss = body.string().trim();
//                                        Log.d("AllConnects", "天气" + ss);
//                                        JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
//                                        Gson gson = new Gson();
//                                        final TianQiBean renShu = gson.fromJson(jsonObject, TianQiBean.class);
//
//                                        final TodayBean todayBean = new TodayBean();
//                                        todayBean.setId(123456L);
//                                        todayBean.setTemperature(renShu.getResult().getToday().getTemperature());//温度
//                                        todayBean.setWeather(renShu.getResult().getToday().getWeather()); //天气
//                                        todayBean.setWind(renShu.getResult().getToday().getWind()); //风力
//                                        todayBean.setUv_index(renShu.getResult().getToday().getUv_index()); //紫外线
//                                        todayBean.setHumidity(renShu.getResult().getSk().getHumidity());//湿度
//                                        todayBean.setDressing_advice(renShu.getResult().getToday().getDressing_advice());
//
//                                        todayBeanBox.put(todayBean);
//                                        baoCunBean.setDangqianShiJian(DateUtils.timesTwo(System.currentTimeMillis() + ""));
//                                        baoCunBeanDao.put(baoCunBean);

                                        ResponseBody body = response.body();
                                        String ss = body.string().trim();
                                        Log.d("AllConnects", "天气" + ss);
                                        JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                                        Gson gson = new Gson();
                                        final TQBean renShu = gson.fromJson(jsonObject, TQBean.class);

                                        final TodayBean todayBean = new TodayBean();
                                        todayBean.setId(123456L);
                                        todayBean.setTemperature(renShu.getResult().getFuture().get(0).getTemperature());//温度
                                        todayBean.setWeather(renShu.getResult().getRealtime().getInfo()); //天气
                                        todayBean.setWind(renShu.getResult().getRealtime().getDirect()); //风力
                                        todayBean.setUv_index(renShu.getResult().getRealtime().getAqi()); //紫外线
                                        todayBean.setHumidity(renShu.getResult().getRealtime().getHumidity());//湿度
                                        //todayBean.setDressing_advice(renShu.getResult().getRealtime().getDressing_advice());

                                        todayBeanBox.put(todayBean);
                                        baoCunBean.setDangqianShiJian(DateUtils.timesTwo(System.currentTimeMillis() + ""));
                                        baoCunBeanDao.put(baoCunBean);


                                    } catch (Exception e) {
                                        Log.d("WebsocketPushMsg", e.getMessage() + "ttttt");
                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                    //  Toast.makeText(context, "1 min passed", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_TIME_CHANGED:
                    //设置了系统时间
                    // Toast.makeText(context, "system time changed", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_TIMEZONE_CHANGED:
                    //设置了系统时区的action
                    //  Toast.makeText(context, "system time zone changed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    //轮播适配器
    private class TestNomalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.dbg_1,
                R.drawable.ceshi,
                R.drawable.ceshi3,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }


    //上传识别记录
    private void link_shangchuanjilu(final Subject subject) {
        // final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = null;
        okhttp3.Request.Builder requestBuilder = null;

        String urll = null;
        if (URL_SS != null && !URL_SS.equals("")) {
            try {
                urll = URL_SS;
                body = new FormBody.Builder()
                        .add("name", subject.getName()) //
                        .add("b64", BitmapUtil.bitmaptoString(BitmapFactory.decodeFile(subject.getZpPath()))) //公司di
                        //.add("companyName",subject.getCompanyName()+"") //公司名称
                        //.add("storeId", subject.getStoreId()+"") //门店id
                        .add("imei", imei) //门店名称
                        .add("subjectId", subject.getSid() + "") //员工ID
                        .add("subjectType", subject.getPeopleType() == null ? "员工" : subject.getPeopleType()) //人员类型
                        .add("department", subject.getDepartmentName() + "") //部门
                        .add("discernPlace", baoCunBean.getTuisongDiZhi() + "")//识别地点
                        // .add("discernAvatar",  "") //头像
                        .add("identificationTime", DateUtils.time(System.currentTimeMillis() + ""))//时间
                        .build();
                requestBuilder = new okhttp3.Request.Builder()
                        .header("Content-Type", "application/json")
                        .post(body)
                        .url(urll);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            body = new FormBody.Builder()
                    // .add("name", subject.getName()) //
                    //  .add("b64", subject.getCompanyId()+"") //公司di
                    //.add("companyName",subject.getCompanyName()+"") //公司名称
                    //.add("storeId", subject.getStoreId()+"") //门店id
                    .add("imei", imei)
                    .add("subjectId", subject.getId() + "") //员工ID
                    .add("subjectType", subject.getPeopleType() == null ? "员工" : subject.getPeopleType()) //人员类型
                    // .add("department", subject.getDepartmentName()+"") //部门
                    .add("discernPlace", baoCunBean.getTuisongDiZhi() + "")//识别地点
                    // .add("discernAvatar",  "") //头像
                    .add("identificationTime", DateUtils.time(System.currentTimeMillis() + ""))//时间
                    .build();
            urll = MyApplication.URL;
            requestBuilder = new okhttp3.Request.Builder()
                    .header("Content-Type", "application/json")
                    .post(body)
                    .url(urll + "/app/historySave");
        }

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                BenDiJiLuBean bean = new BenDiJiLuBean();
                bean.setSubjectId(subject.getId());
                bean.setDiscernPlace(baoCunBean.getTuisongDiZhi() + "");
                bean.setSubjectType(subject.getPeopleType());
                bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
                benDiJiLuBeanBox.put(bean);

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);


                } catch (Exception e) {
                    BenDiJiLuBean bean = new BenDiJiLuBean();
                    bean.setSubjectId(subject.getId());
                    bean.setDiscernPlace(baoCunBean.getTuisongDiZhi() + "");
                    bean.setSubjectType(subject.getPeopleType());
                    bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
                    benDiJiLuBeanBox.put(bean);

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
                }
            }
        });
    }

    //信鸽信息处理
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(XGBean xgBean) {
        if (MyApplication.myApplication.getFacePassHandler() != null) {
            try {
                MianBanJiActivity3.isL = false;
                tsxxChuLi.setData(xgBean, MianBanJiActivity3.this, MyApplication.myApplication.getFacePassHandler());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    //上传识别记录2
    private void link_shangchuanjilu2(final BenDiJiLuBean subject) {
        //  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = null;

        body = new FormBody.Builder()
                //.add("name", subject.getName()) //
                //.add("companyId", subject.getCompanyId()+"") //公司di
                //.add("companyName",subject.getCompanyName()+"") //公司名称
                //.add("storeId", subject.getStoreId()+"") //门店id
                //.add("storeName", subject.getStoreName()+"") //门店名称
                .add("subjectId", subject.getSubjectId() + "") //员工ID
                .add("subjectType", subject.getSubjectType() + "") //人员类型
                // .add("department", subject.getPosition()+"") //部门
                .add("discernPlace", baoCunBean.getTuisongDiZhi() + "")//识别地点
                // .add("discernAvatar",  "") //头像
                .add("identificationTime", subject.getIdentificationTime() + "")//时间
                .build();


        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(MyApplication.URL + "/app/historySave");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                isSC = true;
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);
                    //成功的话 删掉本地保存的记录
                    benDiJiLuBeanBox.remove(subject.getId());

                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");

                } finally {
                    isSC = true;
                }
            }
        });
    }


    public static class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Intent i = new Intent(context, MianBanJiActivity3.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }


    private void guanPing() {

        sendBroadcast(new Intent("com.android.internal.policy.impl.hideNavigationBar"));
        sendBroadcast(new Intent("com.android.systemui.statusbar.phone.statusclose"));
    }


    private void start() {
        //初始化
        File file = new File(MyApplication.SDPATH);
        if (!file.exists()) {
            Log.d("ggg", "file.mkdirs():" + file.mkdirs());
        }
        File file2 = new File(MyApplication.SDPATH2);
        if (!file2.exists()) {
            Log.d("ggg", "file.mkdirs():" + file2.mkdirs());
        }
        FacePassHandler.initSDK(getApplicationContext());
    }


    public void addView(LinearLayout relativeLayout1, LinearLayout relativeLayout2, LinearLayout relativeLayout3) {
        if (relativeLayout1.getChildCount() != 0) {
            addView = relativeLayout1.getChildAt(relativeLayout1.getChildCount() - 1);
            relativeLayout1.removeAllViews();
            if (relativeLayout2.getChildCount() == 0) {
                addLeftAnimator(addView);
                relativeLayout2.addView(addView, 0);
            } else {
                if (relativeLayout3.getChildCount() == 0 && relativeLayout2.getChildCount() != 0) {
                    addRightAnimator(addView);
                    relativeLayout3.addView(addView);
                } else {
                    View addView2 = relativeLayout2.getChildAt(relativeLayout2.getChildCount() - 1);
                    relativeLayout2.removeAllViews();
                    addLeftAnimator(addView);
                    relativeLayout2.addView(addView);
                    relativeLayout3.removeAllViews();
                    addRightAnimator(addView2);
                    relativeLayout3.addView(addView2);
                }
            }
        }
    }

    public void addLeftAnimator(View view) {
        List<Animator> animators = new ArrayList<>();//设置一个装动画的集合
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.72f);
        scaleX.setDuration(200);//设置持续时间
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.72f);
        scaleY.setDuration(200);//设置持续时间
        ObjectAnimator alpha = ObjectAnimator.ofFloat(viewRemove, "alpha", 1.0f, 0.6f);
        alpha.setDuration(400);//设置持续时间
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", dw * -0.18f, -dw * 0.25f);
        translationX.setDuration(400);//设置持续时间
        translationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //底部列表的

            }
        });
        translationX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isDH = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDH = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        animators.add(scaleX);
        animators.add(scaleY);
        animators.add(translationX);
        animators.add(alpha);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//动画集
        btnSexAnimatorSet.playTogether(animators);//设置一起播放
        btnSexAnimatorSet.start();//开始播放

    }

    public void addRightAnimator(View view) {
        List<Animator> animators = new ArrayList<>();//设置一个装动画的集合
        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.7f);
        scaleX.setDuration(200);//设置持续时间
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.7f);
        scaleY.setDuration(200);//设置持续时间
        ObjectAnimator alpha = ObjectAnimator.ofFloat(viewRemove, "alpha", 1.0f, 0.6f);
        alpha.setDuration(400);//设置持续时间
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -dw * 0.05f, dw * 0.0005f);
        translationX.setDuration(400);//设置持续时间
        translationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //底部列表的

            }
        });
        translationX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isDH = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDH = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
        animators.add(scaleX);
        animators.add(scaleY);
        animators.add(translationX);
        animators.add(alpha);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//动画集
        btnSexAnimatorSet.playTogether(animators);//设置一起播放
        btnSexAnimatorSet.start();//开始播放
    }


    //上传识别记录
    private void link_shangchuanjilu22(String nianling,String xingbie,String yanzhi,String biaoqing,String guanzhu,String fuzhi, String b64,String baifenbi) {
        Log.d("MianBanJiActivity3", baifenbi);
        RequestBody body = new FormBody.Builder()
                .add("age", nianling)
                .add("sex", xingbie)
                .add("skinType", fuzhi)
                .add("expression", biaoqing)
                .add("charmValue", yanzhi)
                .add("attentionTime", guanzhu)
                .add("avatorBase64", b64)
                .add("faceScoreRankPercent", baifenbi)
                .build();

        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                .header("Content-Type", "application/json")
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi() + "facescore/save");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "请求数据失败！", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        tastyToast.setGravity(Gravity.CENTER, 0, 0);
                        tastyToast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "上传识别记录" + ss);
                    JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
                    long dsa= jsonObject.get("faceScore").getAsJsonObject().get("id").getAsLong();
                    erweima(baoCunBean.getHoutaiDiZhi() + "?id="+dsa);

                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
                }
            }
        });


    }

    //首先登录-->获取所有主机-->创建或者删除或者更新门禁
    private void getOkHttpClient2(final Bitmap bitmap) {

        final String ss = BitmapUtil.bitmaptoString(bitmap);
        RequestBody body = new FormBody.Builder()
                .add("api_key", "BBDRR-nwJM38qGHUiBV0k4eMUZ2jDsa1")
                .add("api_secret", "YDhcIhcjc5OVnDVQvspwSoSQnjM-fWYn")
                .add("image_base64", ss)
                .add("return_attributes", "gender,age,emotion,eyestatus,beauty,skinstatus")
                .build();

        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        //requestBuilder.header("User-Agent", "Koala Admin");
        //requestBuilder.header("Content-Type","application/json");
        requestBuilder.post(body);
        requestBuilder.url("https://api-cn.faceplusplus.com/facepp/v3/detect");
        final okhttp3.Request request = requestBuilder.build();

        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("YanShiActivity", "请求失败" + e.getMessage());
                isHuoQu=true;
                mFacePassHandler.reset();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "请求数据失败！", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            tastyToast.setGravity(Gravity.CENTER, 0, 0);
                            tastyToast.show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MianBanJiActivity3.this)
                                    .load(new BitmapDrawable(getResources(), bitmap))
                                    .apply(myOptions)
                                    .into(touxiang);
                        }
                    });
                    count = 0;
                    timeThread = new TimeThread();
                    timeThread.start();

                    String s = response.body().string();
                    Log.d("YanShiActivitytttttt", "检测" + s);
                    JsonObject jsonObject = GsonUtil.parse(s).getAsJsonObject();
                    Gson gson = new Gson();
                    final YanZhiBean menBean = gson.fromJson(jsonObject, YanZhiBean.class);
                    if (menBean.getFaces() != null && menBean.getFaces().get(0) != null) {

                        //更新
                        YanZhiBean.FacesBean.AttributesBean.SkinstatusBean nn = menBean.getFaces().get(0).getAttributes().getSkinstatus();
                        final HashMap<Double, String> kk = new HashMap<>();
                        final double[] a = new double[4];
                        a[0] = nn.getAcne();
                        a[1] = nn.getDark_circle();
                        a[2] = nn.getHealth();
                        a[3] = nn.getStain();
                        kk.put(a[0], "青春痘");
                        kk.put(a[1], "黑眼圈");
                        kk.put(a[2], "健康");
                        kk.put(a[3], "暗淡");
                        Arrays.sort(a);  //进行排序

                        YanZhiBean.FacesBean.AttributesBean.EmotionBean nn2 = menBean.getFaces().get(0).getAttributes().getEmotion();
                        final HashMap<Double, String> kk2 = new HashMap<>();
                        final double[] a2 = new double[7];
                        a2[0] = nn2.getAnger();
                        a2[1] = nn2.getDisgust();
                        a2[2] = nn2.getFear();
                        a2[3] = nn2.getHappiness();
                        a2[4] = nn2.getNeutral();
                        a2[5] = nn2.getSadness();
                        a2[6] = nn2.getSurprise();
                        kk2.put(a2[0], "愤怒");
                        kk2.put(a2[1], "厌恶");
                        kk2.put(a2[2], "害怕");
                        kk2.put(a2[3], "高兴");
                        kk2.put(a2[4], "平静");
                        kk2.put(a2[5], "悲伤");
                        kk2.put(a2[6], "惊讶");
                        Arrays.sort(a2);  //进行排序

                        String xb = "";
                        if (menBean.getFaces().get(0).getAttributes().getGender().getValue().equals("Male")) {
                            xb = "男";
                        } else {
                            xb = "女";
                        }

                        float yan = (float) (xb.equals("女") ? menBean.getFaces().get(0).getAttributes().getBeauty().getFemale_score() : menBean.getFaces().get(0).getAttributes().getBeauty().getMale_score());
                        float fl = (yan + 25) >= 100 ? 99.9f : (yan + 25);
                        if (fl < 80) {
                            fl = (80.0f + (fl / 100f));
                        }

                        final float finalFl = fl;
                        final String finalXb = xb;
                       final DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
                       float ll2 = 0;
                        ll2 = 74 + (Float.valueOf(decimalFormat2.format(finalFl)) / 6.0f);
                        if (ll2 >= 100) {
                            ll2 = 99.99f;
                        }
                        final String baifen = decimalFormat2.format(ll2) + "";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                meilizhi.setText(Float.valueOf(decimalFormat.format(finalFl)) + "");
                                biaoqing.setText(kk2.get(a2[6]));
                                fuzhi.setText(kk.get(a[3]));
                                xingbie.setText(finalXb);
                                baifenbi.setText(baifen+"%");
                            }
                        });

                        String nl = "";
                        if (xb.equals("女")) {
                            nl = (menBean.getFaces().get(0).getAttributes().getAge().getValue() - 10) + "";
                            final String finalNl = nl;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nianling.setText(finalNl);
                                }
                            });

                        } else {
                            nl = (menBean.getFaces().get(0).getAttributes().getAge().getValue() - 10) + "";
                            final String finalNl = nl;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nianling.setText(finalNl);
                                }
                            });

                        }

                        DecimalFormat decimalFormat = new DecimalFormat("0.0");
                        link_shangchuanjilu22(nl,xb,Float.valueOf(decimalFormat.format(finalFl)) + "",kk2.get(a2[6]),22+"",kk.get(a[3]),ss,baifen );

                                if (!isTC) {
                                    //启动定时器或重置定时器
                                    if (task != null) {
                                        task.cancel();
                                        //timer.cancel();
                                        task = new TimerTask() {
                                            @Override
                                            public void run() {

                                                Message message = new Message();
                                                message.what = 120;

                                                mHandler.sendMessage(message);

                                            }
                                        };
                                        timer.schedule(task, 30000);
                                    } else {
                                        task = new TimerTask() {
                                            @Override
                                            public void run() {
                                                Message message = new Message();
                                                message.what = 120;

                                                mHandler.sendMessage(message);
                                            }
                                        };
                                        timer.schedule(task, 30000);
                                    }
                                }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /** 倒计时60秒，一次1秒 */
                                countDownTimer = new CountDownTimer(15*1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        if ((millisUntilFinished/1000)<=10){
                                            daojishi.setVisibility(View.VISIBLE);
                                            daojishi.setText(millisUntilFinished/1000+" 秒后清空");
                                        }

                                    }
                                    @Override
                                    public void onFinish() {

                                        if (timeThread!=null){
                                            timeThread.isRing=true;
                                            timeThread.interrupt();
                                            timeThread=null;
                                        }
                                        if (!isTC){
                                            touxiang.setVisibility(View.GONE);
                                            touxiang.setImageBitmap(null);
                                            daojishi.setVisibility(View.GONE);
                                            dh3.setVisibility(View.GONE);
                                            tv1.setVisibility(View.GONE);
                                            tv2.setVisibility(View.GONE);
                                            tv3.setVisibility(View.GONE);
                                            tv4.setVisibility(View.GONE);
                                            tv5.setVisibility(View.GONE);
                                            dh2.setVisibility(View.GONE);
                                            dh1.setVisibility(View.GONE);
                                            concurrentHashMap.clear();
                                            rl1.setVisibility(View.GONE);
                                            rl4.setVisibility(View.VISIBLE);
                                            isHuoQu = true;
                                            mFacePassHandler.reset();
                                        }

                                    }
                                };
                                countDownTimer.start();
                            }
                        });

                        donghua(touxiang);
                        donghua(dh3);
                        donghua(tv1);
                        donghua(tv2);
                        donghua(tv3);
                        donghua(tv4);
                        donghua(tv5);
                        SystemClock.sleep(2000);
                        donghua(dh2);
                        SystemClock.sleep(2000);
                        donghua(dh1);




                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "获取数据失败，5秒后自动返回", TastyToast.LENGTH_LONG, TastyToast.INFO);
                                tastyToast.setGravity(Gravity.CENTER, 0, 0);
                                tastyToast.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SystemClock.sleep(5000);
                                        rl1.setVisibility(View.VISIBLE);
                                        rl4.setVisibility(View.GONE);
                                    }
                                }).start();
                            }
                        });

                    }
                    Log.d("YanShiActivitytttttt", "更新成功");

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast tastyToast = TastyToast.makeText(MianBanJiActivity3.this, "获取数据失败，5秒后自动返回", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            tastyToast.setGravity(Gravity.CENTER, 0, 0);
                            tastyToast.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SystemClock.sleep(5000);
                                            rl1.setVisibility(View.VISIBLE);
                                            rl4.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
                }

            }

        });

    }


    public void donghua(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                anim.setDuration(3000);
                anim.setRepeatMode(ValueAnimator.RESTART);
                Interpolator interpolator = new DecelerateInterpolator(2f);
                anim.setInterpolator(interpolator);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float currentValue = (float) animation.getAnimatedValue();
                        view.setAlpha(currentValue);
                        view.requestLayout();
                    }
                });
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                anim.start();
            }
        });


    }


    public void erweima(String ss) {
        final Bitmap bitmap = ZXingUtils.createQRImage(ss, 500, 500);
        erweima.setImageBitmap(bitmap);
    }


}
