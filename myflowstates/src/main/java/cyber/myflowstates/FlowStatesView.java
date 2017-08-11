package cyber.myflowstates;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import static android.text.TextUtils.isEmpty;

/**
 * Created by MS on 2017/8/10.
 */

public class FlowStatesView extends View {

    private Paint mPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private int  DEFAULT_COLOR = Color.parseColor("#BCBCBC");
    private int  DEFAULT_CHECK_COLOR = Color.parseColor("#46FFA3");
    private int  mCheckColor = DEFAULT_CHECK_COLOR;

    private int  DEFAULT_TEXT_SIZE = 14;
    private int  mTextSize;

    private String  mShowTexts;//  xxxxx,xxxxx,xxxxx,xxxx;

    private int   mCircl_Num; //显示几段圆点
    private String [] strs;

    private float mCirleRadius; //圆点半径
    private float mLineWidth;   //线长
    private float blockBord;

    private Rect  mBound;

    private int mWidth;
    private int mHeigh;

    private int selected = 0;

    private String mStartInfo;
    private String mEndInfo;

    public FlowStatesView(Context context) throws Exception{
        this(context, null);
    }

    public FlowStatesView(Context context, AttributeSet attrs) throws Exception {
        this(context, attrs, 0);
    }

    public FlowStatesView(Context context, AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);
        init(context, attrs,defStyleAttr);
}

    private void init(Context context, AttributeSet attrs ,int defStyleAttr) throws Exception {

        mTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics());

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowStatesView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            if (attr == R.styleable.FlowStatesView_ShowTexts) {
                mShowTexts = a.getString(attr);

            } else if (attr == R.styleable.FlowStatesView_StartInfo) {
                mStartInfo = a.getString(attr);

            } else if (attr == R.styleable.FlowStatesView_EndInfo) {
                mEndInfo = a.getString(attr);

            } else if (attr == R.styleable.FlowStatesView_TextColor) {
                mCheckColor = a.getColor(attr, DEFAULT_CHECK_COLOR);

            } else if (attr == R.styleable.FlowStatesView_TextSize) {// 默认设置为16sp，TypeValue把sp转化为px
                mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics()));
            }
        }
        a.recycle();

        mPaint  = new Paint();
        mPaint.setAntiAlias(true);

        mLinePaint  = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(4.0f);

        mTextPaint  = new Paint();
        mTextPaint.setAntiAlias(true);

        if(isEmpty(mShowTexts)){throw new Exception(" 状态数据不能为空，请确认！");}

        if(mShowTexts.indexOf(",")!=-1){
            strs  = mShowTexts.split(",");
            mCircl_Num = strs.length;
        }else {
            throw new Exception(" 状态数据个数必须大于1，请确认！");
        }

        float density = context.getResources().getDisplayMetrics().density;
        mCirleRadius =  5  * density;
        blockBord    =  2  * mCirleRadius;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth  = getMeasuredWidth();
        mHeigh  = getMeasuredHeight();
        mLineWidth = (mWidth - getPaddingRight() - getPaddingLeft())/(mCircl_Num);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0;i< mCircl_Num;i++){
            float cx = mLineWidth/2  + (i * 1)*(mLineWidth);  //圆心坐标
            mPaint.setColor(DEFAULT_COLOR);
            mLinePaint.setColor(DEFAULT_COLOR);
            mTextPaint.setColor(DEFAULT_COLOR);
            mTextPaint.setTextSize(mTextSize);
            if(i == selected - 1){
                mPaint.setColor(mCheckColor);
                mTextPaint.setColor(Color.WHITE);
            }
            canvas.drawCircle(cx ,mHeigh/2,mCirleRadius,mPaint);
            if(i < (mCircl_Num -1)){
                canvas.drawLine(cx + blockBord,mHeigh/2 ,
                                cx + mLineWidth - blockBord ,mHeigh/2
                        ,mLinePaint);
            }

            //画圆角矩形 + 倒三角 ++Text
            drawTextAndRoundRect(canvas,cx ,mPaint,i);
            //画 开始 结束信息
            drawStartEndInfo(canvas,cx ,mTextPaint,i);
        }
    }

    private void drawStartEndInfo(Canvas canvas, float cx, Paint p, int i) {
        p.setColor(Color.BLACK);
        String s;
        if(i == 0 && !isEmpty(mStartInfo)){
            s = mStartInfo;
        }else if(i == mCircl_Num -1 && !isEmpty(mEndInfo)){
            s = mEndInfo;
        }else{
            return;
        }

        mBound = new Rect();
        mTextPaint.getTextBounds(s, 0, s.length(), mBound);
        canvas.drawText(s,cx - mBound.width()/2,mHeigh/2 + blockBord * 3,p);
    }

    private void drawTextAndRoundRect(Canvas canvas, float cx ,Paint p,int i) {
        //测量文字宽度高度
        mBound = new Rect();
        String info = strs[i];
        mTextPaint.getTextBounds(info, 0, info.length(), mBound);

        if (i == selected -1 ) {
            p.setColor(mCheckColor);
            p.setStyle(Paint.Style.FILL);//充满
            RectF oval3 = new RectF(cx - mBound.width()/2 -  (float)(1.5 * blockBord), mHeigh/2 - 2*(mBound.height() + blockBord ),cx - mBound.width()/2 +  mBound.width() +  (float)(1.5 * blockBord),mHeigh/2 - (mBound.height() + blockBord ) );// 设置个新的长方形
            canvas.drawRoundRect(oval3, 20, 20, p);//第二个参数是x半径，第三个参数是y半径
            canvas.drawText(info,cx - mBound.width()/2,mHeigh/2 - blockBord * 3,mTextPaint);
            drawTriangle(canvas,p,cx,(mHeigh/2 - (mBound.height() + blockBord) ) );
        }else {
            canvas.drawText(info,cx - mBound.width()/2,mHeigh/2 - blockBord * 2,mTextPaint);
        }
    }

    private void drawTriangle(Canvas canvas, Paint p, float cx,float lb) {
        Path path = new Path();
        path.moveTo(cx, lb  +  (float)(blockBord * 0.6));// 此点为多边形的起点
        path.lineTo(cx - (float)(blockBord * 0.6), lb);
        path.lineTo(cx + (float)(blockBord * 0.6), lb);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setSycnSelected(int selected) {
        this.selected = selected;
        invalidate();
    }
}
