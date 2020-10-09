package com.msalikhov.dictionarysample.presentation.presenter.translation

import android.annotation.SuppressLint
import com.msalikhov.dictionarysample.domain.translation.TranslationHistoryInterator
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationsHistoryView
import com.msalikhov.dictionarysample.utils.extensions.mapToResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import javax.inject.Inject

class TranslationsHistoryPresenter @Inject constructor(
    private val translationHistoryInterator: TranslationHistoryInterator
) : MvpPresenter<TranslationsHistoryView>() {

    private val disposable = translationHistoryInterator
        .observeHistoryModels()
        .mapToResult()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.updateProgress(true) }
        .subscribe { result ->
            viewState.updateProgress(false)
            result.onSuccess(viewState::displayTranslationHistoryItems)
            result.onFailure(viewState::displayError)
        }

    @SuppressLint("CheckResult")
    fun toggleFavouriteItem(model: TranslationHistoryModel) {
        translationHistoryInterator
            .toggleFavouriteItem(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { viewState.displayError(it) })
    }

    override fun onDestroy() {
        disposable.dispose()
    }
}