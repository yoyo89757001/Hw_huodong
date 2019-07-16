package cn.richinfo.manager;

import android.content.Context;

/**
 * Created by Administrator on 2018/8/9.
 */

public class DeviceInfoManager {
    private Context mContext;
    private static DeviceInfoManager mDeviceInfoManager;


    private DeviceInfoManager(Context context)
    {
        this.mContext = context;
    }


    public static synchronized final DeviceInfoManager getInstance(Context context) {
        if (mDeviceInfoManager == null) {
            mDeviceInfoManager = new DeviceInfoManager(context);
        }
        return mDeviceInfoManager;
    }


    public  String getRomStorageSize () {
        return "16g";
    }

    public String getRamStorageSize() {
        return "4g";
    }

    public String getMacAddress ()
    {
        return "MacAddress";
    }

    public String getCPUModel ()
    {
        return "CPUModel";
    }

    //返回设备的操作系统版本号信息，指Android或YunOS的系统大版本
    public String getOSVersion ()
    {
        return "OSVersion";
    }

    //固件版本号
    public String getDeviceSoftwareVersion()
    {
        return "DeviceSoftwareVersion";
    }

    //固件名称
    public String getDeviceSoftwareName()
    {
        return "DeviceSoftwareName";
    }

    //Volte开关状态
    public int getVoLTEState()
    {
        return 0;
    }

    public String getNetworkType()
    {
        return "wifi";
    }

    //宽带账号
    public String getNetAccount()
    {
        return "NetAccount";
    }


    public String getPhoneNumber()
    {
        return "13872456363";
    }

    //同步返回上次定位请求缓存的位置信息
    public LocationMsg getLocation()
    {
        return new LocationMsg(112, 24, 1);
    }

    //发起定位请求，并缓存位置信息备查询
    public static void startLocation()
    {

    }

    public static class LocationMsg {
        public LocationMsg(double longitude, double latitude, int locationMode) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.locationMode = locationMode;
        }

        public double longitude;
        public double latitude;
        public int locationMode;   //定位方式，gps 1或者是北斗2
    }

//    public interface LocationListener {
//        /** Called when a response is received. */
//        public void success(LocationMsg response);
//        public void fail();
//    }

//    public static class LocationThread extends Thread{
//        private LocationListener mLocationListener;
//        public LocationThread(String name, LocationListener locationListener){
//            super(name);
//            mLocationListener = locationListener;
//        }
//
//        @Override
//        public void run() {
//            try{
//                this.sleep(3000);
//                boolean isgetLocation = true;
//                LocationMsg mLocationMsg = new LocationMsg(112.1212, 88.88, 1);
//                if(isgetLocation)
//                {
//                    mLocationListener.success(mLocationMsg);
//                }else
//                {
//                    mLocationListener.fail();
//                }
//            }catch (Exception e)
//            {
//
//            }
//
//        }
//    }

}
