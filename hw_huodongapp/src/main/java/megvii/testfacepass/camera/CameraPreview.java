package megvii.testfacepass.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;


/**
 * Created by linyue on 16/1/2.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera = null;

    private CameraPreviewListener listener;

    private float scaleW = 1;
    private int widthSize;
    private float scaleH = 1;

    private int hight;

    public CameraPreview(Context context) {
        super(context);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    void setCamera(Camera camera) {
        this.camera = camera;
        restartPreview(getHolder());
    }

    private void restartPreview(SurfaceHolder holder) {
        if (camera != null) {
            if (holder.getSurface() == null) {
                return;
            }

            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
//                camera.startFaceDetection();
                if (listener != null) {
                    listener.onStartPreview();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setHight(int hight){
        this.hight=hight;
    }

    public void setScale(float scaleW, float scaleH) {
        this.scaleW = scaleW;
        this.scaleH = scaleH;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        restartPreview(holder);

//        RelativeLayout.LayoutParams  pp= (RelativeLayout.LayoutParams) this.getLayoutParams();
//        pp.height=(int)(hight*0.2);
//        pp.width=(int)(hight*0.2);
//        this.setLayoutParams(pp);
//        this.invalidate();

    }

    @Override
    //绘制一个圆形的框，并设置圆框的坐标和半径大小
    //这个绘制在16:9的手机上显示很好，但是在更长的手机上（大于16/9）会偏上，
    public void draw(Canvas canvas) {
     //   Log.e("onDraw", "draw: test");
        Path path = new Path();
        //path.addCircle(widthSize / 2, height / 2, height / 2, Path.Direction.CCW);
        path.addCircle(widthSize * 0.38f, widthSize  * 0.38f, widthSize  * 0.36f, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        restartPreview(holder);
    }

    public void setListener(CameraPreviewListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int screenWidth = getScreenWidth(getContext());
        int screenHeight = getScreenHeight(getContext());
     //   Log.d("screenWidth",Integer.toString(screenWidth));
    ////    Log.d("screenHeight",Integer.toString(screenHeight));
        w = screenWidth;
        h = screenHeight;
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取屏幕长宽比例，这样设置不会发生畸变，千万不要根据一个手机设定一个数
        //那样换一个手机就可能会出现显示的比例问题
        int screenWidth = getScreenWidth(getContext());
        int screenHeight = getScreenHeight(getContext());
       // height=600;
        //可以理解为红色的背景盖住了大部分的区域，我们只能看到圆框里面的，如果还是按照原来的比例绘制surfaceview
        //需要把手机拿的很远才可以显示出整张脸，故而我用了一个比较取巧的办法就是，按比例缩小，试验了很多数后，感觉0.55
        //是最合适的比例
        double screenWidth1= 0.366*screenWidth;
        double screenHeight1= 0.22*screenHeight;
     //   Log.e("onMeasure", "widthSize="+widthSize);
     //   Log.e("onMeasure", "draw: widthMeasureSpec = " +screenWidth + "  heightMeasureSpec = " + screenHeight);
        //绘制的输入参数必须是整数型，做浮点型运算后为float型数据，故需要做取整操作
        setMeasuredDimension((int) screenWidth1, (int) screenHeight1);
        //setMeasuredDimension(widthSize, heightSize);
    }

    public interface CameraPreviewListener {
        public void onStartPreview();
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)        context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }


    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }


}