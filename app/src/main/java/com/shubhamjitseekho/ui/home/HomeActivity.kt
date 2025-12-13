package com.shubhamjitseekho.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.shubhamjitseekho.data.local.AppDatabase
import com.shubhamjitseekho.data.remote.RetrofitClient
import com.shubhamjitseekho.data.repository.AnimeRepositoryImpl
import com.shubhamjitseekho.databinding.ActivityHomeBinding
import com.shubhamjitseekho.ui.common.UiState
import com.shubhamjitseekho.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: AnimeListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupRecyclerView()
        observeAnimeList()
        setupSwipeRefresh()
    }
    
    private fun setupViewModel() {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = AnimeRepositoryImpl(
            api = RetrofitClient.api,
            dao = database.animeDao()
        )
        viewModel = HomeViewModel(repository)
    }
    
    private fun setupRecyclerView() {
        adapter = AnimeListAdapter { anime ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ANIME_ID, anime.id)
            startActivity(intent)
        }
        
        binding.rvAnimeList.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = this@HomeActivity.adapter
            setHasFixedSize(true)
        }
    }
    
    private fun observeAnimeList() {
        lifecycleScope.launch {
            viewModel.animeListState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        hideLoading()
                    }
                    
                    is UiState.Loading -> {
                        showLoading()
                    }
                    
                    is UiState.Success -> {
                        hideLoading()
                        adapter.submitList(state.data)
                        binding.tvError.visibility = View.GONE
                        binding.rvAnimeList.visibility = View.VISIBLE
                    }
                    
                    is UiState.Error -> {
                        hideLoading()
                        showError(state.message)
                    }
                    
                    is UiState.Empty -> {
                        hideLoading()
                        showError("No anime found")
                    }
                }
            }
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAnimeList()
        }
    }
    
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefresh.isRefreshing = false
    }
    
    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
    }
    
    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
        binding.rvAnimeList.visibility = View.GONE
    }
}