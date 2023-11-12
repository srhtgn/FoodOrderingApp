package com.example.foodorderingapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodorderingapp.databinding.FragmentMainPageBinding
import com.example.foodorderingapp.ui.adapter.FoodsAdapter
import com.example.foodorderingapp.ui.viewmodel.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPageFragment : Fragment() {
    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var foodsAdapter: FoodsAdapter
    private lateinit var searchView: SearchView

    // Called when the fragment view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)

        // Set up the RecyclerView for displaying food items in a grid layout
        binding.rVFoods.layoutManager = GridLayoutManager(requireContext(), 2)

        // Observe changes in the food list and update the adapter
        viewModel.foodList.observe(viewLifecycleOwner) {
            foodsAdapter.updateData(it)
        }

        // Observe changes in the search results and update the adapter
        viewModel.searchFoods.observe(viewLifecycleOwner) { foods ->
            foodsAdapter.updateData(foods)
        }

        return binding.root
    }

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MainPageViewModel using viewModels delegate
        val tempViewModel: MainPageViewModel by viewModels()
        viewModel = tempViewModel
    }

    // Called after the fragment's view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an instance of FoodsAdapter and set it on the RecyclerView
        foodsAdapter = FoodsAdapter(requireContext(), mutableListOf(), viewModel)
        binding.rVFoods.adapter = foodsAdapter

        // Set up the SearchView listener for handling search queries
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Perform search based on the entered text
                viewModel.search(newText.orEmpty())
                return true
            }
        })
    }

    // Called when the fragment is resumed
    override fun onResume() {
        super.onResume()

        // Upload food items when the fragment is resumed
        viewModel.uploadFoods()
    }
}