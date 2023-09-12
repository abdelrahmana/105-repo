package com.urcloset.smartangle.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class MySwipHelper(context: Context,private val recyclerView:RecyclerView,internal var buttonWidth:Int):
    ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {


    private var buttonList:MutableList<MyButton>?=null
    lateinit var gesterDetector:GestureDetector
    var swipePostion=-1
    var swipThreshold=0.5f
    val buttonBuffer:MutableMap<Int,MutableList<MyButton>>
    lateinit var removeQueue: LinkedList<Int>

    abstract fun instantiateMyButton(viewHolder:RecyclerView.ViewHolder,buffer:MutableList<MyButton>)


    private val gesterListener=object:GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for(button in buttonList!!)
                if(button.onClick(e!!.x,e!!.y))
                    break
            return true
        }
    }


    private val onTouchListener= View.OnTouchListener { _, motionEvent ->

        if (swipePostion<0) return@OnTouchListener false
        val point= Point(motionEvent.rawX.toInt(),motionEvent.rawY.toInt())
        val swipViewHolder=recyclerView.findViewHolderForAdapterPosition(swipePostion)



        val swipItem=swipViewHolder?.itemView
        val rect= Rect()
        swipItem?.getGlobalVisibleRect(rect)

        if(motionEvent.action==MotionEvent.ACTION_DOWN||
                motionEvent.action==MotionEvent.ACTION_MOVE||
                motionEvent.action==MotionEvent.ACTION_UP)
        {
            if(rect.top<point.y&& rect.bottom>point.y)
                gesterDetector.onTouchEvent(motionEvent)
            else{
                removeQueue.add(swipePostion)
                swipePostion=-1
                recoverSwipItem()
            }
        }
        false

    }

    @Synchronized
    private fun recoverSwipItem() {

        while(!removeQueue.isEmpty()){
            val pos=removeQueue.poll()!!.toInt()
            if(pos>-1)
                recyclerView.adapter!!.notifyItemChanged(pos)
        }

    }

    init {
        this.buttonList=ArrayList()
        this.gesterDetector= GestureDetector(context,gesterListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer=HashMap()
        this.removeQueue=IntLinkedList()

        attachSwip()
    }

    private fun attachSwip() {
        val itemTouchHelper=ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLinkedList:LinkedList<Int>(){
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if(contains(element))
                false
            else super.add(element)
        }
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val pos=viewHolder.adapterPosition
        if(swipePostion!=pos)
            removeQueue.add(swipePostion)
        swipePostion=pos
        if(buttonBuffer.containsKey(swipePostion))
            buttonList=buttonBuffer[swipePostion]
        else
            buttonList!!.clear()
        buttonBuffer.clear()
        swipThreshold=0.5f*buttonList!!.size.toFloat()*buttonWidth.toFloat()
        recoverSwipItem()

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
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

        val pos=viewHolder.adapterPosition
        var translationX=dX
        var itemView=viewHolder.itemView
        if(pos<0){
            swipePostion=pos
            return
        }
        if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){

            if(dX<0){

                var buffer:MutableList<MyButton> =ArrayList()
                if(!buttonBuffer.containsKey(pos)){
                    instantiateMyButton(viewHolder,buffer)
                    buttonBuffer[pos]=buffer
                }else{
                    buffer=buttonBuffer[pos]!!
                }
                translationX=dX*buffer.size.toFloat()*buttonWidth.toFloat()/itemView.width
                drawButton(c,itemView,buffer,pos,translationX)

            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)

    }

    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<MyButton>, pos: Int, translationX: Float) {
        var right=itemView.right.toFloat()
        val dButtonWidth=-1*translationX/buffer.size
        for(button in buffer){
            val left=right-dButtonWidth
            button.onDraw(c,RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
            right=left
        }


    }


}