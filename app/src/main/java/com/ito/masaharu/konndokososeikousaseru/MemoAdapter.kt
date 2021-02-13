package com.ito.masaharu.konndokososeikousaseru

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MemoAdapter (
    private val context: Context,
    private var memoList: OrderedRealmCollection<Memo>?,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean
) : RealmRecyclerViewAdapter<Memo, MemoAdapter.MemoViewHolder>(memoList, autoUpdate) {

    override fun getItemCount(): Int = memoList?.size ?: 0

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo: Memo = memoList?.get(position) ?: return

        holder.container.setOnClickListener{
            listener.onItemClick(memo)
        }

        holder.titleTextView.text = memo.title
        holder.dateTextView.text =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(memo.createdAt)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MemoViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return MemoViewHolder(v)
    }

    class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container : LinearLayout = view.container
        val titleTextView: TextView = view.titleTextView
        val dateTextView: TextView = view.dateTextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: Memo)
    }

}