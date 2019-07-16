package megvii.testfacepass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import megvii.testfacepass.R;

public class Dddddd extends View {
    private Paint mPaint = null;
    private Bitmap mMaskBitmap=null;
    private Bitmap mImageBitmap;
   // private int mWidth;
   // private int mHeight;
    private RectF rectF =new RectF();
    private RectF rectF2 =new RectF();

  //  private RectF dstRect, srcRect;
    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.DST_IN;
    public Dddddd(Context context) {
        super(context);
        init();
    }

    public Dddddd(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Dddddd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


     private void init(){

         mPaint = new Paint();
         mPaint.setColor(Color.YELLOW);
         mPaint.setAntiAlias(true);
         mImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lingxing2);
         mXfermode = new PorterDuffXfermode(mPorterDuffMode);
         rectF.set(14,2,236,248);
         rectF2.set(12,0,238,250);
     }

     public void setmMaskBitmap(Bitmap bitmap){
         mMaskBitmap=bitmap;
     }



    @Override
    protected void onDraw(Canvas canvas) {
        if (mMaskBitmap==null){
            invalidate();
            return;
        }
        canvas.drawColor(Color.TRANSPARENT);

        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        int saveCount = canvas.saveLayer(rectF, mPaint, Canvas.ALL_SAVE_FLAG);
        //绘制目标图
        canvas.drawBitmap(mMaskBitmap, null, rectF, mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //绘制源图
        canvas.drawBitmap(mImageBitmap, null, rectF, mPaint);
        //清除混合模式
        mPaint.setXfermode(null);

        //还原画布
        canvas.restoreToCount(saveCount);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lingxing),null,rectF2,mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w/2;
        int centerY = h/2;
        int quarterWidth = width /4;
      //  srcRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);
      //  dstRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);
    }


//    private Bitmap getBitmap() {
////        创建图片
//
////        获取图片的宽高
//        mWidth = mImageBitmap.getWidth();
//        mHeight = mImageBitmap.getHeight();
//
//        Canvas maskCanvas = new Canvas(mMaskBitmap);
//
//
////        画布先平移后旋转
//        maskCanvas.translate(mWidth / 2, 0);
//        maskCanvas.rotate(45);
//        int rectSize = (int) (mWidth / 2 / Math.sin(Math.toRadians(45)));
////        绘制圆角矩形
//        maskCanvas.drawRoundRect(0, 0, rectSize, rectSize, 50, 50, mPaint);
////        设置混合显示模式为SRC_IN
//
////        画布先旋转后平移，防止图片也跟着旋转
//        maskCanvas.rotate(-45);
//        maskCanvas.translate(-mWidth / 2, 0);
//        maskCanvas.drawBitmap(mImageBitmap, 0, 0, mPaint);
//
//        return mMaskBitmap;
//    }

}
