package com.gxx.electronicsignlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gxx.electronicsignlibrary.model.ElectronModel
import kotlin.math.abs


class ElectronicSign @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
)  : View(context, attrs,defStyleAttr) {
    private val TAG = "ElectronicSign"

    val electronModelList = mutableListOf<ElectronModel>()

    private var currentPathIndex = -1
    private var paintWidth = dip2px(context,8.0f).toFloat()

    private var path = Path()
    private val paint:Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = paintWidth
        color = Color.BLACK
    }

    var startX = 0.0f
    var startY = 0.0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                startX = event.x
                startY = event.y
                path.moveTo(startX,startY)
            }
            MotionEvent.ACTION_MOVE->{
                val x = event.x
                val y = event.y
                if (abs(x - startX) > 10 || abs(y - startY) > 10){
                    val controalX = (x + startX) / 2
                    val controalY = (y + startY) / 2
                    path.quadTo(startX,startY,controalX,controalY)
                    startX = x
                    startY = y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP->{
                electronModelList.add(ElectronModel(path,paintWidth,true))
                currentPathIndex++
                path = Path()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (model in electronModelList) {
            if (model.isShow){
                paint.strokeWidth = model.strokeWidth
                canvas.drawPath(model.path, paint)
            }
        }
        paint.strokeWidth = paintWidth
        canvas.drawPath(path, paint)
    }

    /**
      * 撤销
      */
    fun undo() {
        if (currentPathIndex >= 0) {
            electronModelList[currentPathIndex].isShow = false
            currentPathIndex--
            invalidate()
        }
    }

    /**
      * 还原
      */
    fun redo() {
        if (currentPathIndex + 1 < electronModelList.size) {
            currentPathIndex++
            electronModelList[currentPathIndex].isShow = true
            invalidate()
        }
    }

    /**
      * 设置文字大小
      */
    fun setFontSize(size:Float){
        paintWidth = dip2px(context,size).toFloat()
        paint.strokeWidth = paintWidth
    }

    /**
      * 清除
      */
    fun clean(){
        currentPathIndex = -1
        electronModelList.clear()
        path = Path()
        invalidate()
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
   private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}