package com.example.todoappbystevdzasan.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.todoappbystevdzasan.data.models.Todo


class SingleTodoController(private val epoxyClickListener: EpoxyClickListener) : EpoxyController() {

    var listItems = emptyList<Todo>()

    fun addData(todos: List<Todo>) {
        this.listItems = todos
        requestModelBuild()
    }

    override fun buildModels() = listItems.forEach {
        SingleTodoModel_(it)
            .id(it.title)
            .clickListener { model, parentView, clickedView, position ->
                epoxyClickListener.onEpoxyItemClick(model.todo)
            }
            .addTo(this)
    }

    interface EpoxyClickListener {
        fun onEpoxyItemClick(todo: Todo)
    }
}