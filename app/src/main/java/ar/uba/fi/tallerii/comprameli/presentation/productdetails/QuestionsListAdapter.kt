package ar.uba.fi.tallerii.comprameli.presentation.productdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.productdetails.ProductDetailsContract.Question
import kotlinx.android.synthetic.main.product_details_question_list_item.view.*

class QuestionsListAdapter : RecyclerView.Adapter<QuestionsListAdapter.QuestionItemViewHolder>() {

    inner class QuestionItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Question) {
            with(itemView) {
                question.text = item.question
                if (item.answer.isNullOrEmpty()) {
                    answer.visibility = GONE
                } else {
                    answer.visibility = VISIBLE
                    answer.text = item.answer
                }
            }
        }
    }

    private var mItems: List<Question> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_details_question_list_item, parent, false)
        return QuestionItemViewHolder(view)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: QuestionItemViewHolder, position: Int) =
            holder.bind(mItems[position])

    fun setItems(items: List<Question>) {
        mItems = items.subList(0, items.lastIndex + 1)
        notifyDataSetChanged()
    }

}