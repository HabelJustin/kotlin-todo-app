package com.example.todoappbystevdzasan.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoappbystevdzasan.R
import com.example.todoappbystevdzasan.data.TodoViewModel
import com.example.todoappbystevdzasan.data.models.Todo
import com.example.todoappbystevdzasan.databinding.FragmentListBinding
import com.example.todoappbystevdzasan.epoxy.SingleTodoController
import com.example.todoappbystevdzasan.fragments.*
import com.example.todoappbystevdzasan.fragments.list.adapter.ListAdapter
import com.example.todoappbystevdzasan.fragments.list.adapter.SwipeToDelete
import com.example.todoappbystevdzasan.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

const val SORT_HIGH = "SORT_HIGH"
const val SORT_LOW = "SORT_LOW"

const val LAYOUT_TYPE = "LAYOUT_TYPE"

class ListFragment : Fragment(), ListAdapter.OnItemClickListener, SearchView.OnQueryTextListener,
    SingleTodoController.EpoxyClickListener {

    private val mTodoViewModel: TodoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
    }
    private val mSharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var _layoutType: String = LINEAR

    private val adapter: ListAdapter by lazy {
        ListAdapter(
            this
        )
    }

    private lateinit var searchView: SearchView
    private lateinit var menuSortByHigh: MenuItem
    private lateinit var menuSortByLow: MenuItem
    private lateinit var menuViewByList: MenuItem
    private lateinit var menuViewByGrid: MenuItem

    private val singleTodoController: SingleTodoController by lazy {
        SingleTodoController(
            this
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.apply {
            putString(LAYOUT_TYPE, _layoutType)
        }
    }

    override fun onPause() {
        super.onPause()
        arguments?.apply {
            putString(LAYOUT_TYPE, _layoutType)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _layoutType = it.getString(LAYOUT_TYPE, LINEAR)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mSharedViewModel = mSharedViewModel

        checkDataSortType()

        setupRecyclerView()

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun checkDataSortType() {
        // Observe TodoData
        /*mTodoViewModel.getAllData.observe(viewLifecycleOwner, Observer { todos ->
            mSharedViewModel.checkIfDatabaseEmpty(todos)
            *//*adapter.addData(todos)*//*
            singleTodoController.addData(todos)
        })*/
        when (mSharedViewModel.sortBy.value) {
            cHigh -> {
                mTodoViewModel.sortByHighPriority.observe(viewLifecycleOwner, Observer {
                    mSharedViewModel.checkIfDatabaseEmpty(it)
                    singleTodoController.addData(it)
                })
            }
            cLow -> {
                mTodoViewModel.sortByLowPriority.observe(viewLifecycleOwner, Observer {
                    mSharedViewModel.checkIfDatabaseEmpty(it)
                    singleTodoController.addData(it)
                })
            }
            else -> {
                mTodoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
                    mSharedViewModel.checkIfDatabaseEmpty(it)
                    adapter.addData(it)
                    singleTodoController.addData(it)
                })
            }
        }
    }

    private fun setupRecyclerView() {

        val recyclerView = binding.recyclerView
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.apply {
            layoutManager =
                if (mSharedViewModel.layoutType.value === GRID) staggeredGridLayoutManager else linearLayoutManager
            adapter = singleTodoController.adapter
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
        }

        // This statement builds model and add it to the recycler view
        /*singleTodoController.requestModelBuild()*/

        /*val recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            if (mSharedViewModel.layoutType.value === GRID) staggeredGridLayoutManager else linearLayoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // init swipeToDelete
        swipeToDelete(recyclerView)*/
        swipeToDelete2(recyclerView)

    }

    private fun swipeToDelete2(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentTodo = singleTodoController.listItems[viewHolder.adapterPosition]
                mTodoViewModel.deleteData(currentTodo.id)
                // Undo Deleted Item
                restoreDeletedItem(viewHolder.itemView, currentTodo, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentItem =
                    adapter.listItems[viewHolder.adapterPosition]
                mTodoViewModel.deleteData(currentItem.id)
                // Undo Deleted Item
                restoreDeletedItem(viewHolder.itemView, currentItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: Todo, position: Int) {
        Snackbar.make(view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                mTodoViewModel.insertData(deletedItem)
            }
        }.show()
    }

    private fun confirmDeletion(callback: (Boolean) -> Unit) {
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton("Yes") { _, _ ->
                callback(true)
            }
            setNegativeButton("No") { _, _ -> callback(false) }
            setTitle("Confirm delete all items?")
            setMessage("Are you sure want to delete all data?")
            create()
            show()
        }
    }

    private fun onSortList(sortType: String): Boolean {
        return when (sortType) {
            SORT_HIGH -> {
                mTodoViewModel.sortByHighPriority.observe(viewLifecycleOwner, Observer {
                    singleTodoController.addData(it)
                })
                return true
            }
            SORT_LOW -> {
                mTodoViewModel.sortByLowPriority.observe(viewLifecycleOwner, Observer {
                    singleTodoController.addData(it)
                })
                return true
            }
            else -> true
        }
    }

    private fun initMenuState() {
        when (mSharedViewModel.sortBy.value) {
            cHigh -> {
                menuSortByHigh.isChecked = true
                menuSortByLow.isChecked = false
            }
            cLow -> {
                menuSortByLow.isChecked = true
                menuSortByHigh.isChecked = false
            }
        }

        when (mSharedViewModel.layoutType.value) {
            LINEAR -> {
                menuViewByList.isChecked = true
                menuViewByGrid.isChecked = false
            }
            GRID -> {
                menuViewByGrid.isChecked = true
                menuViewByList.isChecked = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        menuSortByHigh = menu.findItem(R.id.menu_priority_high)
        menuSortByLow = menu.findItem(R.id.menu_priority_low)

        menuViewByList = menu.findItem(R.id.menu_view_by_list)
        menuViewByGrid = menu.findItem(R.id.menu_view_by_grid)

        initMenuState()

        val search = menu.findItem(R.id.menu_search)
        searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_deleteAll -> {
                confirmDeletion { status ->
                    if (status) {
                        mTodoViewModel.deleteAllData()
                        Toast.makeText(
                            requireContext(),
                            "Successfully Delete Data.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                return true
            }
            R.id.menu_priority_high -> {
                item.isChecked = !item.isChecked
                menuSortByLow.isChecked = false
                mSharedViewModel.setSortBy(cHigh)
                onSortList(SORT_HIGH)
            }
            R.id.menu_priority_low -> {
                item.isChecked = !item.isChecked
                menuSortByHigh.isChecked = false
                mSharedViewModel.setSortBy(cLow)
                onSortList(SORT_LOW)
            }
            R.id.menu_view_by_list -> {
                item.isChecked = !item.isChecked
                menuViewByGrid.isChecked = false
                mSharedViewModel.setLayoutType(LINEAR)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                return true
            }
            R.id.menu_view_by_grid -> {
                item.isChecked = !item.isChecked
                menuViewByList.isChecked = false
                mSharedViewModel.setLayoutType(GRID)
                binding.recyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(todo: Todo) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToUpdateFragment(todo))
    }

    override fun onEpoxyItemClick(todo: Todo) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToUpdateFragment(todo))
    }

    /* @OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
            hideKeyboard(requireActivity())
            /*searchView.setQuery(" ", false)
            searchView.onActionViewCollapsed()*/
            searchView.clearFocus()
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        mTodoViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer {
            singleTodoController.addData(it)
        })

        // reset sortBy (if any)
        if (mSharedViewModel.sortBy.value != cNone) {
            menuSortByHigh.isChecked = false
            menuSortByLow.isChecked = false
            mSharedViewModel.setSortBy(cNone)
        }
    }
    /* End of @OnQueryTextListener */
}