package com.urcloset.smartangle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.ChatModel

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private var chats:ArrayList<ChatModel.Data.Chat.ChatItems>?=null
    private var context: Context?=null
    constructor(context:Context, items:ArrayList<ChatModel.Data.Chat.ChatItems>) : this() {

        this.context=context
        this.chats=items

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        return ChatAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.box_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat:ChatModel.Data.Chat.ChatItems =   chats?.get(position)!!

            holder.title.setText(chat.subject)
            holder.body.setText(chat.message)

    }
    fun addNewItem(item:ChatModel.Data.Chat.ChatItems){
        chats?.add(item)
        notifyItemInserted(chats?.size!!-1)

    }

    override fun getItemCount(): Int {
        return chats!!.size
    }
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.tv_box_title)
        val body = view.findViewById<TextView>(R.id.tv_box_body)


    }
}