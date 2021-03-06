package com.artem_obrazumov.mycity.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.ui.adapters.AdapterInterfaces
import com.artem_obrazumov.mycity.ui.adapters.PlacesAdapter
import com.artem_obrazumov.mycity.ui.adapters.UsersAdapter
import com.artem_obrazumov.mycity.databinding.FragmentHomeBinding
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var criticsAdapter: UsersAdapter
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(HomeViewModel::class.java)
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
        binding = FragmentHomeBinding.inflate(inflater)
        val root = binding.root
        binding.swipeRefreshLayout.setOnRefreshListener {
            Navigation.findNavController(root).navigate(R.id.refresh)
        }
        initializeLists()
        initializeShowMoreButtons()
        return root
    }

    @Suppress("UNCHECKED_CAST")
    private fun initializeLists() {
        binding.placesList.apply {
            placesAdapter = PlacesAdapter()
            placesAdapter.listener = object: AdapterInterfaces.PlacesAdapterEventListener{
                override fun onPlaceClicked(placeId: String) {
                    val bundle = Bundle()
                    bundle.putString("placeId", placeId)
                    view?.findNavController()?.navigate(R.id.navigation_place_detail, bundle)
                }
            }
            binding.placesList.layoutManager = LinearLayoutManager(context)
            binding.placesList.adapter = placesAdapter
            viewModel.placesList.observe(viewLifecycleOwner, Observer { placesList ->
                placesAdapter.setDataSet(placesList as ArrayList<Place>)
                binding.placesProgressBar.visibility = View.GONE
                if (placesList.size == 0) {
                    binding.placesLabel.visibility = View.GONE
                    binding.placesList.visibility = View.GONE
                }
            })
        }

        binding.criticsList.apply {
            criticsAdapter = UsersAdapter()
            criticsAdapter.listener = object: AdapterInterfaces.UsersAdapterEventListener{
                override fun onUserClicked(userId: String) {
                    val bundle = Bundle()
                    bundle.putString("userId", userId)
                    view?.findNavController()?.navigate(R.id.navigation_profile, bundle)
                }
            }
            binding.criticsList.layoutManager = LinearLayoutManager(context)
            binding.criticsList.adapter = criticsAdapter
            viewModel.criticsList.observe(viewLifecycleOwner, { criticsList ->
                criticsAdapter.setDataSet(criticsList as ArrayList<User>)
                binding.criticsProgressBar.visibility = View.GONE
                if (criticsList.size == 0) {
                    binding.criticsLabel.visibility = View.GONE
                    binding.criticsList.visibility = View.GONE
                }
            })
        }
    }

    private fun initializeShowMoreButtons() {
        binding.showMoreUsers.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.navigation_show_more_users)
        }
        binding.showMorePlaces.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.navigation_show_more_places)
        }
    }
}