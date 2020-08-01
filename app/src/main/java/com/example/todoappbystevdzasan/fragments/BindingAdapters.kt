package com.example.todoappbystevdzasan.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.models.Priority
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {

        /* Navigation Methods */
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }
        /* End of Navigation Methods */


        /* @ListFragment Section */
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToColor")
        @JvmStatic
        fun parsePriorityToColor(view: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {
                    view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
                }
                Priority.MEDIUM -> {
                    view.setCardBackgroundColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.yellow
                        )
                    )
                }
                Priority.LOW -> {
                    view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
                }
            }
        }
        /* End of @ListFragment Section */


        /* @Update/Add Fragement Section */
        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {
                    view.setSelection(0)
                }
                Priority.MEDIUM -> {
                    view.setSelection(1)
                }
                Priority.LOW -> {
                    view.setSelection(2)
                }
            }
        }
        /* End of @Update/Add Fragement Section */
    }

}