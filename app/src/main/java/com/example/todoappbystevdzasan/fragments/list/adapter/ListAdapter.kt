package com.example.todoappbystevdzasan.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.models.Priority
import com.example.todoappbystevdzasan.data.models.Todo
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter(
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var listItems = emptyList<Todo>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val title = itemView.title_txt
        val description = itemView.description_txt
        val priority = itemView.priority_indicator
        val _context = itemView.context

        val red = R.color.red
        val yellow = R.color.yellow
        val green = R.color.green

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(listItems[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listItems[position]

        holder.title.text = currentItem.title
        holder.description.text = currentItem.description

        when (currentItem.priority) {
            Priority.HIGH -> holder.priority.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder._context,
                    holder.red
                )
            )
            Priority.MEDIUM -> holder.priority.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder._context,
                    holder.yellow
                )
            )
            Priority.LOW -> holder.priority.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder._context,
                    holder.green
                )
            )
        }
    }

    fun addData(todos: List<Todo>) {
        val toDiffUtil =
            TodoDiffUtil(
                listItems,
                todos
            )
        val diffResult = DiffUtil.calculateDiff(toDiffUtil)
        this.listItems = todos
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(todo: Todo)
    }
}