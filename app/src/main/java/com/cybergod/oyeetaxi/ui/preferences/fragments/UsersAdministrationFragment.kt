package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentUsersAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.UsersEditListAdapterNew
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants.QUERRY_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UsersAdministrationFragment : BaseFragment(),SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private var _binding: FragmentUsersAdministrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView:RecyclerView
//    private lateinit var recyclerViewAdapter : UsersEditListAdapter
    private lateinit var recyclerViewAdapter : UsersEditListAdapterNew

    val viewModel: UsersAdministrationViewModel by activityViewModels()
//    lateinit var  viewModel: UsersAdministrationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUsersAdministrationBinding.inflate(inflater, container, false)

        //viewModel = ViewModelProvider(this)[UsersAdministrationViewModel::class.java]

        initRecyclerView()

        (requireActivity() as BaseActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        setupObservers()


        return  binding.root
    }






    private val myScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                viewModel.isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val primeraPosicionVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val cantidadDeItemsVisibles = layoutManager.childCount
            val cantidadTotalDeItems = layoutManager.itemCount
            val noEstaCargando = !viewModel.isLoading.value!!
            val noEstaEnLaUtlimaPagina = !viewModel.isLastPage
            val esLaUltimaItem = primeraPosicionVisibleItem + cantidadDeItemsVisibles >= cantidadTotalDeItems
            //val noEstaAlPrincipio = primeraPosicionVisibleItem >= 0
            val elTotalEsMasQueLasItemsRequeridas = cantidadTotalDeItems >= QUERRY_PAGE_SIZE
            //val deberiaPaginar = noEstaCargando && noEstaEnLaUtlimaPagina && esLaUltimaItem && noEstaAlPrincipio && elTotalEsMasQueLasItemsRequeridas && viewModel.isScrolling
            val deberiaPaginar = noEstaCargando && noEstaEnLaUtlimaPagina && esLaUltimaItem && elTotalEsMasQueLasItemsRequeridas && viewModel.isScrolling


//            Log.d("searchUsersPaginated","onScrolled()")
//            Log.d("searchUsersPaginated","primeraPosicionVisibleItem=$primeraPosicionVisibleItem")
//            Log.d("searchUsersPaginated","cantidadDeItemsVisibles=$cantidadDeItemsVisibles")
//            Log.d("searchUsersPaginated","cantidadTotalDeItems=$cantidadTotalDeItems")
//            Log.d("searchUsersPaginated","noEstaCargando=$noEstaCargando")
//            Log.d("searchUsersPaginated","noEstaEnLaUtlimaPagina=$noEstaEnLaUtlimaPagina")
//            Log.d("searchUsersPaginated","esLaUltimaItem=$esLaUltimaItem")
//            //Log.d("searchUsersPaginated","noEstaAlPrincipio=$noEstaAlPrincipio")
//            Log.d("searchUsersPaginated","elTotalEsMasQueLasItemsRequeridas=$elTotalEsMasQueLasItemsRequeridas")
//            Log.d("searchUsersPaginated","deberiaPaginar=$deberiaPaginar")

            if (deberiaPaginar) {
                viewModel.getUsersPaginated()
                viewModel.isScrolling = false
            }
        }
    }


    private fun initRecyclerView(){
        recyclerViewAdapter = UsersEditListAdapterNew(this)
        recyclerView = binding.recylerViewUsers
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.addOnScrollListener(myScrollListener)
    }


    private fun setupObservers() {

        setupIsLoadingObserver()

        setupVehiclesTypesListObserver()


    }

    private fun setupIsLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            with (binding) {
                val visibility = if (it == true) {View.VISIBLE} else (View.GONE)
                isLoadingAnimation.visibility = visibility
            }
        })
    }


    private fun setupVehiclesTypesListObserver(){
        viewModel.usersList.observe(viewLifecycleOwner, Observer {

            viewModel.isLoading.postValue(false)


            if (it != null) {

                recyclerViewAdapter.differ.submitList(it)
                //recyclerViewAdapter.setData(it)


                viewModel.isLastPage = viewModel.getPage > viewModel.totalPages.value!!


            } else {

                showSnackBar(
                    getString(R.string.fail_server_comunication),
                    true
                )

            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView :SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Buscar ..."
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_search -> {

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private var job: Job? = null
    private var findTime = 1000L

    override fun onQueryTextSubmit(query: String?): Boolean {
        job?.cancel()
        job = MainScope().launch {
            delay(findTime)
            search(query)
        }
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {

            job?.cancel()
            job = MainScope().launch {
                delay(findTime)
                search(query)
            }

        return false
    }


    private fun search(text:String?) {
            //recyclerViewAdapter.setData(emptyList())
            viewModel.getPage=1
            viewModel.textSearch = text?:""
            viewModel.getUsersPaginated()
    }

    override fun onClose(): Boolean {
        Toast.makeText(requireContext(),"PASO",Toast.LENGTH_LONG).show()
        search(null)
        return false
    }


}




