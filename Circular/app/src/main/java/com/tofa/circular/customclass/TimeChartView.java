package com.tofa.circular.customclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.tofa.circular.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeChartView extends View {
    private Paint textPaint;
    private Paint centerTextPaint;
    private Paint progressPaint;
    private Paint progressBackgroundPaint;
    private Paint backgroundPaint;
    private Paint centerPaint;
    private Paint linePaint;
    private int mViewWidth;
    private int pieRadius;
    private Point pieCenterPoint;
    private RectF cirRect;
    private RectF lineRect;
    private Drawable bmpLeft;
    private Drawable bmpTop;
    private Drawable bmpRight;
    private Drawable bmpBotom;

    private ArrayList<TimeChartData> pieArrayList = new ArrayList<>();

    private final int TEXT_VALUE_COLOR = Color.parseColor("#364249");
    private final int BG_COLOR = Color.parseColor("#EEEEEE");
    private final int CENTER_COLOR = Color.parseColor("#FFFFFF");
    private final int BORDER_COLOR = Color.parseColor("#bbc8fc");
    private final int PROGRESS_COLOR = Color.parseColor("#3996F7");
    private final int SEPARATOR_COLOR = Color.parseColor("#364249");
    private final int TEXT_VALUE_SIZE = 12;
    private final int BORDER_SIZE = 10;
    private final int SEPARATOR_THIKNESS = 10;
    private final int SEPARATOR_LENGTH = 5;
    private final int CENTER_RADIUS = 20;
    private final int TEXT_VALUE_SPACE = 10;
    private final int ICON_SIZE = 25;
    private final boolean SHOW_VALUE = true;
    private final boolean SHOW_SEPARATOR = true;
    private final boolean SHOW_CENTERLABEL = true;
    private final int CENTER_LABEL_TEXT_COLOR = Color.parseColor("#364249");
    private final int CENTER_LABEL_TEXT_SIZE = 14;

    private int mBackgroundColor = BG_COLOR;
    private int mTextValueColor = TEXT_VALUE_COLOR;
    private int mCenterColor = CENTER_COLOR;
    private int mProgessColor = PROGRESS_COLOR;
    private int mBorderColor = BORDER_COLOR;
    private int mTextValueSize = TEXT_VALUE_SIZE;
    private int mBorderSize = BORDER_SIZE;
    private int mSeparatorThikness = SEPARATOR_THIKNESS;
    private int mSeparatorColor = SEPARATOR_COLOR;
    private int mSeparatorLength = SEPARATOR_LENGTH;
    private int mCenterRadius = CENTER_RADIUS;
    private int mTextValueSpace = TEXT_VALUE_SPACE;
    private int mIconLeft;
    private int mIconTop;
    private int mIconRight;
    private int mIconBotom;
    private int mIconSize = ICON_SIZE;
    private boolean mShowValue = SHOW_VALUE;
    private boolean mShowSeparator = SHOW_SEPARATOR;
    private boolean mShowCenterLable = SHOW_CENTERLABEL;
    private int mCenterLabelColor = CENTER_LABEL_TEXT_COLOR;
    private int mCenterLabelSize = CENTER_LABEL_TEXT_SIZE;
    private Rect textRec;
    private String mCenterLabelText;
    private long totalTime = 0;

    public TimeChartView(Context context) {
        this(context, null);
    }

    public TimeChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeChartView, defStyleAttr, 0);

        try {
            mBackgroundColor = attributes.getColor(R.styleable.TimeChartView_backgoundColor, BG_COLOR);
            mTextValueColor = attributes.getColor(R.styleable.TimeChartView_valueTextColor, TEXT_VALUE_COLOR);
            mCenterColor = attributes.getColor(R.styleable.TimeChartView_centerColor, CENTER_COLOR);
            mProgessColor = attributes.getColor(R.styleable.TimeChartView_progressColor, PROGRESS_COLOR);
            mBorderColor = attributes.getColor(R.styleable.TimeChartView_borderColor, BORDER_COLOR);
            mSeparatorColor = attributes.getColor(R.styleable.TimeChartView_separatorColor, SEPARATOR_COLOR);
            mTextValueSize = attributes.getInt(R.styleable.TimeChartView_valueTextSize, TEXT_VALUE_SIZE);
            mBorderSize = attributes.getInt(R.styleable.TimeChartView_borderSize, BORDER_SIZE);
            mSeparatorThikness = attributes.getInt(R.styleable.TimeChartView_separatorThickness, SEPARATOR_THIKNESS);
            mSeparatorLength = attributes.getInt(R.styleable.TimeChartView_separatorLength, SEPARATOR_LENGTH);
            mCenterRadius = attributes.getInt(R.styleable.TimeChartView_centerRadius, CENTER_RADIUS);
            mTextValueSpace = attributes.getInt(R.styleable.TimeChartView_valueTextSpace, TEXT_VALUE_SPACE);

            mIconLeft = attributes.getResourceId(R.styleable.TimeChartView_iconLeft, -1);
            mIconTop = attributes.getResourceId(R.styleable.TimeChartView_iconTop, -1);
            mIconRight = attributes.getResourceId(R.styleable.TimeChartView_iconRight, -1);
            mIconBotom = attributes.getResourceId(R.styleable.TimeChartView_iconBottom, -1);
            mIconSize = attributes.getInt(R.styleable.TimeChartView_centerIconSize, ICON_SIZE);
            mShowValue = attributes.getBoolean(R.styleable.TimeChartView_showValue, SHOW_VALUE);
            mShowSeparator = attributes.getBoolean(R.styleable.TimeChartView_showSeparator, SHOW_SEPARATOR);
            mShowCenterLable = attributes.getBoolean(R.styleable.TimeChartView_showCenterLabel, SHOW_CENTERLABEL);
            mCenterLabelColor = attributes.getColor(R.styleable.TimeChartView_centerLabelColor, CENTER_LABEL_TEXT_COLOR);
            mCenterLabelSize = attributes.getInt(R.styleable.TimeChartView_centerLabelSize, CENTER_LABEL_TEXT_SIZE);

        } finally {
            attributes.recycle();
        }

        mTextValueSize = DimensUtils.sp2px(context, mTextValueSize);
        mTextValueSpace = DimensUtils.sp2px(context, mTextValueSpace);
        mBorderSize = DimensUtils.dip2px(context, mBorderSize);
        mSeparatorLength = DimensUtils.dip2px(context, mSeparatorLength);
        mIconSize = DimensUtils.dip2px(context, mIconSize);
        mCenterRadius = DimensUtils.dip2px(context, mCenterRadius);
        mCenterLabelSize = DimensUtils.sp2px(context, mCenterLabelSize);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(mBackgroundColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextValueColor);
        textPaint.setTextSize(mTextValueSize);
        //textPaint.setTextAlign(Paint.Align.LEFT);

        centerTextPaint = new Paint();
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setColor(mCenterLabelColor);
        centerTextPaint.setTextSize(mCenterLabelSize);
        centerTextPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = new Paint.FontMetrics();
        textPaint.getFontMetrics(fm);

        progressPaint = new Paint();
        progressPaint.setColor(mProgessColor);
        progressPaint.setAntiAlias(true);

        progressBackgroundPaint = new Paint();
        progressBackgroundPaint.setColor(mBorderColor);
        progressBackgroundPaint.setAntiAlias(true);

        centerPaint = new Paint();
        centerPaint.setColor(mCenterColor);
        centerPaint.setAntiAlias(true);

        pieCenterPoint = new Point();
        cirRect = new RectF();
        lineRect = new RectF();

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(mTextValueColor);
        linePaint.setStrokeWidth(mSeparatorThikness);

        if (mIconLeft != -1) {
            bmpLeft = context.getResources().getDrawable(mIconLeft);
        }
        if (mIconTop != -1) {
            bmpTop = context.getResources().getDrawable(mIconTop);
        }
        if (mIconRight != -1) {
            bmpRight = context.getResources().getDrawable(mIconRight);
        }
        if (mIconBotom != -1) {
            bmpBotom = context.getResources().getDrawable(mIconBotom);
        }

        textRec = new Rect();
    }

    public void setDate(ArrayList<TimeChartData> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            this.pieArrayList = dataList;

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm");

            Date d1, d2;
            long diff;
            for(TimeChartData data : dataList){
                try {
                    d1 = format.parse(data.getStartValueText());
                    d2 = format.parse(data.getEndValueText());
                    diff = (d2 != null ? d2.getTime() : 0) - (d1 != null ? d1.getTime() : 0);
                    totalTime += diff;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Calendar sum = Calendar.getInstance();
            sum.set(Calendar.HOUR, 0);
            sum.set(Calendar.MINUTE, 0);
            sum.set(Calendar.SECOND, 0);
            sum.add(Calendar.MILLISECOND, (int) totalTime);
            mCenterLabelText = sum.get(Calendar.HOUR) + "h " + sum.get(Calendar.MINUTE) + " min";

        } else {
            pieArrayList.clear();
        }
    }

    public long getTotalTime() {
        return totalTime;
    }

    private void drawStartSeparator(Canvas canvas, TimeChartData data) {
        float outerCircle = pieRadius + mSeparatorLength + mBorderSize;
        float angle = (float) (data.getStart() * Math.PI / 180);
        float stopX = (float) (pieCenterPoint.x + outerCircle * Math.cos(angle));
        float stopY = (float) (pieCenterPoint.y + outerCircle * Math.sin(angle));

        if (mShowSeparator) {
            canvas.drawLine(pieCenterPoint.x, pieCenterPoint.y, stopX, stopY, linePaint);
        }

        if(mShowValue) {
            String startLabelText = data.getStartLabelText();
            float stopTextX = (float) (pieCenterPoint.x + (outerCircle + mTextValueSpace) * Math.cos(angle));
            float stopTextY = (float) (pieCenterPoint.y + (outerCircle + mTextValueSpace) * Math.sin(angle));
            if (stopX < stopTextX) {
                textPaint.setTextAlign(Paint.Align.LEFT);
            } else {
                textPaint.setTextAlign(Paint.Align.RIGHT);
            }

            canvas.drawText(startLabelText, stopTextX, stopTextY, textPaint);
            canvas.drawText(data.getStartValueText(), stopTextX, stopTextY + textPaint.descent() - textPaint.ascent(), textPaint);
        }
    }

    private void drawEndSeparator(Canvas canvas, TimeChartData data) {
        float outerCircle = pieRadius + mSeparatorLength + mBorderSize;
        float angle = (float) ((data.getStart() + data.getSweep()) * Math.PI / 180);
        float stopX = (float) (pieCenterPoint.x + outerCircle * Math.cos(angle));
        float stopY = (float) (pieCenterPoint.y + outerCircle * Math.sin(angle));

        if (mShowSeparator) {
            canvas.drawLine(pieCenterPoint.x, pieCenterPoint.y, stopX, stopY, linePaint);
        }

        if(mShowValue) {
            String endLabelText = data.getEndLabelText();
            float stopTextX = (float) (pieCenterPoint.x + (outerCircle + mTextValueSpace) * Math.cos(angle));
            float stopTextY = (float) (pieCenterPoint.y + (outerCircle + mTextValueSpace) * Math.sin(angle));
            if (stopX < stopTextX) {
                textPaint.setTextAlign(Paint.Align.LEFT);
            } else {
                textPaint.setTextAlign(Paint.Align.RIGHT);
            }

            canvas.drawText(endLabelText, stopTextX, stopTextY, textPaint);
            canvas.drawText(data.getEndValueText(), stopTextX, stopTextY + textPaint.descent() - textPaint.ascent(), textPaint);
        }
    }

    private void drawIcon(Canvas canvas){
        int outerCircle = pieRadius - ((pieRadius - mCenterRadius) / 2);
        if (bmpLeft != null){
            bmpLeft.setBounds(pieCenterPoint.x - outerCircle - (mIconSize / 2),
                    pieCenterPoint.y - (mIconSize / 2),
                    pieCenterPoint.x  - outerCircle + (mIconSize / 2),
                    pieCenterPoint.y + (mIconSize / 2));
            bmpLeft.draw(canvas);
        }
        if (bmpTop != null){
            bmpTop.setBounds(pieCenterPoint.x - (mIconSize / 2),
                    pieCenterPoint.y - outerCircle - (mIconSize / 2),
                    pieCenterPoint.x  + (mIconSize / 2),
                    pieCenterPoint.y - outerCircle + (mIconSize / 2));
            bmpTop.draw(canvas);
        }
        if (bmpRight != null){
            bmpRight.setBounds(pieCenterPoint.x + outerCircle - (mIconSize / 2),
                    pieCenterPoint.y - (mIconSize / 2),
                    pieCenterPoint.x + outerCircle + (mIconSize / 2),
                    pieCenterPoint.y + (mIconSize /2));
            bmpRight.draw(canvas);
        }
        if (bmpBotom != null){
            bmpBotom.setBounds(pieCenterPoint.x - (mIconSize / 2),
                    pieCenterPoint.y + outerCircle - (mIconSize / 2),
                    pieCenterPoint.x  + (mIconSize / 2),
                    pieCenterPoint.y + outerCircle + (mIconSize / 2));
            bmpBotom.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius + mBorderSize, progressBackgroundPaint);
        if (pieArrayList != null || pieArrayList.size() != 0) {
            for (TimeChartData data : pieArrayList) {
                canvas.drawArc(cirRect, data.getStart(), data.getSweep(), true, progressPaint);
                switch (data.getSeparatorType()) {
                    case BOTH:
                        drawStartSeparator(canvas, data);
                        drawEndSeparator(canvas, data);
                        break;
                    case START:
                        drawStartSeparator(canvas, data);
                        break;
                    case END:
                        drawEndSeparator(canvas, data);
                        break;
                }
            }
            canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, pieRadius, backgroundPaint);
            canvas.drawCircle(pieCenterPoint.x, pieCenterPoint.y, mCenterRadius, centerPaint);
            drawIcon(canvas);
            if (mShowCenterLable && mCenterLabelText != null){
                centerTextPaint.getTextBounds(mCenterLabelText, 0, 1, textRec);
                int center = textRec.height() / 2;
                canvas.drawText(mCenterLabelText, pieCenterPoint.x, pieCenterPoint.y + center, centerTextPaint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        int mViewHeight = measureHeight(heightMeasureSpec);
        if (mViewHeight < mViewWidth) {
            pieRadius = mViewHeight / 2 - mBorderSize * 2 - mSeparatorLength - mTextValueSpace - getPaddingStart() - getPaddingEnd();
        } else {
            pieRadius = mViewWidth / 2 - mBorderSize * 2 - mSeparatorLength - mTextValueSpace- getPaddingTop() - getPaddingBottom();
        }
        pieCenterPoint.set(mViewWidth / 2,
                mViewHeight / 2);
        cirRect.set(pieCenterPoint.x - pieRadius - mBorderSize,
                pieCenterPoint.y - pieRadius - mBorderSize,
                pieCenterPoint.x + pieRadius + mBorderSize,
                pieCenterPoint.y + pieRadius + mBorderSize);

        lineRect.set(pieCenterPoint.x - pieRadius - mBorderSize - mSeparatorLength,
                pieCenterPoint.y - pieRadius - mBorderSize - mSeparatorLength,
                pieCenterPoint.x + pieRadius + mBorderSize + mSeparatorLength,
                pieCenterPoint.y + pieRadius + mBorderSize + mSeparatorLength);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 3;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = mViewWidth;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int measurement = 0;

        switch (View.MeasureSpec.getMode(measureSpec)) {
            case View.MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case View.MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return measurement;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        backgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    public int getTextValueColor() {
        return mTextValueColor;
    }

    public void setTextValueColor(int mTextValueColor) {
        this.mTextValueColor = mTextValueColor;
        textPaint.setColor(mTextValueColor);
        invalidate();
    }

    public int getCenterColor() {
        return mCenterColor;
    }

    public void setCenterColor(int mCenterColor) {
        this.mCenterColor = mCenterColor;
        centerPaint.setColor(mCenterColor);
        invalidate();
    }

    public int getProgessColor() {
        return mProgessColor;
    }

    public void setProgessColor(int mProgessColor) {
        this.mProgessColor = mProgessColor;
        progressPaint.setColor(mProgessColor);
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        progressBackgroundPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getTextValueSize() {
        return mTextValueSize;
    }

    public void setTextValueSize(int mTextValueSize) {
        this.mTextValueSize = mTextValueSize;
        textPaint.setTextSize(DimensUtils.sp2px(this.getContext(), mTextValueSize));
        invalidate();
    }

    public int getBorderSize() {
        return mBorderSize;
    }

    public void setBorderSize(int mBorderSize) {
        this.mBorderSize = DimensUtils.sp2px(this.getContext(), mBorderSize);
        invalidate();
    }

    public int getSeparatorThikness() {
        return mSeparatorThikness;
    }

    public void setSeparatorThikness(int mSeparatorThikness) {
        this.mSeparatorThikness = mSeparatorThikness;
        linePaint.setStrokeWidth(mSeparatorThikness);
        invalidate();
    }

    public int getSeparatorColor() {
        return mSeparatorColor;
    }

    public void setSeparatorColor(int mSeparatorColor) {
        this.mSeparatorColor = mSeparatorColor;
        linePaint.setColor(mSeparatorColor);
        invalidate();
    }

    public int getSeparatorLength() {
        return mSeparatorLength;
    }

    public void setSeparatorLength(int mSeparatorLength) {
        this.mSeparatorLength = DimensUtils.sp2px(this.getContext(), mSeparatorLength);
        invalidate();
    }

    public int getCenterRadius() {
        return mCenterRadius;
    }

    public void setCenterRadius(int mCenterRadius) {
        this.mCenterRadius = DimensUtils.sp2px(this.getContext(), mCenterRadius);
        invalidate();
    }

    public int getTextValueSpace() {
        return mTextValueSpace;
    }

    public void setTextValueSpace(int mTextValueSpace) {
        this.mTextValueSpace = mTextValueSpace;
        invalidate();
    }

    public int getIconLeft() {
        return mIconLeft;
    }

    public void setIconLeft(int mIconLeft) {
        this.mIconLeft = mIconLeft;
        invalidate();
    }

    public int getIconTop() {
        return mIconTop;
    }

    public void setIconTop(int mIconTop) {
        this.mIconTop = mIconTop;
        invalidate();
    }

    public int getIconRight() {
        return mIconRight;
    }

    public void setIconRight(int mIconRight) {
        this.mIconRight = mIconRight;
        invalidate();
    }

    public int getIconBotom() {
        return mIconBotom;
    }

    public void setIconBotom(int mIconBotom) {
        this.mIconBotom = mIconBotom;
        invalidate();
    }

    public int getIconSize() {
        return mIconSize;
    }

    public void setIconSize(int mIconSize) {
        this.mIconSize = mIconSize;
        invalidate();
    }

    public boolean isShowValue() {
        return mShowValue;
    }

    public void setShowValue(boolean mShowValue) {
        this.mShowValue = mShowValue;
        invalidate();
    }

    public boolean isShowSeparator() {
        return mShowSeparator;
    }

    public void setShowSeparator(boolean mShowSeparator) {
        this.mShowSeparator = mShowSeparator;
        invalidate();
    }

    public boolean isShowCenterLable() {
        return mShowCenterLable;
    }

    public void setShowCenterLable(boolean mShowCenterLable) {
        this.mShowCenterLable = mShowCenterLable;
        invalidate();
    }

    public int gemCenterLabelColor() {
        return mCenterLabelColor;
    }

    public void setCenterLabelColor(int mCenterLabelColor) {
        this.mCenterLabelColor = mCenterLabelColor;
        invalidate();
    }

    public int getCenterLabelSize() {
        return mCenterLabelSize;
    }

    public void setCenterLabelSize(int mCenterLabelSize) {
        this.mCenterLabelSize = DimensUtils.sp2px(this.getContext(), mCenterLabelSize);
        invalidate();
    }
}
