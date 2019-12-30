package com.ld.pool.data.datautils

import com.ld.pool.data.R
import com.ld.pool.data.entity.CityEntity
import java.util.*
import kotlin.collections.ArrayList


object CityUtil {
    private val CityEntityS =
        arrayOf("福建省", "安徽省", "浙江省", "江苏省")

    /**
     * 获取城市名
     *
     * @return
     */
    val cityEntityList: MutableList<CityEntity>
        get() {
            val dataList: MutableList<CityEntity> = ArrayList()
            val FU_JIAN = CityEntityS[0]
            val FU_JIAN_ICON: Int = R.mipmap.city1;
            dataList.add(CityEntity("福州", FU_JIAN, FU_JIAN_ICON))
            dataList.add(CityEntity("厦门", FU_JIAN, FU_JIAN_ICON))
            dataList.add(CityEntity("泉州", FU_JIAN, FU_JIAN_ICON))
            dataList.add(CityEntity("宁德", FU_JIAN, FU_JIAN_ICON))
            dataList.add(CityEntity("漳州", FU_JIAN, FU_JIAN_ICON))
            val AN_HUI = CityEntityS[1]
            val AN_HUI_ICON: Int = R.mipmap.city2
            dataList.add(CityEntity("合肥", AN_HUI, AN_HUI_ICON))
            dataList.add(CityEntity("芜湖", AN_HUI, AN_HUI_ICON))
            dataList.add(CityEntity("蚌埠", AN_HUI, AN_HUI_ICON))
            val ZHE_JIANG = CityEntityS[2]
            val ZHE_JIANG_ICON: Int = R.mipmap.city3
            dataList.add(CityEntity("杭州", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("宁波", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("温州", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("嘉兴", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("绍兴", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("金华", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("湖州", ZHE_JIANG, ZHE_JIANG_ICON))
            dataList.add(CityEntity("舟山", ZHE_JIANG, ZHE_JIANG_ICON))
            val JIANG_SU = CityEntityS[3]
            val JIANG_SU_ICOM: Int = R.mipmap.city4
            dataList.add(CityEntity("南京", JIANG_SU, JIANG_SU_ICOM))
            return dataList
        }

    /**
     * 获取城市名
     *
     * @return
     */
    val randomCityEntityList: MutableList<CityEntity>
        get() {
            val dataList: MutableList<CityEntity> = ArrayList()
            val random = Random()
            val provinceSize: Int = random.nextInt(5) + 3
            for (i in 0 until provinceSize) {
                val province = randomCityEntityName
                val cityEntitySize: Int = random.nextInt(3) + 1
                for (j in 0 until cityEntitySize) {
                    dataList.add(CityEntity("$province : CityEntity $j", province, R.mipmap.city4))
                }
            }
            return dataList
        }

    private val randomCityEntityName: String
        private get() {
            val random = Random()
            return CityEntityS[random.nextInt(4)]
        }
}