package cn.edu.jlnu.customview_01.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import cn.edu.jlnu.customview_01.R;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CustomTitleView1 extends View {

    private String titleText;
    private int titleTextColor;
    private int titleTextSize;
    private Paint mPaint;
    private Rect textRect;


    /***
     * 一个参数的构造方法
     * @param context
     */
    public CustomTitleView1(Context context) {
        this(context,null);
    }

    /***
     * 两个参数的构造方法
     * @param context
     * @param attrs
     */
    public CustomTitleView1(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    /**
     * 三个参数的构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTitleView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView,defStyleAttr,0);
        titleText=ta.getString(R.styleable.CustomTitleView_titleText);
        titleTextColor=ta.getColor(R.styleable.CustomTitleView_titleTextColor, Color.BLACK);
        titleTextSize= (int) ta.getDimension(R.styleable.CustomTitleView_titleTextSize,12);
        ta.recycle();
        init();
    }

    /***
     * 初始化
     */
    private void init() {
        mPaint=new Paint();
        mPaint.setTextSize(titleTextSize);
        textRect=new Rect();
        mPaint.getTextBounds(titleText,0,titleText.length(),textRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec); //获取宽度的mode
        int widthSize=MeasureSpec.getSize(widthMeasureSpec); //获取宽度的大小
        int heightMode=MeasureSpec.getMode(heightMeasureSpec); //获取高度的mode
        int heightSize=MeasureSpec.getSize(heightMeasureSpec); //获取高度的大小
        int width;
        int height;
        if(widthMode==MeasureSpec.EXACTLY){
            //Match_parent 以及制定了View大小常用这个属性
            width=widthSize;
        }else{
            mPaint.setTextSize(titleTextSize); //设置字体大小
            mPaint.getTextBounds(titleText,0,titleText.length(),textRect);
            float textHeight=textRect.width(); //获取textReact的宽度
            int desired= (int) (getPaddingLeft()+textHeight+getPaddingRight());
            width=desired;
        }

        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else {
            mPaint.setTextSize(titleTextSize);
            mPaint.getTextBounds(titleText,0,titleText.length(),textRect);
            float textHeight=textRect.height();
            int desired= (int) (getPaddingTop()+textHeight+getPaddingTop());
            height=desired;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(titleTextColor);
        canvas.drawText(titleText,getWidth()/2-textRect.width()/2,getHeight()/2+textRect.height()/2,mPaint);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
