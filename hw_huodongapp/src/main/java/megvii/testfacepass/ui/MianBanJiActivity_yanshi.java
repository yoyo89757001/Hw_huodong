//package megvii.testfacepass.ui;
//
//import android.Manifest;
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.CursorLoader;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.content.res.AssetManager;
//import android.content.res.Configuration;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.ImageFormat;
//import android.graphics.Matrix;
//import android.graphics.Rect;
//import android.graphics.RectF;
//
//import android.graphics.YuvImage;
//import android.graphics.drawable.BitmapDrawable;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.LruCache;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.DecelerateInterpolator;
//import android.view.animation.Interpolator;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.HttpHeaderParser;
//import com.android.volley.toolbox.ImageLoader;
//import com.badoo.mobile.util.WeakHandler;
//import com.baidu.tts.client.SpeechSynthesizer;
//import com.baidu.tts.client.SpeechSynthesizerListener;
//import com.baidu.tts.client.TtsMode;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.ChecksumException;
//import com.google.zxing.FormatException;
//import com.google.zxing.NotFoundException;
//import com.google.zxing.RGBLuminanceSource;
//import com.google.zxing.Reader;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.QRCodeReader;
//import com.jude.rollviewpager.adapter.StaticPagerAdapter;
//import com.sdsmdg.tastytoast.TastyToast;
//import com.tencent.android.tpush.XGIOperateCallback;
//import com.tencent.android.tpush.XGPushConfig;
//import com.tencent.android.tpush.XGPushManager;
//import com.yatoooon.screenadaptation.ScreenAdapterTools;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.ByteArrayBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.util.CharsetUtils;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import butterknife.ButterKnife;
//import io.objectbox.Box;
//
//import megvii.facepass.FacePassException;
//import megvii.facepass.FacePassHandler;
//import megvii.facepass.types.FacePassDetectionResult;
//import megvii.facepass.types.FacePassFace;
//import megvii.facepass.types.FacePassImage;
//import megvii.facepass.types.FacePassImageRotation;
//import megvii.facepass.types.FacePassImageType;
//import megvii.facepass.types.FacePassRecognitionResult;
//import megvii.facepass.types.FacePassRecognitionResultType;
//import megvii.testfacepass.MyApplication;
//import megvii.testfacepass.R;
//import megvii.testfacepass.beans.BaoCunBean;
//import megvii.testfacepass.beans.BenDiJiLuBean;
//import megvii.testfacepass.beans.GuanHuai;
//import megvii.testfacepass.beans.GuanHuai_;
//import megvii.testfacepass.beans.Subject;
//import megvii.testfacepass.beans.Subject_;
//import megvii.testfacepass.beans.TianQiBean;
//import megvii.testfacepass.beans.TodayBean;
//import megvii.testfacepass.beans.XGBean;
//import megvii.testfacepass.beans.XinXiAll;
//import megvii.testfacepass.beans.XinXiIdBean;
//import megvii.testfacepass.beans.XinXiIdBean_;
//import megvii.testfacepass.camera.CameraManager;
//import megvii.testfacepass.camera.CameraPreview;
//import megvii.testfacepass.camera.CameraPreviewData;
//import megvii.testfacepass.dialog.MiMaDialog4;
//import megvii.testfacepass.dialog.XuanZeDialog;
//import megvii.testfacepass.dialog.YanShiDialog;
//import megvii.testfacepass.tts.control.InitConfig;
//import megvii.testfacepass.tts.control.MySyntherizer;
//import megvii.testfacepass.tts.control.NonBlockSyntherizer;
//import megvii.testfacepass.tts.listener.UiMessageListener;
//import megvii.testfacepass.tts.util.OfflineResource;
//import megvii.testfacepass.tuisong_jg.TSXXChuLi;
//
//import megvii.testfacepass.utils.DateUtils;
//import megvii.testfacepass.utils.FacePassUtil;
//import megvii.testfacepass.utils.FileUtil;
//import megvii.testfacepass.utils.GetDeviceId;
//import megvii.testfacepass.utils.GsonUtil;
//import megvii.testfacepass.utils.NV21ToBitmap;
//import megvii.testfacepass.utils.SettingVar;
//import megvii.testfacepass.utils.ToastUtils;
//import megvii.testfacepass.view.GlideCircleTransform;
//import megvii.testfacepass.view.GlideRoundTransform;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//
//
//public class MianBanJiActivity_yanshi extends Activity implements CameraManager.CameraListener {
//
//
//    private Box<BenDiJiLuBean> benDiJiLuBeanBox = null;
//   // private  ConcurrentHashMap map =new ConcurrentHashMap();
//    private Box<Subject> subjectBox = null;
//    protected Handler mainHandler;
//    private String appId = "11644783";
//    private String appKey = "knGksRFLoFZ2fsjZaMC8OoC7";
//    private String secretKey = "IXn1yrFezEo55LMkzHBGuTs1zOkXr9P4";
//    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
//    private TtsMode ttsMode = TtsMode.MIX;
//    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
//    // assets目录下bd_etts_speech_female.data为离线男声模型；bd_etts_speech_female.data为离线女声模型
//    private String offlineVoice = OfflineResource.VOICE_FEMALE;
//    // 主控制类，所有合成控制方法从这个类开始
//    private MySyntherizer synthesizer;
//    private long lingshiId=-1;
//    private static boolean chifan=false;
//    private RequestOptions myOptions = new RequestOptions()
//            .fitCenter()
//           // .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .error(R.drawable.erroy_bg)
//            .transform(new GlideCircleTransform(MyApplication.myApplication, 0, Color.parseColor("#ffffffff")));
//    // .transform(new GlideRoundTransform(MainActivity.this,10));
//
//    private RequestOptions myOptions2 = new RequestOptions()
//            .fitCenter()
//            .error(R.drawable.erroy_bg)
//            //   .transform(new GlideCircleTransform(MyApplication.myApplication, 2, Color.parseColor("#ffffffff")));
//            .transform(new GlideRoundTransform(MianBanJiActivity_yanshi.this, 20));
//    private TextView chifantv;
//    private LinearLayout ghjj;
//    private ImageView qiandaobg;
//   private OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .writeTimeout(20000, TimeUnit.MILLISECONDS)
//            .connectTimeout(20000, TimeUnit.MILLISECONDS)
//            .readTimeout(20000, TimeUnit.MILLISECONDS)
////				    .cookieJar(new CookiesManager())
//            //        .retryOnConnectionFailure(true)
//            .build();
//    private final Timer timer = new Timer();
//    private TimerTask task;
//
//    private LinkedBlockingQueue<Subject> linkedBlockingQueue;
//    /* 人脸识别Group */
//    private static final String group_name = "facepasstestx";
//    /* 程序所需权限 ：相机 文件存储 网络访问 */
//    private static final int PERMISSIONS_REQUEST = 1;
//    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
//    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
//    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
//    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
//    private static final String PERMISSION_ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
//    private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
//    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE,
//            PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE,PERMISSION_ACCESS_WIFI_STATE,PERMISSION_READ_PHONE_STATE};
//
//    private MediaPlayer mMediaPlayer;
//  //  private WindowManager wm;
//    /* SDK 实例对象 */
//    public  FacePassHandler mFacePassHandler;
//    /* 相机实例 */
//    private CameraManager manager;
//    /* 显示人脸位置角度信息 */
//   // private XiuGaiGaoKuanDialog dialog = null;
//    /* 相机预览界面 */
//    private CameraPreview cameraView;
//  //  private boolean isAnXia = true;
//
//    /* 在预览界面圈出人脸 */
//    // private FaceView faceView;
//    /* 相机是否使用前置摄像头 */
//    private static boolean cameraFacingFront = true;
//    /* 相机图片旋转角度，请根据实际情况来设置
//     * 对于标准设备，可以如下计算旋转角度rotation
//     * int windowRotation = ((WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
//     * Camera.CameraInfo info = new Camera.CameraInfo();
//     * Camera.getCameraInfo(cameraFacingFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK, info);
//     * int cameraOrientation = info.orientation;
//     * int rotation;
//     * if (cameraFacingFront) {
//     *     rotation = (720 - cameraOrientation - windowRotation) % 360;
//     * } else {
//     *     rotation = (windowRotation - cameraOrientation + 360) % 360;
//     * }
//     */
//    private int cameraRotation;
//    private static final int cameraWidth = 1280;
//    private static final int cameraHeight = 720;
//    private boolean isOP=true;
//    private int heightPixels;
//    private int widthPixels;
//    int screenState = 0;// 0 横 1 竖
//
//    ArrayBlockingQueue<FacePassDetectionResult> mDetectResultQueue;
//    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;
//    /*recognize thread*/
//    RecognizeThread mRecognizeThread;
//    FeedFrameThread mFeedFrameThread;
//    TanChuangThread tanChuangThread;
//    private ConcurrentHashMap<Long, Integer> concurrentHashMap = new ConcurrentHashMap<Long, Integer>();
//    private int dw, dh;
//
//    private Box<BaoCunBean> baoCunBeanDao = null;
//    private Box<DaKaBean> daKaBeanBox = null;
//    private Box<TodayBean> todayBeanBox = null;
//    private BaoCunBean baoCunBean = null;
//    private TimeChangeReceiver timeChangeReceiver;
//    private WeakHandler mHandler;
//
//   private TSXXChuLi tsxxChuLi=null;
//    private static boolean isSC=true;
//    private RelativeLayout linearLayout;
//    private static boolean isDH=false;
//   // private static boolean isShow=true;
//   // private static boolean isERM=true;
//    private static boolean liet=true;
//    private int w,h;
//
//    private Box<GuanHuai> guanHuaiBox = null;
//    private Box<XinXiAll> xinXiAllBox = null;
//    private Box<XinXiIdBean> xinXiIdBeanBox = null;
//    public static boolean isL=true;
//    private NV21ToBitmap nv21ToBitmap;
//    private  static boolean isSB=true;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mDetectResultQueue = new ArrayBlockingQueue<>(5);
//        mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);
//        benDiJiLuBeanBox = MyApplication.myApplication.getBenDiJiLuBeanBox();
//        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
//        guanHuaiBox = MyApplication.myApplication.getGuanHuaiBox();
//        xinXiAllBox = MyApplication.myApplication.getXinXiAllBox();
//        xinXiIdBeanBox = MyApplication.myApplication.getXinXiIdBeanBox();
//
//        mainHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//            }
//
//        };
//        baoCunBean = baoCunBeanDao.get(123456L);
//        subjectBox = MyApplication.myApplication.getSubjectBox();
//        daKaBeanBox=MyApplication.myApplication.getDaKaBeanBox();
//        isL=true;
//
//        //每分钟的广播
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
//        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
//        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
//        timeChangeReceiver = new TimeChangeReceiver();
//        registerReceiver(timeChangeReceiver, intentFilter);
//        linkedBlockingQueue = new LinkedBlockingQueue<>();
//
//        EventBus.getDefault().register(this);//订阅
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        dw = dm.widthPixels;
//        dh = dm.heightPixels;
//        tsxxChuLi=new TSXXChuLi();
//
//        nv21ToBitmap=new NV21ToBitmap(MianBanJiActivity_yanshi.this);
//
//        /* 初始化界面 */
//        initView();
//
//
//        /* 申请程序所需权限 */
//        if (!hasPermission()) {
//            requestPermission();
//        } else {
//            //初始化
//            start();
//        }
//
//        if (baoCunBean != null)
//            initialTts();
//
//        if (baoCunBean != null) {
//            FacePassUtil util=new FacePassUtil();
//            util.init(MianBanJiActivity_yanshi.this, getApplicationContext(), cameraRotation, baoCunBean);
//        } else {
//            Toast tastyToast = TastyToast.makeText(MianBanJiActivity_yanshi.this, "获取本地设置失败,请进入设置界面设置基本信息", TastyToast.LENGTH_LONG, TastyToast.INFO);
//            tastyToast.setGravity(Gravity.CENTER, 0, 0);
//            tastyToast.show();
//        }
//
//        /* 初始化网络请求库 */
//        //   requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        mFeedFrameThread = new FeedFrameThread();
//        mFeedFrameThread.start();
//
//        mRecognizeThread = new RecognizeThread();
//        mRecognizeThread.start();
//
//        tanChuangThread = new TanChuangThread();
//        tanChuangThread.start();
//
//        mHandler = new WeakHandler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 111:{
//
//                        //isShow=false;
//                        //弹窗
//                        if (linearLayout.getChildCount() > 0 && !isDH) {
//                            final View view1 = linearLayout.getChildAt(0);
//                            //消失动画(从右往左)
//                            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0f);
//                            anim.setDuration(400);
//                            anim.setRepeatMode(ValueAnimator.RESTART);
//                            Interpolator interpolator = new DecelerateInterpolator();
//                            anim.setInterpolator(interpolator);
//                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                @Override
//                                public void onAnimationUpdate(ValueAnimator animation) {
//                                    float currentValue = (float) animation.getAnimatedValue();
//                                    // 获得改变后的值
////								System.out.println(currentValue);
//                                    // 输出改变后的值
//                                    // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明
//                                    view1.setScaleX(currentValue);
//                                    view1.setScaleY(currentValue);
//                                    // 步骤5：刷新视图，即重新绘制，从而实现动画效果
//                                    view1.requestLayout();
//                                }
//                            });
//                            anim.addListener(new Animator.AnimatorListener() {
//                                @Override
//                                public void onAnimationStart(Animator animation) {
//                                    isDH = true;
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    isDH = false;
//                                    linearLayout.removeView(view1);
//                                }
//
//                                @Override
//                                public void onAnimationCancel(Animator animation) {
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animator animation) {
//                                }
//                            });
//                            anim.start();
//                        }
//
//
//                        Subject bean = (Subject) msg.obj;
//                        if (!bean.getName().equals("陌生人")){
//                            bofang();
//                        }
//
//                        final View view1 = View.inflate(MianBanJiActivity_yanshi.this, R.layout.yanshi_item, null);
//                        RelativeLayout llll =  view1.findViewById(R.id.llllll);
//                        LinearLayout klkl =  view1.findViewById(R.id.klkl);
//                        TextView name =  view1.findViewById(R.id.name);
//                        ImageView touxiang = view1.findViewById(R.id.touxiang);
//                        TextView bumen =  view1.findViewById(R.id.bumen);
//                       // final ScrollView scrollView_03 = view1.findViewById(R.id.scrollview_03);
//                        final LinearLayout xiaoxi_ll = view1.findViewById(R.id.xiaoxi_ll);
//
//                        if (!liet){
//                            xiaoxi_ll.setVisibility(View.GONE);
//                        }
//
//                        name.setText(bean.getName() + "");
//                        if (chifan){
//                            bumen.setText("祝您用餐愉快！");
//                        }else {
//                            bumen.setText("欢迎参加\n平安产险2019年全国工作会议");
//                        }
//
//
////                        if (bean.getDepartmentName().equals("访客") || bean.getDepartmentName().equals("VIP访客")){
////                          //  llll.setBackgroundResource(R.drawable.fangkebg3);
////                        }else if (bean.getDepartmentName().equals("没有进入权限,请与前台联系!")){
////                           // llll.setBackgroundResource(R.drawable.moshengrenbg3);
////                        }else {
////                           // llll.setBackgroundResource(R.drawable.lanseygbg);
////                        }
//
//
//                        try {
//                            if (bean.getDisplayPhoto() != null) {
//
//                                Glide.with(MianBanJiActivity_yanshi.this)
//                                        .load(bean.getDisplayPhoto())
//                                        .apply(myOptions)
//                                        .into(touxiang);
//                            } else {
//                                if (bean.getTeZhengMa()!=null) {
//                                    Bitmap bitmap = mFacePassHandler.getFaceImage(bean.getTeZhengMa().getBytes());
//                                    //   mianBanJiView.setBitmap(FileUtil.toRoundBitmap(bitmap), 0);
//                                    //     Bitmap bitmap=BitmapFactory.decodeByteArray(bean2.getBitmap(),0,bean2.getBitmap().length);
//                                    //    mianBanJiView.setBitmap(FileUtil.toRoundBitmap(bitmap), 0);
//                                    Glide.with(MianBanJiActivity_yanshi.this)
//                                            .load(new BitmapDrawable(getResources(), bitmap))
//                                            .apply(myOptions)
//                                            .into(touxiang);
//                                }else {
////                                    YuvImage image2 = new YuvImage(bean.getTxBytes(), ImageFormat.NV21, bean.getW(), bean.getH(), null);
////                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                                    image2.compressToJpeg(new Rect(0, 0, bean.getW(),  bean.getH()), 70, stream);
////                                    final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
////                                    stream.close();
//                                    Bitmap fileBitmap = FileUtil.adjustPhotoRotation(
//                                            nv21ToBitmap.nv21ToBitmap(bean.getTxBytes(),bean.getW(),bean.getH()),
//                                            360-SettingVar.faceRotation);
//                                  //  Log.d("MianBanJiActivity2", "SettingVar.faceRotation:" + SettingVar.faceRotation);
//
//                                    Glide.with(MianBanJiActivity_yanshi.this)
//                                            .load(new BitmapDrawable(getResources(), fileBitmap))
//                                            .apply(myOptions)
//                                            .into(touxiang);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//
//                        view1.setY(dh);
//                        linearLayout.addView(view1);
//
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) touxiang.getLayoutParams();
//                        layoutParams.width = (int)(dw*0.22f);
//                        layoutParams.height = (int)(dw*0.22f);
//                        layoutParams.topMargin = (int)(dw*0.07f);
//                        touxiang.setLayoutParams(layoutParams);
//                        touxiang.invalidate();
//
//                        LinearLayout.LayoutParams naeee = (LinearLayout.LayoutParams) name.getLayoutParams();
//                        naeee.topMargin = (int) (dh * 0.02f);
//                        name.setLayoutParams(naeee);
//                        name.invalidate();
//
//                        RelativeLayout.LayoutParams sscr = (RelativeLayout.LayoutParams) xiaoxi_ll.getLayoutParams();
//                        sscr.width = (int) (dw * 0.7f);
//                        xiaoxi_ll.setLayoutParams(sscr);
//                        xiaoxi_ll.invalidate();
//
//                        RelativeLayout.LayoutParams layoutParams13 = (RelativeLayout.LayoutParams) klkl.getLayoutParams();
//                        layoutParams13.height = (int) (dh * 0.32f);
//                        layoutParams13.width = dw;
//                        klkl.setLayoutParams(layoutParams13);
//                        klkl.invalidate();
//
//                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) llll.getLayoutParams();
//                        layoutParams1.height = (int) (dh * 0.44f);
//                        layoutParams1.width = dw;
//                        llll.setLayoutParams(layoutParams1);
//                        llll.invalidate();
//
//                        //启动定时器或重置定时器
//                        if (task != null) {
//                            task.cancel();
//                            //timer.cancel();
//                            task = new TimerTask() {
//                                @Override
//                                public void run() {
//
//                                    Message message = new Message();
//                                    message.what = 222;
//                                    mHandler.sendMessage(message);
//
//                                }
//                            };
//                            timer.schedule(task, 5000);
//                        } else {
//                            task = new TimerTask() {
//                                @Override
//                                public void run() {
//                                    Message message = new Message();
//                                    message.what = 222;
//                                    mHandler.sendMessage(message);
//                                }
//                            };
//                            timer.schedule(task, 5000);
//                        }
//
//                        //入场动画(从右往左)
//                        ValueAnimator anim = ValueAnimator.ofInt(dh,-((int) (dh * 0.02f)));
//                        anim.setDuration(400);
//                        anim.setRepeatMode(ValueAnimator.RESTART);
//                        Interpolator interpolator = new DecelerateInterpolator(2f);
//                        anim.setInterpolator(interpolator);
//                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                int currentValue = (Integer) animation.getAnimatedValue();
//                              //  Log.d("MianBanJiActivity2", "currentValue:" + currentValue);
//                                // 获得改变后的值
////								System.out.println(currentValue);
//                                // 输出改变后的值
//                                // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明
//                                view1.setY(currentValue);
//                                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
//                                view1.requestLayout();
//                            }
//                        });
//                        anim.addListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animation) {
//                            }
//                        });
//                        anim.start();
//                        break;
//                }
//
//                    case 222: {
//
//                        if (linearLayout.getChildCount() > 0 && !isDH) {
//                            final View view1 = linearLayout.getChildAt(0);
//                            //消失动画(从右往左)
//                            ValueAnimator anim = ValueAnimator.ofFloat(1f, 0f);
//                            anim.setDuration(400);
//                            anim.setRepeatMode(ValueAnimator.RESTART);
//                            Interpolator interpolator = new DecelerateInterpolator();
//                            anim.setInterpolator(interpolator);
//                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                @Override
//                                public void onAnimationUpdate(ValueAnimator animation) {
//                                    float currentValue = (float) animation.getAnimatedValue();
//                                    // 获得改变后的值
////								System.out.println(currentValue);
//                                    // 输出改变后的值
//                                    // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明
//                                    view1.setScaleX(currentValue);
//                                    view1.setScaleY(currentValue);
//                                    // 步骤5：刷新视图，即重新绘制，从而实现动画效果
//                                    view1.requestLayout();
//                                }
//                            });
//                            anim.addListener(new Animator.AnimatorListener() {
//                                @Override
//                                public void onAnimationStart(Animator animation) {
//                                    isDH = true;
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    isDH = false;
//                                    linearLayout.removeView(view1);
//                                    isSB=true;
//
//                                }
//
//                                @Override
//                                public void onAnimationCancel(Animator animation) {
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animator animation) {
//                                }
//                            });
//                            anim.start();
//                        }
//
//                        break;
//                    }
//
//                }
//                return false;
//            }
//        });
//
//        isSC=true;
//
//    }
//
//
//
//
//
//
//
//
//    public void im1(View view){
//        if (task != null)
//            task.cancel();
//
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im1_bg));
//        miMaDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                //启动定时器或重置定时器
//                if (task != null) {
//                    task.cancel();
//                    //timer.cancel();
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                } else {
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                }
//            }
//        });
//        miMaDialog.show();
//
//    }
//
//    public void im2(View view){
//        if (task != null)
//            task.cancel();
//
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im2_bg));
//        miMaDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                //启动定时器或重置定时器
//                if (task != null) {
//                    task.cancel();
//                    //timer.cancel();
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                } else {
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                }
//            }
//        });
//        miMaDialog.show();
//
//    }
//
//    public void im3(View view){
//        if (task != null)
//            task.cancel();
//
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im3_bg));
//        miMaDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                //启动定时器或重置定时器
//                if (task != null) {
//                    task.cancel();
//                    //timer.cancel();
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                } else {
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = new Message();
//                            message.what = 222;
//                            mHandler.sendMessage(message);
//                        }
//                    };
//                    timer.schedule(task, 3000);
//                }
//            }
//        });
//        miMaDialog.show();
//
//    }
//
//
//    public void im11(View view){
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im1_bg));
//        miMaDialog.show();
//    }
//    public void im22(View view){
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im2_bg));
//        miMaDialog.show();
//    }
//
//    public void im33(View view){
//        YanShiDialog miMaDialog=new YanShiDialog(MianBanJiActivity_yanshi.this);
//        WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//        params.width=dw;
//        params.height=(int)(dh*0.7f);
//        miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//        miMaDialog.getWindow().setAttributes(params);
//        miMaDialog.setImageView(BitmapFactory.decodeResource(getResources(),R.drawable.im3_bg));
//        miMaDialog.show();
//    }
//
//
//    @Override
//    protected void onResume() {
//        /* 打开相机 */
//        if (hasPermission()) {
//            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);
//        }
//
//        super.onResume();
//    }
//
//
//
//    /* 相机回调函数 */
//    @Override
//    public void onPictureTaken(CameraPreviewData cameraPreviewData) {
//        /* 如果SDK实例还未创建，则跳过 */
//        if (mFacePassHandler == null) {
//            return;
//        }
//        /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
//        FacePassImage image;
//        try {
//            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
//        } catch (FacePassException e) {
//            e.printStackTrace();
//            return;
//        }
//        mFeedFrameQueue.offer(image);
//    }
//
//
//    private class FeedFrameThread extends Thread {
//        boolean isIterrupt;
//
//        @Override
//        public void run() {
//            while (!isIterrupt) {
//
//                try {
//                     final FacePassImage image = mFeedFrameQueue.take();
//                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
//                    FacePassDetectionResult detectionResult = null;
//                    detectionResult = mFacePassHandler.feedFrame(image);
//
//
//                    /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
//
//                    if (detectionResult != null && detectionResult.message.length != 0) {
//                        if (isSB)
//                        mDetectResultQueue.offer(detectionResult);
//                    }
//
//                } catch (InterruptedException | FacePassException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void interrupt() {
//            isIterrupt = true;
//            super.interrupt();
//        }
//    }
//
//
//
//    public class RecognizeThread extends Thread {
//
//        boolean isInterrupt;
//
//        @Override
//        public void run() {
//            while (!isInterrupt) {
//                try {
//                    if (isL){
//                    Log.d("RecognizeThread", "识别线程");
//                    FacePassDetectionResult detectionResult = mDetectResultQueue.take();
//                    FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult.message);
//                    if (recognizeResult != null && recognizeResult.length > 0) {
//                        for (final FacePassRecognitionResult result : recognizeResult) {
//                            //String faceToken = new String(result.faceToken);
//                            if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
//                                //识别的
//                                    try {
//                                        final Subject subject = subjectBox.query().equal(Subject_.teZhengMa, new String(result.faceToken)).build().findUnique();
//                                        if (subject != null) {
//                                            linkedBlockingQueue.offer(subject);
//                                            if (!chifan)
//                                            isSB=false;
//                                            break;
//
//                                        } else {
//                                            EventBus.getDefault().post("没有查询到人员信息");
//
//                                        }
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        EventBus.getDefault().post(e.getMessage()+"");
//                                    }
//
//
//                            } else {
//                                //未识别的
//                                // ConcurrentHashMap 建议用他去重
////
//                                if (concurrentHashMap.size() > 10) {
//                                    concurrentHashMap.clear();
//                                }
//                                if (concurrentHashMap.get(result.trackId) == null) {
//                                    //找不到新增
//                                    concurrentHashMap.put(result.trackId, 1);
//                                } else {
//                                    //找到了 把value 加1
//                                    concurrentHashMap.put(result.trackId, (concurrentHashMap.get(result.trackId)) + 1);
//                                }
//                                //判断次数超过3次
//                                if (concurrentHashMap.get(result.trackId) == 2) {
//                                    for (FacePassImage images:detectionResult.images){
//                                        if (images.trackId==result.trackId){
//
//                                            Subject subject1 = new Subject();
//                                            subject1.setTxBytes(images.image);
//                                            subject1.setW(images.width);
//                                            subject1.setH(images.height);
//                                            subject1.setId(System.currentTimeMillis());
//                                            subject1.setName("未登记");
//                                            subject1.setTeZhengMa(null);
//                                            subject1.setPeopleType("员工");
//                                            subject1.setDepartmentName("没有进入权限,请与前台联系!");
//                                            linkedBlockingQueue.offer(subject1);
//                                            break;
//                                        }
//
//                                    }
//                                }
//
//                                Log.d("RecognizeThread", "未识别的" + result.trackId);
//                            }
//
//
//
//                        }
//                     }
//                    }
//                } catch (InterruptedException | FacePassException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        @Override
//        public void interrupt() {
//            isInterrupt = true;
//            super.interrupt();
//        }
//
//    }
//
//    private class TanChuangThread extends Thread {
//        boolean isRing;
//
//        @Override
//        public void run() {
//            while (!isRing) {
//                try {
//                    //有动画 ，延迟到一秒一次
//                    SystemClock.sleep(400);
//
//                    Subject subject = linkedBlockingQueue.take();
//                    if (subject.getPeopleType() != null) {
//                        switch (subject.getPeopleType()) {
//                            case "员工": {
//
//                                Message message2 = Message.obtain();
//                                message2.what = 111;
//                                message2.obj = subject;
//                                mHandler.sendMessage(message2);
//                                break;
//                            }
//                            case "普通访客":{
//                                //普通访客
//                                subject.setDepartmentName("访客");
//                                Message message2 = Message.obtain();
//                                message2.what = 111;
//                                message2.obj = subject;
//                                mHandler.sendMessage(message2);
//
//                                break;
//                            }
//                            case "白名单":
//                                //vip
//                                subject.setDepartmentName("VIP访客");
//                                Message message2 = Message.obtain();
//                                message2.what = 111;
//                                message2.obj = subject;
//                                mHandler.sendMessage(message2);
//
//                                break;
//                            case "黑名单":
//
//                                break;
//                            default:
//                                EventBus.getDefault().post("没有对应身份类型,无法弹窗");
//
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void interrupt() {
//            isRing = true;
//           // Log.d("RecognizeThread", "中断了弹窗线程");
//            super.interrupt();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        shipingView.pause();
//    }
//
//
//
//    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
//    private boolean hasPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(PERMISSION_READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
//        } else {
//            return true;
//        }
//    }
//
//    /* 请求程序所需权限 */
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(Permission, PERMISSIONS_REQUEST);
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSIONS_REQUEST) {
//            boolean granted = true;
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED)
//                    granted = false;
//            }
//            if (!granted) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_STORAGE)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_WRITE_STORAGE)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_INTERNET)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_WIFI_STATE)
//                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_PHONE_STATE)) {
//                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
//                    }
//            } else {
//
//               start();
//            }
//        }
//    }
//
//
//
//    private void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
//        if (windowRotation == 0) {
//            cameraRotation = FacePassImageRotation.DEG90;
//        } else if (windowRotation == 90) {
//            cameraRotation = FacePassImageRotation.DEG0;
//        } else if (windowRotation == 270) {
//            cameraRotation = FacePassImageRotation.DEG180;
//        } else {
//            cameraRotation = FacePassImageRotation.DEG270;
//        }
//
//        cameraFacingFront = true;
//        SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
//        SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
//        SettingVar.isCross = preferences.getBoolean("isCross", SettingVar.isCross);
//        SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
//        SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
//        SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);
//        if (SettingVar.isSettingAvailable) {
//            cameraRotation = SettingVar.faceRotation;
//            cameraFacingFront = SettingVar.cameraFacingFront;
//        }
//
//        final int mCurrentOrientation = getResources().getConfiguration().orientation;
//
//        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            screenState = 1;
//        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            screenState = 0;
//        }
//        setContentView(R.layout.activity_mianbanji_yanshi);
//        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
//        ButterKnife.bind(this);
//        AssetManager mgr = getAssets();
//
//        linearLayout=findViewById(R.id.linearLayout);
//
//        ImageView shezhi = findViewById(R.id.shezhi);
//        ImageView shezhi2 = findViewById(R.id.shezhi2);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        heightPixels = displayMetrics.heightPixels;
//        widthPixels = displayMetrics.widthPixels;
//        SettingVar.mHeight = heightPixels;
//        SettingVar.mWidth = widthPixels;
//
//
//        ghjj=findViewById(R.id.ghjj);
//        qiandaobg=findViewById(R.id.qiandaobg);
//        chifantv=findViewById(R.id.chifantv);
//        /* 初始化界面 */
//        //faceView = findViewById(R.id.fcview);
//
//        manager = new CameraManager();
//        cameraView = (CameraPreview) findViewById(R.id.preview);
//        manager.setPreviewDisplay(cameraView);
//        /* 注册相机回调函数 */
//        manager.setListener(this);
//
//
//
//        RelativeLayout.LayoutParams dp = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
//        dp.height = (int) (dh * 0.62f);
//        dp.topMargin = (int) (dh * 0.32f);
//        dp.width = dw;
//        linearLayout.setLayoutParams(dp);
//        linearLayout.invalidate();
//
//        shezhi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MiMaDialog4 miMaDialog=new MiMaDialog4(MianBanJiActivity_yanshi.this,baoCunBean.getMima());
//                WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//                params.width=dw;
//                params.height=dh;
////                miMaDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//                miMaDialog.getWindow().setAttributes(params);
//                miMaDialog.show();
//            }
//        });
//
//        shezhi2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final XuanZeDialog miMaDialog=new XuanZeDialog(MianBanJiActivity_yanshi.this);
//                WindowManager.LayoutParams params= miMaDialog.getWindow().getAttributes();
//                params.width=(int)(dw*0.6f);
//                params.height=(int)(dh*0.3f);
//                miMaDialog.getWindow().setGravity(Gravity.CENTER );
//                miMaDialog.getWindow().setAttributes(params);
//                miMaDialog.set1(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        chifan=false;
//                        isSB=true;
//                        ghjj.setVisibility(View.GONE);
//                        liet=true;
//                        qiandaobg.setVisibility(View.VISIBLE);
//                        chifantv.setVisibility(View.GONE);
//                        miMaDialog.dismiss();
//                    }
//                });
//                miMaDialog.set2(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        chifan=false;
//                        isSB=false;
//                        ghjj.setVisibility(View.VISIBLE);
//                        qiandaobg.setVisibility(View.GONE);
//                        chifantv.setVisibility(View.GONE);
//                        liet=false;
//
//                        if (task!=null)
//                        task.cancel();
//                        if (linearLayout.getChildCount() > 0 ) {
//                            final View view1 = linearLayout.getChildAt(0);
//                            isDH=false;
//                            linearLayout.removeView(view1);
//                        }
//                        miMaDialog.dismiss();
//                    }
//                });
//                miMaDialog.set3(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        chifan=true;
//                        isSB=true;
//                        ghjj.setVisibility(View.GONE);
//                        qiandaobg.setVisibility(View.GONE);
//                        chifantv.setVisibility(View.VISIBLE);
//                        liet=false;
//                        miMaDialog.dismiss();
//                    }
//                });
//                miMaDialog.show();
//            }
//        });
//
//
//    }
//
//
//
//
//    @Override
//    protected void onStop() {
//
//        SettingVar.isButtonInvisible = false;
//       // mToastBlockQueue.clear();
//        mDetectResultQueue.clear();
//        mFeedFrameQueue.clear();
//        if (manager != null) {
//            manager.release();
//        }
//      //  marqueeView.stopFlipping();
//        super.onStop();
//    }
//
//    @Override
//    protected void onRestart() {
//        //faceView.clear();
//        // faceView.invalidate();
//        //  if (shipingView!=null)
//        // shipingView.start();
//        super.onRestart();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        //marqueeView.startFlipping();
//    }
//
//
//    @Override
//    protected void onDestroy() {
//
//
//        if (mFeedFrameQueue != null) {
//            mFeedFrameQueue.clear();
//        }
//        if (mFeedFrameThread != null) {
//            mFeedFrameThread.isIterrupt = true;
//            mFeedFrameThread.interrupt();
//        }
//
//        if (tanChuangThread != null) {
//            tanChuangThread.isRing = true;
//            tanChuangThread.interrupt();
//        }
//
//        if (mRecognizeThread != null) {
//            mRecognizeThread.isInterrupt = true;
//            mRecognizeThread.interrupt();
//        }
//
//
//     //   mianBanJiView.desoure();
//        //时钟
//        //clockView.crean();
//
//        unregisterReceiver(timeChangeReceiver);
//
//
//        EventBus.getDefault().unregister(this);//解除订阅
//
//        if (manager != null) {
//            manager.release();
//        }
//
////        if (mFacePassHandler != null) {
////            mFacePassHandler.release();
////        }
//
//        if (synthesizer != null)
//            synthesizer.release();
//
//
//        timer.cancel();
//        if (task != null)
//            task.cancel();
//
//        super.onDestroy();
//    }
//
//
//
//    private void showFacePassFace(final FacePassFace[] detectResult) {
//         runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                faceView.clear();
//                for (FacePassFace face : detectResult) {
//
//                    boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
//
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
//                   // faceView.addRect(drect);
//                   // faceView.addId(faceIdString.toString());
//                   // faceView.addRoll(faceRollString.toString());
//                   // faceView.addPitch(facePitchString.toString());
//                   // faceView.addYaw(faceYawString.toString());
//                   // faceView.addBlur(faceBlurString.toString());
//                   // faceView.addAge(faceAgeString.toString());
//                   // faceView.addGenders(faceGenderString.toString());
//                }
//                //faceView.invalidate();
//            }
//        });
//
//    }
//
//
//
//    private static final int REQUEST_CODE_CHOOSE_PICK = 1;
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            //从相册选取照片后读取地址
//            case REQUEST_CODE_CHOOSE_PICK:
//                if (resultCode == RESULT_OK) {
//                    String path = "";
//                    Uri uri = data.getData();
//                    String[] pojo = {MediaStore.Images.Media.DATA};
//                    CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null, null, null);
//                    Cursor cursor = cursorLoader.loadInBackground();
//                    if (cursor != null) {
//                        cursor.moveToFirst();
//                        path = cursor.getString(cursor.getColumnIndex(pojo[0]));
//                    }
//                    if (!TextUtils.isEmpty(path) && "file".equalsIgnoreCase(uri.getScheme())) {
//                        path = uri.getPath();
//                    }
//                    if (TextUtils.isEmpty(path)) {
//                        try {
//                            path = FileUtil.getPath(getApplicationContext(), uri);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (TextUtils.isEmpty(path)) {
//                        toast("图片选取失败！");
//                        return;
//                    }
////                    if (!TextUtils.isEmpty(path) && mFaceOperationDialog != null && mFaceOperationDialog.isShowing()) {
////                        EditText imagePathEdt = (EditText) mFaceOperationDialog.findViewById(R.id.et_face_image_path);
////                        imagePathEdt.setText(path);
////                    }
//                }
//                break;
//        }
//    }
//
//
//    private void toast(String msg) {
//        Toast.makeText(MianBanJiActivity_yanshi.this, msg, Toast.LENGTH_SHORT).show();
//    }
//
//
//    /**
//     * 根据facetoken下载图片缓存
//     */
//    private static class FaceImageCache implements ImageLoader.ImageCache {
//
//        private static final int CACHE_SIZE = 6 * 1024 * 1024;
//
//        LruCache<String, Bitmap> mCache;
//
//        public FaceImageCache() {
//            mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
//
//                @Override
//                protected int sizeOf(String key, Bitmap value) {
//                    return value.getRowBytes() * value.getHeight();
//                }
//            };
//        }
//
//        @Override
//        public Bitmap getBitmap(String url) {
//            return mCache.get(url);
//        }
//
//        @Override
//        public void putBitmap(String url, Bitmap bitmap) {
//            mCache.put(url, bitmap);
//        }
//    }
//
//    private class FacePassRequest extends Request<String> {
//
//        HttpEntity entity;
//
//        FacePassDetectionResult mFacePassDetectionResult;
//        private Response.Listener<String> mListener;
//
//        public FacePassRequest(String url, FacePassDetectionResult detectionResult, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//            super(Method.POST, url, errorListener);
//            mFacePassDetectionResult = detectionResult;
//            mListener = listener;
//        }
//
//        @Override
//        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//            String parsed;
//            try {
//                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            } catch (UnsupportedEncodingException e) {
//                parsed = new String(response.data);
//            }
//            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
//        }
//
//        @Override
//        protected void deliverResponse(String response) {
//            mListener.onResponse(response);
//        }
//
//        @Override
//        public String getBodyContentType() {
//            return entity.getContentType().getValue();
//        }
//
//        @Override
//        public byte[] getBody() throws AuthFailureError {
//            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
////        beginRecogIdArrayList.clear();
//
//            for (FacePassImage passImage : mFacePassDetectionResult.images) {
//                /* 将人脸图转成jpg格式图片用来上传 */
//                YuvImage img = new YuvImage(passImage.image, ImageFormat.NV21, passImage.width, passImage.height, null);
//                Rect rect = new Rect(0, 0, passImage.width, passImage.height);
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                img.compressToJpeg(rect, 95, os);
//                byte[] tmp = os.toByteArray();
//                ByteArrayBody bab = new ByteArrayBody(tmp, String.valueOf(passImage.trackId) + ".jpg");
////            beginRecogIdArrayList.add(passImage.trackId);
//                entityBuilder.addPart("image_" + String.valueOf(passImage.trackId), bab);
//            }
//            StringBody sbody = null;
//            try {
//                sbody = new StringBody(MianBanJiActivity_yanshi.group_name, ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            entityBuilder.addPart("group_name", sbody);
//            StringBody data = null;
//            try {
//                data = new StringBody(new String(mFacePassDetectionResult.message), ContentType.TEXT_PLAIN.withCharset(CharsetUtils.get("UTF-8")));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            entityBuilder.addPart("face_data", data);
//            entity = entityBuilder.build();
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            try {
//                entity.writeTo(bos);
//            } catch (IOException e) {
//                VolleyLog.e("IOException writing to ByteArrayOutputStream");
//            }
//            byte[] result = bos.toByteArray();
//            if (bos != null) {
//                try {
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return result;
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (keyCode == KeyEvent.KEYCODE_MENU) {
//                startActivity(new Intent(MianBanJiActivity_yanshi.this, SheZhiActivity2.class));
//                finish();
//            }
//
//        }
//
//        return super.onKeyDown(keyCode, event);
//
//    }
//
//
//
//    /**
//     * 初始化引擎，需要的参数均在InitConfig类里
//     * <p>
//     * DEMO中提供了3个SpeechSynthesizerListener的实现
//     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
//     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
//     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
//     */
//    protected void initialTts() {
//        // 设置初始化参数
//        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler); // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
//        Map<String, String> params = getParams();
//        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
//        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
//        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
//
//    }
//
//    /**
//     * 合成的参数，可以初始化时填写，也可以在合成前设置。
//     *
//     * @return
//     */
//    protected Map<String, String> getParams() {
//        Map<String, String> params = new HashMap<String, String>();
//        // 以下参数均为选填
//        params.put(SpeechSynthesizer.PARAM_SPEAKER, baoCunBean.getBoyingren() + ""); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
//        params.put(SpeechSynthesizer.PARAM_VOLUME, "8"); // 设置合成的音量，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_SPEED, baoCunBean.getYusu() + "");// 设置合成的语速，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_PITCH, baoCunBean.getYudiao() + "");// 设置合成的语调，0-9 ，默认 5
//        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);         // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
//        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//
//        // 离线资源文件
//        OfflineResource offlineResource = createOfflineResource(offlineVoice);
//        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
//        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
//        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
//                offlineResource.getModelFilename());
//
//        return params;
//    }
//
//    protected OfflineResource createOfflineResource(String voiceType) {
//        OfflineResource offlineResource = null;
//        try {
//            offlineResource = new OfflineResource(this, voiceType);
//        } catch (IOException e) {
//            // IO 错误自行处理
//            e.printStackTrace();
//            // toPrint("【error】:copy files from assets failed." + e.getMessage());
//        }
//        return offlineResource;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
//    public void onDataSynEvent(String event) {
//        if (event.equals("mFacePassHandler")) {
//            mFacePassHandler = MyApplication.myApplication.getFacePassHandler();
//          //  diBuAdapter = new DiBuAdapter(dibuList, MianBanJiActivity.this, dibuliebiao.getWidth(), dibuliebiao.getHeight(), mFacePassHandler);
//          //  dibuliebiao.setLayoutManager(gridLayoutManager);
//         //   dibuliebiao.setAdapter(diBuAdapter);
//            return;
//        }
//
//        if (event.equals("ditu123")) {
//            // if (baoCunBean.getTouxiangzhuji() != null)
//            //    daBg.setImageBitmap(BitmapFactory.decodeFile(baoCunBean.getTouxiangzhuji()));
//            baoCunBean = baoCunBeanDao.get(123456L);
//
//
//
//
//            return;
//        }
//
//
//
//        if (event.equals("guanbimain")){
//            finish();
//            return;
//        }
//        Toast tastyToast = TastyToast.makeText(MianBanJiActivity_yanshi.this, event, TastyToast.LENGTH_LONG, TastyToast.INFO);
//        tastyToast.setGravity(Gravity.CENTER, 0, 0);
//        tastyToast.show();
//
//    }
//
//
//    class TimeChangeReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (Objects.requireNonNull(intent.getAction())) {
//                case Intent.ACTION_TIME_TICK:
//                    //mianBanJiView.setTime(DateUtils.time(System.currentTimeMillis()+""));
//                   // String riqi11 = DateUtils.getWeek(System.currentTimeMillis()) + "   " + DateUtils.timesTwo(System.currentTimeMillis() + "");
//                    //  riqi.setTypeface(tf);
////                    List<Subject> subjectList=subjectBox.query().contains(Subject_.birthday,DateUtils.nyr(System.currentTimeMillis()+"")).and()
////                            .equal(Subject_.peopleType,"普通访客")
////                            .or().equal(Subject_.peopleType,"白名单").build().find();
////
//
//
//
//                    String xiaoshiss=DateUtils.timeMinute(System.currentTimeMillis() + "");
//                    if (xiaoshiss.split(":")[0].equals("06") && xiaoshiss.split(":")[1].equals("30")){
//                        Log.d("TimeChangeReceiver", "同步");
//                        final List<BenDiJiLuBean> benDiJiLuBeans=benDiJiLuBeanBox.getAll();
//                        final int size=benDiJiLuBeans.size();
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                for (int i=0;i<size;i++){
//                                    while (isSC){
//                                        isSC=false;
//                                        link_shangchuanjilu2(benDiJiLuBeans.get(i));
//                                    }
//
//                                }
//
//                            }
//                        }).start();
//
//                    }
//
//                    if (xiaoshiss.split(":")[0].equals("22") && xiaoshiss.split(":")[1].equals("30")){
//                        //删除今天的访客
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                List<Subject> subjectList=subjectBox.query().contains(Subject_.birthday,DateUtils.nyr(System.currentTimeMillis()+"")).and()
//                                        .equal(Subject_.peopleType,"普通访客")
//                                        .or().equal(Subject_.peopleType,"白名单").build().find();
//                                for (Subject s:subjectList){
//                                    Log.d("TimeChangeReceiver", s.toString());
//                                    try {
//                                        mFacePassHandler.deleteFace(s.getTeZhengMa().getBytes());
//                                        subjectBox.remove(s);
//                                    } catch (FacePassException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                            }
//                        }).start();
//
//                    }
//                    if (xiaoshiss.split(":")[0].equals("10") && xiaoshiss.split(":")[1].equals("01")){
//                        //如果用户在22：30 前关了机 就需要第二天早上删除昨天的访客
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                List<Subject> subjectList=subjectBox.query().contains(Subject_.birthday,DateUtils.nyr((System.currentTimeMillis()-86400000)+"")).and()
//                                        .equal(Subject_.peopleType,"普通访客")
//                                        .or().equal(Subject_.peopleType,"白名单").build().find();
//                                for (Subject s:subjectList){
//                                    Log.d("TimeChangeReceiver", s.toString());
//                                    try {
//                                        mFacePassHandler.deleteFace(s.getTeZhengMa().getBytes());
//                                        subjectBox.remove(s);
//                                    } catch (FacePassException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                            }
//                        }).start();
//
//                    }
//
//
//                    AssetManager mgr = getAssets();
//                  //  Univers LT 57 Condensed
//                 //   Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
//                //    Typeface tf2 = Typeface.createFromAsset(mgr, "fonts/hua.ttf");
//                //    Typeface tf3 = Typeface.createFromAsset(mgr, "fonts/kai.ttf");
//                //   String riqi2 = DateUtils.timesTwo(System.currentTimeMillis() + "") + "   " + DateUtils.getWeek(System.currentTimeMillis());
//                    //  riqi.setTypeface(tf);
//                    //  riqi.setText(riqi2);
//                 ///   xiaoshi.setTypeface(tf);
//                 //   xiaoshi.setText(DateUtils.timeMinute(System.currentTimeMillis() + ""));
//
//                    Date date = new Date();
//                    date.setTime(System.currentTimeMillis());
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(date);
//                    int t = calendar.get(Calendar.HOUR_OF_DAY);
//
//                    //每过一分钟 触发
//                    if (baoCunBean != null && baoCunBean.getDangqianShiJian()!=null && !baoCunBean.getDangqianShiJian().equals(DateUtils.timesTwo(System.currentTimeMillis() + "")) && t >= 6) {
//
//                        //一天请求一次
//                        try {
//                            if (baoCunBean.getDangqianChengShi2() == null) {
////                                Toast tastyToast = TastyToast.makeText(MianBanJiActivity_yanshi.this, "获取天气失败,没有设置当前城市", TastyToast.LENGTH_LONG, TastyToast.INFO);
////                                tastyToast.setGravity(Gravity.CENTER, 0, 0);
////                                tastyToast.show();
//                                return;
//                            }
//                            Log.d("TimeChangeReceiver", baoCunBean.getDangqianChengShi());
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
//                                    .get()
//                                    .url("http://v.juhe.cn/weather/index?format=1&cityname=" + baoCunBean.getDangqianChengShi() + "&key=356bf690a50036a5cfc37d54dc6e8319");
//                            // step 3：创建 Call 对象
//                            Call call = okHttpClient.newCall(requestBuilder.build());
//                            //step 4: 开始异步请求
//                            call.enqueue(new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    Log.d("AllConnects", "请求失败" + e.getMessage());
//                                }
//
//                                @Override
//                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                                    Log.d("AllConnects", "请求成功" + call.request().toString());
//                                    //获得返回体
//                                    try {
//
//                                        ResponseBody body = response.body();
//                                        String ss = body.string().trim();
//                                        Log.d("AllConnects", "天气" + ss);
//                                        JsonObject jsonObject = GsonUtil.parse(ss).getAsJsonObject();
//                                        Gson gson = new Gson();
//                                        final TianQiBean renShu = gson.fromJson(jsonObject, TianQiBean.class);
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
//                                        //更新界面
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//
//                                            }
//                                        });
//                                        //把所有人员的打卡信息重置
//                                        List<Subject> subjectList = subjectBox.getAll();
//                                        for (Subject s : subjectList) {
//                                            s.setDaka(0);
//                                            subjectBox.put(s);
//                                        }
//
//                                    } catch (Exception e) {
//                                        Log.d("WebsocketPushMsg", e.getMessage() + "ttttt");
//                                    }
//
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
////                            Toast tastyToast = TastyToast.makeText(MianBanJiActivity_yanshi.this, "获取天气失败,没有设置当前城市", TastyToast.LENGTH_LONG, TastyToast.INFO);
////                            tastyToast.setGravity(Gravity.CENTER, 0, 0);
////                            tastyToast.show();
//                            return;
//                        }
//
//                    }
//                    //  Toast.makeText(context, "1 min passed", Toast.LENGTH_SHORT).show();
//                    break;
//                case Intent.ACTION_TIME_CHANGED:
//                    //设置了系统时间
//                    // Toast.makeText(context, "system time changed", Toast.LENGTH_SHORT).show();
//                    break;
//                case Intent.ACTION_TIMEZONE_CHANGED:
//                    //设置了系统时区的action
//                    //  Toast.makeText(context, "system time zone changed", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }
//
//
//    //轮播适配器
//    private class TestNomalAdapter extends StaticPagerAdapter {
//        private int[] imgs = {
//                R.drawable.dbg_1,
//                R.drawable.ceshi,
//                R.drawable.ceshi3,
//        };
//
//        @Override
//        public View getView(ViewGroup container, int position) {
//            ImageView view = new ImageView(container.getContext());
//            view.setImageResource(imgs[position]);
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return imgs.length;
//        }
//    }
//
//
//    //上传识别记录
//    private void link_shangchuanjilu(final Subject subject) {
//       // final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//        RequestBody body = null;
//
//        body = new FormBody.Builder()
//                //.add("name", subject.getName()) //
//                //.add("companyId", subject.getCompanyId()+"") //公司di
//                //.add("companyName",subject.getCompanyName()+"") //公司名称
//                //.add("storeId", subject.getStoreId()+"") //门店id
//                //.add("storeName", subject.getStoreName()+"") //门店名称
//                .add("subjectId", subject.getId() + "") //员工ID
//                .add("subjectType", subject.getPeopleType()==null?"员工":subject.getPeopleType()) //人员类型
//                // .add("department", subject.getPosition()+"") //部门
//                .add("discernPlace",baoCunBean.getTuisongDiZhi()+"")//识别地点
//                // .add("discernAvatar",  "") //头像
//                .add("identificationTime", DateUtils.time(System.currentTimeMillis() + ""))//时间
//                .build();
//
//
//        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
//                .header("Content-Type", "application/json")
//                .post(body)
//                .url(MyApplication.URL + "/app/historySave");
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("AllConnects", "请求失败" + e.getMessage());
//                BenDiJiLuBean bean=new BenDiJiLuBean();
//                bean.setSubjectId(subject.getId());
//                bean.setDiscernPlace(baoCunBean.getTuisongDiZhi()+"");
//                bean.setSubjectType(subject.getPeopleType());
//                bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
//                benDiJiLuBeanBox.put(bean);
//
//                List<BenDiJiLuBean> bb=  benDiJiLuBeanBox.getAll();
//                for (int i=0;i<bb.size();i++){
//                    Log.d("MainActivity2", bb.toString());
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                Log.d("AllConnects", "请求成功" + call.request().toString());
//                //获得返回体
//                try {
//                    ResponseBody body = response.body();
//                    String ss = body.string().trim();
//                    Log.d("AllConnects", "上传识别记录" + ss);
//
//
//                } catch (Exception e) {
//                    BenDiJiLuBean bean=new BenDiJiLuBean();
//                    bean.setSubjectId(subject.getId());
//                    bean.setDiscernPlace(baoCunBean.getTuisongDiZhi()+"");
//                    bean.setSubjectType(subject.getPeopleType());
//                    bean.setIdentificationTime(DateUtils.time(System.currentTimeMillis() + ""));
//                    benDiJiLuBeanBox.put(bean);
//
//                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
//                }
//            }
//        });
//    }
//
//    //信鸽信息处理
//    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
//    public void onDataSynEvent(XGBean xgBean) {
//        if (MyApplication.myApplication.getFacePassHandler()!=null){
//            try {
//                MianBanJiActivity_yanshi.isL=false;
//                tsxxChuLi.setData(xgBean, MianBanJiActivity_yanshi.this, MyApplication.myApplication.getFacePassHandler());
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//
//
//
//    //上传识别记录2
//    private void link_shangchuanjilu2(final BenDiJiLuBean subject) {
//      //  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//        RequestBody body = null;
//
//        body = new FormBody.Builder()
//                //.add("name", subject.getName()) //
//                //.add("companyId", subject.getCompanyId()+"") //公司di
//                //.add("companyName",subject.getCompanyName()+"") //公司名称
//                //.add("storeId", subject.getStoreId()+"") //门店id
//                //.add("storeName", subject.getStoreName()+"") //门店名称
//                .add("subjectId", subject.getSubjectId() + "") //员工ID
//                .add("subjectType", subject.getSubjectType()+"") //人员类型
//                // .add("department", subject.getPosition()+"") //部门
//                .add("discernPlace",baoCunBean.getTuisongDiZhi()+"")//识别地点
//                // .add("discernAvatar",  "") //头像
//                .add("identificationTime",subject.getIdentificationTime()+"")//时间
//                .build();
//
//
//        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
//                .header("Content-Type", "application/json")
//                .post(body)
//                .url(MyApplication.URL + "/app/historySave");
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("AllConnects", "请求失败" + e.getMessage());
//                isSC=true;
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                Log.d("AllConnects", "请求成功" + call.request().toString());
//                //获得返回体
//                try {
//                    ResponseBody body = response.body();
//                    String ss = body.string().trim();
//                    Log.d("AllConnects", "上传识别记录" + ss);
//                    //成功的话 删掉本地保存的记录
//                    benDiJiLuBeanBox.remove(subject.getId());
//
//                } catch (Exception e) {
//
//                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
//
//                }finally {
//                    isSC=true;
//                }
//            }
//        });
//    }
//
//
//    private void bofang(){
//
//        mMediaPlayer=MediaPlayer.create(MianBanJiActivity_yanshi.this, R.raw.deng2);
//
//        mMediaPlayer.start();
//
//    }
//
//
//
//    public static class MyReceiver extends BroadcastReceiver {
//        public MyReceiver() {
//
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//                Intent i = new Intent(context, MianBanJiActivity_yanshi.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//            }
//        }
//    }
//
//
//
//
//    private void contrlLED(int progress) {
//        Process p = null;
//        String cmd = "";
//        if (Build.MODEL.equals("TPS980P")) {
//            cmd = "echo " + progress + " > /sys/class/backlight/backlight_extend/brightness";
//            try {
//                p = Runtime.getRuntime().exec("sh");
//                DataOutputStream os = new DataOutputStream(p.getOutputStream());
//                os.writeBytes(cmd + "\n");
//                os.writeBytes("exit\n");
//                os.flush();
//                p.waitFor();
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            cmd = "echo " + progress + " > /sys/class/backlight/rk28_bl_sub/brightness";
//            try {
//                p = Runtime.getRuntime().exec("su");
//                DataOutputStream os = new DataOutputStream(p.getOutputStream());
//                os.writeBytes(cmd + "\n");
//                os.writeBytes("exit\n");
//                os.flush();
//                p.waitFor();
//            } catch (IOException | InterruptedException e) {
//                Log.d("MianBanJiActivity2", e.getMessage()+"关灯");
//            }
//        }
//
//    }
//
//
//    private void start(){
//        //初始化
//        File file = new File(MyApplication.SDPATH);
//        if (!file.exists()) {
//            Log.d("ggg", "file.mkdirs():" + file.mkdirs());
//        }
//        File file2 = new File(MyApplication.SDPATH2);
//        if (!file2.exists()) {
//            Log.d("ggg", "file.mkdirs():" + file2.mkdirs());
//        }
//        FacePassHandler.initSDK(getApplicationContext());
//        Log.d("MainActivity201", FacePassHandler.getVersion()+"");
//        //开启信鸽的日志输出，线上版本不建议调用
//        XGPushConfig.enableDebug(getApplicationContext(), true);
//        //ed02bf3dc1780d644f0797a9153963b37ed570a5
//
// /*
//        注册信鸽服务的接口
//        如果仅仅需要发推送消息调用这段代码即可
//        */
//        XGPushManager.registerPush(getApplicationContext(),
//                new XGIOperateCallback() {
//                    @Override
//                    public void onSuccess(Object data, int flag) {
//                        String deviceId=null;
//                        baoCunBean.setXgToken(data+"");
//                        Log.w("MainActivity", "+++ register push sucess. token:" + data + "flag" + flag);
//
//                        if (baoCunBean.getTuisongDiZhi()==null || baoCunBean.getTuisongDiZhi().equals("")) {
//                             deviceId = GetDeviceId.getDeviceId(MianBanJiActivity_yanshi.this);
//                            if (deviceId==null){
//                                ToastUtils.show2(MianBanJiActivity_yanshi.this,"获取设备唯一标识失败");
//                            }else {
//                                Log.d("BaseActivity", deviceId+"设备唯一标识");
//                                baoCunBean.setTuisongDiZhi(deviceId);
//                                baoCunBeanDao.put(baoCunBean);
//                            }
//                        }else {
//                            Log.d("BaseActivity", baoCunBean.getTuisongDiZhi()+"设备唯一标识");
//                        }
//                        link_uplod(baoCunBean.getTuisongDiZhi(),data+"");
//                    }
//                    @Override
//                    public void onFail(Object data, int errCode, String msg) {
//                        Log.w("MainActivity",
//                                "+++ register push fail. token:" + data
//                                        + ", errCode:" + errCode + ",msg:"
//                                        + msg);
//                        ToastUtils.show2(MianBanJiActivity_yanshi.this,"注册推送失败"+msg);
//                    }
//                });
//
//    }
//
//
//    //更新信鸽token
//    private void link_uplod(String deviceId,  String token){
//        //	final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
//        //RequestBody requestBody = RequestBody.create(JSON, json);
//        RequestBody body = new FormBody.Builder()
//                .add("machineCode", deviceId+"")
//                .add("machineToken",token+"")
//                .build();
//        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
////				.header("Content-Type", "application/json")
////				.header("user-agent","Koala Admin")
//                //.post(requestBody)
//                //.get()
//                .post(body)
//                .url(MyApplication.URL+"/app/updateToken");
//
//        // step 3：创建 Call 对象
//        Call call = okHttpClient.newCall(requestBuilder.build());
//        //step 4: 开始异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("AllConnects", "请求失败"+e.getMessage());
//              //  EventBus.getDefault().post("网络请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) {
//                //  Log.d("AllConnects", "请求成功"+call.request().toString());
//                //获得返回体
//                String ss=null;
//                try{
//                    ResponseBody body = response.body();
//                    ss=body.string().trim();
//                    Log.d("AllConnects", "更新信鸽token:"+ss);
//
//
//                }catch (Exception e){
//
//                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
//                }
//
//            }
//        });
//    }
//
//
//
//    private void setxinxi(Subject bean2, final ScrollView scrollView_03, final LinearLayout xiaoxi_ll){
//        //0小邮局 1生日提醒 2入职关怀 3节日关怀
//        final List<GuanHuai> ygguanHuaiList = guanHuaiBox.query().equal(GuanHuai_.employeeId, bean2.getId()).build().find();
//        final List<GuanHuai> ygguanHuaiList2 = guanHuaiBox.query().equal(GuanHuai_.employeeId, 0L).build().find();
//        if (ygguanHuaiList2.size() > 0) {
//            ygguanHuaiList.addAll(ygguanHuaiList2);
//        }
//        //信息推送
//        List<XinXiAll> xinXiAlls = xinXiAllBox.getAll();
//        for (XinXiAll all : xinXiAlls) {
//            if (all.getEmployeeId().equals("0") && all.getStartTime() <= System.currentTimeMillis()) {
//                GuanHuai guanHuai1 = new GuanHuai();
//                guanHuai1.setMarkedWords(all.getEditNews());
//                guanHuai1.setNewsStatus("");
//                guanHuai1.setProjectileStatus("4");
//                ygguanHuaiList.add(guanHuai1);
//
//            } else {
//                //查出来
//                final List<XinXiIdBean> xinXiIdBeans = xinXiIdBeanBox.query().equal(XinXiIdBean_.ygid, bean2.getId()).build().find();
//                for (XinXiIdBean idBean : xinXiIdBeans) {
//                    if (idBean.getUuid().equals(all.getId()) && all.getStartTime() <= System.currentTimeMillis()) {
//                        GuanHuai guanHuai1 = new GuanHuai();
//                        guanHuai1.setMarkedWords(all.getEditNews());
//                        guanHuai1.setNewsStatus("");
//                        guanHuai1.setProjectileStatus("4");
//                        ygguanHuaiList.add(guanHuai1);
//                    }
//                }
//            }
//        }
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final int si = ygguanHuaiList.size();
//                for (int i = 0; i < si; i++) {
//
//                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final View view_xiaoxi = View.inflate(MianBanJiActivity_yanshi.this, R.layout.xiaoxi_item, null);
//                            RelativeLayout rl_xiaoxi = view_xiaoxi.findViewById(R.id.rl_xiaoxi);
//                            TextView neirong = view_xiaoxi.findViewById(R.id.neirong);
//
//                            switch (ygguanHuaiList.get(finalI).getProjectileStatus()) {
//                                case "0":
//                                    //小邮局
//                                    try {
//
//                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case "1":
//                                    // 生日提醒
//
//                                    try {
//
//                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case "2":
//                                    //入职关怀
//
//                                    try {
//                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case "3":
//                                    //节日关怀
//
//                                    try {
//                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case "4":
//                                    //节日关怀
//
//                                    try {
//                                        neirong.setText(ygguanHuaiList.get(finalI).getMarkedWords());
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                            }
//
//                            view_xiaoxi.setY(dh);
//                            xiaoxi_ll.addView(view_xiaoxi);
//
//                            LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) rl_xiaoxi.getLayoutParams();
//                            layoutParams6.bottomMargin = 10;
//                            layoutParams6.topMargin = 10;
//                            layoutParams6.height = ((int) ((float) dh * 0.05));
//                            rl_xiaoxi.setLayoutParams(layoutParams6);
//                            rl_xiaoxi.invalidate();
//
//
//                            float sfff = 20 + ((float) dh * 0.05f);
//
//                            ValueAnimator animator = ValueAnimator.ofFloat(dh, sfff * finalI);
//                            //动画时长，让进度条在CountDown时间内正好从0-360走完，
//                            animator.setDuration(1000);
//                            animator.setInterpolator(new DecelerateInterpolator());//匀速
//                            animator.setRepeatCount(0);//0表示不循环，-1表示无限循环
//                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                @Override
//                                public void onAnimationUpdate(ValueAnimator animation) {
//                                    /**
//                                     * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
//                                     * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
//                                     * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
//                                     */
//                                    float jiaodu = (float) animation.getAnimatedValue();
//                                    view_xiaoxi.setY(jiaodu);
//
//                                }
//                            });
//                            animator.start();
//
//                            scrollView_03.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    });
//                    SystemClock.sleep(600);
//
//                }
//            }
//        }).start();
//    }
//
//
//    private void erweimaBitmap(Bitmap bitmap){
//
//        if (bitmap!=null){
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            int[] pixels = new int[width * height];
//            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//
//            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
//            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
//            Reader reader = new QRCodeReader();
//            try {
//                Result result = reader.decode(binaryBitmap);
//
//                Log.d("MianBanJiActivity2", result.getText()+"二维码文字");
//              //  isERM=true;
//                EventBus.getDefault().post(result.getText()+"");
//            } catch (NotFoundException | ChecksumException | FormatException e) {
//                e.printStackTrace();
//               // isERM=true;
//            }
//        }else {
//          //  isERM=true;
//        }
//
//    }
//
//}
