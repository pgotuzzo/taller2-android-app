package ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.CHAT
import ar.uba.fi.tallerii.comprameli.presentation.dashboard.purchases.PurchasesContract.Companion.RATE
import kotlinx.android.synthetic.main.dashboard_purchases_list_item.view.*
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
                chatBtn.setOnClickListener { mChatClickListener(item.transactionId) }
                chatBtn.visibility = if (item.action == CHAT) VISIBLE else GONE
                rateBtn.setOnClickListener { mRateClickListener(item.trackingNumber) }
                rateBtn.visibility = if (item.action == RATE) VISIBLE else GONE
            }
        }
    }

    private var mItems: List<PurchasesContract.Transaction> = ArrayList()
    private var mChatClickListener: (transactionId: String) -> Unit = {}
    private var mRateClickListener: (trackingNumber: Int) -> Unit = {}

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
                 chatClickListener: (transactionId: String) -> Unit,
                 rateClickListener: (trackingNumber: Int) -> Unit) {
        mItems = items.subList(0, items.lastIndex + 1)
        mChatClickListener = chatClickListener
        mRateClickListener = rateClickListener
        notifyDataSetChanged()
    }

}