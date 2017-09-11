package com.ncslab.pyojihye.interpret.ECT;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.ncslab.pyojihye.interpret.ECT.Const.LineNum;
import static com.ncslab.pyojihye.interpret.ECT.Const.Max;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ModeTextView extends TextView {
    private static String TAG = "ModeTextView";
    private int availableWidth = 0;
    public Paint paint;
    private List<String> cutText = new ArrayList<String>();

    public ModeTextView(Context context) {
        super(context);
    }

    public ModeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int setTextInfo(String text, int textWidth, int textHeight) {
//        Log.d(TAG, "setTextInfo");
        paint = getPaint();
        paint.setColor(getTextColors().getDefaultColor());
        paint.setTextSize(getTextSize());

        int textLineHeight = textHeight;

        if (textWidth > 0) {
            availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            Max = 0;
            for (int i = 0; i < paint.breakText(text, true, availableWidth, null); i++) {
                if (Character.getType(text.toCharArray()[i]) == 5) {
                    Max += 2;
                } else {
                    Max++;
                }
            }
            LineNum = textHeight / getLineHeight();

            cutText.clear();
            int end = 0;
            boolean exit = false;
            do {
                end = paint.breakText(text, true, availableWidth, null);
                if (end > 0 && !text.substring(0, end).contains("\n")) {
                    cutText.add(text.substring(0, end));
                    text = text.substring(end);
                    if (textHeight == 0) textLineHeight += getLineHeight();
                } else if (text.contains("\n")) {
                    exit = false;
                    end = text.indexOf("\n") + 1;
                    cutText.add(text.substring(0, end));
                    text = text.substring(end);
                    if (textHeight == 0) textLineHeight += getLineHeight();
                } else if (text.length() == 0) {
                    end = 0;
                    exit = true;
                }
            } while (end > 0 && !exit);
        }
        textLineHeight += getPaddingTop() + getPaddingBottom();
        return textLineHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw");
        float height = getPaddingTop() + getLineHeight();
        for (String text : cutText) {
            canvas.drawText(text, getPaddingLeft(), height, paint);
            height += getLineHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int height = setTextInfo(this.getText().toString(), parentWidth, parentHeight);
        if (parentHeight == 0)
            parentHeight = height;
        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
//        Log.d(TAG, "onTextChanged");
        setTextInfo(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
//        Log.d(TAG, "onSizeChanged");
        if (w != oldW) {
            setTextInfo(this.getText().toString(), w, h);
        }
    }
}