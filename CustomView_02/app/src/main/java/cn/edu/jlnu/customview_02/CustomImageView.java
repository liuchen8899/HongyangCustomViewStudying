package cn.edu.jlnu.customview_02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CustomImageView extends View {

    private  Rect mTextBound;
    private  Paint mPaint;
    private  Rect rect;
    private  int mTextSize;
    private  int mTextColor;
    private  String mTitle;
    private  int mImageScale;
    private  Bitmap mImage;
    private int mWidth;
    private int mHeight;
    private int IMAGE_SCALE_FITXY=0;

    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomImageView,defStyleAttr,0);
        int n= ta.getIndexCount();
        for(int i=0;i<n;i++){
            int attr= ta.getIndex(i);
            switch (attr){
                case R.styleable.CustomImageView_image:
                    mImage= BitmapFactory.decodeResource(getResources(),ta.getResourceId(attr,0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale=ta.getInt(attr,0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitle=ta.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTextColor=ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    mTextSize= (int) ta.getDimension(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,
                            getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        rect = new Rect();
        mPaint=new Paint();
        mTextBound=new Rect();
        mPaint.setTextSize(mTextSize);
        //计算了描绘字体需要额范围
        mPaint.getTextBounds(mTitle,0,mTitle.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置宽度
        int specMode=MeasureSpec.getMode(widthMeasureSpec); //获取mode
        int specSize=MeasureSpec.getSize(widthMeasureSpec); //获取size

        if(specMode==MeasureSpec.EXACTLY){ //设置具体的值以及设置match_parent
            mWidth=specSize;
        }else{
            //由图片决定的宽
            int desireByImg=getPaddingLeft()+getPaddingRight()+mImage.getWidth();
            //由字体决定的宽
            int desireByTitle=getPaddingLeft()+getPaddingRight()+mTextBound.width();

            if(specMode==MeasureSpec.AT_MOST){ //设置wrap_content
                int desire=Math.max(desireByImg,desireByTitle); //获取他们最大的宽度
                mWidth=Math.min(desire,specSize); //获取最小的宽度
            }
        }

        //设置高度
        specMode=MeasureSpec.getMode(heightMeasureSpec);
        specSize=MeasureSpec.getSize(heightMeasureSpec);
        if(specMode==MeasureSpec.EXACTLY){
            mHeight=specSize;
        }else{
            int desire=getPaddingTop()+getPaddingBottom()+mImage.getHeight()+mTextBound.height();
            if(specMode==MeasureSpec.AT_MOST){
                mHeight=Math.min(desire,specSize);
            }
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(4); //设置边框的颜色
        mPaint.setStyle(Paint.Style.STROKE); //设置画笔为空心[
        mPaint.setColor(Color.CYAN); //设置画笔的颜色
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint); //画矩形

        rect.left=getPaddingLeft();
        rect.right=mWidth-getPaddingRight();
        rect.top=getPaddingTop();
        rect.bottom=mHeight-getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        //当前设置的宽度小于字体需要的宽度，将字体改为XXX...
        if(mTextBound.width()>mWidth){
            TextPaint paint=new TextPaint(mPaint);
            String msg= TextUtils.ellipsize(mTitle,paint,(float) mWidth-getPaddingLeft()-getPaddingRight(),TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),mHeight-getPaddingBottom(),mPaint);
        }else{
            //正常情况，将字体居中
            canvas.drawText(mTitle,mWidth/2-mTextBound.width()*1.0f/2,mHeight-getPaddingBottom(),mPaint);
        }

        //取消使用掉的块
        rect.bottom-=mTextBound.height();

        if(mImageScale==IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }else{
            //计算居中的矩形范围
            rect.left=mWidth/2-mImage.getWidth()/2;
            rect.right=mWidth/2+mImage.getWidth()/2;
            rect.top=((mHeight-mTextBound.height()))/2-mImage.getHeight()/2;
            rect.bottom=(mHeight-mTextBound.height())/2+mImage.getHeight()/2;

            canvas.drawBitmap(mImage,null,rect,mPaint);
        }

    }
}
