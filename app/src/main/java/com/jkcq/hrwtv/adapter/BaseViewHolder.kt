package com.jkcq.hrwtv.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author Snail
 * @date 2018/6/22
 * @description
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun getItemView(): View = itemView
}
