package com.shubhamjitseekho.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamjitseekho.domain.model.AnimeDetail
import com.shubhamjitseekho.domain.repository.AnimeRepository
import com.shubhamjitseekho.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: AnimeRepository) : ViewModel() {
    
    private val _animeDetailState = MutableStateFlow<UiState<AnimeDetail>>(UiState.Idle)
    val animeDetailState: StateFlow<UiState<AnimeDetail>> = _animeDetailState.asStateFlow()
    
    fun loadAnimeDetail(animeId: Int) {
        viewModelScope.launch {
            _animeDetailState.value = UiState.Loading
            
            repository.getAnimeDetail(animeId)
                .catch { e ->
                    _animeDetailState.value = UiState.Error(
                        e.message ?: "Failed to load anime details"
                    )
                }
                .collect { animeDetail ->
                    _animeDetailState.value = if (animeDetail != null) {
                        UiState.Success(animeDetail)
                    } else {
                        UiState.Error("Anime not found")
                    }
                }
        }
    }
}