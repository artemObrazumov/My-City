package com.artem_obrazumov.mycity.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.FragmentRecyclerViewBinding
import com.artem_obrazumov.mycity.ui.adapters.AdapterInterfaces
import com.artem_obrazumov.mycity.ui.adapters.PlacesAdapter
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FavoritesFragment : Fragment() {
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var binding: FragmentRecyclerViewBinding
    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository())
        ).get(FavoritesViewModel::class.java)
        if (!viewModel.initialized) {
            viewModel.getFavorites(requireContext())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        binding = FragmentRecyclerViewBinding.inflate(inflater)
        binding.swipeRefreshLayout.setOnRefreshListener {
            Navigation.findNavController(binding.root).navigate(R.id.refresh)
        }
        initializeFavoritesList()
        return binding.root
    }

    private fun initializeFavoritesList() {
        viewModel.favoritePlacesList.observe(viewLifecycleOwner, { favorites ->
            adapter = PlacesAdapter()
            adapter.listener = object: AdapterInterfaces.PlacesAdapterEventListener{
                override fun onPlaceClicked(placeId: String) {
                    val bundle = Bundle()
                    bundle.putString("placeId", placeId)
                    view?.findNavController()?.navigate(R.id.navigation_place_detail, bundle)
                }
            }
            adapter.setDataSet(favorites as ArrayList<Place>)
            binding.list.layoutManager = LinearLayoutManager(context)
            binding.list.adapter = adapter
            binding.progressBar.visibility = View.GONE
        })
    }
}