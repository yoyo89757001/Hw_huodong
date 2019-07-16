package megvii.testfacepass.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

public class ToastUtils {

    public static void show (Context context,String s){
        Toast tastyToast = TastyToast.makeText(context, s, TastyToast.LENGTH_LONG, TastyToast.INFO);
        tastyToast.setGravity(Gravity.CENTER, 0, 0);
        tastyToast.show();
    }

    public static void show2 (Context context,String s){
        Toast tastyToast = TastyToast.makeText(context, s, TastyToast.LENGTH_LONG, TastyToast.ERROR);
        tastyToast.setGravity(Gravity.CENTER, 0, 0);
        tastyToast.show();
    }

}
