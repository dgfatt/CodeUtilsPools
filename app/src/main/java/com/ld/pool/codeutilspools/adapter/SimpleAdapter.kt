package com.ld.pool.codeutilspools.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.ld.pool.codeutilspools.R
import com.ld.pool.data.entity.CityEntity


class SimpleAdapter(context: Context?, cities: MutableList<CityEntity>) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private val mCities: MutableList<CityEntity> = cities
    private val mContext: Context? = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder?
        if (mCities.size > position) {
            val city: CityEntity = mCities[position]
            if (city.isExpanded) {
                val layoutParams = holder!!.mLlBg.layoutParams
                layoutParams.height = SizeUtils.dp2px(100F)
                holder.mLlBg.layoutParams = layoutParams
                //holder.itemView.setVisibility(View.VISIBLE);
                val i = position % 5 + 1
                if (i == 1) {
                    holder.mIvCity.setImageResource(R.mipmap.subject1)
                    holder.mLlBg.setBackgroundColor(mContext?.resources?.getColor(R.color.bg1)!!)
                } else if (i == 2) {
                    holder.mIvCity.setImageResource(R.mipmap.subject2)
                    holder.mLlBg.setBackgroundColor(mContext?.resources?.getColor(R.color.bg2)!!)
                } else if (i == 3) {
                    holder.mIvCity.setImageResource(R.mipmap.subject3)
                    holder.mLlBg.setBackgroundColor(mContext?.resources?.getColor(R.color.bg3)!!)
                } else if (i == 4) {
                    holder.mIvCity.setImageResource(R.mipmap.subject4)
                    holder.mLlBg.setBackgroundColor(mContext?.resources?.getColor(R.color.bg4)!!)
                } else {
                    holder.mIvCity.setImageResource(R.mipmap.subject5)
                    holder.mLlBg.setBackgroundColor(mContext?.resources?.getColor(R.color.bg5)!!)
                }
                holder.mTvCity.setText(city.name)
            } else {
                val layoutParams = holder!!.mLlBg.layoutParams
                layoutParams.height = SizeUtils.dp2px(0F)
                holder.mLlBg.layoutParams = layoutParams
                //holder.itemView.setVisibility(View.GONE);
            }
        }
    }

    override fun getItemCount(): Int {
        return mCities.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mIvCity: ImageView = view.findViewById(R.id.iv_city)
        var mTvCity: TextView = view.findViewById(R.id.tv_city)
        var mTvBrief: TextView = view.findViewById(R.id.tv_brief)
        var mLlBg: LinearLayout = view.findViewById(R.id.ll_bg)

    }

}
