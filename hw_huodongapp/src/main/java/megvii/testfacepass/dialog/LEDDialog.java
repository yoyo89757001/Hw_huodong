package megvii.testfacepass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import io.objectbox.Box;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.beans.BaoCunBean;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class LEDDialog extends Dialog implements  View.OnClickListener {
    private SeekBar seekBar2;
    private Button l1;
    private Box<BaoCunBean> baoCunBeanDao=null;
    private BaoCunBean baoCunBean=null;
    private int aa=0;

    public LEDDialog(Context context) {
        super(context, R.style.dialog_style2);
        Window window =  this.getWindow();
        if ( window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = LayoutParams.WRAP_CONTENT;
                attr.width = LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        baoCunBeanDao = MyApplication.myApplication.getBaoCunBeanBox();
        baoCunBean=baoCunBeanDao.get(123456L);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.queren_ll5, null);



        seekBar2= (SeekBar) mView.findViewById(R.id.seekBar2);
        seekBar2.setMax(5);
        seekBar2.setProgress(baoCunBean.getYusu());
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("LEDDialog", "progress:" + progress);
                aa=1;
                baoCunBean.setLed(progress*50);
                baoCunBeanDao.put(baoCunBean);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        l1= (Button)mView. findViewById(R.id.queren);
        l1.setOnClickListener(this);

        super.setContentView(mView);


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




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.queren:
                if (aa!=1){
                    baoCunBean.setLed(0);
                    baoCunBeanDao.put(baoCunBean);
                }
                this.dismiss();

                break;

        }

    }

//    /**
//     * 确定键监听器
//     * @param listener
//     */
//    public void setOnPositiveListener(View.OnClickListener listener){
//        positiveButton.setOnClickListener(listener);
//    }
//    /**
//     * 取消键监听器
//     * @param listener
//     */
//    public void setOnQuXiaoListener(View.OnClickListener listener){
//        quxiao.setOnClickListener(listener);
//    }
}
