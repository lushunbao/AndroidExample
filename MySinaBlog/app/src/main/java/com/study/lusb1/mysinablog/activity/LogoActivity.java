package com.study.lusb1.mysinablog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.service.MainService;

public class LogoActivity extends BaseActivity implements IWeiboActivity {

    private ImageView img_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启主服务
        startService(new Intent(LogoActivity.this, MainService.class));

        setContentView(R.layout.logo_activity);
        init();
        startLaunchAnim();
    }

    public void startLaunchAnim(){
        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LogoActivity.this,LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_logo.startAnimation(animation);
    }

    @Override
    public void init() {
        img_logo = (ImageView)findViewById(R.id.img_logo);
    }

    @Override
    public void refresh(Object... params) {

    }
}
