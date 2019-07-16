package megvii.testfacepass.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.testfacepass.MyApplication;
import megvii.testfacepass.R;
import megvii.testfacepass.adapter.UserListAdapter;
import megvii.testfacepass.beans.Subject;
import megvii.testfacepass.beans.Subject_;

public class UserListActivity extends Activity implements UserListAdapter.ItemDeleteButtonClickListener {
    private FacePassHandler facePassHandler=MyApplication.myApplication.getFacePassHandler();
    private Box<Subject> subjectBox=MyApplication.myApplication.getSubjectBox();
    private ListView listView;
    private UserListAdapter adapter;
    private List<Subject> subjectList=new ArrayList<>();
    private TextView zongrenshu;
    private EditText editText;
    private ZLoadingDialog zLoadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ImageView fh=findViewById(R.id.fanhui);
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zongrenshu=findViewById(R.id.renshu);
        listView=findViewById(R.id.recyle);
        editText=findViewById(R.id.sousuo);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String ss=s.toString();
                if (!ss.equals("")){
                    final List<Subject> subjectList2=  subjectBox.query().contains(Subject_.name,ss)
                            .build().find();
                    if (subjectList2.size()>0) {

                        zLoadingDialog = new ZLoadingDialog(UserListActivity.this);
                        zLoadingDialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                                .setLoadingColor(Color.parseColor("#0d2cf9"))//颜色
                                .setHintText("加载中...")
                                .setHintTextSize(16) // 设置字体大小 dp
                                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                                .setDialogBackgroundColor(Color.parseColor("#99111111")) // 设置背景色，默认白色
                                .show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                subjectList.clear();
                                subjectList.addAll(subjectList2);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        if (zLoadingDialog != null)
                                            zLoadingDialog.dismiss();
                                        showOrHide(UserListActivity.this);
                                    }
                                });
                            }
                        }).start();
                    }

                }else {

                    zLoadingDialog = new ZLoadingDialog(UserListActivity.this);
                    zLoadingDialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                            .setLoadingColor(Color.parseColor("#0d2cf9"))//颜色
                            .setHintText("加载中...")
                            .setHintTextSize(16) // 设置字体大小 dp
                            .setHintTextColor(Color.WHITE)  // 设置字体颜色
                            .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                            .setDialogBackgroundColor(Color.parseColor("#99111111")) // 设置背景色，默认白色
                            .show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            subjectList.clear();
                            subjectList.addAll(subjectBox.getAll());

                            //  Log.d("UserListActivity", "subjectList.size():" + subjectList.size());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter.notifyDataSetChanged();
                                    zongrenshu.setText("总人数:"+subjectList.size());
                                    if (zLoadingDialog!=null)
                                    zLoadingDialog.dismiss();
                                    showOrHide(UserListActivity.this);
                                }
                            });

                        }
                    }).start();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (facePassHandler!=null){
            adapter=new UserListAdapter(subjectList,UserListActivity.this,facePassHandler);
            adapter.setOnItemDeleteButtonClickListener(this);
            listView.setAdapter(adapter);
        }else {
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                subjectList.addAll(subjectBox.getAll());

              //  Log.d("UserListActivity", "subjectList.size():" + subjectList.size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        zongrenshu.setText("总人数:"+subjectList.size());
                    }
                });

            }
        }).start();
    }



    //如果输入法在窗口上已经显示，则隐藏，反之则显示
    private   void showOrHide(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void OnItemDeleteButtonClickListener(final int position) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(UserListActivity.this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    facePassHandler.deleteFace(subjectList.get(position).getTeZhengMa().getBytes());
                    subjectBox.remove(subjectList.get(position));
                } catch (FacePassException e) {
                    e.printStackTrace();
                }
                subjectList.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("你确定要删除吗？");
        builder.setTitle("温馨提示");
        builder.show();

    }


}
