package com.artem_obrazumov.mycity.ui.placeDetail

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
class PlaceDetailFragment : Fragment() {
    private lateinit var criticsAdapter: UsersAdapter
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var viewModel: PlaceDetailViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(PlaceDetailViewModel::class.java)
        if (!viewModel.initialized) {
            viewModel.getPlaceData(requireArguments().getString("placeId")!!)
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
        initializeShowMoreButtons()
        return root
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