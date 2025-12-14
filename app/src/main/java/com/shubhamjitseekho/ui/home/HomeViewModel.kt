package com.shubhamjitseekho.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamjitseekho.domain.model.Anime
import com.shubhamjitseekho.domain.repository.AnimeRepository
import com.shubhamjitseekho.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AnimeRepository) : ViewModel() {
    
    private val _animeListState = MutableStateFlow<UiState<List<Anime>>>(UiState.Idle)
    val animeListState: StateFlow<UiState<List<Anime>>> = _animeListState.asStateFlow()
    
    init {
        loadAnimeList()
    }
    
    fun loadAnimeList() {
        viewModelScope.launch {
            _animeListState.value = UiState.Loading
            
            repository.getTopAnime()
                .catch { e ->
                    _animeListState.value = UiState.Error(
                        e.message ?: "Unknown error occurred"
                    )
                }
                .collect { animeList ->
                    _animeListState.value = if (animeList.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(animeList)
                    }
                }
        }
    }
    
    fun refreshAnimeList() {
        viewModelScope.launch {
            _animeListState.value = UiState.Loading
            
            val result = repository.refreshAnimeList()
            
            if (result.isFailure) {
                _animeListState.value = UiState.Error(
                    result.exceptionOrNull()?.message ?: "Refresh failed"
                )
            }
        }
    }
}