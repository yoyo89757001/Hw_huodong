package megvii.testfacepass.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.objectbox.Box;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;
import megvii.testfacepass.utils.GetDeviceId;
import megvii.testfacepass.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class BaseActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private static final String PERMISSION_ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
    private static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE,
            PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE,PERMISSION_ACCESS_WIFI_STATE,PERMISSION_READ_PHONE_STATE};

    private BaoCunBean baoCunBean=null;
    private Box<BaoCunBean> baoCunBeanDao;
    private       OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .readTimeout(30000, TimeUnit.MILLISECONDS)
//				    .cookieJar(new CookiesManager())
   //         .retryOnConnectionFailure(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();


        baoCunBean = baoCunBeanDao.get(123456L);


        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {
            start();
        }

    }


    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED&&
                    checkSelfPermission(PERMISSION_ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED&&
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



    private void start(){
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
        Log.d("MainActivity201", FacePassHandler.getVersion()+"");
        //1代表横  2代表竖
        switch (baoCunBean.getMoban()) {
            case 1://横屏 第一版
                startActivity(new Intent(BaseActivity.this, MianBanJiActivity3.class));
                finish();

                break;
            case 2:
                startActivity(new Intent(BaseActivity.this, MianBanJiActivity4.class));
                finish();
                break;
            case 3:

                break;

        }



    }

    public static class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Intent i = new Intent(context, BaseActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }



    public static String getLocalMacAddress() {
        String mac = null;
        try {
            String path = "sys/class/net/wlan0/address";
            FileInputStream fis_name = new FileInputStream(path);
            byte[] buffer_name = new byte[8192];
            int byteCount_name = fis_name.read(buffer_name);
            if (byteCount_name > 0) {
                mac = new String(buffer_name, 0, byteCount_name, "utf-8");
            }
            if (mac == null) {
                fis_name.close();
                return "";
            }
            fis_name.close();
        } catch (Exception io) {
            String path = "sys/class/net/eth0/address";
            FileInputStream fis_name;
            try {
                fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if (byteCount_name > 0) {
                    mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis_name.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (mac == null) {
            return "";
        } else {
            return mac.trim();
        }

    }


    //更新信鸽token
    private void link_uplod(String deviceId,  String token){
        //	final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

        //RequestBody requestBody = RequestBody.create(JSON, json);
        RequestBody body = new FormBody.Builder()
                .add("machineCode", deviceId+"")
                .add("machineToken",token+"")
                .build();
        Log.d("BaseActivity", deviceId+"");
        Log.d("BaseActivity", token+"");

        Request.Builder requestBuilder = new Request.Builder()
//				.header("Content-Type", "application/json")
//				.header("user-agent","Koala Admin")
                //.post(requestBody)
                //.get()
                .post(body)
                .url(baoCunBean.getHoutaiDiZhi()+"/app/updateToken");

        // step 3：创建 Call 对象
        Call call = okHttpClient.newCall(requestBuilder.build());
        //step 4: 开始异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AllConnects", "请求失败"+e.getMessage());
               // EventBus.getDefault().post("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                //  Log.d("AllConnects", "请求成功"+call.request().toString());
                //获得返回体
                String ss=null;
                try{
                    ResponseBody body = response.body();
                    ss=body.string().trim();
                    Log.d("AllConnects", "更新信鸽token:"+ss);


                }catch (Exception e){

                    Log.d("WebsocketPushMsg", e.getMessage()+"ttttt");
                }

            }
        });
    }


}
