package ar.uba.fi.tallerii.comprameli.presentation.publish

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.uba.fi.tallerii.comprameli.R
import ar.uba.fi.tallerii.comprameli.presentation.GlideApp
import kotlinx.android.synthetic.main.publish_image_item.view.*

class ImagesAdapter(private val mAddListener: () -> Unit) :
        RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(position: Int)
    }

    inner class AddViewHolder(view: View) : ViewHolder(view) {
        override fun bind(position: Int) {
            itemView.setOnClickListener { mAddListener() }
        }
    }

    inner class ImageViewHolder(view: View) : ViewHolder(view) {
        override fun bind(position: Int) {
            with(itemView) {
                GlideApp.with(itemView).load(mItems[adapterPosition - 1]).into(image)
                deleteBtn.setOnClickListener {
                    mItems.removeAt(adapterPosition - 1)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    object ViewType {
        const val ADD_ITEM = 0
        const val IMAGE = 1
    }

    private var mItems: MutableList<Uri> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.ADD_ITEM -> AddViewHolder(inflater.inflate(R.layout.publish_add_item, parent, false))
            ViewType.IMAGE -> ImageViewHolder(inflater.inflate(R.layout.publish_image_item, parent, false))
            else -> throw IllegalArgumentException("View type unknown")
        }
    }

    override fun getItemCount(): Int = mItems.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemViewType(position: Int): Int =
            if (position == 0) ViewType.ADD_ITEM else ViewType.IMAGE

    fun addImage(imageUri: Uri) {
        mItems.add(imageUri)
        notifyItemInserted(itemCount)
    }

    fun getImages() = mItems

}