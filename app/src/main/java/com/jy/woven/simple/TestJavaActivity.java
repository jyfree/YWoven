package com.jy.woven.simple;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jy.woven.YWoven;
import com.jy.woven.api.PointcutRoute;


public class TestJavaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YWoven.init();
        YLogUtils.INSTANCE.i("fuck", PointcutRoute.wovenInfoMap.size());
    }

    private String test() {

        String msg = "123";
        return msg;
    }

    private String test2() {

        String msg = "456";
        return msg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
