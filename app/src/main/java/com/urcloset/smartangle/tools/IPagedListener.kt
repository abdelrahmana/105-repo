package com.urcloset.smartangle.tools


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface IPagedListener<T> : IMovable, IImagesListener
     {

    fun scroll(
        size_before_update: Int,
        layoutManager: LinearLayoutManager,
        recyclerView: RecyclerView
    )

    fun delete_with_confirm(position: Int, item: T, message: String)
    fun delete_from_db(position: Int, item: T)
    fun store_in_db(item: T, position: Int, image_source: String)
    fun report(item: T, position: Int)
    fun refresh()
    fun login_first()



}
