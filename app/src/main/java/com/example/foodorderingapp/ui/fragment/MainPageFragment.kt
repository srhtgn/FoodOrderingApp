package com.example.foodorderingapp.ui.fragment

import android.os.Bundle
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)

        binding.rVFoods.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.foodList.observe(viewLifecycleOwner){
            val foodsAdapter = FoodsAdapter(requireContext(),it,viewModel)
            binding.rVFoods.adapter = foodsAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
                return false
            }

        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MainPageViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun search(searchWord: String) {
        viewModel.search(searchWord)
    }

    override fun onResume() {
        super.onResume()
        viewModel.uploadFoods()
    }
}