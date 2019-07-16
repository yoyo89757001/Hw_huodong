package megvii.testfacepass.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassAddFaceResult;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class FacePassUtil {

    /* SDK 实例对象 */
    private Context context;
    private int TIMEOUT=30*1000;

    private    FacePassHandler mFacePassHandler;  /* 人脸识别Group */
    private  final String group_name = "facepasstestx";
    private  boolean isLocalGroupExist = false;
    private   BaoCunBean baoCunBean;

    public  void init(final Activity activity , final Context context, final int cameraRotation, final BaoCunBean baoCunBean){
        this.context=context;
        this.baoCunBean=baoCunBean;

            new Thread() {
                @Override
                public void run() {
                    while (!activity.isFinishing()) {
                        if (FacePassHandler.isAvailable()) {
                            FacePassConfig config;
                            try {
                                /* 填入所需要的配置 */
                                config = new FacePassConfig();
                                config.poseModel = FacePassModel.initModel(context.getAssets(), "pose.alfa.tiny.170515.bin");
                                config.blurModel = FacePassModel.initModel(context.getAssets(), "blurness.v5.l2rsmall.bin");

                                config.livenessModel = FacePassModel.initModel(context.getAssets(), "panorama.facepass.190224_3288_CPU_2.combine.bin");
                                //也可以使用GPU活体模型，GPU活体模型分两个，用于GPU加速的模型和CACHE，当使用CPU活体模型时，请传null，当使用GPU活体模型时，必须传入加速cache模型,当前版本没GPU，后续提供
                                //config.livenessModel = FacePassModel.initModel(getApplicationContext().getAssets(), "panorama.facepass.190131_3288_GPU_2.bin");
                                //config.livenessGPUCache = FacePassModel.initModel(getApplicationContext().getAssets(), "model_gpu.cache");

                                config.searchModel = FacePassModel.initModel(context.getAssets(), "feat.inu.3comps.inp96.180ms.e6000.pca512.181225.bin");
                                config.detectModel = FacePassModel.initModel(context.getAssets(), "detector.retinanet.face2head.x14.190128.bin");
                                config.detectRectModel = FacePassModel.initModel(context.getAssets(), "det.rect.retinanet.face2head.x14.190128.bin");
                                config.landmarkModel = FacePassModel.initModel(context.getAssets(), "lmk.rect_score.vgg.12M.20190121_81.bin");

                                config.smileModel = FacePassModel.initModel(context.getAssets(), "attr.blur.align.gray.general.mgf29.0.1.1.181229.bin");
                                config.ageGenderModel = FacePassModel.initModel(context.getAssets(), "age_gender.v2.bin");
                                //如果不需要表情和年龄性别功能，smileModel和ageGenderModel可以为null
                                //config.smileModel = null;
                                //config.ageGenderModel = null;

                                config.searchThreshold =  baoCunBean.getShibieFaZhi();
                                config.livenessThreshold = baoCunBean.getHuoTiFZ();
                                config.livenessEnabled = baoCunBean.isHuoTi();
                               boolean ageGenderEnabledGlobal = (config.ageGenderModel != null);
                                config.faceMinThreshold = baoCunBean.getShibieFaceSize();
                                config.poseThreshold = new FacePassPose(30f, 30f, 30f);
                                config.blurThreshold = 0.2f;
                                config.lowBrightnessThreshold = 70f;
                                config.highBrightnessThreshold = 210f;
                                config.brightnessSTDThreshold = 60f;
                                config.retryCount = 2;
                                config.smileEnabled = false;
                                config.maxFaceEnabled = false;
                                config.rotation = cameraRotation;
                                config.fileRootPath = MyApplication.SDPATH2;

                                /* 创建SDK实例 */
                                mFacePassHandler = new FacePassHandler(config);
                                MyApplication.myApplication.setFacePassHandler(mFacePassHandler);


                                checkGroup(activity,context);

                              //  float searchThreshold2 = 75f;
                              //  float livenessThreshold2 = 48f;
                             //   boolean livenessEnabled2 = true;
                                int faceMinThreshold2 = baoCunBean.getRuKuFaceSize();
                                float blurThreshold2 = baoCunBean.getRuKuMoHuDu();
                                float lowBrightnessThreshold2 = 70f;
                                float highBrightnessThreshold2 = 210f;
                                float brightnessSTDThreshold2 = 60f;
                                FacePassConfig config1=new FacePassConfig(faceMinThreshold2,25f,25f,25f,blurThreshold2,
                                        lowBrightnessThreshold2,highBrightnessThreshold2,brightnessSTDThreshold2);
                                boolean is=   mFacePassHandler.setAddFaceConfig(config1);

                                Log.d("YanShiActivity", " 设置入库质量配置"+is );


                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast tastyToast= TastyToast.makeText(context,"识别模块初始化成功",TastyToast.LENGTH_LONG,TastyToast.INFO);
                                        tastyToast.setGravity(Gravity.CENTER,0,0);
                                        tastyToast.show();
                                       MyApplication.myApplication.setFacePassHandler(mFacePassHandler);
                                        EventBus.getDefault().post("mFacePassHandler");
                                   //     chaxuncuowu();


                                    }
                                });



                            } catch (FacePassException e) {
                                e.printStackTrace();

                                return;
                            }
                            return;
                        }
                        try {
                            /* 如果SDK初始化未完成则需等待 */
                            sleep(500);
                            Log.d("FacePassUtil", "激活中。。。");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }


    private  void checkGroup(Activity activity, final Context context) {
        if (mFacePassHandler == null) {
            return;
        }
        String[] localGroups = mFacePassHandler.getLocalGroups();

        isLocalGroupExist = false;
        if (localGroups == null || localGroups.length == 0) {
            try {
               boolean a= mFacePassHandler.createLocalGroup(group_name);
                Log.d("FacePassUtil", "创建组:" + a);
                FacePassAddFaceResult result= mFacePassHandler.addFace(BitmapFactory.decodeResource(context.getResources(), R.drawable.a2));
                if (result.result==0){
                  boolean bb =   mFacePassHandler.bindGroup(group_name,result.faceToken);
                  Log.d("FacePassUtil", "第一个人入库:" + bb);
                }
            } catch (FacePassException e) {
                e.printStackTrace();
            }
        }else {
            Log.d("FacePassUtil", "组名:"+localGroups[0]);
        }
    }

    private void chaxuncuowu(){
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
//				.cookieJar(new CookiesManager())
                //.retryOnConnectionFailure(true)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("machineCode)", FileUtil.getSerialNumber(context) == null ? FileUtil.getIMSI() : FileUtil.getSerialNumber(context))
                .build();
        Request.Builder requestBuilder = new Request.Builder()
                //.header("Content-Type", "application/json")
                .post(body)
                .url(MyApplication.URL + "/app/findFailurePush");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());

        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("AllConnects", "请求成功" + call.request().toString());
                //获得返回体
                try {
                    //没了删除，所有在添加前要删掉所有

                    ResponseBody body = response.body();
                    String ss = body.string().trim();
                    Log.d("AllConnects", "查询错误推送" + ss);

                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");
                }
            }
        });
    }

}
