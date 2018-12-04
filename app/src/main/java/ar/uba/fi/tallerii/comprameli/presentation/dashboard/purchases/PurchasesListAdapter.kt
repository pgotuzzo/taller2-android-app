package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import kotlinx.android.synthetic.main.dashboard_sales_list_item.view.*
import java.text.NumberFormat

class PurchasesListAdapter : RecyclerView.Adapter<PurchasesListAdapter.PurchasesItemViewHolder>() {

    inner class PurchasesItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PurchasesContract.Transaction) {
            with(itemView) {
                productName.text = item.productName
                units.text = context.getString(R.string.dashboard_purchases_list_item_units, item.units.toString())
                total.text = context.getString(R.string.dashboard_purchases_list_item_total, NumberFormat.getCurrencyInstance().format(item.total))
                status.text = context.getString(R.string.dashboard_purchases_list_item_status, item.status)
                GlideApp.with(context)
                        .load(item.productImage)
                        .placeholder(R.drawable.search_product_placeholder)
                        .into(productImage)
                chatBtn.setOnClickListener { mItemClickListener(item.transactionId) }
            }
        }
    }

    private var mItems: List<PurchasesContract.Transaction> = ArrayList()
    private var mItemClickListener: (transactionId: String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasesItemViewHolder {
        val view =
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.dashboard_purchases_list_item, parent, false)
        return PurchasesItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: PurchasesItemViewHolder, position: Int) =
            holder.bind(mItems[position])

    fun setItems(items: List<PurchasesContract.Transaction>,
                 itemClickListener: (transactionId: String) -> Unit) {
        mItems = items.subList(0, items.lastIndex + 1)
        mItemClickListener = itemClickListener
        notifyDataSetChanged()
    }

}