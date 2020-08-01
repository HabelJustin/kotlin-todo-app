package com.example.todoappbystevdzasan.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.models.Priority
import com.example.todoappbystevdzasan.data.models.Todo

const val cHigh = "High Priority"
const val cMedium = "Medium Priority"
const val cLow = "Low Priority"

const val LINEAR = "LINEAR"
const val GRID = "GRID"

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _layoutType: MutableLiveData<String> = MutableLiveData(LINEAR)
    val layoutType: LiveData<String> = _layoutType

    fun setLayoutType(layoutType: String) {
        _layoutType.value = layoutType
    }

    fun checkIfDatabaseEmpty(toDoData: List<Todo>) {
        emptyDatabase.value = toDoData.isEmpty()
    }

    val spinnerListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent?.getChildAt(0) != null) {
                    when (position) {
                        0 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    application,
                                    R.color.red
                                )
                            )
                        }
                        1 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    application,
                                    R.color.yellow
                                )
                            )
                        }
                        2 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    application,
                                    R.color.green
                                )
                            )
                        }
                    }
                }

            }

        }


    fun parsePriority(priority: String): Priority {
        return when (priority) {
            cHigh -> Priority.HIGH
            cMedium -> Priority.MEDIUM
            cLow -> Priority.LOW
            else -> Priority.LOW
        }
    }

    fun fieldsNotEmpty(title: String, description: String): Boolean =
        !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
}