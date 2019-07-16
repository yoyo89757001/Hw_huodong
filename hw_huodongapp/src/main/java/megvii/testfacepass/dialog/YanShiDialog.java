package megvii.testfacepass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import megvii.testfacepass.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class YanShiDialog extends Dialog {
    private ImageView imageView;

    public YanShiDialog(Context context) {
        super(context, R.style.dialog_style);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.xiugaidialog_ys, null);
        imageView= mView.findViewById(R.id.im);

        super.setContentView(mView);
    }





    public void setImageView(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {


    }



    //    /**
//     * 确定键监听器
//     * @param listener
//     */
//    public void setOnQueRenListener(View.OnClickListener listener){
//        l1.setOnClickListener(listener);
//    }
//    /**
//     * 取消键监听器
//     * @param listener
//     */
//    public void setQuXiaoListener(View.OnClickListener listener){
//        l2.setOnClickListener(listener);
//    }


}
