package jp.co.charco.praisesreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.databinding.ItemPraiseBinding

class PraiseListAdapter : ListAdapter<Praise, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemPraiseBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(getItem(position))
        }
    }
}

private class ViewHolder(private val binding: ItemPraiseBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(praise: Praise) {
        binding.praise = praise
        binding.executePendingBindings()
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Praise>() {
    override fun areItemsTheSame(oldItem: Praise, newItem: Praise): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Praise, newItem: Praise): Boolean {
        return oldItem.id == newItem.id
    }
}