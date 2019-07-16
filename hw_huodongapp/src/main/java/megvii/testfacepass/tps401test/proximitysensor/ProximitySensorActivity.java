//package megvii.testfacepass.tps401test.proximitysensor;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//
//import megvii.testfacepass.R;
//
//
//public class ProximitySensorActivity extends Activity implements View.OnClickListener {
//	Button start_service = null;
//	Button stop_service = null;
//	public static TextView showTextView = null;
//	private static Activity activity = null;
//	Intent intent = null;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_proximity_sensor);
//		activity = ProximitySensorActivity.this;
//
//		initUI();
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		stopService(intent);
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//	}
//
//	private void initUI() {
//		start_service = (Button) findViewById(R.id.start_service);
//		stop_service = (Button) findViewById(R.id.stop_service);
//		start_service.setOnClickListener(this);
//		stop_service.setOnClickListener(this);
//		showTextView = (TextView) findViewById(R.id.showTV);
//	}
//
//	@Override
//	public void onClick(View v) {
//		intent = new Intent(this, ProximityService.class);
//
//		switch (v.getId()) {
//		case R.id.start_service:
//			startService(intent);
//			// Toast.makeText(ProximitySensorActivity.this, "click",
//			// Toast.LENGTH_SHORT);
//			break;
//		case R.id.stop_service:
//			// setBrightness(150);
//			// Toast.makeText(ProximitySensorActivity.this, "click",
//			// Toast.LENGTH_SHORT);
//			stopService(intent);
//		}
//	}
//
//	public static void setBrightness(int brightness) {
//		if (activity.getWindow() != null) {
//			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//			lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
//			activity.getWindow().setAttributes(lp);
//			Log.i("WINDOW", "not null");
//		}
//
//	}
//
//	public static void setText(String str) {
//		showTextView.setText(str);
//	}
//
//}