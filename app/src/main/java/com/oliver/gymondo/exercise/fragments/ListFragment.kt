package com.oliver.gymondo.exercise.fragments

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat.getActionView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.paging.toLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oliver.gymondo.R
import com.oliver.gymondo.database.GymondoDatabase
import com.oliver.gymondo.database.models.ModelExercise
import com.oliver.gymondo.databinding.FragmentListBinding
import com.oliver.gymondo.exercise.ExerciseActivity
import com.oliver.gymondo.exercise.adapters.SectionAdapter
import com.oliver.gymondo.exercise.components.DaggerExerciseComponent
import com.oliver.gymondo.exercise.modules.ExerciseModule
import com.oliver.gymondo.exercise.viewmodels.ExerciseViewModel
import com.oliver.gymondo.exercise.viewmodels.ExerciseViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.layout_fab_submenu.view.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class ListFragment : BaseFragment(), SectionAdapter.Interaction {

    @Inject
    lateinit var exerciseViewModelFactory: ExerciseViewModelFactory

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        exerciseViewModelFactory
    }

    lateinit var gymondoDatabase: GymondoDatabase
    var page = 0
    var isLoading = false
    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Main + job)
    val adapter: SectionAdapter by lazy { SectionAdapter(context!!, this) }
    private var mSearchItem: MenuItem? = null
    private var fabExpanded = false
    private var fabUsed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)

        context ?: return binding.root
        setHasOptionsMenu(true)
        DaggerExerciseComponent.builder().exerciseModule(ExerciseModule()).build().inject(this)
        gymondoDatabase = GymondoDatabase.getInstance(context!!)
        binding.included.fabSetting.setOnClickListener {
            if (fabExpanded) {
                closeSubMenusFab(binding.root)
            } else {
                openSubMenusFab()
            }
        }
        closeSubMenusFab(binding.root)
        val layoutManager =
            binding.recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        scope.launch {
            val p = ProgressDialog(context)
            p.setMessage("Loading...")
            p.show()
            val one = async { exerciseViewModel.getExercises() }
            exerciseViewModel.boolean.observe(viewLifecycleOwner) {
                if (it == true) {
                    binding.loadingImage.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    observeData(adapter)
                    p.dismiss()

                }

            }
        }
        filterByBodyParts()
        binding.recyclerView.addOnScrollListener(newScrollListener)

        return binding.root

    }
    var isLoadingList = false
    var isLastPage = false
    var isScrolling = false

    private val newScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoadingList && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                val next = exerciseViewModel.nextPage.value
                scope.launch {
                    val response = async {
                        exerciseViewModel.getNexExercises(next!!)
                    }
                    val totalPages = response.await().body()?.count!! / 20 + 2
                    isLastPage = exerciseViewModel.page == totalPages
                    observeData(adapter)

                }
                isScrolling = false
            } else {
                view!!.recycler_view.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            R.menu.search_menu,
            menu
        ) // Put your search menu in "menu_search" menu file.
        mSearchItem = menu.findItem(R.id.action_search)
        val sv = getActionView(mSearchItem) as SearchView
        sv.isIconified = true
        val searchManager =
            activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        sv.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                sv.clearFocus()
                searchByName(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                searchByName(query)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun observeData(adapter: SectionAdapter) {
        exerciseViewModel.nekojResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isLoading = false
        }
        exerciseViewModel.exception.observe(viewLifecycleOwner) {
            handleNetworkError(it)
        }
    }

    fun searchByName(characterTextForSearch: String) {
        var characterText = characterTextForSearch
        characterText = characterText.toLowerCase(Locale.getDefault())
        CoroutineScope(Dispatchers.IO).launch {
            val text = gymondoDatabase.exerciseDao().searchByName(characterText).toLiveData(20)
            if (characterText.isEmpty()) {
                withContext(Dispatchers.Main) {
                    observeData(adapter)
                }
            } else {
                scope.launch {
                    text.observe(viewLifecycleOwner) {
                        for (exercise in it) {
                            if (exercise.name!!.toLowerCase(Locale.getDefault())
                                    .contains(characterText)
                            ) adapter.submitList(it)
                        }
                    }
                }
            }
        }
    }

    //closes FAB submenus
    private fun closeSubMenusFab(view: View) {
        view.layoutFabAbs.visibility = View.INVISIBLE
        view.layoutFabLegs.visibility = View.INVISIBLE
        view.layoutFabArms.visibility = View.INVISIBLE
        view.fabSetting.setImageResource(R.drawable.common_google_signin_btn_text_dark_normal)
        fabExpanded = false
        if (fabUsed)
            observeData(adapter)
        fabUsed = false
    }

    //Opens FAB submenus
    private fun openSubMenusFab() {
        view!!.layoutFabAbs.visibility = View.VISIBLE
        view!!.layoutFabLegs.visibility = View.VISIBLE
        view!!.layoutFabArms.visibility = View.VISIBLE
        //Change settings icon to 'X' icon
        view!!.fabSetting.setImageResource(R.drawable.ic_map_white_24dp)
        fabExpanded = true
    }

    private fun filterByBodyParts() {
        scope.launch {
            val i = gymondoDatabase.exerciseDao().searchByCategoryName("Abs").toLiveData(20)
            i.observe(viewLifecycleOwner) { pagedList ->
                view!!.layoutFabAbs.setOnClickListener {
                    adapter.submitList(pagedList)
                    fabUsed = true
                }
            }
        }
        scope.launch {
            val i = gymondoDatabase.exerciseDao().searchByCategoryName("Arms").toLiveData(20)
            i.observe(viewLifecycleOwner) { pagedList ->
                view!!.layoutFabArms.setOnClickListener {
                    adapter.submitList(pagedList)
                    fabUsed = true
                }
            }

        }
        scope.launch {
            val i = gymondoDatabase.exerciseDao().searchByCategoryName("Legs").toLiveData(20)
            i.observe(viewLifecycleOwner) { pagedList ->
                view!!.layoutFabLegs.setOnClickListener {
                    adapter.submitList(pagedList)
                    fabUsed = true
                }
            }
        }
    }

    override fun itemClicked(modelExercise: ModelExercise) {
        val id = modelExercise.id
        ((activity as ExerciseActivity).addFragment(DetailsFragment.newInstance(id!!)))
    }

}