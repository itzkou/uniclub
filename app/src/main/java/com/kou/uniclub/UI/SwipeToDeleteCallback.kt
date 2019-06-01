package com.kou.uniclub.UI

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.kou.uniclub.R

abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_trash_can)
    private val iconWidth = deleteIcon?.intrinsicWidth
    private val iconHeight = deleteIcon?.intrinsicHeight
    private val deleteText = ContextCompat.getDrawable(context, R.drawable.delete)
    private val textWidth = deleteText?.intrinsicWidth
    private val textHeight = deleteText?.intrinsicHeight

    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder?.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }


        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        c.drawRoundRect(
            itemView.right + dX.toInt().toFloat(),
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat(),
            15f,
            15f,
            paint
        )

        // Calculate position of delete icon
        val deleteIconMargin = (itemHeight - iconHeight!!) / 2
        val deleteIconTop = itemView.top + deleteIconMargin
        val deleteIconBottom = deleteIconTop + iconHeight
        val deleteIconLeft = itemView.right - deleteIconMargin - iconWidth!!
        val deleteIconRight = itemView.right - deleteIconMargin

        // Calculate position of delete text
        val tMargin = (itemHeight - textHeight!!) / 2
        val txTop = itemView.top + tMargin
        val txBottom = txTop + textHeight
        val txLeft = itemView.right - tMargin - textWidth!! - 48
        val txRight = itemView.right - tMargin - 48

        // Draw the delete icon
        deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

        //Draw the text
        deleteText!!.setBounds(txLeft, txTop, txRight, txBottom)
        deleteText.draw(c)


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}