package ar.uba.fi.tallerii.comprameli.presentation.checkout.cardform

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.checkout.CheckOutContract
import kotlinx.android.synthetic.main.check_out_card_list_item.view.*

class CardsListAdapter(items: List<CheckOutContract.Card>) :
        RecyclerView.Adapter<CardsListAdapter.ToggleItemViewHolder>() {

    inner class ToggleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: CheckOutContract.Card) {
            with(itemView) {
                GlideApp.with(this).load(item.image).centerInside().into(image)
                toggle.isChecked = item.name == mSelection
                toggle.setOnCheckedChangeListener { _, _ ->
                    mSelection = item.name
                    post { notifyDataSetChanged() }
                }
            }
        }
    }

    private val mItems: MutableList<CheckOutContract.Card> = items.toMutableList()
    private var mSelection: String = mItems[0].name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToggleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.check_out_card_list_item, parent, false)
        return ToggleItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ToggleItemViewHolder, position: Int) = holder.bind(mItems[position])

    fun getSelection() = mSelection
}
