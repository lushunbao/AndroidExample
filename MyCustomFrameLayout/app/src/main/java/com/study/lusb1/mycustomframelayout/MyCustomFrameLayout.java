package com.study.lusb1.mycustomframelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lusb1 on 2017/2/13.
 */

public class MyCustomFrameLayout extends FrameLayout {
    TextView testView;
    public MyCustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyCustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomFrameLayout(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        testView = (TextView)findViewById(R.id.test);
    }
}
