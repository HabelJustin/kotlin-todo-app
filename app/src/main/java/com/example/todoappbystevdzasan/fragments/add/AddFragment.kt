package com.example.todoappbystevdzasan.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.TodoViewModel
import com.example.todoappbystevdzasan.data.models.Todo
import com.example.todoappbystevdzasan.fragments.SharedViewModel
import com.example.todoappbystevdzasan.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()


    companion object {

        @JvmStatic
        fun newInstance(instance: Int) =
            AddFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        view.priorities_spinner.onItemSelectedListener = mSharedViewModel.spinnerListener

        return view
    }

    private fun addTodo() {
        hideKeyboard(requireActivity())

        val title = title_et.text.toString()
        val priority = priorities_spinner.selectedItem.toString()
        val description = description_et.text.toString()

        if (mSharedViewModel.fieldsNotEmpty(title, description)) {

            val newTodo = Todo(
                0,
                title,
                mSharedViewModel.parsePriority(priority),
                description
            )

            // start inserting to db
            mTodoViewModel.insertData(newTodo)

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill the empty field", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                addTodo()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}