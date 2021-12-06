package com.jkcq.hrwtv.wu.newversion.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.wu.newversion.view.CircleImageView
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchView
import kotlinx.android.synthetic.main.activity_course_sort.*

class CourseResultAdapter(
    private val mContext: Context,
    private var mDatas: List<DevicesDataShowBean>
) : RecyclerView.Adapter<CourseResultAdapter.MyViewHolder>() {

    init {
        mDatas = mDatas.sortedByDescending { it.precent }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_sort, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val info = mDatas[position]
        holder.tv_cal.text = "" + info.cal.toInt() + "kcal"

        if (CacheDataUtil.mCurrentRange > 5) {
            holder.tv_match.visibility = View.GONE
        } else {
            holder.tv_match.visibility = View.VISIBLE
        }

        holder.tv_match.text = "" + info.matchRate + "%"
        holder.tv_point.text = "" + info.point.toInt()
        holder.tv_hr.setText("" + info.averageHeartRate + "bpm")
        holder.tv_heart_strength.setText("" + info.averageHeartPercent + "%")
        holder.tv_name.text = info.nikeName
        holder.tv_rank.setText("" + (position + 1))

        holder.heart_strenght_view.setValue(info.getmDatas())
        LoadImageUtil.getInstance()
            .loadCirc(
                mContext,
                info.headUrl,
                holder.iv_rank_head,
                mContext.resources.getDimension(R.dimen.dp8)
            )
        // LoadImageUtil.getInstance().load(mContext, info.headUrl, holder.iv_rank_head);

    }

    override fun getItemCount(): Int {
        return mDatas.size
    }


    fun sortDataAndResetView(sortType: Int) {
        when (sortType) {
            EventConstant.SORT_DATA_CAL -> {
                mDatas = mDatas.sortedByDescending { it.cal }
            }
            EventConstant.SORT_DATA_POINT -> {
                mDatas = mDatas.sortedByDescending { it.point }
            }
            EventConstant.SORT_DATA_MATCH -> {
                mDatas = mDatas.sortedByDescending { it.matchRate }
            }
            EventConstant.SORT_DATA_HR -> {
                mDatas = mDatas.sortedByDescending { it.averageHeartRate }
            }
            EventConstant.SORT_DATA_HR_STRENGTH -> {
                mDatas = mDatas.sortedByDescending { it.averageHeartPercent }
            }
        }
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var heart_strenght_view: CourseMatchView
        var iv_rank_head: ImageView
        var tv_name: TextView
        var tv_rank: TextView
        var tv_cal: TextView
        var tv_match: TextView
        var tv_point: TextView
        var tv_hr: TextView
        var tv_heart_strength: TextView


        init {
            heart_strenght_view = itemView.findViewById(R.id.heart_strenght_view)
            iv_rank_head = itemView.findViewById(R.id.iv_rank_head)
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_rank = itemView.findViewById(R.id.tv_rank)
            tv_cal = itemView.findViewById(R.id.tv_cal)
            tv_match = itemView.findViewById(R.id.tv_match)
            tv_point = itemView.findViewById(R.id.tv_point)
            tv_hr = itemView.findViewById(R.id.tv_hr)
            tv_heart_strength = itemView.findViewById(R.id.tv_heart_strength)

        }
    }
}
