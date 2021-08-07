package com.artem_obrazumov.mycity.ui.showMore.showMorePlaces

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.ui.adapters.AdapterInterfaces
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.FragmentRecyclerViewBinding
import com.artem_obrazumov.mycity.ui.adapters.PlacesAdapter
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.utils.initializeBackPress
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ShowMorePlacesFragment : Fragment() {
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var viewModel: ShowMorePlacesViewModel
    private lateinit var binding: FragmentRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(ShowMorePlacesViewModel::class.java)
        if (!viewModel.initialized) {
            viewModel.cityName =
                activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    ?.getString("cityName", "NonExistingCity") as String
            viewModel.getData()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        binding = FragmentRecyclerViewBinding.inflate(inflater)
        val root = binding.root
        binding.swipeRefreshLayout.setOnRefreshListener {
            Navigation.findNavController(root).navigate(R.id.refresh)
        }
        initializeList()
        return root
    }

    override fun onStart() {
        super.onStart()
        initializeBackPress()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireView().findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    @Suppress("UNCHECKED_CAST")
    private fun initializeList() {
        binding.list.apply {
            placesAdapter = PlacesAdapter()
            placesAdapter.listener = object: AdapterInterfaces.PlacesAdapterEventListener{
                override fun onPlaceClicked(placeId: String) {
                    val bundle = Bundle()
                    bundle.putString("placeId", placeId)
                    view?.findNavController()?.navigate(R.id.navigation_profile, bundle)
                }
            }
            binding.list.layoutManager = LinearLayoutManager(context)
            binding.list.adapter = placesAdapter
            viewModel.placesList.observe(viewLifecycleOwner, { placesList ->
                placesAdapter.setDataSet(placesList as ArrayList<Place>)
                binding.progressBar.visibility = View.GONE
            })
        }
    }
}