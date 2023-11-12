package com.example.foodorderingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.databinding.FragmentFavoritesBinding
import com.example.foodorderingapp.ui.adapter.FavoritesAdapter
import com.example.foodorderingapp.ui.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    // Called when the fragment view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        // Set up the RecyclerView for displaying favorites
        binding.rVFavorites.layoutManager = GridLayoutManager(requireContext(), 2)

        // Observe changes in the favorites list and update the adapter
        viewModel.favoritesList.observe(viewLifecycleOwner) {
            val favoritesAdapter = FavoritesAdapter(requireContext(), it, viewModel)
            binding.rVFavorites.adapter = favoritesAdapter
        }

        // Set up the search view listener for handling search queries
        binding.searchView2.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return false
            }
        })

        return binding.root
    }

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FavoritesViewModel using viewModels delegate
        val tempViewModel: FavoritesViewModel by viewModels()
        viewModel = tempViewModel
    }

    // Called when the fragment is resumed
    override fun onResume() {
        super.onResume()

        // Upload favorites data when the fragment is resumed
        viewModel.uploadFavorites()
    }

    // Function to perform search based on the provided search word
    fun search(searchWord: String) {
        viewModel.search(searchWord)
    }
}