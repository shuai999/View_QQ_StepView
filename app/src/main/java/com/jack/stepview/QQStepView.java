package com.jack.stepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Email 2185134304@qq.com
 * Created by JackChen on 2018/1/20.
 * Version 1.0
 * Description: 仿QQ运动步数
 */

/**
 * 自定义View步骤：
 *      1.自定义属性
 *      2.在布局文件中引用 ，然后写命名空间
 *      3.写QQStepView继承View，重写构造方法及onMeasure()及onDraw()
 */

/**
 * 仿QQ运动步数效果思路分析
 *      1. 固定不动的蓝色大圆弧 颜色和宽度 [color、borderWidth]
 *      2. 可以变化的红色小圆弧 颜色和宽度 [color、borderWidth]
 *      3. 中间的步数文字 颜色和大小[color、textSize]
 */
public class QQStepView extends View {


    //设置默认外圆颜色
    private int mOuterColor = Color.RED ;
    //设置默认内圆颜色
    private int mInnerColor = Color.BLUE ;
    //设置默认圆弧宽度
    private int mBorderWidth = 20 ; //20sp
    //设置默认文字大小
    private int mStepTextSize ;
    //设置默认文字颜色
    private int mStepTextColor ;


    //设置3个画笔 外圆画笔、内圆画笔、文字画笔
    private Paint mOutPaint , mInnerPaint , mTextPaint ;

    //设置总共步数
    private int mStepMax = 0  ;
    //设置当前步数
    private int mCurrentStep = 0 ;


    public QQStepView(Context context) {
        this(context , null);
    }

    public QQStepView(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public QQStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //编写步骤
        // 1. 确定自定义属性，编写attrs.xml文件 [已写，详情请见attrs.xml文件]
        // 2. 在布局中使用，需要写命名空间   [已写，详情请见activity_main.xml布局文件]
        // 3. 在自定义View中获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        //以下为获取外圆颜色、内圆颜色、圆弧宽度、文字大小、文字颜色
        mOuterColor = typedArray.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnerColor = typedArray.getColor(R.styleable.QQStepView_innerColor, mInnerColor) ;
        mBorderWidth = (int) typedArray.getDimension(R.styleable.QQStepView_borderWidth , mBorderWidth);
        mStepTextSize = typedArray.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize) ;
        mStepTextColor = typedArray.getColor(R.styleable.QQStepView_stepTextColor, mStepTextColor) ;
        typedArray.recycle();


        //4. 初始化对应画笔
        //初始化外圆画笔
        mOutPaint = new Paint() ;
        mOutPaint.setAntiAlias(true); //设置抗锯齿，为了文字能清晰显示不至于很模糊
        mOutPaint.setStrokeWidth(mBorderWidth); //设置画笔宽度，单位px
        mOutPaint.setColor(mOuterColor); //设置外圆画笔颜色
        mOutPaint.setStrokeCap(Paint.Cap.ROUND) ; //设置画笔笔刷类型 帽子 圆弧两边的小盖子 把圆弧封闭住
        mOutPaint.setStyle(Paint.Style.STROKE); //设置画笔空心


        //初始化内圆画笔
        mInnerPaint = new Paint() ;
        mInnerPaint.setAntiAlias(true); //设置内圆抗锯齿，为了文字能清晰显示不至于很模糊
        mInnerPaint.setStrokeWidth(mBorderWidth); //设置画笔宽度 ，单位px
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND); //设置画笔笔刷类型
        mInnerPaint.setStyle(Paint.Style.STROKE); //设置画笔空心  FILL：是实心


        //初始化文字
        mTextPaint = new Paint() ;
        mTextPaint.setAntiAlias(true); //设置抗锯齿
        mTextPaint.setTextSize(mStepTextSize);
        mTextPaint.setColor(mStepTextColor);

        //5. 重写onMeasure() ,
        //6. 画外圆弧、内圆弧、文字
        //7. 其他
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        //获取宽高的模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec) ;
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec) ;

        //调用者可能会在布局文件中写wrap_content
        //此时需要在此获取宽高的模式 widthMode、heightMode,此处有2种做法：
         //1>：在这里判断：如果模式为widthMode或eightMode时，直接给用户抛出异常，告诉用户，不可以设置warp_content,宽高需要给固定的大小
         //2>：如果模式为widthMode或eightMode时，我们可以直接给它40个dp，这样也是可以的

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //宽、高不一致时，取最小值，确保它是一个正方形
        setMeasuredDimension(width>height?height:width , width>height?height:width);
    }



    //6. 画外圆弧、内圆弧、文字
    @Override
    protected void onDraw(Canvas canvas) {
        //6.1 画外圆弧
        //canvas.drawArc()：画圆弧,点击进去看源码如下，
        /*public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
            throw new RuntimeException("Stub!");
        }*/
        // 参数一表示：区域 --> 由此就需要你new RectF()对象 [RectF和Rect都表示区域，区别就是RectF是float类型更精确，Rect返回int类型]
        // 参数二表示：开始的角度
        // 参数三表示：扫过的角度
        // 参数四表示：你圆弧里边部分是否都是充满颜色 false表示你画圆弧时不需要给圆弧区域把颜色全部填充
        // 参数五表示：画笔

        /*public RectF(float left, float top, float right, float bottom) {
            throw new RuntimeException("Stub!");
        }*/
        /*由RectF源码可以：左、上、右、下*/
        RectF rectF = new RectF(mBorderWidth/2,mBorderWidth/2,getWidth()-mBorderWidth/2,getHeight()-mBorderWidth/2) ;

        canvas.drawArc(rectF , 135 , 270 , false , mOutPaint);

        if (mStepMax == 0) return;

        //6.2 画内圆弧 该怎么画 肯定不能写死 因为需要扫角度 使用百分比 让使用者设置的从外边传递
        float sweepAngle = (float)mCurrentStep/mStepMax ;
        canvas.drawArc(rectF , 135 , sweepAngle*270 , false , mInnerPaint);

        //6.3 画文字
        String stepText = mCurrentStep+"" ;
        //文字的区域
        Rect textBounds = new Rect() ;
        //参数一：要画的文字 参数二:从0开始 参数三：整个文字长度 参数四:
        mTextPaint.getTextBounds(stepText , 0 , stepText.length() , textBounds);
        //控件的一半 - 文字区域的一半
        int dx = getWidth()/2 - textBounds.width()/2 ;

        //基线baseLine
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt() ;
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 -fontMetrics.bottom ;
        int baseLine = getHeight()/2 + dy ;
        canvas.drawText(stepText , dx , baseLine , mTextPaint);

    }


    /**
     * 为了达到面向对象思想，我们必须在外边来操控它，
     * 即就是在MainActivity中让运动步数动起来 而不能在这个自定义View中让它动起来
     * @param stepMax
     */

    //7. 其他 写几个方法让它动起来  加synchronized防止多个线程操作它
    public synchronized void setMaxStep(int stepMax){
        this.mStepMax = stepMax ;
    }

    //加synchronized防止多个线程操作它
    public synchronized void setCurrentStep(int currentStep){
        this.mCurrentStep = currentStep ;
        //不断的重新绘制
        invalidate();
    }
}





