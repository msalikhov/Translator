package com.msalikhov.dictionarysample.presentation.translation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInteratorImpl
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import com.msalikhov.dictionarysample.utils.extensions.mapToResult
import com.msalikhov.dictionarysample.utils.lifecycle.BaseViewModel
import com.msalikhov.dictionarysample.utils.lifecycle.LCEState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TranslationHistoryViewModel @AssistedInject constructor(
    private val translationsHistoryInteractor: TranslationHistoryInteratorImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
): BaseViewModel() {

    private val _translationsHistory = MutableLiveData<LCEState<List<TranslationHistoryModel>>>()
    val translationsHistory: LiveData<LCEState<List<TranslationHistoryModel>>> get() = _translationsHistory

    init {
        translationsHistoryInteractor
            .observeHistoryModels()
            .mapToResult()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _translationsHistory.value = LCEState.Loading }
            .subscribe { result ->
                result
                    .onSuccess {
                        _translationsHistory.value = LCEState.Success(it)
                    }
                    .onFailure {
                        _translationsHistory.value = LCEState.Error(it)
                    }
            }
            .disposeOnCleared()
    }

    fun toggleFavouriteItem(model: TranslationHistoryModel) {
        translationsHistoryInteractor
            .toggleFavouriteItem(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { _translationsHistory.value = LCEState.Error(it) })
            .disposeOnCleared()
    }

    @AssistedFactory
    interface Factory {
        fun create(state: SavedStateHandle): TranslationHistoryViewModel
    }
}