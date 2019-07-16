package megvii.testfacepass.ui;

import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;


import android.util.DisplayMetrics;
import android.util.Log;
;
import android.widget.Toast;



import java.io.IOException;

import java.util.concurrent.TimeUnit;

import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;

import megvii.testfacepass.beans.BenDiJiLuBean;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;



public class MianBanJiActivity extends Activity  {


    /* 程序所需权限 ：相机 文件存储 网络访问 */
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE, PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE};

    private int dw,dh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1layout);


     //   EventBus.getDefault().register(this);//订阅

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dw = dm.widthPixels;
        dh = dm.heightPixels;

        initView();

        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {


        }

    }


    public void  bck1(){


    }

    public void  bck2 (){


    }

    public void  bck3 (){


    }


    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onPause() {
        super.onPause();

    }



    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
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
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)) {
                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
                    }
            } else {
            ///////////


            }
        }
    }



    private void initView() {



    }



    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    public void onStart() {
        super.onStart();
        //marqueeView.startFlipping();
    }


    @Override
    protected void onDestroy() {


       // EventBus.getDefault().unregister(this);//解除订阅

        super.onDestroy();
    }








    //上传识别记录2
    private void link_shangchuanjilu2(final BenDiJiLuBean subject) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(100000, TimeUnit.MILLISECONDS)
                .connectTimeout(100000, TimeUnit.MILLISECONDS)
                .readTimeout(100000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();
        RequestBody body = null;

        body = new FormBody.Builder()
                //.add("name", subject.getName()) //
                //.add("companyId", subject.getCompanyId()+"") //公司di
                //.add("companyName",subject.getCompanyName()+"") //公司名称
                //.add("storeId", subject.getStoreId()+"") //门店id
                //.add("storeName", subject.getStoreName()+"") //门店名称
                .add("subjectId", subject.getSubjectId() + "") //员工ID
                .add("subjectType", subject.getSubjectType()+"") //人员类型
                // .add("department", subject.getPosition()+"") //部门
                .add("discernPlace","")//识别地点
                // .add("discernAvatar",  "") //头像
                .add("identificationTime",subject.getIdentificationTime()+"")//时间
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


                } catch (Exception e) {

                    Log.d("WebsocketPushMsg", e.getMessage() + "gggg");

                }finally {

                }
            }
        });
    }
}
