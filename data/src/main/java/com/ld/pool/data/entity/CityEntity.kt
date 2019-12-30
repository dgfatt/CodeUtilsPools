package com.ld.pool.data.entity

class CityEntity(name: String?, province: String?, icon: Int) {
    /**
     * 城市名
     */
    var name: String? = null
    /**
     * 所属省份
     */
    var province: String? = null
    /**
     * 省份icon
     */
    var icon = 0
    var isExpanded = true

    init {
        this.name = name
        this.province = province
        this.icon = icon
    }

    override fun toString(): String {
        return "CityEntity{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", icon=" + icon +
                '}'
    }
}