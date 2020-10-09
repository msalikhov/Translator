package com.msalikhov.dictionarysample.utils.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

object SimpleAdapterFactory {

    @JvmSynthetic
    inline fun <T, VH> build(
        @LayoutRes layoutRes: Int,
        crossinline viewHolderCreator: (view: View) -> VH
    ): ListAdapter<T, VH> where VH : RecyclerView.ViewHolder, VH : (T) -> Unit, T : Diffable<T> {
        val diffUtilItemCallback = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.areItemsTheSame(newItem)

            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.areContentsTheSame(newItem)
        }
        return object : ListAdapter<T, VH>(diffUtilItemCallback) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LayoutInflater
                .from(parent.context)
                .inflate(layoutRes, parent, false)
                .let(viewHolderCreator)

            override fun onBindViewHolder(holder: VH, position: Int) = holder(getItem(position))
        }
    }
}

interface Diffable<T> {
    fun areItemsTheSame(item: T): Boolean
    fun areContentsTheSame(item: T): Boolean
}