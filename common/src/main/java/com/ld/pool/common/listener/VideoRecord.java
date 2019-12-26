package com.ld.pool.common.listener;

import android.hardware.Camera;
import android.view.SurfaceHolder;

public interface VideoRecord {
    /**
     * 返回相机对象
     */
    Camera getCamera();

    /**
     * 返回SurfaceHolder对象
     */
    SurfaceHolder getSurfaceHolder();
}
