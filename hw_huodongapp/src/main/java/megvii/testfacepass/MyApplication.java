package megvii.testfacepass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.tencent.bugly.Bugly;
import com.yatoooon.screenadaptation.ScreenAdapterTools;


import java.io.File;
import java.io.IOException;

import cn.richinfo.mt.MTSdk;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.beans.BenDiJiLuBean;
import megvii.testfacepass.beans.ChengShiIDBean;
import megvii.testfacepass.beans.DaKaBean;
import megvii.testfacepass.beans.GuanHuai;
import megvii.testfacepass.beans.LunBoBean;
import megvii.testfacepass.beans.MSRBean;
import megvii.testfacepass.beans.MyObjectBox;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.beans.TodayBean;
import megvii.testfacepass.beans.XinXiAll;
import megvii.testfacepass.beans.XinXiIdBean;
import megvii.testfacepass.beans.ZhiChiChengShi;
import megvii.testfacepass.dialogall.CommonData;
import megvii.testfacepass.dialogall.CommonDialogService;
import megvii.testfacepass.dialogall.ToastUtils;
import megvii.testfacepass.utils.GsonUtil;
import megvii.testfacepass.utils.MacUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/8/3.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private FacePassHandler facePassHandler=null;
    public static MyApplication myApplication;
    private Box<ChengShiIDBean> chengShiIDBeanBox=null;
    private Box<BaoCunBean> baoCunBeanBox=null;
    private Box<Subject> subjectBox=null;
    private Box<LunBoBean> lunBoBeanBox=null;
    private Box<XinXiAll> xinXiAllBox=null;
    private Box<XinXiIdBean> xinXiIdBeanBox= null;
    private Box<GuanHuai> guanHuaiBox=null;
    private Box<TodayBean> todayBeanBox = null;
    private Box<MSRBean> msrBeanBox = null;
    private Box<BenDiJiLuBean> benDiJiLuBeanBox = null;
    private Box<DaKaBean> daKaBeanBox = null;
    public static String URL ="http://hy.inteyeligence.com/front";
    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"ruitonghwzip/zppath";
    public static final String SDPATH2 = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"ruitonghw";



    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        BoxStore mBoxStore = MyObjectBox.builder().androidContext(this).build();

        Bugly.init(getApplicationContext(), "b1f6250dc0", false);
        //适配
        ScreenAdapterTools.init(this);

       // Log.d("MyApplication", "IMEI"+FileUtil.getIMEI(MyApplication.myApplication));

      //  Log.d("MyApplication","设备id"+ GetDeviceId.getDeviceId(MyApplication.myApplication));
        MTSdk.init(MyApplication.myApplication,MacUtils.getIMEI(MyApplication.myApplication), null , null , "M100000394");

        //登录名:gzruitong
       // 登录密码:RuiTong1205~

      //  Log.d("MyApplication","机器码"+ FileUtil.getSerialNumber(this) == null ? FileUtil.getIMSI() : FileUtil.getSerialNumber(this));
        //全局dialog
        this.registerActivityLifecycleCallbacks(this);//注册
        CommonData.applicationContext = this;
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        CommonData.ScreenWidth = metric.widthPixels; // 屏幕宽度（像素）
        Intent dialogservice = new Intent(this, CommonDialogService.class);
        startService(dialogservice);

        baoCunBeanBox= mBoxStore.boxFor(BaoCunBean.class);
        subjectBox= mBoxStore.boxFor(Subject.class);
        lunBoBeanBox= mBoxStore.boxFor(LunBoBean.class);
        xinXiAllBox= mBoxStore.boxFor(XinXiAll.class);
        xinXiIdBeanBox= mBoxStore.boxFor(XinXiIdBean.class);
        guanHuaiBox= mBoxStore.boxFor(GuanHuai.class);
        chengShiIDBeanBox= mBoxStore.boxFor(ChengShiIDBean.class);
        todayBeanBox= mBoxStore.boxFor(TodayBean.class);
        benDiJiLuBeanBox= mBoxStore.boxFor(BenDiJiLuBean.class);
        chengShiIDBeanBox= mBoxStore.boxFor(ChengShiIDBean.class);
        daKaBeanBox= mBoxStore.boxFor(DaKaBean.class);
        msrBeanBox= mBoxStore.boxFor(MSRBean.class);

        if(chengShiIDBeanBox.getAll().size()==0){
            OkHttpClient okHttpClient= new OkHttpClient();
            okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
                    .get()
                    .url("http://apis.juhe.cn/simpleWeather/cityList?key=8ee00f3d480b636d67d6a8966f07ffb7");
           // .url("http://v.juhe.cn/weather/citys?key=356bf690a50036a5cfc37d54dc6e8319");
            // .url("http://v.juhe.cn/weather/index?format=2&cityname="+text1+"&key=356bf690a50036a5cfc37d54dc6e8319");
            // step 3：创建 Call 对象
            Call call = okHttpClient.newCall(requestBuilder.build());
            //step 4: 开始异步请求
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("AllConnects", "请求失败"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    Log.d("AllConnects", "请求成功"+call.request().toString());
                    //获得返回体
                    try{

                        ResponseBody body = response.body();
                        String ss=body.string().trim();
                        Log.d("AllConnects", "天气"+ss);

                        JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
                        Gson gson=new Gson();
                        final ZhiChiChengShi renShu=gson.fromJson(jsonObject,ZhiChiChengShi.class);
                        int size=renShu.getResult().size();
                        //  chengShiIDBeanBox.removeAll();

                        for (int i=0;i<size;i++){
                            ChengShiIDBean bean=new ChengShiIDBean();
                            bean.setId(renShu.getResult().get(i).getId());
                            bean.setCity(renShu.getResult().get(i).getCity());
                            bean.setDistrict(renShu.getResult().get(i).getDistrict());
                            bean.setProvince(renShu.getResult().get(i).getProvince());
                            chengShiIDBeanBox.put(bean);
                        }


                    }catch (Exception e){
                        Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                    }

                }
            });
        }



      BaoCunBean  baoCunBean = mBoxStore.boxFor(BaoCunBean.class).get(123456L);
        if (baoCunBean == null) {
            baoCunBean = new BaoCunBean();
            baoCunBean.setId(123456L);
            baoCunBean.setHoutaiDiZhi("http://encore.inteyeligence.com:8082/");
            baoCunBean.setShibieFaceSize(40);
            baoCunBean.setShibieFaZhi(88);
            baoCunBean.setRuKuFaceSize(70);
            baoCunBean.setRuKuMoHuDu(0.3f);
            baoCunBean.setLed(100);
            baoCunBean.setMoban(1);
            baoCunBean.setHuoTiFZ(70);
            baoCunBean.setMima(123456);
            baoCunBean.setMima2(123456);
            baoCunBean.setJihuoma("0000-0000-0000-0000-0000");
            baoCunBean.setHuoTi(false);
            baoCunBean.setDangqianShiJian("d");
            baoCunBean.setTianQi(false);
            mBoxStore.boxFor(BaoCunBean.class).put(baoCunBean);
        }


        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }


    public Box<TodayBean> getTodayBeanBox(){
        return todayBeanBox;
    }

    public Box<BenDiJiLuBean> getBenDiJiLuBeanBox(){
        return benDiJiLuBeanBox;
    }
    public Box<ChengShiIDBean> getChengShiIDBeanBox(){
        return chengShiIDBeanBox;
    }

    public Box<Subject> getSubjectBox(){
        return subjectBox;
    }

    public Box<LunBoBean> getLunBoBeanBox(){
        return lunBoBeanBox;
    }

    public Box<XinXiAll> getXinXiAllBox(){
        return xinXiAllBox;
    }
    public Box<XinXiIdBean> getXinXiIdBeanBox(){
        return xinXiIdBeanBox;
    }
    public Box<GuanHuai> getGuanHuaiBox(){
        return guanHuaiBox;
    }
    public Box<BaoCunBean> getBaoCunBeanBox(){
        return baoCunBeanBox;
    }
    public Box<DaKaBean> getDaKaBeanBox(){
        return daKaBeanBox;
    }
    public Box<MSRBean> getMSRBeanBox(){
        return msrBeanBox;
    }


    public FacePassHandler getFacePassHandler() {

        return facePassHandler;
    }

    public void setFacePassHandler(FacePassHandler facePassHandler1){
        facePassHandler=facePassHandler1;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if(activity.getParent()!=null){
            CommonData.mNowContext = activity.getParent();
        }else
            CommonData.mNowContext = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(activity.getParent()!=null){
            CommonData.mNowContext = activity.getParent();
        }else
            CommonData.mNowContext = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ToastUtils.getInstances().cancel();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
