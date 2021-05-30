package com.artem_obrazumov.mycity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.adapters.PlacesAdapter
import com.artem_obrazumov.mycity.adapters.UsersAdapter
import com.artem_obrazumov.mycity.databinding.FragmentHomeBinding
import com.artem_obrazumov.mycity.models.PlaceModel
import com.artem_obrazumov.mycity.models.UserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var criticsAdapter: UsersAdapter
    private lateinit var placesAdapter: PlacesAdapter

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater)
        val root = binding.root
        binding.swipeRefreshLayout.setOnRefreshListener {
            Navigation.findNavController(root).navigate(R.id.refresh)
        }

        initializeLists()

        return root
    }

    private fun initializeLists() {
        binding.placesList.apply {
            placesAdapter = PlacesAdapter()
            binding.placesList.layoutManager = LinearLayoutManager(context)
            binding.placesList.adapter = placesAdapter
            homeViewModel.placesList.observe(viewLifecycleOwner, Observer {
                placesAdapter.setDataSet(it as ArrayList<PlaceModel>)
                binding.placesProgressBar.visibility = View.GONE
            })
        }

        binding.criticsList.apply {
            criticsAdapter = UsersAdapter()
            binding.criticsList.layoutManager = LinearLayoutManager(context)
            binding.criticsList.adapter = criticsAdapter
            homeViewModel.criticsList.observe(viewLifecycleOwner, Observer {
                criticsAdapter.setDataSet(it as ArrayList<UserModel>)
                binding.criticsProgressBar.visibility = View.GONE
            })
        }
    }
}