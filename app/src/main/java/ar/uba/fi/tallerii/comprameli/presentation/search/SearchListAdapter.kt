package ar.uba.fi.tallerii.comprameli.presentation.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import kotlinx.android.synthetic.main.search_list_item.view.*
import java.text.NumberFormat

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.SearchItemViewHolder>() {

    class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SearchContract.SearchItem) {
            with(itemView) {
                title.text = item.title
                price.text = NumberFormat.getCurrencyInstance().format(item.price)
                GlideApp
                        .with(context)
                        .load(item.images[0])
                        .placeholder(R.drawable.search_product_placeholder)
                        .into(image)
            }
        }
    }

    private var mItems: List<SearchContract.SearchItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_list_item, parent, false)
        return SearchItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) =
            holder.bind(mItems[position])

    fun setItems(items: List<SearchContract.SearchItem>) {
        mItems = items.subList(0, items.lastIndex + 1)
        notifyDataSetChanged()
    }

}