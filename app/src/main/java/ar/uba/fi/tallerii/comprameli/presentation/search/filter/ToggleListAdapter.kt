package ar.uba.fi.tallerii.comprameli.presentation.search.filter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.search_filters_toggle_item.view.*

class ToggleListAdapter : RecyclerView.Adapter<ToggleListAdapter.ToggleItemViewHolder>() {

    inner class ToggleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SearchFiltersContract.SelectableItem, position: Int) {
            with(itemView) {
                toggle.textOff = item.label
                toggle.textOn = item.label
                toggle.isChecked = item.selected
                toggle.setOnCheckedChangeListener { _, isChecked ->
                    mItems[position] = item.copy(selected = isChecked)
                }
            }
        }
    }

    private var mItems: MutableList<SearchFiltersContract.SelectableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToggleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_filters_toggle_item, parent, false)
        return ToggleItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ToggleItemViewHolder, position: Int) =
            holder.bind(mItems[position], position)

    fun setItems(items: List<SearchFiltersContract.SelectableItem>) {
        mItems = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getItemsSelected() = mItems.filter { it.selected }.map { it.label }

}