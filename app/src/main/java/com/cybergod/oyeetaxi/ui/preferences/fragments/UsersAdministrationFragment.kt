package com.cybergod.oyeetaxi.ui.preferences.fragments

import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cybergod.oyeetaxi.R
import com.cybergod.oyeetaxi.databinding.FragmentUsersAdministrationBinding
import com.cybergod.oyeetaxi.ui.base.BaseActivity
import com.cybergod.oyeetaxi.ui.base.BaseFragment
import com.cybergod.oyeetaxi.ui.preferences.adapters.UsersEditListAdapterNew
import com.cybergod.oyeetaxi.ui.preferences.viewmodel.UsersAdministrationViewModel
import com.cybergod.oyeetaxi.utils.Constants.KEY_USER_FILTER_OPTIONS
import com.cybergod.oyeetaxi.utils.Constants.QUERRY_PAGE_SIZE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UsersAdministrationFragment : BaseFragment(),SearchView.OnQueryTextListener {


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

    private fun setupObservers(){
        with (viewModel) {

            usersList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    recyclerViewAdapter.differ.submitList(it)
                }

            })

            usuariosPaginadosResponse.observe(viewLifecycleOwner, Observer {
                if (it==null) {
                    showSnackBar(
                        getString(R.string.fail_server_comunication),
                        true
                    )
                }
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                with (binding) {
                    val visibility = if (it == true) {View.VISIBLE} else (View.GONE)
                    isLoadingAnimation.visibility = visibility
                }
            })
        }
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
            R.id.action_filter -> {
                launchSearchFilterUserFragment()
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
            viewModel.getPage=1
            viewModel.userFilterOptions.texto = text?:""
            viewModel.getUsersPaginated()
    }




    private fun launchSearchFilterUserFragment() {
        findNavController().navigate(R.id.action_go_to_searchFilterUserFragment,
            Bundle().apply {
                putParcelable(KEY_USER_FILTER_OPTIONS,viewModel.userFilterOptions)
            }
        )
    }


}




