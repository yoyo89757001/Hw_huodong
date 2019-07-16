package megvii.testfacepass.ui;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import megvii.testfacepass.R;

public class HuiChangPingMianActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hui_chang_ping_mian);


        final Button daojishi = findViewById(R.id.daojishi);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /** 倒计时60秒，一次1秒 */
                CountDownTimer timer = new CountDownTimer(16*1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        daojishi.setVisibility(View.VISIBLE);
                        daojishi.setText(millisUntilFinished/1000+" 秒后返回");
                    }
                    @Override
                    public void onFinish() {
                        finish();

                    }
                };
                timer.start();
            }
        });
    }


}
