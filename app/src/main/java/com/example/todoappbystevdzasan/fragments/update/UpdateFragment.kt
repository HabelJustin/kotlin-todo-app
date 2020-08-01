package com.example.todoappbystevdzasan.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.TodoViewModel
import com.example.todoappbystevdzasan.data.models.Priority
import com.example.todoappbystevdzasan.data.models.Todo
import com.example.todoappbystevdzasan.databinding.FragmentUpdateBinding
import com.example.todoappbystevdzasan.fragments.SharedViewModel
import com.example.todoappbystevdzasan.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedTodoViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Binding View
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.args = args

        setHasOptionsMenu(true)

        // set spinner item color
        binding.currentPrioritiesSpinner.onItemSelectedListener =
            mSharedTodoViewModel.spinnerListener

        return binding.root
    }

    private fun deleteTodo(): Boolean {


        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteData(args.currentTodo.id)
            hideKeyboard(requireActivity())
            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToListFragment())
            Toast.makeText(
                requireContext(),
                "Successfully Removed ${args.currentTodo.title}",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentTodo.title}")
        builder.setMessage("Are you sure want to delete ${args.currentTodo.title}")
        builder.create().show()

        return true
    }

    private fun saveTodo(): Boolean {
        hideKeyboard(requireActivity())

        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val priority = current_priorities_spinner.selectedItem.toString()

        if (mSharedTodoViewModel.fieldsNotEmpty(title, description)) {

            val updatedTodo = Todo(
                args.currentTodo.id,
                title,
                mSharedTodoViewModel.parsePriority(priority),
                description
            )

            mTodoViewModel.updateData(updatedTodo)

            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToListFragment())

            Toast.makeText(requireContext(), "Update Successfully!", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Please fill the empty field.", Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> saveTodo()
            R.id.menu_delete -> deleteTodo()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}