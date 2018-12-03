package ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatListAdapter.ItemTypes.LEFT
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.chat.ChatListAdapter.ItemTypes.RIGHT

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatItemViewHolder>() {

    object ItemTypes {
        const val LEFT = 0
        const val RIGHT = 1
    }

    inner class ChatItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ChatContract.Message) {
            with(itemView) {
                findViewById<TextView>(R.id.text).text = item.text
                findViewById<TextView>(R.id.userName).text =
                        if (item.isCurrentUser) context.getString(R.string.dashboard_chat_list_item_me)
                        else item.userName
            }
        }
    }

    private var mItems: List<ChatContract.Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view = when (viewType) {
            LEFT -> LayoutInflater.from(parent.context).inflate(R.layout.dashboard_chat_list_item_left, parent, false)
            RIGHT -> LayoutInflater.from(parent.context).inflate(R.layout.dashboard_chat_list_item_right, parent, false)
            else -> throw RuntimeException()
        }
        return ChatItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) =
            holder.bind(mItems[position])

    override fun getItemViewType(position: Int): Int {
        return if (mItems[position].isCurrentUser) LEFT else RIGHT
    }

    fun setItems(items: List<ChatContract.Message>) {
        mItems = items.subList(0, items.lastIndex + 1)
        notifyDataSetChanged()
    }

}