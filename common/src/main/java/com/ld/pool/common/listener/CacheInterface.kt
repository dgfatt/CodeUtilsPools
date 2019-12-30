package com.ld.pool.common.listener

/**
 * 缓存工具需要暴露的接口
 */
interface CacheInterface<T> {
    /**
     * 加入缓存
     * @param position
     * @param t
     */
    fun put(position: Int, t: T)

    /**
     * 从缓存中获取
     * @param position
     * @return
     */
    operator fun get(position: Int): T?

    /**
     * 移除
     * @param position
     */
    fun remove(position: Int)

    /**
     * 清空缓存
     */
    fun clean()
}