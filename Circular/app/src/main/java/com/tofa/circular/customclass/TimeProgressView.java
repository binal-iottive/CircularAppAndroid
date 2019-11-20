package com.tofa.circular.customclass;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.tofa.circular.R;

import java.util.List;

public class TimeProgressView extends View {

    private List<Segment> segments;
    private Integer valueSegment;

    private Rect rectBounds;
    private RectF roundRectangleBounds;
    private Paint fillPaint;
    private Paint startEndTextPaint;
    private Paint linePaint;

    private int emptySegmentColor;
    private int barHeight;
    private int startEndBoxHeight;

    private String startText;

    private String endText;

    private int barRoundingRadius = 0;

    private boolean showStartEndLabel;

    private int startEndTextSize;

    private int startEndTextColor;

    private Path trianglePath;
    private Rect segmentRect;

    private int totalValue;

    public TimeProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public TimeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimeProgressView,
                0, 0);

        try {
            Resources resources = getResources();
            startEndTextSize = a.getDimensionPixelSize(R.styleable.TimeProgressView_startEndTextSize,
                    resources.getDimensionPixelSize(R.dimen.description_text_size));
            barHeight = a.getDimensionPixelSize(R.styleable.TimeProgressView_barHeight,
                    resources.getDimensionPixelSize(R.dimen.bar_height));
            startEndBoxHeight = a.getDimensionPixelSize(R.styleable.TimeProgressView_startEndBoxHeight,
                    resources.getDimensionPixelSize(R.dimen.description_box_height));

            showStartEndLabel = a.getBoolean(R.styleable.TimeProgressView_showStartEndLabel, true);
            startText = a.getString(R.styleable.TimeProgressView_startText);
            endText = a.getString(R.styleable.TimeProgressView_endText);
            startEndTextColor = a.getColor(R.styleable.TimeProgressView_startEndTextColor, Color.BLACK);

            emptySegmentColor = a.getColor(R.styleable.TimeProgressView_emptySegmentBackground, context.getResources().getColor(R.color.colorAlphaBlue));
        } finally {
            a.recycle();
        }


        startEndTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        startEndTextPaint.setColor(Color.DKGRAY);
        startEndTextPaint.setStyle(Paint.Style.FILL);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);

        rectBounds = new Rect();
        roundRectangleBounds = new RectF();
        segmentRect = new Rect();

        trianglePath = new Path();
        trianglePath.setFillType(Path.FillType.EVEN_ODD);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawEmptySegment(canvas);
        int segmentsSize = segments == null ? 0 : segments.size();
        if (segmentsSize > 0) {
            for (int segmentIndex = 0; segmentIndex < segmentsSize; segmentIndex++) {
                Segment segment = segments.get(segmentIndex);
                drawSegment(canvas, segment, segmentIndex, segmentsSize);
            }
        }

        if (showStartEndLabel) {
            rectBounds.set(getPaddingLeft(), getPaddingTop(), getContentWidth() - getPaddingRight(), barHeight + getPaddingTop());
            segmentRect.set(rectBounds);
            startEndTextPaint.setTextSize(startEndTextSize);
            startEndTextPaint.setColor(startEndTextColor);

            float textHeight = startEndTextPaint.descent() - startEndTextPaint.ascent();
            float textOffset = (textHeight / 2) - startEndTextPaint.descent();

            if(startText != null) {
                startEndTextPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(startText, segmentRect.left + 40, segmentRect.top + startEndBoxHeight + textOffset, startEndTextPaint);
            }

            if(endText != null) {
                startEndTextPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(endText, segmentRect.right - 40, segmentRect.top + startEndBoxHeight + textOffset, startEndTextPaint);
            }

            canvas.drawLine(segmentRect.left + 14, segmentRect.top + segmentRect.bottom + 5, segmentRect.left + 14, segmentRect.bottom + startEndBoxHeight - 8, linePaint);
            canvas.drawCircle(segmentRect.left + 14, segmentRect.bottom + startEndBoxHeight - 14, 8, linePaint);

            canvas.drawLine(segmentRect.right - 14, segmentRect.top + segmentRect.bottom  + 5, segmentRect.right - 14, segmentRect.bottom + startEndBoxHeight - 8, linePaint);
            canvas.drawCircle(segmentRect.right - 14, segmentRect.bottom + startEndBoxHeight - 14, 8, linePaint);
        }
    }

    private void drawEmptySegment(Canvas canvas) {
        int segmentsSize = 1;

        int singleSegmentWidth = getContentWidth() / segmentsSize;
        rectBounds.set(getPaddingLeft(), getPaddingTop(), singleSegmentWidth + getPaddingLeft(), barHeight + getPaddingTop());

        fillPaint.setColor(emptySegmentColor);

        barRoundingRadius = rectBounds.height() / 2;

        segmentRect.set(rectBounds);

        roundRectangleBounds.set(rectBounds.left, rectBounds.top, rectBounds.right, rectBounds.bottom);
        canvas.drawRoundRect(roundRectangleBounds, barRoundingRadius, barRoundingRadius, fillPaint);
    }

    private int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getContentHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private void drawSegment(Canvas canvas, Segment segment, int segmentIndex, int segmentsSize) {
        boolean isLeftSegment = segmentIndex == 0;
        boolean isRightSegment = segmentIndex == segmentsSize - 1;
        boolean isLeftAndRight = isLeftSegment && isRightSegment;

        float orgSegmentWidth = (segment.getMaxValue() - segment.getMinValue()) / totalValue * 100;
        float orgSegmentLeft = segment.getMinValue() / totalValue * 100;

        int singleSegmentWidth = (int) (orgSegmentWidth / 100 * (getContentWidth()));
        int segmentLeft = (int) (orgSegmentLeft / 100 * (getContentWidth()));
        int segmentRight = segmentLeft + singleSegmentWidth;

        rectBounds.set(segmentLeft + getPaddingLeft(), getPaddingTop(), segmentRight + getPaddingLeft(), barHeight + getPaddingTop());

        fillPaint.setColor(segment.getColor());

        segmentRect.set(rectBounds);

        if (isLeftSegment || isRightSegment) {
            barRoundingRadius = rectBounds.height() / 2;

            roundRectangleBounds.set(rectBounds.left, rectBounds.top, rectBounds.right, rectBounds.bottom);
            canvas.drawRoundRect(roundRectangleBounds, barRoundingRadius, barRoundingRadius, fillPaint);
            if (!isLeftAndRight) {
                if (isLeftSegment) {
                    rectBounds.set(segmentLeft + barRoundingRadius + getPaddingLeft(), getPaddingTop(), segmentRight + getPaddingLeft(), barHeight + getPaddingTop());
                    canvas.drawRect(
                            rectBounds,
                            fillPaint
                    );
                } else {
                    rectBounds.set(segmentLeft + getPaddingLeft(), getPaddingTop(), segmentRight - barRoundingRadius + getPaddingLeft(), barHeight + getPaddingTop());
                    canvas.drawRect(
                            rectBounds,
                            fillPaint
                    );
                }
            }
        } else {
            canvas.drawRect(
                    rectBounds,
                    fillPaint
            );
        }
    }

    private void drawTriangle(Canvas canvas, Point point1, Point point2, Point point3, Paint paint) {
        trianglePath.reset();
        trianglePath.moveTo(point1.x, point1.y);
        trianglePath.lineTo(point2.x, point2.y);
        trianglePath.lineTo(point3.x, point3.y);
        trianglePath.lineTo(point1.x, point1.y);
        trianglePath.close();

        canvas.drawPath(trianglePath, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = getPaddingLeft() + getPaddingRight();
        int minHeight = barHeight + getPaddingBottom() + getPaddingTop();
        if (showStartEndLabel) {
            minHeight += startEndBoxHeight;
        }
        int w = resolveSizeAndState(minWidth, widthMeasureSpec, 0);
        int h = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    public void drawTextCentredInRect(Canvas canvas, Paint paint, String text, Rect outsideRect) {
        drawTextCentredInRectWithSides(canvas, paint, text, outsideRect.left, outsideRect.top, outsideRect.right, outsideRect.bottom);
    }

    public void drawTextCentredInRectWithSides(Canvas canvas, Paint paint, String text, float left, float top, float right, float bottom) {
        paint.setTextAlign(Paint.Align.CENTER);

        float textHeight = paint.descent() - paint.ascent();
        float textOffset = (textHeight / 2) - paint.descent();

        canvas.drawText(text, (left + right) / 2, (top + bottom) / 2 + textOffset, paint);
    }


    public void setSegments(List<Segment> segments) {
        this.segments = segments;
        int value = 0;
        for(int i = 0; i < segments.size(); i++) {
            if (i > 0) {
                value += segments.get(i).getMinValue() - segments.get(i - 1).getMaxValue();
            }
            value += segments.get(i).getMaxValue() - segments.get(i).getMinValue();
        }
        totalValue = value;
        invalidate();
        requestLayout();
    }

    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
        invalidate();
        requestLayout();
    }

    public void setShowStartEndLabel(boolean showStartEndLabel) {
        this.showStartEndLabel = showStartEndLabel;
        invalidate();
        requestLayout();
    }

    public void setEmptySegmentColor(int emptySegmentColor) {
        this.emptySegmentColor = emptySegmentColor;
        invalidate();
        requestLayout();
    }

    public void setStartEndTextSize(int startEndTextSize) {
        this.startEndTextSize = startEndTextSize;
        invalidate();
        requestLayout();
    }

    public void setStartEndTextColor(int startEndTextColor) {
        this.startEndTextColor = startEndTextColor;
        invalidate();
        requestLayout();
    }

    public void setStartEndBoxHeight(int startEndBoxHeight) {
        this.startEndBoxHeight = startEndBoxHeight;
        invalidate();
        requestLayout();
    }


    public Integer getValueSegment() {
        return valueSegment;
    }

    public void setValueSegment(Integer valueSegment) {
        this.valueSegment = valueSegment;
    }

    public String getStartText() {
        return startText;
    }

    public void setStartText(String startText) {
        this.startText = startText;
        requestLayout();
    }

    public String getEndText() {
        return endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
        requestLayout();
    }
}
