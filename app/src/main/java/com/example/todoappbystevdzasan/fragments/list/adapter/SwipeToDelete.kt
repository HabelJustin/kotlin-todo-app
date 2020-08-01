package com.example.todoappbystevdzasan.fragments.list.adapter

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

const val FLOAT_TRESHOLD = 1.00

abstract class SwipeToDelete : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        viewHolder.itemView.alpha =
            (FLOAT_TRESHOLD - ((abs(dX) * FLOAT_TRESHOLD) / 100).toFloat() * 0.16).toFloat()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}