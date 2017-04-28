package com.gm.a80066158.indexbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.health.TimerStat;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 80066158 on 2017-04-26.
 */

public class IndexBar extends View {
    private static final String TAG = "IndexBar";

    private static final int AUTO_HIDE_PERIOD = 5000;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    private int textColor = DEFAULT_TEXT_COLOR;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private String[] indexs = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Context context = null;
    private Paint paint = null;
    private IOnTouchIndexChangeListener onTouchIndexChangeListener = null;
    private int curTouchedIndex = -1;
    private float textSize = 0;
    private Timer autoHideTimer = null;

    public IndexBar(Context context) {
        super(context);
        paint = new Paint();
        this.context = context;
        autoHide();
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context = context;
        getAttrs(attrs, 0);
        autoHide();
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        this.context = context;
        getAttrs(attrs, defStyleAttr);
        autoHide();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        paint = new Paint();
    }

    public void setOnTouchIndexChangeListener(IOnTouchIndexChangeListener onTouchIndexChangeListener) {
        this.onTouchIndexChangeListener = onTouchIndexChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(backgroundColor);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        textSize = height / indexs.length;
        paint.reset();
        paint.setColor(textColor);
        paint.setTextSize(textSize * 0.75f);
        for (int i = 0; i < indexs.length; i++) {
            canvas.drawText(indexs[i],
                    (width - paint.measureText(indexs[i])) / 2,
                    textSize * (i + 1),
                    paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "<dispatchTouchEvent> " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                stopAutoHide();
                int index = (int) (event.getY() / textSize);
                if (index != curTouchedIndex && index >=0 && index < indexs.length) {
                    curTouchedIndex = index;
                    if (null != onTouchIndexChangeListener) {
                        onTouchIndexChangeListener.ouTouchIndexChange(indexs[curTouchedIndex]);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                autoHide();
                break;
            }
        }
        return true;
    }

    @Override
    public void setVisibility(int visibility) {
        Log.i(TAG, "<setVisibility> visibility = " + visibility);
        super.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            autoHide();
        }
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        Log.i(TAG, "<getAttrs> start");
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.IndexBarAttrs,
                defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndexBarAttrs_textColor: {
                    textColor = typedArray.getColor(attr, DEFAULT_TEXT_COLOR);
                    break;
                }
                case R.styleable.IndexBarAttrs_backgroundColor: {
                    backgroundColor = typedArray.getColor(attr, DEFAULT_BACKGROUND_COLOR);
                    break;
                }
            }
        }
        typedArray.recycle();
    }

    private void autoHide() {
        Log.i(TAG, "<autoHide> start");

        if (null != autoHideTimer) {
            autoHideTimer.cancel();
        }

        autoHideTimer = new Timer();
        autoHideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                IndexBar.this.post(new Runnable() {
                    @Override
                    public void run() {
                        IndexBar.this.setVisibility(View.GONE);
                    }
                });
                autoHideTimer.cancel();
                autoHideTimer = null;
            }
        }, AUTO_HIDE_PERIOD);
    }

    private void stopAutoHide() {
        Log.i(TAG, "<stopAutoHide> start");
        if (null != autoHideTimer) {
            autoHideTimer.cancel();
            autoHideTimer = null;
        }
    }
}
