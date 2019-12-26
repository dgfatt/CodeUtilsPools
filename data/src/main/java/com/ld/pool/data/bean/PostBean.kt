package com.ld.pool.data.bean

class PostBean(text: String?) : TextViewBean(text!!) {
    var comments: MutableList<CommentBean>? = null
}