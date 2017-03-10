package com.study.lusb1.mysinablog.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.study.lusb1.mysinablog.R;

public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initDialog();
    }

    private void initDialog(){
        View v = View.inflate(this,R.layout.dialog_layout,null);
        Dialog dialog = new Dialog(this,R.style.auth_dialog_style);
        dialog.setContentView(v);
        dialog.show();
    }
}
