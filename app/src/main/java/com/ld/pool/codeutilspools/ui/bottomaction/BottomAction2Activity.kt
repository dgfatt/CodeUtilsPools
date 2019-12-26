package com.ld.pool.codeutilspools.ui.bottomaction

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.ld.pool.codeutilspools.R
import kotlinx.android.synthetic.main.activity_bottom_action2.*

class BottomAction2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_action2)
        initTitle()
        initView()
    }

    private fun initTitle() {
        supportActionBar?.title = "底部导航";
        // 显示返回按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        supportActionBar?.setDisplayShowHomeEnabled(false);

    }

    private fun initView() {
        // API28 BottomNavigationView 去除位移方式
        bottomnavigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED;
//        bottomnavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_home -> true
//
//
//            }
//            false
//        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish();
                false
            }  //返回键的id
            else ->
                super.onOptionsItemSelected(item)
        }

    }

}
