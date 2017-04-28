package com.gm.a80066158.indexbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private IndexBar indexBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initContent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Log.i(TAG, "<dispatchTouchEvent> " + ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            indexBar.setVisibility(View.VISIBLE);
        }

        return super.dispatchTouchEvent(ev);
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        indexBar = (IndexBar) findViewById(R.id.index_bar);
        indexBar.setOnTouchIndexChangeListener(new IOnTouchIndexChangeListener() {
            @Override
            public void ouTouchIndexChange(String index) {
                Log.i(TAG, "<ouTouchIndexChange> index = " + index);
            }
        });
    }
}
