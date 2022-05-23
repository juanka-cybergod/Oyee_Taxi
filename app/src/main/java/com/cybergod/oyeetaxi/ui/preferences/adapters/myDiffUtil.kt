package com.cybergod.oyeetaxi.ui.preferences.adapters

import androidx.recyclerview.widget.DiffUtil
import com.cybergod.oyeetaxi.api.model.Usuario

class myDiffUtil(
    private val oldList:List<Usuario>,
    private val newList:List<Usuario>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}