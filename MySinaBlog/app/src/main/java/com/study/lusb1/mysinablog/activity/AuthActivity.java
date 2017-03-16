package com.study.lusb1.mysinablog.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.beans.Constants;

public class AuthActivity extends BaseActivity {
    private final String LOG_TAG = "AuthActivity";

    private Button btn_auth;
    private TextView authText;

    private AuthInfo authInfo;

    private Oauth2AccessToken mAccessToken;

    private SsoHandler ssoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        authText = (TextView)findViewById(R.id.access_info);
        authInfo = new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        ssoHandler = new SsoHandler(this,authInfo);
        initDialog();
    }

    private void initDialog(){
        View v = View.inflate(this,R.layout.dialog_layout,null);
        btn_auth = (Button)v.findViewById(R.id.btn_auth);
        btn_auth.setOnClickListener(new MyOnClickListener());
        Dialog dialog = new Dialog(AuthActivity.this,R.style.auth_dialog_style);
        dialog.setContentView(v);
        dialog.show();
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ssoHandler.authorize(new AuthListener());
        }
    }

    private class AuthListener implements WeiboAuthListener{

        @Override
        public void onComplete(final Bundle bundle) {
            Log.d(LOG_TAG,"onComplete");
            AuthActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    if(mAccessToken.isSessionValid()){
                        Toast.makeText(AuthActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
                        authText.setText(mAccessToken.getToken());
                    }
                }
            });
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }

        @Override
        public void onCancel() {
            Log.d(LOG_TAG,"onCancel");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }
}
