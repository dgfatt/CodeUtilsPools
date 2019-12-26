package com.ld.pool.codeutilspools.ui.bottomaction

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ld.pool.codeutilspools.R

class BottomActionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_action)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    /**
     * 重写onSupportNavigateUp（）方法，目的是将back事件委托出去。
     * 若栈中有两个以上Fragment，点击back键就会返回到上一个Fragment。
     */
    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
//        val fragment: Fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
//        return fragment.let { NavHostFragment.findNavController(it).navigateUp() }
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}
