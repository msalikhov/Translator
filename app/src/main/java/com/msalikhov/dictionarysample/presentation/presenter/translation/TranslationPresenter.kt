package com.msalikhov.dictionarysample.presentation.presenter.translation

import com.msalikhov.dictionarysample.domain.translation.TranslationInteractorImpl
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.domain.translation.model.TranslationModel
import com.msalikhov.dictionarysample.presentation.view.translation.TranslationView
import com.msalikhov.dictionarysample.utils.extensions.mapToResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import moxy.MvpPresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val QUERY_DEBOUNCE_MILLIS = 500L

class TranslationPresenter @Inject constructor(
    private val translationInteractor: TranslationInteractorImpl
) : MvpPresenter<TranslationView>() {

    private val inputSubject = PublishSubject.create<String>()
    private val supportedLanguagesSubject = BehaviorSubject.createDefault(emptyList<LanguageModel>())
    private var lastTranslationModel: TranslationModel? = null

    private val translationDisposable = Observable
        .combineLatest(inputSubject, supportedLanguagesSubject, ::Pair)
        .doOnNext { viewState.updateTranslationProgress(true) }
        .debounce(QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .distinctUntilChanged { oldPair, newPair ->
            (oldPair == newPair).also { isEqual ->
                if (isEqual) viewState.updateTranslationProgress(false)
            }
        }
        .switchMapSingle { (text, models) ->
            translationInteractor
                .translateText(text, models)
                .mapToResult()
                .subscribeOn(Schedulers.io())
        }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { viewState.updateTranslationProgress(false) }
        .subscribe { result ->
            result.onSuccess{
                viewState.updateInputLanguage(it.inputLangName)
                viewState.displayTranslationResult(it.text)
                lastTranslationModel = it
            }
            result.onFailure(viewState::displayError)
        }

    private val supportedLanguagesDisposable = translationInteractor
        .getSupportedLanguages()
        .mapToResult()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.updateSupportedLanguagesProgress(true) }
        .doFinally { viewState.updateSupportedLanguagesProgress(false) }
        .subscribe { result ->
            result.onSuccess(this::updateSupportedLanguages)
            result.onFailure(viewState::displayError)
        }

    override fun onDestroy() {
        translationDisposable.dispose()
        supportedLanguagesDisposable.dispose()
    }

    fun onTextChanged(text: String) {
        inputSubject.onNext(text)
    }

    fun changeTargetLanguage(code: String) {
        val models = supportedLanguagesSubject.value ?: return
        updateSupportedLanguages(models.map { it.copy(isSelected = it.code == code) })
    }

    fun swapLanguages() {
        val model = lastTranslationModel ?: return
        changeTargetLanguage(model.inputLangCode)
        viewState.setInput(model.text)
    }

    private fun updateSupportedLanguages(it: List<LanguageModel>) {
        supportedLanguagesSubject.onNext(it)
        viewState.displaySupportedLanguages(it)
    }
}