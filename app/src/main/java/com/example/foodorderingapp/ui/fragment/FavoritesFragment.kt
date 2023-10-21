package com.example.foodorderingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentFavoritesBinding
import com.example.foodorderingapp.ui.adapter.FavoritesAdapter
import com.example.foodorderingapp.ui.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.rVFavorites.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favoritesList.observe(viewLifecycleOwner) {
            val favoritesAdapter = FavoritesAdapter(requireContext(), it, viewModel)
            binding.rVFavorites.adapter = favoritesAdapter
        }

        binding.searchView2.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FavoritesViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.uploadFavorites()
    }

    fun search(searchWord:String){
        viewModel.search(searchWord)
    }
}