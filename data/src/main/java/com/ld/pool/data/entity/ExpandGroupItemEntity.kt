package com.ld.pool.data.entity

/**
 * 每个分组对应的entity
 *
 * @param <G> 标题栏entity
 * @param <S> 子项entity
</S></G> */
class ExpandGroupItemEntity<G, S> {
    /**
     * 分组对应的标题栏
     */
    var parent: G? = null
        private set
    /**
     * 分组里面的子项
     */
    var childList: List<S>? = null
    /**
     * 分组展开还是收起
     */
    var isExpand = false

    fun setParent(parent: G) {
        this.parent = parent
    }

}