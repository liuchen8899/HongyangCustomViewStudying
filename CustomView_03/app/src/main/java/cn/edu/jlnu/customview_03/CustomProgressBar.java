package cn.edu.jlnu.customview_03;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import static android.R.attr.id;
import static android.R.attr.track;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CustomProgressBar extends View{

    private int mSpeed;
    private float mCircleWidth;
    private int mSecondColor;
    private int mFirstColor;
    private Paint mPaint;
    private int  mProgress; //当前进度
    private boolean isNext=true;

    /***
     * 一个参数的构造方法
     * @param context
     */
    public CustomProgressBar(Context context) {
        this(context,null);
    }

    /***
     * 两个参数的构造方法
     * @param context
     * @param attrs
     */
    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    /***
     * 三个参数的构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomProgressBar,defStyleAttr,0);
        int n=ta.getIndexCount();
        for(int i=0;i<n;i++){
            int attr=ta.getIndex(i);
            switch (attr){
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor=ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor=ta.getColor(attr,Color.RED);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth=ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    mSpeed=ta.getInt(attr,20);
                    break;
            }
        }
        ta.recycle();
        init();
    }

    private void init() {
        mPaint=new Paint();
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    mProgress++;
                    if(mProgress==360) {
                        if(isNext){
                            isNext=true;
                        }else{
                            isNext=false;
                        }
                    }
                    postInvalidate();

                    try {
                        Thread.sleep(mSpeed);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center=getWidth()/2; //获取圆心的X坐标
        int radius= (int) (center-mCircleWidth/2); //半径
        mPaint.setStrokeWidth(mCircleWidth); //设置圆环的宽度
        Log.e("liuchen","center="+center+"---radius="+radius);
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        RectF oval=new RectF(center-radius,center-radius,center+radius,center+radius); //用于定于圆弧的形状和大小的界限
        if(!isNext){
            //第一颜色的圈完整，第二颜色跑
            mPaint.setColor(mFirstColor); //设置圆环的颜色
            canvas.drawCircle(center,center,radius,mPaint); //画出圆环
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval,-90,mProgress,false,mPaint);
        }else{
            mPaint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawCircle(center, center, radius, mPaint); // 画出圆环
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        }
    }
}
