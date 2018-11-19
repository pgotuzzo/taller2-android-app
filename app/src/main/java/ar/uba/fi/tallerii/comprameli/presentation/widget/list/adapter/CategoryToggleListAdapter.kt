package ar.uba.fi.tallerii.comprameli.presentation.widget.list.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import kotlinx.android.synthetic.main.widget_category_toggle_item.view.*

class CategoryToggleListAdapter : RecyclerView.Adapter<CategoryToggleListAdapter.ToggleItemViewHolder> {

    inner class ToggleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SelectableItem, position: Int) {
            with(itemView) {
                toggle.textOff = item.label
                toggle.textOn = item.label
                toggle.isChecked = item.selected
                toggle.setOnCheckedChangeListener { _, isChecked ->
                    mItems[position] = item.copy(selected = isChecked)
                    mOnSelectListener?.invoke(mItems[position].label, isChecked)
                }
            }
        }
    }

    constructor() {
        mOnSelectListener = null
    }

    constructor(onItemSelectedListener: (item: String, isSelected: Boolean) -> Unit) {
        mOnSelectListener = onItemSelectedListener
    }

    private val mOnSelectListener: ((item: String, isSelected: Boolean) -> Unit)?
    private var mItems: MutableList<SelectableItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToggleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_category_toggle_item, parent, false)
        return ToggleItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ToggleItemViewHolder, position: Int) =
            holder.bind(mItems[position], position)

    fun setItems(items: List<SelectableItem>) {
        mItems = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getItemsSelected() = mItems.filter { it.selected }.map { it.label }

}