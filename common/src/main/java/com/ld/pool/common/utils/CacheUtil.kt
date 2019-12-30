package com.ld.pool.common.utils

import android.util.LruCache
import android.util.SparseArray
import com.ld.pool.common.listener.CacheInterface
import java.lang.ref.SoftReference


/**
 * 缓存工具
 */
class CacheUtil<T> : CacheInterface<T> {
    /**
     * 是否缓存
     */
    private var mUseCache = true
    /**
     * lru花痴女
     */
    private var mLruCache: LruCache<Int?, T>? = null
    // TODO: gavin 2018/7/29  mLruCache移除后，使用软引用进行二级缓存
    /**
     * 二级缓存
     * 使用软引用缓存数据
     */
    private val mSoftCache: SparseArray<SoftReference<T>>? = null

    /**
     * 是否使用用缓存
     *
     * @param b
     */
    fun isCacheable(b: Boolean) {
        mUseCache = b
    }

    private fun initLruCache() {
        mLruCache = object : LruCache<Int?, T>(2 * 1024 * 1024) {
            override fun entryRemoved(evicted: Boolean, key: Int?, oldValue: T, newValue: T) {
                super.entryRemoved(evicted, key, oldValue, newValue)
            }
        }
    }

    override fun put(position: Int, t: T) {
        if (!mUseCache) {
            return
        }
        mLruCache?.put(position, t)
    }

    override fun get(position: Int): T? {
        return if (!mUseCache) {
            null
        } else mLruCache?.get(position)
    }

    override fun remove(position: Int) {
        if (!mUseCache) {
            return
        }
        mLruCache?.remove(position)
    }

    override fun clean() {
        mLruCache?.evictAll()
    }

    init {
        initLruCache()
    }
}