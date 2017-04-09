package cn.edu.jlnu.customview_04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CustomVolumControlBar extends View {

    private  Rect mRect;
    private  Paint mPaint;
    private  int mSplitSize;
    private  int mCount;
    private  float mCircleWidth;
    private  Bitmap mImage;
    private  int mSecondColor;
    private  int mFirstColor;
    private int currentCount=3;
    private int xDowm;
    private int xUp;

    /***
     * 一个参数的构造方法
     * @param context
     */
    public CustomVolumControlBar(Context context) {
        this(context,null);
    }

    /**
     * 两个参数的构造方法
     * @param context
     * @param attrs
     */
    public CustomVolumControlBar(Context context, AttributeSet attrs) {
       this(context,attrs,0);
    }

    /***
     * 三个参数的构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomVolumControlBar,defStyleAttr,0);
        int n=ta.getIndexCount();
        for (int i=0;i<n;i++){
            int attr= ta.getIndex(i);
            switch (attr){
                case R.styleable.CustomVolumControlBar_firstColor:
                    mFirstColor=ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomVolumControlBar_secondColor:
                    mSecondColor=ta.getColor(attr,Color.CYAN);
                    break;
                case R.styleable.CustomVolumControlBar_bg:
                    mImage= BitmapFactory.decodeResource(getResources(),ta.getResourceId(attr,0));
                    break;
                case R.styleable.CustomVolumControlBar_circleWidth:
                    mCircleWidth= ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_dotCount:
                    mCount=ta.getInt(attr,20);
                    break;
                case R.styleable.CustomVolumControlBar_splitSize:
                    mSplitSize=ta.getInt(attr,20);
                    break;
            }
        }
        ta.recycle();
        mPaint=new Paint();
        mRect=new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); //设置抗锯齿
        mPaint.setStrokeWidth(mCircleWidth); //设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); //定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        int center=getWidth()/2; //获取圆心的X坐标
        int radius= (int) (center-mCircleWidth/2); //半径

        //画块块去
        drawOval(canvas,center,radius);

        //计算内切正方形的位置
        int neiRadius= (int) (radius-mCircleWidth/2); //获取内圆的半径

        //内切正方形的距离顶部nerRadius*(1-根号2/2)
        mRect.left= (int) (neiRadius*(1-Math.sqrt(2)*1.0f/2)+mCircleWidth); //左
        mRect.top= (int) (neiRadius*(1-Math.sqrt(2)*1.0f/2)+mCircleWidth); //上
        mRect.bottom= (int) (mRect.top+((Math.sqrt(2))*neiRadius)); //下
        mRect.right= (int) (mRect.left+((Math.sqrt(2))*neiRadius)); //右

        //如果图片比较小，那么则根据图片的尺寸放置到正中心
        if(mImage.getWidth()<(Math.sqrt(2))*radius){
            mRect.left= (int) (mRect.left+(Math.sqrt(2)*neiRadius*1.0f/2-mImage.getWidth()*1.0f/2));
            mRect.top= (int) (mRect.top+(Math.sqrt(2)*neiRadius*1.0f/2-mImage.getHeight()*1.0f/2));
            mRect.right=mRect.left+mImage.getWidth();
            mRect.bottom=mRect.top+mImage.getHeight();
        }
        //绘图
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }

    /***
     *
     * @param canvas 画布
     * @param center 圆点X
     * @param radius 半径
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        float itemSize=(360*1.0f -mCount*mSplitSize)/mCount; //获取每一个item所占的size
        RectF oval=new RectF(center-radius,center-radius,center+radius,center+radius); //用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mFirstColor); //设置圆环的颜色

        for(int i=0;i<mCount;i++){
            //画的确实是圆弧，因为strokeWidth导致画出的圆弧的宽度很粗
            canvas.drawArc(oval,i*(itemSize+mSplitSize),itemSize,false,mPaint); //根据进度画圆弧
        }

        mPaint.setColor(mSecondColor);
        for(int i=0;i<currentCount;i++){

            canvas.drawArc(oval,i*(itemSize+mSplitSize),itemSize,false,mPaint); //根据进度画圆弧
            //圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDowm=(int)event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp=(int)event.getY();
                if(xUp>xDowm){ //下滑
                    down();
                }else{
                    up(); //上滑
                }
                break;
        }
        return true;
    }

    /***
     * 音量+
     */
    private void up(){
        currentCount++;
        postInvalidate();
    }

    /****
     * 音量-
     */
    private void down(){
        currentCount--;
        postInvalidate();
    }
}
