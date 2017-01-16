package com.study.lusb1.screenprop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView width_dp_content;
    TextView height_dp_content;
    TextView width_px_content;
    TextView height_px_content;
    TextView density_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        width_dp_content = (TextView)findViewById(R.id.width_dp_content);
        height_dp_content = (TextView)findViewById(R.id.height_dp_content);
        width_px_content = (TextView)findViewById(R.id.width_px_content);
        height_px_content = (TextView)findViewById(R.id.height_px_content);
        density_content = (TextView)findViewById(R.id.density_content);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_get_info:
                getScreenInfo();
                break;
        }
    }

    public void getScreenInfo(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int scale = metrics.densityDpi/160;
        density_content.setText(metrics.densityDpi+"dpi");
        width_px_content.setText(metrics.widthPixels+"px");
        height_px_content.setText(metrics.heightPixels+"px");
        width_dp_content.setText(metrics.widthPixels/scale+"dp");
        height_dp_content.setText(metrics.heightPixels/scale+"dp");
    }
}
