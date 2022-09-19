package com.urcloset.smartangle.adapter




import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.tools.IPagedListener
import java.util.ArrayList
import java.util.HashMap

abstract class PagedAdapter<T> : RecyclerView.Adapter<PagedAdapter.BaseHolder> {

    protected var list = ArrayList<T>()
    protected var context: Context
    private var layout_id: Int = 0
    protected var receiver: IPagedListener<*>
    var imageSource = ""
        protected set
    protected var type: Int = 0

    /*private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean more = true;*/

    constructor(context: Context, receiver: IPagedListener<*>, layout_id: Int, type: Int) {
        this.context = context
        this.receiver = receiver
        this.layout_id = layout_id
        this.type = type
    }

    constructor(context: Context, receiver: IPagedListener<*>, layout_id: Int) {
        this.context = context
        this.receiver = receiver
        this.layout_id = layout_id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout_id, parent, false)
        return BaseHolder(view)
    }

    abstract fun bind_view(holder: BaseHolder, position: Int)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        bind_view(holder, position)
    }


    fun addnRefresh(
        new_list: List<T>,
        layoutManager: LinearLayoutManager,
        recyclerView: RecyclerView
    ) {
        val runnable = Runnable {
            if (list != null) {
                val pre_size = list!!.size
                for (item in new_list) {
                    list!!.add(item)
                    this@PagedAdapter.notifyItemInserted(list!!.size - 1)
                }
                if (list!!.size > 0)
                    receiver.scroll(pre_size, layoutManager, recyclerView)
            }
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    fun addnRefresh(
        new_list: List<T>,
        gridLayoutManager: GridLayoutManager,
        recyclerView: RecyclerView
    ) {
        val runnable = Runnable {
            if (list != null) {
                val pre_size = list!!.size
                for (item in new_list) {
                    list!!.add(item)
                    this@PagedAdapter.notifyItemInserted(list!!.size - 1)
                }
                if (list!!.size > 0)
                    receiver.scroll(pre_size, gridLayoutManager, recyclerView)
            }
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    fun change_data(new_list: List<T>) {
        val runnable = Runnable {
            list!!.clear()
            for (item in new_list)
                list!!.add(item)
            this@PagedAdapter.notifyDataSetChanged()
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    fun change_item(item: T, position: Int) {
        val runnable = Runnable {
            if (position >= 0 && position < list!!.size) {
                list!![position] = item
                this@PagedAdapter.notifyItemChanged(position)
            }
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    fun remove_item(position: Int) {
        val runnable = Runnable {
            if (position >= 0 && position < list!!.size) {
                list!!.removeAt(position)
                this@PagedAdapter.notifyItemRemoved(position)
                this@PagedAdapter.notifyItemRangeChanged(position, list!!.size)
                receiver.refresh()
            }
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    @JvmOverloads
    fun insert_item(item: T, position: Int = list!!.size) {
        val runnable = Runnable {
            if (position < list!!.size) {
                list!!.add(position, item)
                this@PagedAdapter.notifyItemInserted(position)
            } else {
                list!!.add(item)
                this@PagedAdapter.notifyItemInserted(list!!.size - 1)

            }
        }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    fun getList(): List<T>? {
        return list
    }

    fun change_type(type: Int) {
        this.type = type
    }

    class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected var views: HashMap<Int, View>

        init {
            views = HashMap()
            getAllChildViews(view, views)
        }

        fun <V> getView(id: Int, type: Class<V>): V? {
            var view: V? = null
            if (views.containsKey(id)) {
                val v = views[id]
                view = v as V
            }
            return view
        }

        private fun getAllChildViews(view: View, map: HashMap<Int, View>) {
            if (view.id != -1)
                map[view.id] = view
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    getAllChildViews(view.getChildAt(i), map)
                }
            }
        }
    }

    fun setList(list: MutableList<T>) {
        this.list = list as ArrayList<T>
        val runnable = Runnable { this@PagedAdapter.notifyDataSetChanged() }
        val handler = Handler(context.mainLooper)
        handler.post(runnable)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }

    /*@Override
    public int getItemViewType(int position) {
        if ((position == list.size() - 1) && more) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }*/

    fun setImage_source(image_source: String) {
        this.imageSource = image_source
    }

    fun getItemAt(position: Int): T? {
        return if (position >= 0 && position < list!!.size) list!![position] else null
    }
}
