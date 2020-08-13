package com.jy.woven.simple;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jy.woven.annotation.RunTimeTrace;

public class TestJavaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RunTimeTrace
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
