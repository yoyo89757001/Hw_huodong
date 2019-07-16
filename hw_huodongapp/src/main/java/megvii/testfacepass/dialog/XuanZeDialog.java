package megvii.testfacepass.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import megvii.testfacepass.R;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class XuanZeDialog extends Dialog {
    private Button l1,l2,l3;

    public XuanZeDialog(Context context) {
        super(context, R.style.dialog_style);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.xiugaidialog_xuanze, null);

        l1= mView. findViewById(R.id.quxiao1);
        l2= mView.findViewById(R.id.quxiao2);
        l3= mView. findViewById(R.id.quxiao3);
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

    /**
     * 确定键监听器
     * @param listener
     */
    public void set1(View.OnClickListener listener){
        l1.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void set2(View.OnClickListener listener){
        l2.setOnClickListener(listener);
    }
    public void set3(View.OnClickListener listener){
        l3.setOnClickListener(listener);
    }

}
