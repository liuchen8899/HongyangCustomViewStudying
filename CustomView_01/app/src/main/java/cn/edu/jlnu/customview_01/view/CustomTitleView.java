package cn.edu.jlnu.customview_01.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.edu.jlnu.customview_01.R;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CustomTitleView extends View {

    private String titleText;
    private int titleTextColor;
    private int titleTextSize;
    private Paint mPaint;
    private Rect textRect;

    /***
     * 一个参数的构造方法
     * @param context
     */
    public CustomTitleView(Context context) {
        this(context,null);
    }

    /***
     * 两个参数的构造方法
     * @param context
     * @param attrs
     */
    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    /**
     * 三个参数的构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(titleTextColor);
        canvas.drawText(titleText,getWidth()/2-textRect.width()/2,getHeight()/2+textRect.height()/2,mPaint);
    }
}
