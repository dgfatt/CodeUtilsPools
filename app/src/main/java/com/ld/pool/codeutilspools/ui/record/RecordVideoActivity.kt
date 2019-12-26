package com.ld.pool.codeutilspools.ui.record

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ld.pool.codeutilspools.R
import com.ld.pool.common.utils.VideoRecordManager
import kotlinx.android.synthetic.main.activity_record_video.*

class RecordVideoActivity : AppCompatActivity() {

    private var flagPlay:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_video)
        val videoRecordManager = VideoRecordManager(camera)
        bt.setOnClickListener {
            if(flagPlay){
                videoRecordManager.stop()
                bt.text = "开始"
                Toast.makeText(this@RecordVideoActivity,"结束",Toast.LENGTH_SHORT).show()
            }else{
                videoRecordManager.start()
                bt.text = "结束"
                Toast.makeText(this@RecordVideoActivity,"开始",Toast.LENGTH_SHORT).show()
            }
            flagPlay = !flagPlay
        }
        bt_play.setOnClickListener {
            startActivity(Intent(this@RecordVideoActivity, PlayerRecordActivity::class.java))
        }
    }

}
