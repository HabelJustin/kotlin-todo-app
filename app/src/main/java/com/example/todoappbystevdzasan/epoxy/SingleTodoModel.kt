package com.example.todoappbystevdzasan.epoxy

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.models.Priority
import com.example.todoappbystevdzasan.data.models.Todo


@EpoxyModelClass(layout = R.layout.row_layout)
abstract class SingleTodoModel(@EpoxyAttribute var todo: Todo) :
    EpoxyModelWithHolder<SingleTodoModel.ItemHolder>() {

    // Declare your model properties like this
    @EpoxyAttribute
    @StringRes
    var text = 0

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: ItemHolder) {
        holder.titleView.text = todo.title
        holder.descView.text = todo.description

        when (todo.priority) {
            Priority.HIGH -> holder.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.priorityIndicator.context,
                    R.color.red
                )
            )
            Priority.MEDIUM -> holder.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.priorityIndicator.context,
                    R.color.yellow
                )
            )
            Priority.LOW -> holder.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.priorityIndicator.context,
                    R.color.green
                )
            )
        }

        holder.layoutContainer.setOnClickListener(clickListener)
    }


    /**
     * This is ViewHolder class equivalent to Google's RecyclerView.ViewHolder class
     */
    inner class ItemHolder : BaseEpoxyHolder() {
        val titleView by bind<TextView>(R.id.title_txt)
        val descView by bind<TextView>(R.id.description_txt)
        val priorityIndicator by bind<CardView>(R.id.priority_indicator)
        val layoutContainer by bind<View>(R.id.rowLayout)
    }
}