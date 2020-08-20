package com.jy.woven.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jy.woven.YWoven
import com.jy.woven.api.PointcutRoute

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        YWoven.init()
        YLogUtils.i("fuck", PointcutRoute.wovenInfoMap.size)
    }


}
