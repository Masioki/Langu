package pwr.mobilne.langu

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class DrawingView(context: Context)  :
    SurfaceView(context), SurfaceHolder.Callback {
    private var xStart = 0f
    private var yStart = 0f
    private var xEnd = 0f
    private var yEnd = 0f
    private var xStartDraw = 0f
    private var yStartDraw = 0f
    private var xEndDraw = 0f
    private var yEndDraw = 0f
    private lateinit var paint: Paint
    private lateinit var parentActivity: WordSearchActivity
    private var isDrawing = false

    override fun surfaceCreated(holder: SurfaceHolder?) {
        paint = Paint().apply {
            color = Color.argb(255, 66, 135, 245)
            strokeWidth = 20f
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        //TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        //TODO("Not yet implemented")
    }

    init {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setZOrderOnTop(true)
    }
    fun setParent(activity: WordSearchActivity) {
        parentActivity = activity
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) return
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawLine(xStartDraw, yStartDraw, xEndDraw, yEndDraw, paint)

    }
    private fun startDrawing(){
        val canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                xStartDraw = event.x
                yStartDraw = event.y
                xStart = event.rawX
                yStart = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                xEndDraw = event.x
                yEndDraw = event.y
                xEnd = event.rawX
                yEnd = event.rawY
                isDrawing = true
                startDrawing()
            }
            MotionEvent.ACTION_UP -> {
                if(isDrawing) {
                    isDrawing = false
                    xEndDraw = event.x
                    yEndDraw = event.y
                    xEnd = event.rawX
                    yEnd = event.rawY
                    parentActivity.registerMove(
                        xStart.toInt(),
                        yStart.toInt(),
                        xEnd.toInt(),
                        yEnd.toInt()
                    )
                }
            }
        }
        return true
    }

}